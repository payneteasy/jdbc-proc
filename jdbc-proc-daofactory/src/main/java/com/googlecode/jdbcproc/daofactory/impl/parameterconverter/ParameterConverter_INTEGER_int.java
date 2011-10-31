package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  INTEGER - int
 */
public class ParameterConverter_INTEGER_int 
    implements IParameterConverter<ParameterConverter_INTEGER_int, Integer> {

  public static final Type<ParameterConverter_INTEGER_int> TYPE 
      = new Type<ParameterConverter_INTEGER_int>(Types.INTEGER, int.class);
  
    public void setValue(Integer aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setInt(aIndex, aValue);
    }

    public void setValue(Integer aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        aStmt.setInt(aArgument, aValue);
    }

    public Integer getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getInt(aParameterName);
    }

    public Integer getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getInt(aParameterName);
    }

  public Type<ParameterConverter_INTEGER_int> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_INTEGER_int{}";
    }
}
