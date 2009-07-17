package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 * Uses setObject and getObject without convertation
 */
public class ParameterConverter_ANY_Any implements IParameterConverter {

    public void setValue(Object aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setObject(aIndex, aValue);
    }

    public void setValue(Object aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        aStmt.setObject(aParameterName, aValue);
    }

    public Object getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getObject(aParameterName);
    }

    public Object getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getObject(aParameterName);
    }

    public ParameterSetterKey getKey() {
        throw new IllegalStateException("ParameterConverter_ANY_Any has no key");
    }
}