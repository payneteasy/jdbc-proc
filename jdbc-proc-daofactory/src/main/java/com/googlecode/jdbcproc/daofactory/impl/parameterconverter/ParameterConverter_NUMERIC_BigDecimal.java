package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * DECIMAL - BigDecimal
 */
public class ParameterConverter_NUMERIC_BigDecimal
        implements IParameterConverter<ParameterConverter_NUMERIC_BigDecimal, BigDecimal> {

    public static final Type<ParameterConverter_NUMERIC_BigDecimal> TYPE
            = new Type<ParameterConverter_NUMERIC_BigDecimal>(Types.NUMERIC, BigDecimal.class);

    public void setValue(BigDecimal aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        aStmt.setBigDecimal(aArgument, aValue);
    }

    public BigDecimal getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        return aStmt.getBigDecimal(aParameterName);
    }

    public BigDecimal getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getBigDecimal(aParameterName);
    }

    public Type<ParameterConverter_NUMERIC_BigDecimal> getType() {
        return TYPE;
    }

    public String toString() {
        return "ParameterConverter_DECIMAL_BigDecimal{}";
    }
}
