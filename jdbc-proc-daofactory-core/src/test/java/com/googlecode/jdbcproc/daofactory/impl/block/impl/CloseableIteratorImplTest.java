package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.CallableStatementAdapter;
import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.ResultSetAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CloseableIteratorImplTest {
    @Test
    public void testHasNextDoesNotMoveResultSetPointer() {
        TestResultSet resultSet = new TestResultSet();
        CloseableIterator iterator = new CloseableIteratorImpl(resultSet, new CallableStatementAdapter()) {
            @Override
            protected Object readCurrentRow(ResultSet resultSet) {
                return "abc";
            }
        };

        iterator.hasNext();
        iterator.hasNext();
        Assert.assertEquals("hasNext() should not call result set's next() multiple times, but it seems to do it", 1,
                resultSet.getNextCalledTimes());
        Assert.assertEquals("abc", iterator.next());

        iterator.hasNext();
        Assert.assertEquals(2, resultSet.getNextCalledTimes());
    }

    private static class TestResultSet extends ResultSetAdapter {
        private int nextCalledTimes = 0;

        public int getNextCalledTimes() {
            return nextCalledTimes;
        }

        @Override
        public boolean next() throws SQLException {
            nextCalledTimes++;
            return nextCalledTimes <= 2;
        }
    }
}
