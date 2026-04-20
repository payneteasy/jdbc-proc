package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ISleepDao;

import java.sql.SQLTimeoutException;

public class CallTimeoutTest extends DatabaseAwareTest {

    public void testCallIsInterruptedWhenTimeoutExceeded() {
        long start = System.currentTimeMillis();
        try {
            theSleepDao.sleepWithTimeout(5);
            fail("Expected timeout exception, procedure slept to completion");
        } catch (RuntimeException expected) {
            long elapsed = System.currentTimeMillis() - start;
            assertTrue("Call should have been interrupted well before the 5s sleep finished, but elapsed=" + elapsed + "ms",
                    elapsed < 4000);
            assertTrue("Expected SQLTimeoutException somewhere in the cause chain, but got: " + expected,
                    hasCause(expected, SQLTimeoutException.class));
        }
    }

    private static boolean hasCause(Throwable t, Class<? extends Throwable> type) {
        for (Throwable cur = t; cur != null; cur = cur.getCause()) {
            if (type.isInstance(cur)) {
                return true;
            }
        }
        return false;
    }

    public void testCallCompletesWhenWithinTimeout() {
        theSleepDao.sleepNoTimeout(0);
    }

    public void setSleepDao(ISleepDao aSleepDao) {
        theSleepDao = aSleepDao;
    }

    private ISleepDao theSleepDao;
}
