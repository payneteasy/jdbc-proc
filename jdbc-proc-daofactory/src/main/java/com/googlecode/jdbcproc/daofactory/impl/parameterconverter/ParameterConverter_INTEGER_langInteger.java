package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  INTEGER - int
 */
public class ParameterConverter_INTEGER_langInteger 
    implements IParameterConverter<ParameterConverter_INTEGER_langInteger, Integer> {

  public static final Type<ParameterConverter_INTEGER_langInteger> TYPE 
      = new Type<ParameterConverter_INTEGER_langInteger>(Types.INTEGER, Integer.class);
  
    public void setValue(Integer aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setInt(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.INTEGER);
        }
    }

    public void setValue(Integer aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setInt(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.INTEGER);
        }
    }

    public Integer getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        Integer value = aStmt.getInt(aParameterName);
        return aStmt.wasNull() ? null : value;
    }

    public Integer getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        Integer value = aResultSet.getInt(aParameterName);
        return aResultSet.wasNull() ? null : value;
    }

  public Type<ParameterConverter_INTEGER_langInteger> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_INTEGER_langInteger{}";
    }
}
