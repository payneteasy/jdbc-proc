package com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.math.BigDecimal;
import java.sql.*;

public class CallableStatementSetStrategyIndexImpl implements ICallableStatementSetStrategy {

    public CallableStatementSetStrategyIndexImpl(PreparedStatement aStatement) {
        theStmt = aStatement;
    }

    public void setLong(StatementArgument aArgument, long aValue) throws SQLException {
        theStmt.setLong(aArgument.getParameterIndex(), aValue);
    }

    public void setNull(StatementArgument aArgument, int aSqlType) throws SQLException {
        theStmt.setNull(aArgument.getParameterIndex(), aSqlType);
    }

    public void setString(StatementArgument aArgument, String aValue) throws SQLException {
        theStmt.setString(aArgument.getParameterIndex(), aValue);
    }

    public void setDate(StatementArgument aArgument, Date aDateValue) throws SQLException {
        theStmt.setDate(aArgument.getParameterIndex(), aDateValue);
    }

    public void setBigDecimal(StatementArgument aArgument, BigDecimal aValue) throws SQLException {
        theStmt.setBigDecimal(aArgument.getParameterIndex(), aValue);
    }

    public Connection getConnection() throws SQLException {
        return theStmt.getConnection();
    }

    public void setTime(StatementArgument aArgument, Time aValue) throws SQLException {
        theStmt.setTime(aArgument.getParameterIndex(), aValue);
    }

    public void setDouble(StatementArgument aArgument, Double aValue) throws SQLException {
        theStmt.setDouble(aArgument.getParameterIndex(), aValue);
    }

    public void setTimestamp(StatementArgument aArgument, Timestamp aValue) throws SQLException {
        theStmt.setTimestamp(aArgument.getParameterIndex(), aValue);
    }

    public void setBytes(StatementArgument aArgument, byte[] aValue) throws SQLException {
        theStmt.setBytes(aArgument.getParameterIndex(), aValue);
    }

    public void setInt(StatementArgument aArgument, Integer aValue) throws SQLException {
        theStmt.setInt(aArgument.getParameterIndex(), aValue);
    }

    private final PreparedStatement theStmt;
}
