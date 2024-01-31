package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  INTEGER - boolean
 */
public class ParameterConverter_INTEGER_boolean 
    implements IParameterConverter<ParameterConverter_INTEGER_boolean, Boolean> {

  public static final Type<ParameterConverter_INTEGER_boolean> TYPE 
      = new Type<ParameterConverter_INTEGER_boolean>(Types.INTEGER, boolean.class);
  
    public void setValue(Boolean aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        int intValue = aValue!=null && aValue ? 1 : 0;
        aStmt.setInt(aArgument, intValue);
    }

    public Boolean getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        int intValue = aStmt.getInt(aParameterName);
        return intValue==1;
    }

    public Boolean getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        int intValue = aResultSet.getInt(aParameterName);
        return intValue==1;
    }

  public Type<ParameterConverter_INTEGER_boolean> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_INTEGER_boolean{}";
    }
}
