package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  BIGINT - long
 */
public class ParameterConverter_BIGINT_long 
    implements IParameterConverter<ParameterConverter_BIGINT_long, Long> {

  public static final Type<ParameterConverter_BIGINT_long> TYPE 
      = new Type<ParameterConverter_BIGINT_long>(Types.BIGINT, long.class);
  
    public void setValue(Long aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.BIGINT);
        }
    }

    public void setValue(Long aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aArgument, aValue);
        } else {
            aStmt.setNull(aArgument, Types.BIGINT);
        }
    }

    public Long getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getLong(aParameterName);
    }

    public Long getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getLong(aParameterName);
    }

  public Type<ParameterConverter_BIGINT_long> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_BIGINT_long{}";
    }
}
