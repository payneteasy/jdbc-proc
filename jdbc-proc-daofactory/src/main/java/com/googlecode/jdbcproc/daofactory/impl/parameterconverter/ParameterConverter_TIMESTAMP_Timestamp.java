package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
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

    public void setValue(Timestamp aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setTimestamp(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.TIMESTAMP);
        }
    }

    public Timestamp getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
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
