package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  LONGVARBINARY - byte[]
 */
public class ParameterConverter_LONGVARBINARY_byteArray 
    implements IParameterConverter<ParameterConverter_LONGVARBINARY_byteArray, byte[]> {

  public static final Type<ParameterConverter_LONGVARBINARY_byteArray> TYPE 
      = new Type<ParameterConverter_LONGVARBINARY_byteArray>(Types.LONGVARBINARY, byte[].class);
  
    public void setValue(byte[] aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        aStmt.setBytes(aArgument, aValue);
    }

    public byte[] getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
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
