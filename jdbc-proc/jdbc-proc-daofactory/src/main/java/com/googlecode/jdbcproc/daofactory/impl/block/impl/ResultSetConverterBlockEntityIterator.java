package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.CloseableIterator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.*;

import org.springframework.util.Assert;

/**
 * Returns Iterator
 */
public class ResultSetConverterBlockEntityIterator implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntityIterator(ResultSetConverterBlockEntity aBlock) {
        theBlock = aBlock;
    }

    public Object convertResultSet(final ResultSet aResultSet, final CallableStatement aStmt) throws SQLException {
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
                                aStmt.close();
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException("Error while Iterator.hasNext(): "+e.getMessage(), e);
                    }
                }
                
                return theHasNext;
            }

            public Object next() {
                return theBlock.createEntity(aResultSet);
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
                            aStmt.close();
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException("Unable to close ResultSet or CallableStatement: "+e.getMessage(), e);
                    }
                }
            }

            private boolean theHasNext = true;
        };

    }

    private final ResultSetConverterBlockEntity theBlock;
}
