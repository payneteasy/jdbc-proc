package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;
import java.util.Date;

/**
 *  TIMESTAMP - java.util.Date
 */
public class ParameterConverter_TIMESTAMP_utilDate 
    implements IParameterConverter<ParameterConverter_TIMESTAMP_utilDate, java.util.Date> {

  public static final Type<ParameterConverter_TIMESTAMP_utilDate> TYPE 
      = new Type<ParameterConverter_TIMESTAMP_utilDate>(Types.TIMESTAMP, java.util.Date.class);
  
    public void setValue(Date aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            java.sql.Timestamp timestamp = new Timestamp(aValue.getTime());
            aStmt.setTimestamp(aIndex, timestamp);
        } else {
            aStmt.setNull(aIndex, Types.TIMESTAMP);
        }
    }

    public void setValue(java.util.Date aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            java.sql.Timestamp timestamp = new Timestamp(aValue.getTime());
            aStmt.setTimestamp(aParameterName, timestamp);
        } else {
            aStmt.setNull(aParameterName, Types.TIMESTAMP);
        }
    }

    public java.util.Date getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getTimestamp(aParameterName);
    }

    public Date getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getTimestamp(aParameterName);
    }

  public Type<ParameterConverter_TIMESTAMP_utilDate> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_TIMESTAMP_utilDate{}";
    }
}
