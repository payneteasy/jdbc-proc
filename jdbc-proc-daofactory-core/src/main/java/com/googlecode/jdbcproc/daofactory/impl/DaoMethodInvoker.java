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

    public CallableStatementCallback createCallableStatementCallback(final Object[] aMethodParameters, DataSource dataSource) {
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

                ResultSet resultSet;
                try {
                    // register output parameters
                    // eg. aStmt.registerOutParameter(1, Types.INTEGER);
                    if(theRegisterOutParametersBlock!=null) {
                        theRegisterOutParametersBlock.registerOutParameters(aStmt);
                    }

                    try {
                        // set parameters value
                        // eg. aStmt.setString(1, "hello");
                        if(theParametersSetterBlocks !=null) {
                            ICallableStatementSetStrategy callableStatementSetStrategy = theSetStrategyFactory.create(aStmt);
                            for (IParametersSetterBlock block : theParametersSetterBlocks)
                                block.setParameters(callableStatementSetStrategy, aMethodParameters);
                        }
    
                        // callable statement executor
                        // eg. int result = aStmt.executeUpdate();
                        // or  ResultSet rs = aStmt.executeQuery();
                        resultSet = theCallableStatementExecutor.execute(aStmt);
                    } finally {
                        // cleaning up
                        if (theParametersSetterBlocks != null) {
                            for (IParametersSetterBlock block : theParametersSetterBlocks) {
                                try {
                                    block.cleanup(aStmt);
                                } catch (Exception e) {
                                    // just log
                                    if (cleanUpFailedBecauseStreamingIsActive(e)) {
                                        LOG.debug("Exception while cleaning up because streaming is active and we cannot clean up", e);
                                    } else {
                                        LOG.error("Exception while cleaning up", e);
                                    }
                                    // TODO: run the cleaning task in CloseableIterator.close()?
                                }
                            }
                        }
                    }
                } finally {
                    if (LOG.isDebugEnabled()) {
                        LOG_CALLABLE_STATEMENT.debug(logger.toString());
                    }
                }

                ICallableStatementGetStrategy callableStatementGetStrategy = theGetStrategyFactory.create(aStmt);
                try {
                    // gets output parameters and sets it to arguments
                    if(theOutputParametersGetterBlock !=null) {
                        theOutputParametersGetterBlock.fillOutputParameters(callableStatementGetStrategy, aMethodParameters);
                    }

                    if(theOutputParametersGetterBlock!=null && theOutputParametersGetterBlock.hasReturn()) {
                        return theOutputParametersGetterBlock.getReturnValue(callableStatementGetStrategy);

                    } else {
                        // converts result set to return value
                        if(theResultSetConverterBlock!=null) {
                            IResultSetConverterContext context = ResultSetConverterContextImpl.builder()
                                .setResultSet(resultSet)
                                .setCallableStatement(aStmt)
                                .setDataSource(dataSource)
                                .build();
                            return theResultSetConverterBlock.convertResultSet(context);
                        } else {
                            return null;
                        }
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

    private boolean cleanUpFailedBecauseStreamingIsActive(Exception e) {
        return theIsReturnIterator && cannotMakeUpdatesWhileStreamingIsActive(e);
    }

    private boolean cannotMakeUpdatesWhileStreamingIsActive(Exception e) {
        return e.getMessage() != null && e.getMessage().contains(
                "No statements may be issued when any streaming result sets are open");
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

}
