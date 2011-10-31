package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  LONGVARBINARY - byte[]
 */
public class ParameterConverter_LONGVARBINARY_byteArray 
    implements IParameterConverter<ParameterConverter_LONGVARBINARY_byteArray, byte[]> {

  public static final Type<ParameterConverter_LONGVARBINARY_byteArray> TYPE 
      = new Type<ParameterConverter_LONGVARBINARY_byteArray>(Types.LONGVARBINARY, byte[].class);
  
    public void setValue(byte[] aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setBytes(aIndex, aValue);
    }

    public void setValue(byte[] aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        aStmt.setBytes(aParameterName, aValue);
    }

    public byte[] getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getBytes(aParameterName);
    }

    public byte[] getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBytes(aParameterName);
    }

  public Type<ParameterConverter_LONGVARBINARY_byteArray> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_LONGVARBINARY_byteArray{}";
    }
}
