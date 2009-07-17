package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  INTEGER - int
 */
public class ParameterConverter_INTEGER_int implements IParameterConverter<Integer> {

    public void setValue(Integer aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setInt(aIndex, aValue);
    }

    public void setValue(Integer aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        aStmt.setInt(aParameterName, aValue);
    }

    public Integer getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getInt(aParameterName);
    }

    public Integer getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getInt(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.INTEGER, int.class);
    }
}