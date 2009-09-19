package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  INTEGER - long
 */
public class ParameterConverter_INTEGER_langLong implements IParameterConverter<Long> {

    public void setValue(Long aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.INTEGER);
        }
    }

    public void setValue(Long aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.INTEGER);
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
        return new ParameterSetterKey(Types.INTEGER, Long.class);
    }

    public String toString() {
        return "ParameterConverter_INTEGER_langLong{}";
    }
}