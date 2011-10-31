package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  REAL - BigDecimal
 */
public class ParameterConverter_REAL_BigDecimal 
    implements IParameterConverter<ParameterConverter_REAL_BigDecimal, BigDecimal> {

  public static final Type<ParameterConverter_REAL_BigDecimal> TYPE 
      = new Type<ParameterConverter_REAL_BigDecimal>(Types.REAL, BigDecimal.class);
  
    public void setValue(BigDecimal aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setBigDecimal(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.REAL);
        }
    }

    public void setValue(BigDecimal aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        if(aValue!=null) {
            aStmt.setBigDecimal(aParameterName, aValue);
        } else {
            aStmt.setNull(aParameterName, Types.REAL);
        }
    }

    public BigDecimal getOutputParameter(ICallableStatementStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getBigDecimal(aParameterName);
    }

    public BigDecimal getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBigDecimal(aParameterName);
    }

  public Type<ParameterConverter_REAL_BigDecimal> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_REAL_BigDecimal{}";
    }
}
