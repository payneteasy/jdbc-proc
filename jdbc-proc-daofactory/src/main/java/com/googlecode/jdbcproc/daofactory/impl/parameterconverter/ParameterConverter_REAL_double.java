package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  DECIMAL - boolean
 */
public class ParameterConverter_REAL_double 
    implements IParameterConverter<ParameterConverter_REAL_double, Double> {

  public static final Type<ParameterConverter_REAL_double> TYPE 
      = new Type<ParameterConverter_REAL_double>(Types.REAL, double.class);
  
    public void setValue(Double aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setDouble(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.REAL);
        }
    }

    public void setValue(Double aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setDouble(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.REAL);
        }
    }

    public Double getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        Double value = aStmt.getDouble(aParameterName);
        return aStmt.wasNull() ? null : value;
    }

    public Double getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        Double value = aResultSet.getDouble(aParameterName);
        return aResultSet.wasNull() ? null : value;
    }

  public Type<ParameterConverter_REAL_double> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_REAL_double{}";
    }
}
