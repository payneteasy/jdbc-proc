package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  BIGINT - long
 */
public class ParameterConverter_BIGINT_long implements IParameterConverter<Long> {

    public void setValue(Long aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.BIGINT);
        }
    }

    public void setValue(Long aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setLong(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.BIGINT);
        }
    }

    public Long getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getLong(aParameterName);
    }

    public Long getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getLong(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.BIGINT, long.class);
    }
}