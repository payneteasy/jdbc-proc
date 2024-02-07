package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

/**
* CloseableIterator framework.
*/
abstract class CloseableIteratorImpl implements CloseableIterator {
    private final ResultSet resultSet;
    private final CallableStatement stmt;
    private final DataSource dataSource;

    private boolean positionedToRow = false;
    private boolean reachedEnd = false;
    private boolean closed = false;


    CloseableIteratorImpl(ResultSet resultSet, CallableStatement stmt, DataSource dataSource) {
        this.resultSet = resultSet;
        this.stmt = stmt;
        this.dataSource = dataSource;
    }

    public final boolean hasNext() {
        return positionIfNeeded();
    }

    private boolean positionIfNeeded() {
        if (reachedEnd || closed) {
            return false;
        } else if (positionedToRow) {
            return true;
        } else {
            try {
                boolean currentPositionIsValid = resultSet.next();
                if (currentPositionIsValid) {
                    positionedToRow = true;
                } else {
                    reachedEnd = true;
                    positionedToRow = false;
                    doClose();
                }

                return currentPositionIsValid;
            } catch (SQLException e) {
                throw new RuntimeException("Error while Iterator.hasNext(): " + e.getMessage(), e);
            }
        }
    }

    public final Object next() {
        boolean hasNext = positionIfNeeded();
        if (hasNext) {
            // flag reset to force next record to be probed
            positionedToRow = false;
            return readCurrentRow(resultSet);
        } else {
            throw new NoSuchElementException();
        }
    }

    public final void remove() {
        throw new UnsupportedOperationException("remove method is unsupported");
    }

    public final void close() {
        if (!closed) {
            try {
                doClose();
            } catch (Exception e) {
                throw new IllegalStateException("Unable to close ResultSet or CallableStatement: "+e.getMessage(), e);
            }
        }
    }

    private void doClose() throws SQLException {
        closed = true;

        try {
            resultSet.close();
        } finally {
            DataSourceUtils.releaseConnection(stmt.getConnection(), dataSource);
            stmt.close();
        }
    }

    protected abstract Object readCurrentRow(ResultSet resultSet);
}
