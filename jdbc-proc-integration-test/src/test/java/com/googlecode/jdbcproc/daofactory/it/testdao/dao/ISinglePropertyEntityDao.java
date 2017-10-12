package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AMetaLoginInfo;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.SinglePropertyEntity;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;

import java.util.List;

/**
 * @author rpuch
 */
public interface ISinglePropertyEntityDao {
    @AStoredProcedure(name = "save_single_property_entity")
    void saveEntity(SinglePropertyEntity entity);

    @AStoredProcedure(name = "save_single_property_entity_with_security")
    @AMetaLoginInfo
    void saveEntityWithSecurity(SinglePropertyEntity entity);

    @AStoredProcedure(name = "save_single_property_entity_with_list")
    void saveEntityWithList(SinglePropertyEntity entity, List<ListElement> list);

    @AStoredProcedure(name = "save_single_property_entity_with_list_with_security")
    @AMetaLoginInfo
    void saveEntityWithListWithSecurity(SinglePropertyEntity entity, List<ListElement> list);
}
