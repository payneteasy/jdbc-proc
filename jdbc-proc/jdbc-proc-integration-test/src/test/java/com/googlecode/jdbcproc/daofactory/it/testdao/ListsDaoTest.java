package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IListsDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.EntityWithList;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;
import junit.framework.Assert;

import java.util.Collections;

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
        listsDao.createEntityWithList(entity, Collections.singletonList(elem));

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
        listsDao.updateEntityWithList(entity, Collections.singletonList(elem));

        Assert.assertNotNull(entity.getId());
        Assert.assertEquals(999, (long) entity.getId());
    }

}