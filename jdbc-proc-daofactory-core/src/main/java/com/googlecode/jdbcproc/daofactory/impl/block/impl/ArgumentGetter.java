package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Gets parameter from argument and sets to Callable Statement
 */
public class ArgumentGetter {

    public ArgumentGetter(IParameterConverter aParameterConverter, StatementArgument aStatementArgument) {
        theParameterConverter = aParameterConverter;
        theStatementArgument = aStatementArgument;
    }

    public void setParameter(Object aValue, ICallableStatementSetStrategy aStmt) throws SQLException {
        theParameterConverter.setValue(aValue, aStmt, theStatementArgument);
    }


    public String toString() {
        return "ArgumentGetter{" +
                "theParameterConverter=" + theParameterConverter +
                ", theStatementArgument='" + theStatementArgument + '\'' +
                '}';
    }

    private final IParameterConverter theParameterConverter;
    private final StatementArgument theStatementArgument;
}