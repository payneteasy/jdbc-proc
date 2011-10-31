package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.sql.CallableStatement;

public interface ICallableStatementSetStrategyFactory  {

    ICallableStatementSetStrategy create(CallableStatement aSCallableStatement);
}
