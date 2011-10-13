package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AMetaLoginInfo;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.EntityWithList;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;

import java.util.List;

/**
 * @author rpuch
 */
public interface IListsDao {
    @AStoredProcedure(name = "save_entity_with_list")
    @AMetaLoginInfo
    void saveEntityWithList(EntityWithList entity, List<ListElement> list);
}
