package com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategyFactory;

import java.sql.CallableStatement;

public class CallableStatementGetStrategyFactoryIndexImpl implements ICallableStatementGetStrategyFactory {
    public ICallableStatementGetStrategy create(CallableStatement aCallableStatement) {
        return new CallableStatementStrategyIndexImpl(aCallableStatement);
    }
}
