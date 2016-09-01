package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *  BIGINT - BigInteger
 */
public class ParameterConverter_BIGINT_BigInteger
    implements IParameterConverter<ParameterConverter_BIGINT_BigInteger, BigInteger> {

  public static final Type<ParameterConverter_BIGINT_BigInteger> TYPE
      = new Type<ParameterConverter_BIGINT_BigInteger>(Types.BIGINT, BigInteger.class);
  
    public void setValue(BigInteger aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if(aValue!=null) {
            aStmt.setString(aArgument, aValue.toString());
        } else {
            aStmt.setNull(aArgument, Types.BIGINT);
        }
    }

    public BigInteger getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return new BigInteger(aStmt.getString(aParameterName));
    }

    public BigInteger getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return new BigInteger(aResultSet.getString(aParameterName));
    }

  public Type<ParameterConverter_BIGINT_BigInteger> getType() {
    return TYPE;
  }

    public String toString() {
        return "ParameterConverter_BIGINT_BigInteger{}";
    }
}
