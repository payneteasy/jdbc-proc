package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class StatementCloser {

    public StatementCloser(CallableStatement aStmt) {
        theStmt = aStmt;
    }

    public void closeStatement() throws SQLException {
        theStmt.close();
    }

    private final CallableStatement theStmt;
}
