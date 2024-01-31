package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
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
  
    public void setValue(Double aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setBigDecimal(aArgument, new BigDecimal(aValue));
        } else {
            aStmt.setBigDecimal(aArgument, null);
        }
    }

    public Double getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
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
