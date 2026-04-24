package com.googlecode.jdbcproc.daofactory.impl;

import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
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
                            , methodInvoker.createCallableStatementCallback(aArgs, theJdbcTemplate.getDataSource())
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
            return theInterface.hashCode();
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
        Connection connection = DataSourceUtils.getConnection(theJdbcTemplate.getDataSource());
        CallableStatement stmt = null;
        try {
            // Configuring statement to enable streaming; this is to make driver
            // avoid buffering all the result set in memory. MySQL and MariaDB
            // drivers disagree on how streaming is enabled, so pick per driver:
            //  - MySQL Connector/J: row-by-row streaming via fetchSize=Integer.MIN_VALUE.
            //    A positive fetchSize is silently ignored unless useCursorFetch=true
            //    is in the JDBC url.
            //  - MariaDB Connector/J: rejects Integer.MIN_VALUE, enables server-side
            //    cursor streaming (COM_STMT_FETCH) on any positive fetchSize.
            stmt = connection.prepareCall(methodInvoker.getCallString(),
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(resolveStreamingFetchSize(connection));

            CallableStatementCallback callableStatementCallback = methodInvoker.createCallableStatementCallback(aArgs, theJdbcTemplate.getDataSource());
            return callableStatementCallback.doInCallableStatement(stmt);
        } catch (Exception e) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    LOG.debug("Error while closing CallableStatement", ex);
                }
            }
            DataSourceUtils.releaseConnection(connection, theJdbcTemplate.getDataSource());
            throw e;
        }
    }

    private int resolveStreamingFetchSize(Connection connection) throws SQLException {
        String driverName = connection.getMetaData().getDriverName();
        if (driverName != null && driverName.toLowerCase().contains("mysql")) {
            return MYSQL_STREAMING_FETCH_SIZE;
        }
        return MARIADB_STREAMING_FETCH_SIZE;
    }

    private final Class theInterface;
    private final JdbcTemplate theJdbcTemplate;
    private final Map<Method, DaoMethodInvoker> theDaoMethodInvokersMap;

    /** MySQL Connector/J magic value for row-by-row streaming. */
    private static final int MYSQL_STREAMING_FETCH_SIZE = Integer.MIN_VALUE;

    /** Any positive value enables server-side cursor streaming on MariaDB. */
    private static final int MARIADB_STREAMING_FETCH_SIZE = 1000;
}
