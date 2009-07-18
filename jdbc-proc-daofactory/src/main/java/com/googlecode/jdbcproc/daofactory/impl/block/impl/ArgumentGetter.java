package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Gets parameter from argument and sets to Callable Statement
 */
public class ArgumentGetter {

    public ArgumentGetter(IParameterConverter aParameterConverter, String aParameterName) {
        theParameterConverter = aParameterConverter;
        theParameterName = aParameterName;
    }

    public void setParameter(Object aValue, CallableStatement aStmt) throws SQLException {
        theParameterConverter.setValue(aValue, aStmt, theParameterName);
    }

    private final IParameterConverter theParameterConverter;
    private final String theParameterName ;
}