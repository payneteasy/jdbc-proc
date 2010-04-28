package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  VARCHAR - String
 */
public class ParameterConverter_VARCHAR_String 
    implements IParameterConverter<ParameterConverter_VARCHAR_String, String> {

  public static final Type<ParameterConverter_VARCHAR_String> TYPE 
      = new Type<ParameterConverter_VARCHAR_String>(Types.VARCHAR, String.class);

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

  public Type<ParameterConverter_VARCHAR_String> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_VARCHAR_String{}";
    }
}
