package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  VARCHAR - Boolean
 */
public class ParameterConverter_VARCHAR_langBoolean
    implements IParameterConverter<ParameterConverter_VARCHAR_langBoolean, Boolean> {

  public static final Type<ParameterConverter_VARCHAR_langBoolean> TYPE
      = new Type<ParameterConverter_VARCHAR_langBoolean>(Types.VARCHAR, Boolean.class);

  public void setValue(Boolean value, PreparedStatement stmt, int index) throws SQLException {
    String strValue = value != null && value ? "Y" : "N";
    stmt.setString(index, strValue);
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

  public Type<ParameterConverter_VARCHAR_langBoolean> getType() {
    return TYPE;
  }

  public String toString() {
    return "ParameterConverter_VARCHAR_langBoolean{}";
  }
}
