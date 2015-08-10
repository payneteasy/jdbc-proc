package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.internal.Row;
import com.googlecode.jdbcproc.daofactory.internal.RowIterator;
import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.RowIteratorDao;

import org.junit.Assert;

public class RowIteratorTest extends DatabaseAwareTest {

    private RowIteratorDao rowIteratorDao;

    public void setRowIteratorDao(RowIteratorDao rowIteratorDao) {
        this.rowIteratorDao = rowIteratorDao;
    }

    public void testReport() throws Exception {
        try (RowIterator rowIterator = rowIteratorDao.report("report1")) {
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Assert.assertNotNull(row.getInt("report_id"));
                Assert.assertNotNull(row.getString("firstname"));
                Assert.assertNotNull(row.getString("lastname"));
                Assert.assertNotNull(row.getDecimal("amount"));
            }
        }

        try (RowIterator rowIterator = rowIteratorDao.report("report2")) {
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Assert.assertNotNull(row.getInt("report_id"));
                Assert.assertNotNull(row.getDecimal("amount"));
            }
        }

        try (RowIterator rowIterator = rowIteratorDao.report("report1")) {
            printRows(rowIterator, 4);
        }

        try (RowIterator rowIterator = rowIteratorDao.report("report2")) {
            printRows(rowIterator, 2);
        }
    }

    private void printRows(RowIterator iterator, int expColumnNum) {
        while (iterator.hasNext()) {
            Row row = iterator.next();
            Assert.assertEquals(expColumnNum, row.columns().length);
            for (String column : row.columns()) {
                Assert.assertNotNull(row.getString(column));
            }
        }
    }
}
