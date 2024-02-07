package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Converts first column from result set to java type and returns collection
 */
public class ResultSetConverterBlockSimpleTypeList implements IResultSetConverterBlock {

    public ResultSetConverterBlockSimpleTypeList(IParameterConverter aConverter, String aColumnName) {
        theConverter = aConverter;
        theColumnName = aColumnName;
    }

    public Object convertResultSet(IResultSetConverterContext aContext) throws SQLException {
        List list = new ArrayList<>();
        final ResultSet aResultSet = aContext.getResultSet();
        while(aResultSet.next()) {
            Object value = theConverter.getFromResultSet(aResultSet, theColumnName);
            list.add(value);
        }
        return Collections.unmodifiableList(list);
    }

    public String toString() {
        return "ResultSetConverterBlockSimpleTypeList{" +
                "theConverter=" + theConverter +
                ", theColumnName='" + theColumnName + '\'' +
                '}';
    }

    private final IParameterConverter theConverter;
    private String theColumnName;

}

