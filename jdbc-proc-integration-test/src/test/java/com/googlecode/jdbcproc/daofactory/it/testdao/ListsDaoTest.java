package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IListsDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.EntityWithList;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement2;
import junit.framework.Assert;

import java.util.Arrays;

/**
 *
 */
public class ListsDaoTest extends DatabaseAwareTest {

    private IListsDao listsDao;

    public void setListsDao(IListsDao listsDao) {
        this.listsDao = listsDao;
    }

    public void testCreateEntityWithListAndMetaLoginInfoAndOutputParam() {
        EntityWithList entity = new EntityWithList();
        entity.setName("entity");
        ListElement elem = new ListElement();
        elem.setName("name");
        elem.setValue("value");
        listsDao.createEntityWithList(entity, Arrays.asList(elem, elem));

        Assert.assertNotNull(entity.getId());
        Assert.assertEquals(999, (long) entity.getId());
    }

    public void testCreateEntityWithTwoListsAndMetaLoginInfoAndOutputParam() {
        EntityWithList entity = new EntityWithList();
        entity.setName("entity");
        ListElement elem = new ListElement();
        elem.setName("name");
        elem.setValue("value");
        ListElement2 elem2 = new ListElement2();
        elem.setName("name");
        elem.setValue("value");
        listsDao.createEntityWithTwoLists(entity,
                Arrays.asList(elem, elem), Arrays.asList(elem2, elem2));

        Assert.assertNotNull(entity.getId());
        Assert.assertEquals(999, (long) entity.getId());
    }

    public void testUpdateEntityWithListAndMetaLoginInfoAndOutputParam() {
        EntityWithList entity = new EntityWithList();
        entity.setId(999L);
        entity.setName("entity");
        ListElement elem = new ListElement();
        elem.setName("name");
        elem.setValue("value");
        listsDao.updateEntityWithList(entity, Arrays.asList(elem, elem));

        Assert.assertNotNull(entity.getId());
        Assert.assertEquals(999, (long) entity.getId());
    }

}