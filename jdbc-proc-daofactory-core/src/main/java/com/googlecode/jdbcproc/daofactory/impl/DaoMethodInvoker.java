package com.googlecode.jdbcproc.daofactory.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.ICallableStatementExecutorBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IOutputParametersGetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IRegisterOutParametersBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockOrder;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterContextImpl;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategyFactory;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategyFactory;

import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.util.Assert;

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
            , List<IParametersSetterBlock> aParametersSetterBlocks
            , ICallableStatementExecutorBlock aCallableStatementExecutorBlock
            , IOutputParametersGetterBlock aOutputParametersGetterBlock
            , IResultSetConverterBlock aResultSetConverterBlock
            , boolean aIsReturnIterator
            , ICallableStatementSetStrategyFactory aCallableStatementSetStrategy
            , ICallableStatementGetStrategyFactory aPreparedStatementStrategy
            , int aCallTimeoutSeconds
    ) {
        Assert.notNull(aCallableStatementExecutorBlock, "aCallableStatementExecutorBlock must not be null");

        theProcedureName                = aProcedureName;
        theCallString                   = aCallString;
        theRegisterOutParametersBlock   = aRegisterOutParametersBlock;
        theParametersSetterBlocks       = aParametersSetterBlocks;
        theCallableStatementExecutor    = aCallableStatementExecutorBlock;
        theOutputParametersGetterBlock  = aOutputParametersGetterBlock;
        theResultSetConverterBlock      = aResultSetConverterBlock;
        theIsReturnIterator             = aIsReturnIterator;
        theSetStrategyFactory           = aCallableStatementSetStrategy;
        theGetStrategyFactory           = aPreparedStatementStrategy;
        theCallTimeoutSeconds           = aCallTimeoutSeconds;
        
        // We should sort parameters setter blocks for executing setters in proper order.
        // At first, 'List<?>' setters should be executed, second, other setters should be executed.
        if (theParametersSetterBlocks.size() > 1) {
            Collections.sort(theParametersSetterBlocks, new Comparator<IParametersSetterBlock>() {
                public int compare(IParametersSetterBlock o1, IParametersSetterBlock o2) {
                    ParametersSetterBlockOrder order1 = ParametersSetterBlockOrder.find(o1.getClass());
                    ParametersSetterBlockOrder order2 = ParametersSetterBlockOrder.find(o2.getClass());
                    int index1 = order1.index();
                    int index2 = order2.index();
                    return (index1 < index2 ? -1 : (index1 == index2 ? 0 : 1));
                }
            });
        }
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

    public CallableStatementCallback createCallableStatementCallback(final Object[] aMethodParameters, final DataSource dataSource) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Invoking "+theProcedureName+"...");
        }
        // creates new callback with given arguments to execute dao method
        return new CallableStatementCallbackWrapper(dataSource) {

            public Object doInCallableStatement(CallableStatement aStmt) throws SQLException, DataAccessException {

                long startTime = 0;
                if(LOG_TIME.isDebugEnabled()) {
                    startTime = System.currentTimeMillis();
                }

                final StringBuilder logger = new StringBuilder();
                if(LOG.isDebugEnabled()) {
                    logger.append("Procedure [").append(theProcedureName).append(']');
                    // debugs all methods in CallableStatement
                    aStmt = (CallableStatement) Proxy.newProxyInstance(
                            Thread.currentThread().getContextClassLoader()
                            , new Class[] {CallableStatement.class}
                            , new AppendableLogInvocationHandler(aStmt) {
                          @Override public void append(String str) {
                              logger.append(str);
                          }
                      }
                    );
                }

                try {
                    if (theCallTimeoutSeconds > 0) {
                        aStmt.setQueryTimeout(theCallTimeoutSeconds);
                    }

                    // register output parameters
                    // eg. aStmt.registerOutParameter(1, Types.INTEGER);
                    if(theRegisterOutParametersBlock!=null) {
                        theRegisterOutParametersBlock.registerOutParameters(aStmt);
                    }

                    if (theIsReturnIterator) {
                        return callForIterator(aStmt, aMethodParameters, dataSource);
                    } else {
                        return call(aStmt, aMethodParameters, dataSource);
                    }
                } finally {
                    if (LOG.isDebugEnabled()) {
                        LOG_CALLABLE_STATEMENT.debug(logger.toString());
                    }
                    if(LOG_TIME.isDebugEnabled()) {
                        LOG_TIME.debug("Called time {}(): {}ms", theProcedureName, System.currentTimeMillis() - startTime);
                    }
                }
            }
        };
    }

    /**
     * Calls the procedure for a method that returns a plain value: the
     * parameter-setter cleanup (e.g. clearing the List-parameter temp table)
     * runs right after the call, the result set is read and closed here.
     */
    private Object call(CallableStatement aStmt, Object[] aMethodParameters, DataSource aDataSource) throws SQLException {
        ResultSet resultSet;
        try {
            setParameters(aStmt, aMethodParameters);
            resultSet = theCallableStatementExecutor.execute(aStmt);
        } finally {
            cleanupParameterSetters(aStmt, "");
        }

        try {
            return readResult(aStmt, resultSet, aMethodParameters, aDataSource, null);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * Calls the procedure for a method that returns an iterator. The result set
     * stays open and is handed over to the iterator together with the
     * parameter-setter cleanup, which the iterator runs from its close() once
     * the cursor is closed. The cleanup must NOT run here: its executeUpdate on
     * a connection with an open streaming cursor forces the MariaDB driver to
     * buffer the whole result set into memory (OOM on large reports).
     * <p>
     * If anything fails before the iterator exists, nobody would ever run that
     * deferred cleanup and the temp table data would go back to the pool with
     * the connection, so it is cleaned up here before the exception propagates.
     */
    private Object callForIterator(CallableStatement aStmt, Object[] aMethodParameters, DataSource aDataSource) throws SQLException {
        ResultSet resultSet = null;
        try {
            setParameters(aStmt, aMethodParameters);
            resultSet = theCallableStatementExecutor.execute(aStmt);
            return readResult(aStmt, resultSet, aMethodParameters, aDataSource
                    , () -> cleanupParameterSetters(aStmt, " on iterator close"));
        } catch (Throwable t) {
            closeQuietly(resultSet);
            cleanupParameterSetters(aStmt, " after failed iterator call");
            throw t;
        }
    }

    /**
     * Sets parameters value, eg. aStmt.setString(1, "hello").
     */
    private void setParameters(CallableStatement aStmt, Object[] aMethodParameters) throws SQLException {
        if (theParametersSetterBlocks != null) {
            ICallableStatementSetStrategy setStrategy = theSetStrategyFactory.create(aStmt);
            for (IParametersSetterBlock block : theParametersSetterBlocks) {
                block.setParameters(setStrategy, aMethodParameters);
            }
        }
    }

    /**
     * Fills the output parameters into the arguments and returns the method
     * result: the return output parameter, or the converted result set.
     *
     * @param aOnCloseCleanup cleanup an iterator result runs on close, null for plain results
     */
    private Object readResult(CallableStatement aStmt, ResultSet aResultSet, Object[] aMethodParameters
            , DataSource aDataSource, Runnable aOnCloseCleanup) throws SQLException {
        ICallableStatementGetStrategy getStrategy = theGetStrategyFactory.create(aStmt);

        // gets output parameters and sets it to arguments
        if (theOutputParametersGetterBlock != null) {
            theOutputParametersGetterBlock.fillOutputParameters(getStrategy, aMethodParameters);
        }

        if (theOutputParametersGetterBlock != null && theOutputParametersGetterBlock.hasReturn()) {
            return theOutputParametersGetterBlock.getReturnValue(getStrategy);
        }

        if (theResultSetConverterBlock == null) {
            return null;
        }

        // converts result set to return value
        IResultSetConverterContext context = ResultSetConverterContextImpl.builder()
                .setResultSet(aResultSet)
                .setCallableStatement(aStmt)
                .setDataSource(aDataSource)
                .setOnCloseCleanup(aOnCloseCleanup)
                .build();
        return theResultSetConverterBlock.convertResultSet(context);
    }

    /**
     * Runs the parameter-setter cleanup (e.g. clearing the List-parameter temp
     * table) on the connection of the given statement. Never throws: a failed
     * cleanup is only logged, the block list re-clears the temp table at the
     * start of the next invocation anyway.
     *
     * @param aStmt    statement whose connection holds the data to clean up
     * @param aContext suffix for the log message telling when the cleanup ran
     */
    private void cleanupParameterSetters(CallableStatement aStmt, String aContext) {
        if (theParametersSetterBlocks == null) {
            return;
        }
        for (IParametersSetterBlock block : theParametersSetterBlocks) {
            try {
                block.cleanup(aStmt);
            } catch (Exception e) {
                LOG.error("Exception while cleaning up" + aContext, e);
            }
        }
    }

    private void closeQuietly(ResultSet aResultSet) {
        if (aResultSet != null) {
            try {
                aResultSet.close();
            } catch (Exception e) {
                LOG.debug("Error while closing ResultSet", e);
            }
        }
    }

    public String toString() {
        return "DaoMethodInvoker{" +
                "procedureName='" + theProcedureName + '\'' +
                ", callString='" + theCallString + '\'' +
                ", registerOutParametersBlock=" + theRegisterOutParametersBlock +
                ", parametersSetterBlocks=" + theParametersSetterBlocks +
                ", callableStatementExecutor=" + theCallableStatementExecutor +
                ", outputParametersGetterBlock=" + theOutputParametersGetterBlock +
                ", resultSetConverterBlock=" + theResultSetConverterBlock +
                ", isReturnIterator=" + theIsReturnIterator +
                '}';
    }

    private final String theProcedureName;
    private final String theCallString;
    private final IRegisterOutParametersBlock theRegisterOutParametersBlock;
    private final List<IParametersSetterBlock> theParametersSetterBlocks;
    private final ICallableStatementExecutorBlock theCallableStatementExecutor;
    private final IOutputParametersGetterBlock theOutputParametersGetterBlock;
    private final IResultSetConverterBlock theResultSetConverterBlock;
    private final boolean theIsReturnIterator;
    private final ICallableStatementSetStrategyFactory theSetStrategyFactory;
    private final ICallableStatementGetStrategyFactory theGetStrategyFactory;
    private final int theCallTimeoutSeconds;

}
