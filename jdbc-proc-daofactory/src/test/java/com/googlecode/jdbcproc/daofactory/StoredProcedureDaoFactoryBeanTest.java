package com.googlecode.jdbcproc.daofactory;

import junit.framework.TestCase;

/**
 * Tests for jdbc stored procedures dao
 */
public class StoredProcedureDaoFactoryBeanTest extends TestCase {

    public void test() throws Exception {

        StoredProcedureDaoFactoryBean factory = new StoredProcedureDaoFactoryBean();
        factory.setInterface(ITestDao.class);

        assertTrue(factory.isSingleton());
        assertEquals(ITestDao.class, factory.getObjectType());

        ITestDao testDao = (ITestDao) factory.getObject();
        assertTrue(ITestDao.class.isAssignableFrom(testDao.getClass()));
    }
}
