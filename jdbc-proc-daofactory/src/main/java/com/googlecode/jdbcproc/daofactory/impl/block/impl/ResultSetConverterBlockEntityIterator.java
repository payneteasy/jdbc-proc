package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementCloser;
import org.springframework.util.Assert;

/**
 * Returns Iterator
 */
public class ResultSetConverterBlockEntityIterator implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntityIterator(ResultSetConverterBlockEntity aBlock) {
        theBlock = aBlock;
    }

    public Object convertResultSet(final ResultSet aResultSet, final StatementCloser aStmt) throws SQLException {
        Assert.notNull(aResultSet, "ResultSet is null");

        aResultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
        aResultSet.setFetchSize(1);
        
        return new CloseableIteratorImpl(aResultSet, aStmt) {
            @Override
            protected Object readCurrentRow(ResultSet resultSet) {
                return theBlock.createEntity(aResultSet);
            }
        };

    }

    private final ResultSetConverterBlockEntity theBlock;

}
