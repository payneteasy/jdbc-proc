package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  REAL - BigDecimal
 */
public class ParameterConverter_REAL_BigDecimal implements IParameterConverter<BigDecimal> {

    public void setValue(BigDecimal aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setBigDecimal(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.REAL);
        }
    }

    public void setValue(BigDecimal aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setBigDecimal(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.REAL);
        }
    }

    public BigDecimal getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getBigDecimal(aParameterName);
    }

    public BigDecimal getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBigDecimal(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.REAL, BigDecimal.class);
    }
}