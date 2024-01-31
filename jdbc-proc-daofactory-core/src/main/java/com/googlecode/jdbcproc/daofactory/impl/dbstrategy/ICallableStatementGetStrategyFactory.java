package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.sql.CallableStatement;

public interface ICallableStatementGetStrategyFactory {

    ICallableStatementGetStrategy create(CallableStatement aCallableStatement);
}
