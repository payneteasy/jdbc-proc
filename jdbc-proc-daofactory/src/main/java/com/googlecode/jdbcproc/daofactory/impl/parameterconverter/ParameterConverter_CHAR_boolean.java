package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  CHAR - boolean
 */
public class ParameterConverter_CHAR_boolean implements IParameterConverter<Boolean> {

    public void setValue(Boolean aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        String strValue = aValue!=null && aValue ? "Y" : "N";
        aStmt.setString(aIndex, strValue);
    }

    public void setValue(Boolean aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        String strValue = aValue!=null && aValue ? "Y" : "N";
        aStmt.setString(aParameterName, strValue);
    }

    public Boolean getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        String strValue = aStmt.getString(aParameterName);
        return "Y".equals(strValue);
    }

    public Boolean getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        String strValue = aResultSet.getString(aParameterName);
        return "Y".equals(strValue);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.CHAR, boolean.class);
    }
}