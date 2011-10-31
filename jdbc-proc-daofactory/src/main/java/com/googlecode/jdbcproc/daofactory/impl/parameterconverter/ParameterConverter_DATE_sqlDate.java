package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  DATE - java.sql.Date
 */
public class ParameterConverter_DATE_sqlDate 
    implements IParameterConverter<ParameterConverter_DATE_sqlDate, Date> {

  public static final Type<ParameterConverter_DATE_sqlDate> TYPE 
      = new Type<ParameterConverter_DATE_sqlDate>(Types.DATE, Date.class);
  
    public void setValue(Date aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        if(aValue!=null) {
            aStmt.setDate(aIndex, aValue);
        } else {
            aStmt.setNull(aIndex, Types.DATE);
        }
    }

    public void setValue(Date aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setDate(aArgument, aValue);
        } else {
            aStmt.setNull(aArgument, Types.DATE);
        }
    }

    public Date getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getDate(aParameterName);
    }

    public Date getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getDate(aParameterName);
    }

  public Type<ParameterConverter_DATE_sqlDate> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_DATE_sqlDate{}";
    }
}
