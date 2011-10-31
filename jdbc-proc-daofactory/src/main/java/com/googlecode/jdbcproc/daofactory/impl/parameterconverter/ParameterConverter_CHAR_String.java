package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  CHAR - String
 */
public class ParameterConverter_CHAR_String
    implements IParameterConverter<ParameterConverter_CHAR_String, String> {

  public static final Type<ParameterConverter_CHAR_String> TYPE
      = new Type<ParameterConverter_CHAR_String>(Types.CHAR, String.class);

  public void setValue(String value, PreparedStatement stmt, int index) throws SQLException {
    stmt.setString(index, value);
  }

  public void setValue(String value, CallableStatement stmt, String parameterName)
      throws SQLException {
    stmt.setString(parameterName, value);
  }

  public String getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName)
      throws SQLException {
    return aStmt.getString(aParameterName);
  }

  public String getFromResultSet(ResultSet resultSet, String parameterName) throws SQLException {
    return resultSet.getString(parameterName);
  }

  public Type<ParameterConverter_CHAR_String> getType() {
    return TYPE;
  }

  public String toString() {
    return "ParameterConverter_CHAR_String{}";
  }
}
