package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.math.BigDecimal;
import java.sql.*;

public class CallableStatementStrategyNameImpl implements ICallableStatementStrategy {

    public CallableStatementStrategyNameImpl(CallableStatement aStmt) {
        theStmt = aStmt;
    }

    public long getLong(StatementArgument aArgument) throws SQLException {
        return theStmt.getLong(aArgument.getName());
    }

    public String getString(StatementArgument aArgument) throws SQLException {
        return theStmt.getString(aArgument.getName());
    }

    public boolean wasNull() throws SQLException {
        return theStmt.wasNull();
    }

    public Date getDate(StatementArgument aArgument) throws SQLException {
        return theStmt.getDate(aArgument.getName());
    }

    public BigDecimal getBigDecimal(StatementArgument aArgument) throws SQLException {
        return theStmt.getBigDecimal(aArgument.getName());
    }

    public int getInt(StatementArgument aArgument) throws SQLException {
        return theStmt.getInt(aArgument.getName());
    }

    public double getDouble(StatementArgument aArgument) throws SQLException {
        return theStmt.getDouble(aArgument.getName());
    }

    public Time getTime(StatementArgument aArgument) throws SQLException {
        return theStmt.getTime(aArgument.getName());
    }

    public Timestamp getTimestamp(StatementArgument aArgument) throws SQLException {
        return theStmt.getTimestamp(aArgument.getName());
    }

    public byte[] getBytes(StatementArgument aArgument) throws SQLException {
        return theStmt.getBytes(aArgument.getName());
    }

    private final CallableStatement theStmt;
}
