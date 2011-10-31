package com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.math.BigDecimal;
import java.sql.*;

public class CallableStatementSetStrategyNameImpl implements ICallableStatementSetStrategy {

    public CallableStatementSetStrategyNameImpl(CallableStatement aStatement) {
        theStmt = aStatement;
    }

    public void setLong(StatementArgument aArgument, long aValue) throws SQLException {
        theStmt.setLong(aArgument.getName(), aValue);
    }

    public void setNull(StatementArgument aArgument, int aSqlType) throws SQLException {
        theStmt.setNull(aArgument.getName(), aSqlType);
    }

    public void setString(StatementArgument aArgument, String aValue) throws SQLException {
        theStmt.setString(aArgument.getName(), aValue);
    }

    public void setDate(StatementArgument aArgument, Date aDateValue) throws SQLException {
        theStmt.setDate(aArgument.getName(), aDateValue);
    }

    public void setBigDecimal(StatementArgument aArgument, BigDecimal aValue) throws SQLException {
        theStmt.setBigDecimal(aArgument.getName(), aValue);
    }

    public Connection getConnection() throws SQLException {
        return theStmt.getConnection();
    }

    public void setTime(StatementArgument aArgument, Time aValue) throws SQLException {
        theStmt.setTime(aArgument.getName(), aValue);
    }

    public void setDouble(StatementArgument aArgument, Double aValue) throws SQLException {
        theStmt.setDouble(aArgument.getName(), aValue);
    }

    public void setTimestamp(StatementArgument aArgument, Timestamp aValue) throws SQLException {
        theStmt.setTimestamp(aArgument.getName(), aValue);
    }

    public void setBytes(StatementArgument aArgument, byte[] aValue) throws SQLException {
        theStmt.setBytes(aArgument.getName(), aValue);
    }

    public void setInt(StatementArgument aArgument, Integer aValue) throws SQLException {
        theStmt.setInt(aArgument.getName(), aValue);
    }

    private final CallableStatement theStmt;
}
