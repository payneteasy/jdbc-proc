package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  LONGVARCHAR - String
 */
public class ParameterConverter_LONGVARCHAR_String 
    implements IParameterConverter<ParameterConverter_LONGVARCHAR_String, String> {

  public static final Type<ParameterConverter_LONGVARCHAR_String> TYPE 
      = new Type<ParameterConverter_LONGVARCHAR_String>(Types.LONGVARCHAR, String.class);

    public void setValue(String aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        aStmt.setString(aArgument, aValue);
    }

    public String getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getString(aParameterName);
    }

    public String getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getString(aParameterName);
    }

  public Type<ParameterConverter_LONGVARCHAR_String> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_LONGVARCHAR_String{}";
    }
}
