package com.googlecode.jdbcproc.daofactory.internal;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ResultSetConverterRowIterator implements IResultSetConverterBlock {

    @Override public Object convertResultSet(IResultSetConverterContext aContext) throws SQLException {
        ResultSet resultSet = aContext.getResultSet();
        Objects.requireNonNull(resultSet, "ResultSet is null");

        resultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
        resultSet.setFetchSize(1);

        return new RowIteratorImpl(resultSet, aContext.getCallableStatement(), aContext.getDataSource());
    }
}
