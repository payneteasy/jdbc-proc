package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface ICallableStatementStrategy {

    long getLong(StatementArgument aArgument) throws SQLException;

    int getInt(StatementArgument aArgument) throws SQLException;

    double getDouble(StatementArgument aArgument) throws SQLException;

    String getString(StatementArgument aArgument) throws SQLException;

    boolean wasNull() throws SQLException;

    java.sql.Date getDate(StatementArgument aArgument) throws SQLException;

    java.sql.Time getTime(StatementArgument aArgument) throws SQLException;

    java.sql.Timestamp getTimestamp(StatementArgument aArgument) throws SQLException;

    BigDecimal getBigDecimal(StatementArgument aArgument) throws SQLException;

    byte[] getBytes(StatementArgument aArgument) throws SQLException;

}
