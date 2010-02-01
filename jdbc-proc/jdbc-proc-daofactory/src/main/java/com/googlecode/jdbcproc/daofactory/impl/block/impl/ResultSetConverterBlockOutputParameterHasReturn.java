package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Return from output parameter instead of ResultSet
 */
public class ResultSetConverterBlockOutputParameterHasReturn implements IResultSetConverterBlock {

    public ResultSetConverterBlockOutputParameterHasReturn(
            IParameterConverter aParameterConverter
          , String aParameterName
    ) {
        theParameterConverter = aParameterConverter;
        theParameterName      = aParameterName;
    }

    public Object convertResultSet(ResultSet aResultSet, CallableStatement aStmt) throws SQLException {
        return theParameterConverter.getOutputParameter(aStmt, theParameterName);
    }

    private final IParameterConverter theParameterConverter;
    private final String theParameterName;
}
