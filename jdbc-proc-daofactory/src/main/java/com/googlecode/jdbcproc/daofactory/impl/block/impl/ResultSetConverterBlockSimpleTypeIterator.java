package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
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

        return new CloseableIterator() {
            public boolean hasNext() {
                if(theHasNext) {
                    try {
                        theHasNext = aResultSet.next();
                        if(!theHasNext) {
                            try {
                                aResultSet.close();
                            } finally {
                                aStmt.closeStatement();
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException("Error while Iterator.hasNext(): "+e.getMessage(), e);
                    }
                }

                return theHasNext;
            }

            public Object next() {
                try {
                    return theConverter.getFromResultSet(aResultSet, theColumnName);
                } catch (SQLException e) {
                    throw new IllegalStateException("Error getting value for column "+theColumnName);
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("remove method is unsupported");
            }

            public void close() {
                if(theHasNext) {
                    try {
                        try {
                            aResultSet.close();
                        } finally {
                            aStmt.closeStatement();
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException("Unable to close ResultSet or CallableStatement: "+e.getMessage(), e);
                    }
                }
            }

            public String toString() {
                return "ResultSetConverterBlockSimpleTypeIterator$CloseableIterator[hasNext="+theHasNext+", converter="+theConverter+", columnName="+theColumnName+"]";
            }

            private boolean theHasNext = true;
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