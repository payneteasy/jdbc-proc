package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementCloser;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Converts first column from result set to java type and iterator
 */
public class ResultSetConverterBlockSimpleTypeIterator implements IResultSetConverterBlock {

    public ResultSetConverterBlockSimpleTypeIterator(IParameterConverter aConverter, String aColumnName) {
        theConverter = aConverter;
        theColumnName = aColumnName;
    }

    public Object convertResultSet(final ResultSet aResultSet, final StatementCloser aStmt) throws SQLException {

        Assert.notNull(aResultSet, "ResultSet is null");

        aResultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
        aResultSet.setFetchSize(1);

        return new CloseableIteratorImpl(aResultSet, aStmt) {
            public Object next() {
                try {
                    return theConverter.getFromResultSet(aResultSet, theColumnName);
                } catch (SQLException e) {
                    throw new IllegalStateException("Error getting value for column "+theColumnName);
                }
            }

            public String toString() {
                return "ResultSetConverterBlockSimpleTypeIterator$CloseableIterator[hasNext="+isHasNext()+", converter="+theConverter+", columnName="+theColumnName+"]";
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