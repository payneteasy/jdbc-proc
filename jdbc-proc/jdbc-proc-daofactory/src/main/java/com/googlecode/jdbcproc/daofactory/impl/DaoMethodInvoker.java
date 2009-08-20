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

    public DaoMethodInvoker(String aProcedureName
            , String aCallString
            , IRegisterOutParametersBlock aRegisterOutParametersBlock
            , IParametersSetterBlock aParametersSetterBlock
            , ICallableStatementExecutorBlock aCallableStatementExecutorBlock
            , IOutputParametersGetterBlock aOutputParametersGetterBlock
            , IResultSetConverterBlock aResultSetConverterBlock
    ) {
        Assert.notNull(aCallableStatementExecutorBlock, "aCallableStatementExecutorBlock must not be null");

        theProcedureName                = aProcedureName;
        theCallString                   = aCallString;
        theRegisterOutParametersBlock   = aRegisterOutParametersBlock;
        theParametersSetterBlock        = aParametersSetterBlock;
        theCallableStatementExecutor    = aCallableStatementExecutorBlock;
        theOutputParametersGetterBlock = aOutputParametersGetterBlock;
        theResultSetConverterBlock      = aResultSetConverterBlock;
    }

    public String getCallString() {
        return theCallString;
    }

    public CallableStatementCallback createCallableStatementCallback(final Object[] aArgs) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Invoking "+theProcedureName+"...");
        }
        // creates new callback with given arguments to execute dao method
        return new CallableStatementCallback() {

            public Object doInCallableStatement(CallableStatement aStmt) throws SQLException, DataAccessException {

                if(LOG.isDebugEnabled()) {
                    // debugs all methods in CallableStatement
                    aStmt = (CallableStatement) Proxy.newProxyInstance(
                            Thread.currentThread().getContextClassLoader()
                            , new Class[] {CallableStatement.class}
                            , new DebugLogInvocationHandler(aStmt, LOG)
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

                    // converts result se`t to return value
                    if(theResultSetConverterBlock!=null) {
                        return theResultSetConverterBlock.convertResultSet(resultSet);
                    } else {
                        return null;
                    }
                } finally {
                    if(resultSet!=null) {
                        resultSet.close();
                    }
                }
            }
        };
    }

    private final String theProcedureName;
    private final String theCallString;
    private final IRegisterOutParametersBlock theRegisterOutParametersBlock;
    private final IParametersSetterBlock theParametersSetterBlock;
    private final ICallableStatementExecutorBlock theCallableStatementExecutor;
    private final IOutputParametersGetterBlock theOutputParametersGetterBlock;
    private final IResultSetConverterBlock theResultSetConverterBlock;
}
