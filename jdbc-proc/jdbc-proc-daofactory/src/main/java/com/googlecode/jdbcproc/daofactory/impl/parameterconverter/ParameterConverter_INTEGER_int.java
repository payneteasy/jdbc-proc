package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  INTEGER - int
 */
public class ParameterConverter_INTEGER_int 
    implements IParameterConverter<ParameterConverter_INTEGER_int, Integer> {

  public static final Type<ParameterConverter_INTEGER_int> TYPE 
      = new Type<ParameterConverter_INTEGER_int>(Types.INTEGER, int.class);
  
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

  public Type<ParameterConverter_INTEGER_int> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_INTEGER_int{}";
    }
}
