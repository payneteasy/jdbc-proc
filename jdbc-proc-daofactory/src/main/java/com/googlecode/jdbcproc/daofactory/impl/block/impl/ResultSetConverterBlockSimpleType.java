package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

/**
 * Converts first column from result set to java type
 */
public class ResultSetConverterBlockSimpleType implements IResultSetConverterBlock {

    public ResultSetConverterBlockSimpleType(IParameterConverter aConverter, String aColumnName) {
        theConverter = aConverter;
        theColumnName = aColumnName;
    }

    public Object convertResultSet(ResultSet aResultSet, CallableStatement aStmt) throws SQLException {
        if(aResultSet.next()) {
            Object value = theConverter.getFromResultSet(aResultSet, theColumnName);
            if(aResultSet.next()) {
                throw new IllegalStateException("Result set must return only one record");
            }
            return value;
        } else {
            throw new IllegalStateException("Result set is empty");
        }
    }

    public String toString() {
        return "ResultSetConverterBlockSimpleType{" +
                "theConverter=" + theConverter +
                ", theColumnName='" + theColumnName + '\'' +
                '}';
    }

    private final IParameterConverter theConverter;
    private String theColumnName;
}
