package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  BIGINT - java.lang.Long
 */
public class ParameterConverter_BIGINT_langLong implements IParameterConverter<Long> {

    public void setValue(Long aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.BIGINT);
        }
    }

    public void setValue(Long aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.BIGINT);
        }
    }

    public Long getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        Long value = aStmt.getLong(aParameterName);
        return aStmt.wasNull() ? null : value;
    }

    public Long getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        Long value = aResultSet.getLong(aParameterName);
        return aResultSet.wasNull() ? null : value;
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.BIGINT, Long.class);
    }

    public String toString() {
        return "ParameterConverter_BIGINT_langLong{}";
    }
}