package com.googlecode.jdbcproc.daofactory.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.reflect.Proxy;

/**
 * Dao method information
 */
public class DaoMethodInvoker {

    private final Logger LOG = LoggerFactory.getLogger(DaoMethodInvoker.class);
    private final Logger LOG_CALLABLE_STATEMENT = LoggerFactory.getLogger(DaoMethodInvoker.class.getName()+"_Statement");
    private final Logger LOG_TIME = LoggerFactory.getLogger(DaoMethodInvoker.class.getName()+"_Time");

    public DaoMethodInvoker(String aProcedureName
            , String aCallString
            , IRegisterOutParametersBlock aRegisterOutParametersBlock
            , IParametersSetterBlock aParametersSetterBlock
            , ICallableStatementExecutorBlock aCallableStatementExecutorBlock
            , IOutputParametersGetterBlock aOutputParametersGetterBlock
            , IResultSetConverterBlock aResultSetConverterBlock
            , boolean aIsReturnIterator
    ) {
        Assert.notNull(aCallableStatementExecutorBlock, "aCallableStatementExecutorBlock must not be null");

        theProcedureName                = aProcedureName;
        theCallString                   = aCallString;
        theRegisterOutParametersBlock   = aRegisterOutParametersBlock;
        theParametersSetterBlock        = aParametersSetterBlock;
        theCallableStatementExecutor    = aCallableStatementExecutorBlock;
        theOutputParametersGetterBlock  = aOutputParametersGetterBlock;
        theResultSetConverterBlock      = aResultSetConverterBlock;
        theIsReturnIterator             = aIsReturnIterator;
    }

    public String getCallString() {
        return theCallString;
    }

    /**
     * Does method return iterator
     * 
     * @return true, if return type is iterator
     */
    public boolean isReturnIterator() {
        return theIsReturnIterator;
    }

    public CallableStatementCallback createCallableStatementCallback(final Object[] aArgs) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Invoking "+theProcedureName+"...");
        }
        // creates new callback with given arguments to execute dao method
        return new CallableStatementCallback() {

            public Object doInCallableStatement(CallableStatement aStmt) throws SQLException, DataAccessException {

                long startTime = 0;
                if(LOG_TIME.isDebugEnabled()) {
                    startTime = System.currentTimeMillis();
                }

                if(LOG.isDebugEnabled()) {
                    // debugs all methods in CallableStatement
                    aStmt = (CallableStatement) Proxy.newProxyInstance(
                            Thread.currentThread().getContextClassLoader()
                            , new Class[] {CallableStatement.class}
                            , new DebugLogInvocationHandler(aStmt, LOG_CALLABLE_STATEMENT)
                    );
                }

                // register output parameters
                // eg. aStmt.registerOutParameter(1, Types.INTEGER);
                if(theRegisterOutParametersBlock!=null) {
                    theRegisterOutParametersBlock.registerOutParameters(aStmt);
                }

                // set parameters value
                // eg. aStmt.setString(1, "hello");
                if(theParametersSetterBlock!=null) {
                    theParametersSetterBlock.setParameters(aStmt, aArgs);
                }

                // callable statement executor
                // eg. int result = aStmt.executeUpdate();
                // or  ResultSet rs = aStmt.executeQuery();
                ResultSet resultSet = theCallableStatementExecutor.execute(aStmt);
                try {
                    // gets output parameters and sets it to arguments
                    if(theOutputParametersGetterBlock !=null) {
                        theOutputParametersGetterBlock.fillOutputParameters(aStmt, aArgs);
                    }

                    // converts result set to return value
                    if(theResultSetConverterBlock!=null) {
                        return theResultSetConverterBlock.convertResultSet(resultSet, aStmt);
                    } else {
                        return null;
                    }
                } finally {
                    if (theIsReturnIterator) {
                        // result set will be closed in future
                    } else {
                        if (resultSet != null) {
                            resultSet.close();
                        }
                    }
                    if(LOG_TIME.isDebugEnabled()) {
                        LOG_TIME.debug("Called time {}(): {}ms", theProcedureName, System.currentTimeMillis() - startTime);
                    }
                }
            }
        };
    }


    public String toString() {
        return "DaoMethodInvoker{" +
                "procedureName='" + theProcedureName + '\'' +
                ", callString='" + theCallString + '\'' +
                ", registerOutParametersBlock=" + theRegisterOutParametersBlock +
                ", parametersSetterBlock=" + theParametersSetterBlock +
                ", callableStatementExecutor=" + theCallableStatementExecutor +
                ", outputParametersGetterBlock=" + theOutputParametersGetterBlock +
                ", resultSetConverterBlock=" + theResultSetConverterBlock +
                ", isReturnIterator=" + theIsReturnIterator +
                '}';
    }

    private final String theProcedureName;
    private final String theCallString;
    private final IRegisterOutParametersBlock theRegisterOutParametersBlock;
    private final IParametersSetterBlock theParametersSetterBlock;
    private final ICallableStatementExecutorBlock theCallableStatementExecutor;
    private final IOutputParametersGetterBlock theOutputParametersGetterBlock;
    private final IResultSetConverterBlock theResultSetConverterBlock;
    private final boolean theIsReturnIterator;
}
