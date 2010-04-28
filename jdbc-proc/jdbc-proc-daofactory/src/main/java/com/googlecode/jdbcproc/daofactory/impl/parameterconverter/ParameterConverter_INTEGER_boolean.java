package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  INTEGER - boolean
 */
public class ParameterConverter_INTEGER_boolean 
    implements IParameterConverter<ParameterConverter_INTEGER_boolean, Boolean> {

  public static final Type<ParameterConverter_INTEGER_boolean> TYPE 
      = new Type<ParameterConverter_INTEGER_boolean>(Types.INTEGER, boolean.class);
  
    public void setValue(Boolean aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        int intValue = aValue!=null && aValue ? 1 : 0;
        aStmt.setInt(aIndex, intValue);
    }

    public void setValue(Boolean aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        int intValue = aValue!=null && aValue ? 1 : 0;
        aStmt.setInt(aParameterName, intValue);
    }

    public Boolean getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        int intValue = aStmt.getInt(aParameterName);
        return intValue==1;
    }

    public Boolean getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        int intValue = aResultSet.getInt(aParameterName);
        return intValue==1;
    }

  public Type<ParameterConverter_INTEGER_boolean> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_INTEGER_boolean{}";
    }
}
