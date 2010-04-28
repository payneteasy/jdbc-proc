package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  VARCHAR - Boolean
 */
public class ParameterConverter_CHAR_langBoolean
    implements IParameterConverter<ParameterConverter_CHAR_langBoolean, Boolean> {

  public static final Type<ParameterConverter_CHAR_langBoolean> TYPE
      = new Type<ParameterConverter_CHAR_langBoolean>(Types.CHAR, Boolean.class);

  public void setValue(Boolean aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
    String strValue = aValue != null && aValue ? "Y" : "N";
    aStmt.setString(aIndex, strValue);
  }

  public void setValue(Boolean value, CallableStatement stmt, String parameterName)
      throws SQLException {
    String strValue = value != null && value ? "Y" : "N";
    stmt.setString(parameterName, strValue);
  }

  public Boolean getOutputParameter(CallableStatement stmt, String parameterName)
      throws SQLException {
    String strValue = stmt.getString(parameterName);
    return "Y".equals(strValue);
  }

  public Boolean getFromResultSet(ResultSet resultSet, String parameterName) throws SQLException {
    String strValue = resultSet.getString(parameterName);
    return "Y".equals(strValue);
  }

  public Type<ParameterConverter_CHAR_langBoolean> getType() {
    return TYPE;
  }

  public String toString() {
    return "ParameterConverter_CHAR_langBoolean{}";
  }
}
