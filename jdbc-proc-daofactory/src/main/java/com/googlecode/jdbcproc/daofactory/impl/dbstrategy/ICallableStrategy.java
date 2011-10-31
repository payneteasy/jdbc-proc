package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface ICallableStrategy {

    long getLong(StrategyParameter aParameter) throws SQLException;

    String getString(StrategyParameter aParameter) throws SQLException;

    boolean wasNull() throws SQLException;

    java.sql.Date getDate(StrategyParameter aParameter) throws SQLException;

    BigDecimal getBigDecimal(StrategyParameter aParameter) throws SQLException;

}
