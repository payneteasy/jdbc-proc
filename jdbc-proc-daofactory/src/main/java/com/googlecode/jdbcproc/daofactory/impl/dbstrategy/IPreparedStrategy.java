package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface IPreparedStrategy {

    void setLong(StrategyParameter aParameter, long aValue) throws SQLException;

    void setNull(StrategyParameter aParameter, int aSqlType) throws SQLException;

    void setString(StrategyParameter aParameter, String aValue) throws SQLException;

    void setDate(StrategyParameter aParameter, java.sql.Date aDateValue)	throws SQLException;

    void setBigDecimal(StrategyParameter aParameter, BigDecimal aValue) throws SQLException;

}
