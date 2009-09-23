package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  LONGVARCHAR - String
 */
public class ParameterConverter_LONGVARCHAR_String implements IParameterConverter<String> {


    public void setValue(String aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setString(aIndex, aValue);
    }

    public void setValue(String aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        aStmt.setString(aParameterName, aValue);
    }

    public String getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getString(aParameterName);
    }

    public String getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getString(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.LONGVARCHAR, String.class);
    }

    public String toString() {
        return "ParameterConverter_LONGVARCHAR_String{}";
    }
}