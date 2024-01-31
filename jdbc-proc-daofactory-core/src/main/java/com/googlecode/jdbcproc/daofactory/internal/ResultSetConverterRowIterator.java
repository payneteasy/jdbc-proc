package com.googlecode.jdbcproc.daofactory.internal;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ResultSetConverterRowIterator implements IResultSetConverterBlock {

    @Override public Object convertResultSet(ResultSet resultSet, CallableStatement statement) throws SQLException {
        Objects.requireNonNull(resultSet, "ResultSet is null");

        resultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
        resultSet.setFetchSize(1);

        return new RowIteratorImpl(resultSet, statement);
    }
}
