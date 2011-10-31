package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementCloser;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Converts first column from result set to java type and returns collection
 */
public class ResultSetConverterBlockSimpleTypeList implements IResultSetConverterBlock {

    public ResultSetConverterBlockSimpleTypeList(IParameterConverter aConverter, String aColumnName) {
        theConverter = aConverter;
        theColumnName = aColumnName;
    }

    public Object convertResultSet(ResultSet aResultSet, StatementCloser aStmt) throws SQLException {
        List list = new LinkedList();
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

