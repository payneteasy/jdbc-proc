package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public interface ICallableStatementSetStrategy {

    void setLong(StatementArgument aArgument, long aValue) throws SQLException;

    void setNull(StatementArgument aArgument, int aSqlType) throws SQLException;

    void setString(StatementArgument aArgument, String aValue) throws SQLException;

    void setDate(StatementArgument aArgument, java.sql.Date aDateValue)	throws SQLException;

    void setBigDecimal(StatementArgument aArgument, BigDecimal aValue) throws SQLException;

    Connection getConnection() throws SQLException;

    void setTime(StatementArgument aArgument, Time aValue) throws SQLException;

    void setDouble(StatementArgument aArgument, Double aValue) throws SQLException;

    void setTimestamp(StatementArgument aArgument, Timestamp aValue) throws SQLException;

    void setBytes(StatementArgument aArgument, byte[] aValue) throws SQLException;

    void setInt(StatementArgument aArgument, Integer aValue) throws SQLException;
}
