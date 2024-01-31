package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.ICallableStatementExecutorBlock;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Executes stmt.execute() and returns result set
 */
public class CallableStatementExecutorBlockExecute implements ICallableStatementExecutorBlock {

    public ResultSet execute(CallableStatement aStmt) throws SQLException {
        aStmt.execute();
        return aStmt.getResultSet();
    }


    public String toString() {
        return "CallableStatementExecutorBlockExecute{}";
    }
}