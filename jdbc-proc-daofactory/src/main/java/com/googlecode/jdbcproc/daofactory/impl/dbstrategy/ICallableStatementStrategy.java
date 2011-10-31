package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface ICallableStatementStrategy {

    long getLong(StatementArgument aArgument) throws SQLException;

    String getString(StatementArgument aArgument) throws SQLException;

    boolean wasNull() throws SQLException;

    java.sql.Date getDate(StatementArgument aArgument) throws SQLException;

    BigDecimal getBigDecimal(StatementArgument aArgument) throws SQLException;

}
