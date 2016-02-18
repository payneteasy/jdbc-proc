package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *  VARBINARY - byte[]
 */
public class ParameterConverter_VARBINARY_byteArray 
    implements IParameterConverter<ParameterConverter_VARBINARY_byteArray, byte[]> {

  public static final Type<ParameterConverter_VARBINARY_byteArray> TYPE = new Type<ParameterConverter_VARBINARY_byteArray>(Types.VARBINARY, byte[].class);
  
    public void setValue(byte[] aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        aStmt.setBytes(aArgument, aValue);
    }

    public byte[] getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getBytes(aParameterName);
    }

    public byte[] getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBytes(aParameterName);
    }

  public Type<ParameterConverter_VARBINARY_byteArray> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_VARBINARY_byteArray{}";
    }
}
