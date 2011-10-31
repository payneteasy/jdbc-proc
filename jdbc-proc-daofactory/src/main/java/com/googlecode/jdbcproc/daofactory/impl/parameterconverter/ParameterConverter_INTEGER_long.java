package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  INTEGER - long
 */
public class ParameterConverter_INTEGER_long 
    implements IParameterConverter<ParameterConverter_INTEGER_long, Long> {

  public static final Type<ParameterConverter_INTEGER_long> TYPE 
      = new Type<ParameterConverter_INTEGER_long>(Types.INTEGER, long.class);
  
    public void setValue(Long aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.INTEGER);
        }
    }

    public void setValue(Long aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.INTEGER);
        }
    }

    public Long getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getLong(aParameterName);
    }

    public Long getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getLong(aParameterName);
    }

  public Type<ParameterConverter_INTEGER_long> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_INTEGER_long{}";
    }
}
