package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  TIMESTAMP - java.sql.Timestamp
 */
public class ParameterConverter_TIMESTAMP_Timestamp implements IParameterConverter<Timestamp> {

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

    public Timestamp getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getTimestamp(aParameterName);
    }

    public Timestamp getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getTimestamp(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.TIMESTAMP, Timestamp.class);
    }
}