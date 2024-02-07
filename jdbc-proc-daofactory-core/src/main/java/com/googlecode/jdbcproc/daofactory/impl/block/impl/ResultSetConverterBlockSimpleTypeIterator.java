package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Converts first column from result set to java type and iterator
 */
public class ResultSetConverterBlockSimpleTypeIterator implements IResultSetConverterBlock {

    public ResultSetConverterBlockSimpleTypeIterator(IParameterConverter aConverter, String aColumnName) {
        theConverter = aConverter;
        theColumnName = aColumnName;
    }

    public Object convertResultSet(IResultSetConverterContext aContext) throws SQLException {
        ResultSet aResultSet = aContext.getResultSet();
        Objects.requireNonNull(aResultSet, "ResultSet is null");

        aResultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
        aResultSet.setFetchSize(1);

        return new CloseableIteratorImpl(aResultSet, aContext.getCallableStatement(), aContext.getDataSource()) {
            @Override
            protected Object readCurrentRow(ResultSet resultSet) {
                try {
                    return theConverter.getFromResultSet(aResultSet, theColumnName);
                } catch (SQLException e) {
                    throw new IllegalStateException("Error getting value for column "+theColumnName);
                }
            }

            public String toString() {
                return "ResultSetConverterBlockSimpleTypeIterator$CloseableIterator[converter="+theConverter+", columnName="+theColumnName+"]";
            }
        };
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
