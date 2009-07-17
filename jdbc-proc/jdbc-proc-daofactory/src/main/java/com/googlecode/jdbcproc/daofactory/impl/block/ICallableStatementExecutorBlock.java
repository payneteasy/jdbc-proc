package com.googlecode.jdbcproc.daofactory.impl.block;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Callable statement executor block
 */
public interface ICallableStatementExecutorBlock {

    /**
     * Executes statement
     * @param aStmt statement
     * @return result set or null
     * @throws java.sql.SQLException on sql error
     */
    ResultSet execute(CallableStatement aStmt) throws SQLException;
}
