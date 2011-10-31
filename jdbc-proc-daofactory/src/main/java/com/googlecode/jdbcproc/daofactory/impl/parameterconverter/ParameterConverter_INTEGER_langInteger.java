package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  INTEGER - int
 */
public class ParameterConverter_INTEGER_langInteger 
    implements IParameterConverter<ParameterConverter_INTEGER_langInteger, Integer> {

  public static final Type<ParameterConverter_INTEGER_langInteger> TYPE 
      = new Type<ParameterConverter_INTEGER_langInteger>(Types.INTEGER, Integer.class);
  
    public void setValue(Integer aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setInt(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.INTEGER);
        }
    }

    public void setValue(Integer aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setInt(aArgument, aValue);
        } else {
            aStmt.setNull(aArgument, Types.INTEGER);
        }
    }

    public Integer getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        Integer value = aStmt.getInt(aParameterName);
        return aStmt.wasNull() ? null : value;
    }

    public Integer getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        Integer value = aResultSet.getInt(aParameterName);
        return aResultSet.wasNull() ? null : value;
    }

  public Type<ParameterConverter_INTEGER_langInteger> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_INTEGER_langInteger{}";
    }
}
