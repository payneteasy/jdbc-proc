package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ISinglePropertyEntityDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.SinglePropertyEntity;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * @author rpuch
 */
public class SinglePropertyDaoTest extends DatabaseAwareTest {
    private ISinglePropertyEntityDao singlePropertyEntityDao;

    public void setSinglePropertyEntityDao(
            ISinglePropertyEntityDao singlePropertyEntityDao) {
        this.singlePropertyEntityDao = singlePropertyEntityDao;
    }

    public void testSimpleEntity() {
        SinglePropertyEntity entity = new SinglePropertyEntity();

        singlePropertyEntityDao.saveEntity(entity);

        Assert.assertEquals(99L, (Object) entity.getId());
    }

    public void testSimpleEntityWithSecurity() {
        SinglePropertyEntity entity = new SinglePropertyEntity();

        singlePropertyEntityDao.saveEntityWithSecurity(entity);

        Assert.assertEquals(99L, (Object) entity.getId());
    }

    public void testSimpleEntityWithList() {
        SinglePropertyEntity entity = new SinglePropertyEntity();
        List<ListElement> list = Arrays.asList(new ListElement("aaa", "1"), new ListElement("bbb", "2"));

        singlePropertyEntityDao.saveEntityWithList(entity, list);

        Assert.assertEquals(99L, (Object) entity.getId());
    }

    public void testSimpleEntityWithListWithSecurity() {
        SinglePropertyEntity entity = new SinglePropertyEntity();
        List<ListElement> list = Arrays.asList(new ListElement("aaa", "1"), new ListElement("bbb", "2"));

        singlePropertyEntityDao.saveEntityWithListWithSecurity(entity, list);

        Assert.assertEquals(99L, (Object) entity.getId());
    }
}
