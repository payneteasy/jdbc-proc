package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  CHAR - boolean
 */
public class ParameterConverter_CHAR_boolean 
    implements IParameterConverter<ParameterConverter_CHAR_boolean, Boolean> {

  public static final Type<ParameterConverter_CHAR_boolean> TYPE 
      = new Type<ParameterConverter_CHAR_boolean>(Types.CHAR, boolean.class);
  
    public void setValue(Boolean aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        String strValue = aValue!=null && aValue ? "Y" : "N";
        aStmt.setString(aIndex, strValue);
    }

    public void setValue(Boolean aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        String strValue = aValue!=null && aValue ? "Y" : "N";
        aStmt.setString(aArgument, strValue);
    }

    public Boolean getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        String strValue = aStmt.getString(aParameterName);
        return "Y".equals(strValue);
    }

    public Boolean getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        String strValue = aResultSet.getString(aParameterName);
        return "Y".equals(strValue);
    }

  public Type<ParameterConverter_CHAR_boolean> getType() {
    return TYPE;
  }

  public String toString() {
        return "ParameterConverter_CHAR_boolean{}";
    }
}
