package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.internal.Row;
import com.googlecode.jdbcproc.daofactory.internal.RowIterator;
import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.internal.CountingDataSource;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IListIteratorDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * Regression test for the MariaDB OOM on streaming iterators with a {@code List}
 * parameter (see {@code DaoMethodInvoker} / {@code CloseableIteratorImpl}).
 *
 * <p>A {@code List} parameter is passed through the {@code list_elements} temp
 * table; the only {@code Statement.executeUpdate(String)} in the call path is the
 * {@code "delete from list_elements"} that clears that table. We count those calls
 * to prove WHEN the cleanup runs:
 * <ul>
 *   <li>exactly 1 right after the iterator is created — the leading clear done by
 *       {@code setParameters()} before inserting the list. The trailing cleanup
 *       must NOT have run yet: doing it while the streaming cursor is open forces
 *       the MariaDB driver to buffer the whole result set into memory (the OOM);</li>
 *   <li>still 1 while rows are being read (cursor open);</li>
 *   <li>2 only after {@code close()} — the deferred cleanup runs once the cursor
 *       is drained/closed.</li>
 * </ul>
 *
 * <p>This fails on the pre-fix code (eager cleanup ran in {@code DaoMethodInvoker}'s
 * finally block, so the count would already be 2 right after creation), and it
 * fails on the "skip cleanup entirely for iterators" variant (the count would stay
 * 1 after close). It passes only when the cleanup is deferred to iterator close.
 */
public class ListIteratorCleanupDeferredTest extends DatabaseAwareTest {

    private IListIteratorDao listIteratorDao;

