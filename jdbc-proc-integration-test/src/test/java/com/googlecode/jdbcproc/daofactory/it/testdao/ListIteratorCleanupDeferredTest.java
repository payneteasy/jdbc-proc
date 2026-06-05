package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
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

    private static void drainAndClose(CloseableIterator<ListElement> it) {
        try {
            while (it.hasNext()) {
                it.next();
            }
        } finally {
            it.close();
        }
    }
}
