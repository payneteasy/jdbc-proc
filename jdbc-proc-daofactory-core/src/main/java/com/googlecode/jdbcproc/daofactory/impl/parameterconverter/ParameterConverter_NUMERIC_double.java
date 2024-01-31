package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * DECIMAL - boolean
 */
public class ParameterConverter_NUMERIC_double
        implements IParameterConverter<ParameterConverter_NUMERIC_double, Double> {

    public static final Type<ParameterConverter_NUMERIC_double> TYPE
            = new Type<ParameterConverter_NUMERIC_double>(Types.NUMERIC, double.class);

    public void setValue(Double aValue, ICallableStatementSetStrategy aStmt, StatementArgument aArgument) throws SQLException {
        if (aValue != null) {
            aStmt.setBigDecimal(aArgument, new BigDecimal(aValue));
        } else {
            aStmt.setBigDecimal(aArgument, null);
        }
    }

    public Double getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName) throws SQLException {
        BigDecimal decValue = aStmt.getBigDecimal(aParameterName);
        return decValue != null ? decValue.doubleValue() : 0;
    }

    public Double getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        BigDecimal decValue = aResultSet.getBigDecimal(aParameterName);
        return decValue != null ? decValue.doubleValue() : 0;
    }

    public Type<ParameterConverter_NUMERIC_double> getType() {
        return TYPE;
    }

    public String toString() {
        return "ParameterConverter_DECIMAL_double{}";
    }
}
