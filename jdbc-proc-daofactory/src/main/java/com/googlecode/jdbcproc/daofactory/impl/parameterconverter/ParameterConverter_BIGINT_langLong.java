package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  BIGINT - java.lang.Long
 */
public class ParameterConverter_BIGINT_langLong 
    implements IParameterConverter<ParameterConverter_BIGINT_langLong, Long> {

  public static final Type<ParameterConverter_BIGINT_langLong> TYPE 
      = new Type<ParameterConverter_BIGINT_langLong>(Types.BIGINT, Long.class);
  
    public void setValue(Long aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.BIGINT);
        }
    }

    public void setValue(Long aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.BIGINT);
        }
    }

    public Long getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        Long value = aStmt.getLong(aParameterName);
        return aStmt.wasNull() ? null : value;
    }

    public Long getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        Long value = aResultSet.getLong(aParameterName);
        return aResultSet.wasNull() ? null : value;
    }

  public Type<ParameterConverter_BIGINT_langLong> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_BIGINT_langLong{}";
    }
}