    public void setListIteratorDao(IListIteratorDao listIteratorDao) {
        this.listIteratorDao = listIteratorDao;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                  getSpringConfig("datasource-counting.xml")
                , getSpringConfig("factory-metalogin.xml")
                , "/spring/test-dao-metalogin.xml"
        };
    }

    public void testTempTableCleanupIsDeferredToIteratorClose() {
        CountingDataSource counting = (CountingDataSource) theDataSource;

        List<ListElement> list = Arrays.asList(
                new ListElement("a", "1"),
                new ListElement("b", "2"),
                new ListElement("c", "3"));

        // Warm up so on-demand procedure metadata loading (which queries the
        // database) happens before we start counting.
        drainAndClose(listIteratorDao.getListElements(list));

        counting.resetExecuteUpdateCount();

        CloseableIterator<ListElement> it = listIteratorDao.getListElements(list);
        try {
            Assert.assertEquals(
                    "only the leading temp-table clear must have run; trailing cleanup must be deferred",
                    1, counting.getExecuteUpdateCount());

            // Read one row of three: the streaming cursor stays open (not exhausted),
            // so the iterator does not auto-close here.
            Assert.assertTrue(it.hasNext());
            ListElement first = it.next();
            Assert.assertNotNull(first.getName());

            Assert.assertEquals(
                    "cleanup must not run while the streaming cursor is open",
                    1, counting.getExecuteUpdateCount());
        } finally {
            it.close();
        }

        Assert.assertEquals(
                "deferred cleanup must run exactly once on iterator close",
                2, counting.getExecuteUpdateCount());
    }

    public void testIteratorStreamsExactlyTheListRows() {
        List<ListElement> list = Arrays.asList(
                new ListElement("x", "10"),
                new ListElement("y", "20"));

        int rows = 0;
        try (CloseableIterator<ListElement> it = listIteratorDao.getListElements(list)) {
            while (it.hasNext()) {
                ListElement e = it.next();
                Assert.assertNotNull(e.getName());
                Assert.assertNotNull(e.getValue());
                rows++;
            }
        }
        Assert.assertEquals("iterator must stream exactly the rows passed in the List", list.size(), rows);
    }

    /**
     * The procedure fails (SIGNAL) before any iterator exists, so there is no
     * {@code close()} that could run the deferred cleanup. The temp table must
     * still be cleared before the connection goes back to the pool.
     */
    public void testTempTableIsClearedWhenIteratorCallFails() {
        CountingDataSource counting = (CountingDataSource) theDataSource;

        List<ListElement> list = Arrays.asList(
                new ListElement("a", "1"),
                new ListElement("b", "2"));

        // Warm up (see above). The procedure always fails, so just swallow it.
        try {
            listIteratorDao.getListElementsFailing(list);
        } catch (RuntimeException ignored) {
            // expected
        }

        counting.resetExecuteUpdateCount();
        try {
            listIteratorDao.getListElementsFailing(list);
            Assert.fail("get_list_elements_failing must fail");
        } catch (RuntimeException expected) {
            // the procedure SIGNALs; no iterator was ever created
        }

        Assert.assertEquals(
                "leading clear + cleanup after the failed call: the list rows must not stay "
                        + "in the temp table of the connection returned to the pool",
                2, counting.getExecuteUpdateCount());

        // The connection went back to the pool (maxTotal=2, maxWaitMillis=5000):
        // further calls must not hang waiting for a leaked connection.
        for (int i = 0; i < 3; i++) {
            drainAndClose(listIteratorDao.getListElements(list));
        }
    }

    /**
     * Same as {@link #testTempTableCleanupIsDeferredToIteratorClose()}, but for the
     * dynamic-columns {@link RowIterator} (used by report procedures).
     */
    public void testRowIteratorTempTableCleanupIsDeferredToClose() throws Exception {
        CountingDataSource counting = (CountingDataSource) theDataSource;

        List<ListElement> list = Arrays.asList(
                new ListElement("a", "1"),
                new ListElement("b", "2"),
                new ListElement("c", "3"));

        drainAndClose(listIteratorDao.getListElementsReport(list));

        counting.resetExecuteUpdateCount();

        RowIterator it = listIteratorDao.getListElementsReport(list);
        try {
            Assert.assertEquals(
                    "only the leading temp-table clear must have run; trailing cleanup must be deferred",
                    1, counting.getExecuteUpdateCount());

            // Read one row of three: the streaming cursor stays open (not exhausted),
            // so the iterator does not auto-close here.
            Assert.assertTrue(it.hasNext());
            Row first = it.next();
            Assert.assertNotNull(first.getString("name"));
            Assert.assertNotNull(first.getString("value"));

            Assert.assertEquals(
                    "cleanup must not run while the streaming cursor is open",
                    1, counting.getExecuteUpdateCount());
        } finally {
            it.close();
        }

        Assert.assertEquals(
                "deferred cleanup must run exactly once on RowIterator close",
                2, counting.getExecuteUpdateCount());
    }

    public void testRowIteratorStreamsExactlyTheListRowsAndCleansUpOnExhaustion() throws Exception {
        CountingDataSource counting = (CountingDataSource) theDataSource;

        List<ListElement> list = Arrays.asList(
                new ListElement("x", "10"),
                new ListElement("y", "20"));

        drainAndClose(listIteratorDao.getListElementsReport(list));

        counting.resetExecuteUpdateCount();

        int rows = 0;
        try (RowIterator it = listIteratorDao.getListElementsReport(list)) {
            while (it.hasNext()) {
                Row row = it.next();
                Assert.assertEquals(2, row.columns().length);
                Assert.assertNotNull(row.getString("name"));
                Assert.assertNotNull(row.getString("value"));
                rows++;
            }
            Assert.assertEquals(
                    "reaching the end auto-closes the iterator and runs the deferred cleanup",
                    2, counting.getExecuteUpdateCount());
        }
        Assert.assertEquals("RowIterator must stream exactly the rows passed in the List", list.size(), rows);
        Assert.assertEquals("close() after exhaustion must not run the cleanup again",
                2, counting.getExecuteUpdateCount());
    }

    private static void drainAndClose(CloseableIterator<ListElement> it) {
        try {
            while (it.hasNext()) {
                it.next();
            }
        } finally {
            it.close();
        }
    }

    private static void drainAndClose(RowIterator it) throws Exception {
        try {
            while (it.hasNext()) {
                it.next();
            }
        } finally {
            it.close();
        }
    }
}
