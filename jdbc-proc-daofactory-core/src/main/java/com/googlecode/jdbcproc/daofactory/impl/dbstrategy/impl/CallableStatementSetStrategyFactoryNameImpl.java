package com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategyFactory;

import java.sql.CallableStatement;

public class CallableStatementSetStrategyFactoryNameImpl implements ICallableStatementSetStrategyFactory {
    public ICallableStatementSetStrategy create(CallableStatement aSCallableStatement) {
        return new CallableStatementSetStrategyNameImpl(aSCallableStatement);
    }
}
