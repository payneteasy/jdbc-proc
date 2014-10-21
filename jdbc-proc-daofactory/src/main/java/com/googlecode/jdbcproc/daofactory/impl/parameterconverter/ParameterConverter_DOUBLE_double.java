package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *  REAL - double
 */
public class ParameterConverter_DOUBLE_double
    implements IParameterConverter<ParameterConverter_DOUBLE_double, Double> {

  public static final Type<ParameterConverter_DOUBLE_double> TYPE
      = new Type<ParameterConverter_DOUBLE_double>(Types.DOUBLE, double.class);
  
    public void setValue(Double aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setDouble(aArgument, aValue);
        } else {
            aStmt.setNull(aArgument, Types.DOUBLE);
        }
    }

    public Double getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        Double value = aStmt.getDouble(aParameterName);
        return aStmt.wasNull() ? null : value;
    }

    public Double getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        Double value = aResultSet.getDouble(aParameterName);
        return aResultSet.wasNull() ? null : value;
    }

  public Type<ParameterConverter_DOUBLE_double> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_DOUBLE_double{}";
    }
}
