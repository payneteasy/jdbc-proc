package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  INTEGER - long
 */
public class ParameterConverter_INTEGER_langLong 
    implements IParameterConverter<ParameterConverter_INTEGER_langLong, Long> {

  public static final Type<ParameterConverter_INTEGER_langLong> TYPE 
      = new Type<ParameterConverter_INTEGER_langLong>(Types.INTEGER, Long.class);
  
    public void setValue(Long aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.INTEGER);
        }
    }

    public void setValue(Long aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aArgument, aValue);
        } else {
            aStmt.setNull(aArgument, Types.INTEGER);
        }
    }

    public Long getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        Long value = aStmt.getLong(aParameterName);
        return aStmt.wasNull() ? null : value;
    }

    public Long getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        Long value = aResultSet.getLong(aParameterName);
        return aResultSet.wasNull() ? null : value;
    }

  public Type<ParameterConverter_INTEGER_langLong> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_INTEGER_langLong{}";
    }
}
