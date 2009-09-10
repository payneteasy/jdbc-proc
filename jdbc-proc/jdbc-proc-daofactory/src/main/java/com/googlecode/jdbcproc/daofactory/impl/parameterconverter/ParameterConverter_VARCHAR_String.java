package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  VARCHAR - String
 */
public class ParameterConverter_VARCHAR_String implements IParameterConverter<String> {


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
        return new ParameterSetterKey(Types.VARCHAR, String.class);
    }
}