package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Returns Iterator
 */
public class ResultSetConverterBlockEntityIterator implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntityIterator(ResultSetConverterBlockEntity aBlock) {
        theBlock = aBlock;
    }

    public Object convertResultSet(IResultSetConverterContext aContext) throws SQLException {
        final ResultSet aResultSet = aContext.getResultSet();
        Objects.requireNonNull(aResultSet, "ResultSet is null");

        aResultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
        aResultSet.setFetchSize(1);
        
        return new CloseableIteratorImpl(aResultSet, aContext.getCallableStatement(), aContext.getDataSource()) {
            @Override
            protected Object readCurrentRow(ResultSet resultSet) {
                return theBlock.createEntity(aResultSet);
            }
        };

    }

    private final ResultSetConverterBlockEntity theBlock;

}
