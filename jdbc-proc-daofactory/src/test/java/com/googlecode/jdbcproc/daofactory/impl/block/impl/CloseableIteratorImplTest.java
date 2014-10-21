package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.CallableStatementAdapter;
import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.ResultSetAdapter;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementCloser;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class CloseableIteratorImplTest {
    @Test
    public void testHasNextDoesNotMoveResultSetPointer() {
        TestResultSet resultSet = new TestResultSet();
        StatementCloser closer = new StatementCloser(new CallableStatementAdapter());
        CloseableIterator iterator = new CloseableIteratorImpl(resultSet, closer) {
            public Object next() {
                return "abc";
            }
        };

        iterator.hasNext();
        Assert.assertFalse("hasNext() should not call result set's next(), but it seems to do it", resultSet.isNextCalled());
        Assert.assertEquals("abc", iterator.next());
    }

    private static class TestResultSet extends ResultSetAdapter {
        private boolean nextCalled = false;

        public boolean isNextCalled() {
            return nextCalled;
        }

        @Override
        public boolean next() throws SQLException {
            nextCalled = true;
            return true;
        }
    }
}