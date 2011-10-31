package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  DECIMAL - boolean
 */
public class ParameterConverter_DECIMAL_langDouble 
    implements IParameterConverter<ParameterConverter_DECIMAL_langDouble, Double> {

  public static final Type<ParameterConverter_DECIMAL_langDouble> TYPE 
      = new Type<ParameterConverter_DECIMAL_langDouble>(Types.DECIMAL, Double.class);  
  
    public void setValue(Double aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setBigDecimal(aIndex, new BigDecimal(aValue));
        } else {
            aStmt.setBigDecimal(aIndex, null);
        }
    }

    public void setValue(Double aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setBigDecimal(aParameterName, new BigDecimal(aValue));
        } else {
            aStmt.setBigDecimal(aParameterName, null);
        }
    }

    public Double getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        BigDecimal decValue = aStmt.getBigDecimal(aParameterName);
        return decValue!=null ? decValue.doubleValue() : null;
    }

    public Double getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        BigDecimal decValue = aResultSet.getBigDecimal(aParameterName);
        return decValue!=null ? decValue.doubleValue() : null;
    }

  public Type<ParameterConverter_DECIMAL_langDouble> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_DECIMAL_langDouble{}";
    }
}
