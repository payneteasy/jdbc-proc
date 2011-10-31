package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  DECIMAL - BigDecimal
 */
public class ParameterConverter_DECIMAL_BigDecimal 
    implements IParameterConverter<ParameterConverter_DECIMAL_BigDecimal, BigDecimal> {

  public static final Type<ParameterConverter_DECIMAL_BigDecimal> TYPE 
      = new Type<ParameterConverter_DECIMAL_BigDecimal>(Types.DECIMAL, BigDecimal.class);
  
    public void setValue(BigDecimal aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setBigDecimal(aIndex, aValue);
    }

    public void setValue(BigDecimal aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        aStmt.setBigDecimal(aParameterName, aValue);
    }

    public BigDecimal getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getBigDecimal(aParameterName);
    }

    public BigDecimal getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBigDecimal(aParameterName);
    }

  public Type<ParameterConverter_DECIMAL_BigDecimal> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_DECIMAL_BigDecimal{}";
    }
}
