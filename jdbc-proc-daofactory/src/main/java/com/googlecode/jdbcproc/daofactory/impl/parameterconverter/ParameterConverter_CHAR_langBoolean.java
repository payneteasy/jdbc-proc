package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  VARCHAR - Boolean
 */
public class ParameterConverter_CHAR_langBoolean
    implements IParameterConverter<ParameterConverter_CHAR_langBoolean, Boolean> {

  public static final Type<ParameterConverter_CHAR_langBoolean> TYPE = new Type<ParameterConverter_CHAR_langBoolean>(Types.CHAR, Boolean.class);

  public void setValue(Boolean aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
      if( aValue != null ) {
          aStmt.setString(aIndex, aValue ? "Y" : "N");
      } else {
          aStmt.setString(aIndex, null);
      }
  }

  public void setValue(Boolean aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
      if( aValue != null ) {
          aStmt.setString(aParameterName, aValue ? "Y" : "N");
      } else {
          aStmt.setString(aParameterName, null);
      }
  }

  public Boolean getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
      String strValue = aStmt.getString(aParameterName);
      return aStmt.wasNull() ? null : "Y".equals(strValue);
  }

  public Boolean getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
      String strValue = aResultSet.getString(aParameterName);
      return aResultSet.wasNull() ? null : "Y".equals(strValue);
  }

  public Type<ParameterConverter_CHAR_langBoolean> getType() {
    return TYPE;
  }

  public String toString() {
    return "ParameterConverter_CHAR_langBoolean{}";
  }
}
