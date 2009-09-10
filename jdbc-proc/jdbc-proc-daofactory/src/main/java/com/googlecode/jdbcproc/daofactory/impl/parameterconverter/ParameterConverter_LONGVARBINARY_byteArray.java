package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  LONGVARBINARY - byte[]
 */
public class ParameterConverter_LONGVARBINARY_byteArray implements IParameterConverter<byte[]> {

    public void setValue(byte[] aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setBytes(aIndex, aValue);
    }

    public void setValue(byte[] aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        aStmt.setBytes(aParameterName, aValue);
    }

    public byte[] getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getBytes(aParameterName);
    }

    public byte[] getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBytes(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.LONGVARBINARY, byte[].class);
    }
}