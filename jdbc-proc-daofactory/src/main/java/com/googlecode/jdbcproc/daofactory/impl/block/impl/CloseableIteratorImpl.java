package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementCloser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
* CloseableIterator framework.
*/
abstract class CloseableIteratorImpl implements CloseableIterator {
    private final ResultSet resultSet;
    private final StatementCloser stmt;

    private boolean positionedToRow = false;
    private boolean reachedEnd = false;
    private boolean closed = false;


    CloseableIteratorImpl(ResultSet resultSet, StatementCloser stmt) {
        this.resultSet = resultSet;
        this.stmt = stmt;
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
            stmt.closeStatement();
        }
    }

    protected abstract Object readCurrentRow(ResultSet resultSet);
}
