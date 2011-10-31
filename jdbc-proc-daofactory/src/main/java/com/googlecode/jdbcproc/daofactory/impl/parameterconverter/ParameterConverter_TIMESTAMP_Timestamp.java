package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  TIMESTAMP - java.sql.Timestamp
 */
public class ParameterConverter_TIMESTAMP_Timestamp 
    implements IParameterConverter<ParameterConverter_TIMESTAMP_Timestamp, Timestamp> {

  public static final Type<ParameterConverter_TIMESTAMP_Timestamp> TYPE 
      = new Type<ParameterConverter_TIMESTAMP_Timestamp>(Types.TIMESTAMP, Timestamp.class);
  
    public void setValue(Timestamp aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setTimestamp(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.TIMESTAMP);
        }
    }

    public void setValue(Timestamp aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setTimestamp(aArgument, aValue);
        } else {
            aStmt.setNull(aArgument, Types.TIMESTAMP);
        }
    }

    public Timestamp getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getTimestamp(aParameterName);
    }

    public Timestamp getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getTimestamp(aParameterName);
    }

  public Type<ParameterConverter_TIMESTAMP_Timestamp> getType() {
    return TYPE;
  }
  
    public String toString() {
        return "ParameterConverter_TIMESTAMP_Timestamp{}";
    }
}
