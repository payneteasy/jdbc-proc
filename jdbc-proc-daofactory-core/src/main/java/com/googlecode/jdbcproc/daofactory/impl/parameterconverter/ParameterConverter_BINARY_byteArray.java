package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *  BINARY - byte[]
 */
public class ParameterConverter_BINARY_byteArray 
    implements IParameterConverter<ParameterConverter_BINARY_byteArray, byte[]> {

  public static final Type<ParameterConverter_BINARY_byteArray> TYPE = new Type<ParameterConverter_BINARY_byteArray>(Types.BINARY, byte[].class);
  
    public void setValue(byte[] aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        aStmt.setBytes(aArgument, aValue);
    }

    public byte[] getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getBytes(aParameterName);
    }

    public byte[] getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBytes(aParameterName);
    }

  public Type<ParameterConverter_BINARY_byteArray> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_BINARY_byteArray{}";
    }
}
