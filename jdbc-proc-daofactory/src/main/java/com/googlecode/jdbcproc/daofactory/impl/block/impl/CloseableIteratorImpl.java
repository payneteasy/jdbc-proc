package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementCloser;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @author rpuch
*/
abstract class CloseableIteratorImpl implements CloseableIterator {
    private final ResultSet aResultSet;
    private final StatementCloser aStmt;

    public CloseableIteratorImpl(ResultSet aResultSet, StatementCloser aStmt) {
        this.aResultSet = aResultSet;
        this.aStmt = aStmt;
        theHasNext = true;
    }

    public final boolean hasNext() {
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

    public final void remove() {
        throw new UnsupportedOperationException("remove method is unsupported");
    }

    public final void close() {
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

    protected boolean isHasNext() {
        return theHasNext;
    }

    private boolean theHasNext;
}
