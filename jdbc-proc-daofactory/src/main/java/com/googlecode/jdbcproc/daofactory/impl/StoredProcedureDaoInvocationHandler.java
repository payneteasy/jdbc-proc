package com.googlecode.jdbcproc.daofactory.impl;

import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * Create invocation handler for interface
 */
public class StoredProcedureDaoInvocationHandler implements InvocationHandler {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public StoredProcedureDaoInvocationHandler(
            Class aInterface
            , JdbcTemplate aJdbcTemplate
            , DAOMethodInfo aDaoMethodInfoFactory
    ) throws SQLException {
        theInterface = aInterface;
        theJdbcTemplate = aJdbcTemplate;

        // creates dao method invoker cache map
        theDaoMethodInvokersMap = new HashMap<Method, DaoMethodInvoker>();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Creating implementation for {} ...", theInterface.getSimpleName());
        }

        for (Method method : aInterface.getMethods()) {
            if(method.isAnnotationPresent(AStoredProcedure.class)) {
                if(LOG.isDebugEnabled()) {
                    LOG.debug("    Creating {} [ procedure='{}' ] ...", method.getName(), method.getAnnotation(AStoredProcedure.class).name());
                }

                try {
                    DaoMethodInvoker daoMethodInvoker = aDaoMethodInfoFactory.createDaoMethodInvoker(method);

                    if(LOG.isDebugEnabled()) {
                        LOG.debug("      Created {}", daoMethodInvoker);
                    }
                    theDaoMethodInvokersMap.put(method, daoMethodInvoker);

                } catch (Exception e) {
                    String procedureName = method.getAnnotation(AStoredProcedure.class).name();
                    String classname = aInterface.getSimpleName();
                    String parameters = createMethodParametersString(method.getParameterTypes());

                    String message = String.format("Cannot create %s.%s(%s) method for procedure %s : %s", classname, method.getName(), parameters, procedureName, e.getMessage() );
                    throw new RuntimeException(message, e);
                }

            } else {
                throw new IllegalStateException(
                        String.format("Method %s.%s(...) must have @AStoredProcedure annotation"
                                , aInterface.getSimpleName(), method.getName())
                );
            }
        }
    }

    private String createMethodParametersString(Class<?>[] aTypes) {
        StringBuilder sb = new StringBuilder();
        boolean firstPassed = false;
        for (Class<?> type : aTypes) {
            if(firstPassed) {
                sb.append(", ");
            } else {
                firstPassed = true;
            }
            sb.append(type.getSimpleName());
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {

        // finds invoker and executes
        DaoMethodInvoker methodInvoker = theDaoMethodInvokersMap.get(aMethod);
        if(methodInvoker !=null) {
            try {
                if(methodInvoker.isReturnIterator()) {
                    // call method with iterator
                    return callIterator(aArgs, methodInvoker);
                } else {
                    return theJdbcTemplate.execute(
                              methodInvoker.getCallString()
                            , methodInvoker.createCallableStatementCallback(aArgs)
                    );
                }
            } catch(Exception e) {
                String message = String.format("Error invoking %s.%s(%s) for procedure %s: %s"
                        , theInterface.getSimpleName(), aMethod.getName(), createMethodParametersString(aMethod.getParameterTypes())
                        , getProcedureName(aMethod), methodInvoker.getCallString()
                );
                throw new RuntimeException(message, e);
            }
        } else if(aMethod.getName().equals("toString")) {
           return theInterface.getName() + " proxy";
        } else if(aMethod.getName().equals("hashCode")) {
            return aProxy.hashCode();
        } else {
            throw new IllegalStateException("Method "+aMethod.getName()+" was not proxied");
        }
    }

    private String getProcedureName(Method aMethod) {
        if(aMethod.isAnnotationPresent(AStoredProcedure.class)) {
            return aMethod.getAnnotation(AStoredProcedure.class).name();
        } else {
            return "No_@StoredProcedure_Annotation";
        }
    }

    /**
     * Call iterator with closing ResultSet and CallableStatement with Connection.close()
     *
     * @param aArgs         arguments
     * @param methodInvoker method invoker
     * @return iterator
     * @throws SQLException on sql exception
     */
    private Object callIterator(Object[] aArgs, DaoMethodInvoker methodInvoker) throws SQLException {
        // gets connection from transaction manager
        // and will be closed by transaction manager
        Connection connection = DataSourceUtils.getConnection(theJdbcTemplate.getDataSource());

        // Configuring statement to enable streaming; this is to make driver
        // avoid buffering all the result set in memory. At least Mysql requires this.
        // https://stackoverflow.com/questions/2447324/streaming-large-result-sets-with-mysql
        CallableStatement stmt = connection.prepareCall(methodInvoker.getCallString(),
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(Integer.MIN_VALUE);

        CallableStatementCallback callableStatementCallback = methodInvoker.createCallableStatementCallback(aArgs);
        return callableStatementCallback.doInCallableStatement(stmt);
    }

    private final Class theInterface;
    private final JdbcTemplate theJdbcTemplate;
    private final Map<Method, DaoMethodInvoker> theDaoMethodInvokersMap;
}
