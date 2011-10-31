package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  VARCHAR - Boolean
 */
public class ParameterConverter_VARCHAR_langBoolean
    implements IParameterConverter<ParameterConverter_VARCHAR_langBoolean, Boolean> {

  public static final Type<ParameterConverter_VARCHAR_langBoolean> TYPE
      = new Type<ParameterConverter_VARCHAR_langBoolean>(Types.VARCHAR, Boolean.class);

    public void setValue(Boolean aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if( aValue != null ) {
            aStmt.setString(aArgument, aValue ? "Y" : "N");
        } else {
            aStmt.setString(aArgument, null);
        }
    }

    public Boolean getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        String strValue = aStmt.getString(aParameterName);
        return aStmt.wasNull() ? null : "Y".equals(strValue);
    }

    public Boolean getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        String strValue = aResultSet.getString(aParameterName);
        return aResultSet.wasNull() ? null : "Y".equals(strValue);
    }

  public Type<ParameterConverter_VARCHAR_langBoolean> getType() {
    return TYPE;
  }

  public String toString() {
    return "ParameterConverter_VARCHAR_langBoolean{}";
  }
}
