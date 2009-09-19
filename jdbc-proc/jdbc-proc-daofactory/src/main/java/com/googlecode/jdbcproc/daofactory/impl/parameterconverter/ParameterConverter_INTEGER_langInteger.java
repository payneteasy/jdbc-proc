package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  INTEGER - int
 */
public class ParameterConverter_INTEGER_langInteger implements IParameterConverter<Integer> {

    public void setValue(Integer aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setInt(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.INTEGER);
        }
    }

    public void setValue(Integer aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setInt(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.INTEGER);
        }
    }

    public Integer getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        Integer value = aStmt.getInt(aParameterName);
        return aStmt.wasNull() ? null : value;
    }

    public Integer getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        Integer value = aResultSet.getInt(aParameterName);
        return aResultSet.wasNull() ? null : value;
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.INTEGER, Integer.class);
    }

    public String toString() {
        return "ParameterConverter_INTEGER_langInteger{}";
    }
}