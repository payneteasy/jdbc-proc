/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IJsonDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.EntityWithList;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement2;

import java.util.Arrays;

import org.junit.Assert;

/**
 *
 */
public class JsonDaoTest extends DatabaseAwareTest {

    private IJsonDao jsonDao;

    public void setJsonDao(IJsonDao jsonDao) {
        this.jsonDao = jsonDao;
    }

    public void testCreateEntityWithListAndMetaLoginInfoAndOutputParam() {
        EntityWithList entity = new EntityWithList();
        entity.setName("entity");
        ListElement elem = new ListElement();
        elem.setName("name");
        elem.setValue("value");
        jsonDao.createEntityWithList(entity, Arrays.asList(elem, elem));

        Assert.assertNotNull(entity.getId());
        Assert.assertEquals(999, (long) entity.getId());
    }

    public void testCreateEntityWithNullListAndMetaLoginInfoAndOutputParam() {
        EntityWithList entity = new EntityWithList();
        entity.setName("entity");
        jsonDao.createEntityWithList(entity, null);

        Assert.assertNotNull(entity.getId());
        Assert.assertEquals(999, (long) entity.getId());
    }

    public void testCreateEntityWithTwoListsAndOutputParam() {
        EntityWithList entity = new EntityWithList();
        entity.setName("entity");
        ListElement elem = new ListElement();
        elem.setName("name");
        elem.setValue("value");
        ListElement2 elem2 = new ListElement2();
        elem.setName("name");
        elem.setValue("value");
        jsonDao.createEntityWithTwoLists(entity,
                Arrays.asList(elem, elem), Arrays.asList(elem2, elem2));

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
        jsonDao.createEntityWithTwoListsAndMetaLoginInfo(entity,
                Arrays.asList(elem, elem), Arrays.asList(elem2, elem2));

        Assert.assertNotNull(entity.getId());
        Assert.assertEquals(999, (long) entity.getId());
    }
}