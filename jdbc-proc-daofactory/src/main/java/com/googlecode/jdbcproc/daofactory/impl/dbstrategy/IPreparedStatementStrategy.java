package com.googlecode.jdbcproc.daofactory.impl.dbstrategy;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface IPreparedStatementStrategy {

    void setLong(StatementArgument aArgument, long aValue) throws SQLException;

    void setNull(StatementArgument aArgument, int aSqlType) throws SQLException;

    void setString(StatementArgument aArgument, String aValue) throws SQLException;

    void setDate(StatementArgument aArgument, java.sql.Date aDateValue)	throws SQLException;

    void setBigDecimal(StatementArgument aArgument, BigDecimal aValue) throws SQLException;

}
