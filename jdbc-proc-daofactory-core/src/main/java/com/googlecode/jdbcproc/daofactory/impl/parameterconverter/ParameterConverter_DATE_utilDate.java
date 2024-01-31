package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  DATE - java.util.Date
 */
public class ParameterConverter_DATE_utilDate 
    implements IParameterConverter<ParameterConverter_DATE_utilDate, java.util.Date> {

  public static final Type<ParameterConverter_DATE_utilDate> TYPE 
      = new Type<ParameterConverter_DATE_utilDate>(Types.DATE, java.util.Date.class);
  
    public void setValue(java.util.Date aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            java.sql.Date sqlDate = new java.sql.Date(aValue.getTime());
            aStmt.setDate(aArgument, sqlDate);
        } else {
            aStmt.setNull(aArgument, Types.DATE);
        }
    }

    public java.util.Date getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getDate(aParameterName);
    }

    public java.util.Date getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getDate(aParameterName);
    }

  public Type<ParameterConverter_DATE_utilDate> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_DATE_utilDate{}";
    }
}
