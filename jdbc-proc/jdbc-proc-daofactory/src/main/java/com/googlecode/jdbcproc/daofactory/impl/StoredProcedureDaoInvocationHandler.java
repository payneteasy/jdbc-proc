package com.googlecode.jdbcproc.daofactory.impl;

import com.googlecode.jdbcproc.daofactory.DaoMethodInfoFactory;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;
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
            , DaoMethodInfoFactory aDaoMethodInfoFactory
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
                    LOG.debug("    Creating {} ...", method.getName());
                }
                theDaoMethodInvokersMap.put(method, aDaoMethodInfoFactory.createDaoMethodInvoker(method));
            } else {
                throw new IllegalStateException(
                        String.format("Method %s.%s(...) must have @AStoredProcedure annotation"
                                , aInterface.getSimpleName(), method.getName())
                );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {

        // finds invoker and executes
        DaoMethodInvoker methodInvoker = theDaoMethodInvokersMap.get(aMethod);
        if(methodInvoker !=null) {
            return theJdbcTemplate.execute(
                      methodInvoker.getCallString()
                    , methodInvoker.createCallableStatementCallback(aArgs)
            );
        } else if(aMethod.getName().equals("toString")) {
           return theInterface.getName() + " proxy";
        } else {
            throw new IllegalStateException("Method "+aMethod.getName()+" was not proxied");
        }
    }

    private final Class theInterface;
    private final JdbcTemplate theJdbcTemplate;
    private final Map<Method, DaoMethodInvoker> theDaoMethodInvokersMap;
}
