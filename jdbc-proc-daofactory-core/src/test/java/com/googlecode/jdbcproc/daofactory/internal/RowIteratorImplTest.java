package com.googlecode.jdbcproc.daofactory.internal;

import com.googlecode.jdbcproc.daofactory.CallableStatementAdapter;
import com.googlecode.jdbcproc.daofactory.ResultSetAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The deferred parameter-setter cleanup (clearing the List-parameter temp table)
 * must run exactly once when a {@link RowIterator} is closed: after the result
 * set is closed (so no streaming cursor is open any more) and before the
 * statement is closed (the cleanup needs the statement's connection).
 */
public class RowIteratorImplTest {

    private static final List<String> CLOSE_WITH_CLEANUP =
            Arrays.asList("resultSet.close", "cleanup", "statement.close");

    private final List<String> events = new ArrayList<String>();

    @Test
    public void testCleanupRunsOnExplicitClose() throws Exception {
        RowIteratorImpl it = newIterator(3);

        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("v1", it.next().getString("name"));
        Assert.assertTrue("cleanup must not run while rows are still being read", events.isEmpty());

        it.close();
        Assert.assertEquals(CLOSE_WITH_CLEANUP, events);

        it.close();
        Assert.assertEquals("second close() must be a no-op", CLOSE_WITH_CLEANUP, events);
    }

    @Test
    public void testCleanupRunsOnceWhenIteratorIsExhausted() throws Exception {
        RowIteratorImpl it = newIterator(2);

        int rows = 0;
        while (it.hasNext()) {
            it.next();
            rows++;
        }
        Assert.assertEquals(2, rows);
        Assert.assertEquals("reaching the end auto-closes the iterator and runs the cleanup",
                CLOSE_WITH_CLEANUP, events);

        it.close();
        Assert.assertEquals("close() after exhaustion must not run the cleanup again",
                CLOSE_WITH_CLEANUP, events);
    }

    @Test
    public void testNullCleanupIsAllowed() throws Exception {
        RowIteratorImpl it = new RowIteratorImpl(new TestResultSet(1), new TestStatement(), null, null);

        while (it.hasNext()) {
            it.next();
        }
        it.close();

        Assert.assertEquals(Arrays.asList("resultSet.close", "statement.close"), events);
    }

    private RowIteratorImpl newIterator(int rows) {
        return new RowIteratorImpl(new TestResultSet(rows), new TestStatement(), null,
                () -> events.add("cleanup"));
    }

    /**
     * Result set in the RowIterator format: a header row describing one column
     * ({@code columns = "1"}, column 2 = {@code "name:varchar"}) followed by
     * {@code rows} data rows with {@code name = "v<row>"}.
     */
    private final class TestResultSet extends ResultSetAdapter {
        private final int rows;
        private int position = 0;

        private TestResultSet(int rows) {
            this.rows = rows;
        }

        @Override
        public boolean next() {
            position++;
            return position <= rows + 1;
        }

        @Override
        public String getString(String columnLabel) {
            if ("columns".equals(columnLabel)) {
                return position == 1 ? "1" : null;
            }
            return "v" + (position - 1);
        }

        @Override
        public String getString(int columnIndex) {
            return "name:varchar";
        }

        @Override
        public void close() {
            events.add("resultSet.close");
        }
    }

    private final class TestStatement extends CallableStatementAdapter {
        @Override
        public void close() {
            events.add("statement.close");
        }
    }
}
