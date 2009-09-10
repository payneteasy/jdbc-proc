package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  DECIMAL - BigDecimal
 */
public class ParameterConverter_DECIMAL_BigDecimal implements IParameterConverter<BigDecimal> {

    public void setValue(BigDecimal aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setBigDecimal(aIndex, aValue);
    }

    public void setValue(BigDecimal aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        aStmt.setBigDecimal(aParameterName, aValue);
    }

    public BigDecimal getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getBigDecimal(aParameterName);
    }

    public BigDecimal getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBigDecimal(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.DECIMAL, BigDecimal.class);
    }
}