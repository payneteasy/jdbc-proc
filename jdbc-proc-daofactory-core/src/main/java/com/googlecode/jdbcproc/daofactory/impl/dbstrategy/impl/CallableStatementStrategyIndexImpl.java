package com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.math.BigDecimal;
import java.sql.*;

public class CallableStatementStrategyIndexImpl implements ICallableStatementGetStrategy {

    public CallableStatementStrategyIndexImpl(CallableStatement aStmt) {
        theStmt = aStmt;
    }

    public long getLong(StatementArgument aArgument) throws SQLException {
        return theStmt.getLong(aArgument.getParameterIndex());
    }

    public String getString(StatementArgument aArgument) throws SQLException {
        return theStmt.getString(aArgument.getParameterIndex());
    }

    public boolean wasNull() throws SQLException {
        return theStmt.wasNull();
    }

    public Date getDate(StatementArgument aArgument) throws SQLException {
        return theStmt.getDate(aArgument.getParameterIndex());
    }

    public BigDecimal getBigDecimal(StatementArgument aArgument) throws SQLException {
        return theStmt.getBigDecimal(aArgument.getParameterIndex());
    }

    public int getInt(StatementArgument aArgument) throws SQLException {
        return theStmt.getInt(aArgument.getParameterIndex());
    }

    public double getDouble(StatementArgument aArgument) throws SQLException {
        return theStmt.getDouble(aArgument.getParameterIndex());
    }

    public Time getTime(StatementArgument aArgument) throws SQLException {
        return theStmt.getTime(aArgument.getParameterIndex());
    }

    public Timestamp getTimestamp(StatementArgument aArgument) throws SQLException {
        return theStmt.getTimestamp(aArgument.getParameterIndex());
    }

    public byte[] getBytes(StatementArgument aArgument) throws SQLException {
        return theStmt.getBytes(aArgument.getParameterIndex());
    }

    private final CallableStatement theStmt;
}
