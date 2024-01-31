package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  BIGINT - java.lang.Long
 */
public class ParameterConverter_BIGINT_langLong 
    implements IParameterConverter<ParameterConverter_BIGINT_langLong, Long> {

  public static final Type<ParameterConverter_BIGINT_langLong> TYPE 
      = new Type<ParameterConverter_BIGINT_langLong>(Types.BIGINT, Long.class);
  
    public void setValue(Long aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aArgument, aValue);
        } else {
            aStmt.setNull(aArgument, Types.BIGINT);
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

  public Type<ParameterConverter_BIGINT_langLong> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_BIGINT_langLong{}";
    }
}
