package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  DATE - java.sql.Date
 */
public class ParameterConverter_DATE_sqlDate implements IParameterConverter<Date> {

    public void setValue(Date aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setDate(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.DATE);
        }
    }

    public void setValue(Date aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setDate(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.DATE);
        }
    }

    public Date getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getDate(aParameterName);
    }

    public Date getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getDate(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.DATE, Date.class);
    }
}