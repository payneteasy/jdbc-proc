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

package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AMetaLoginInfo;
import com.googlecode.jdbcproc.daofactory.annotation.ASerializeListToJson;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.EntityWithList;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement2;

import java.util.List;

public interface IJsonDao {

    @AMetaLoginInfo
    @AStoredProcedure(name = "json_create_entity_with_list")
    void createEntityWithList(EntityWithList entity, @ASerializeListToJson("list") List<ListElement> list);

    @AMetaLoginInfo
    @AStoredProcedure(name = "json_create_entity_with_null_list")
    void createEntityWithNullList(EntityWithList entity, @ASerializeListToJson("list") List<ListElement> list);

    @AStoredProcedure(name = "json_create_entity_with_two_lists")
    void createEntityWithTwoLists(EntityWithList entity,
            @ASerializeListToJson("list1") List<ListElement> list,
            @ASerializeListToJson("list2") List<ListElement2> list2);

    @AMetaLoginInfo
    @AStoredProcedure(name = "json_create_entity_with_two_lists_and_metalogin_info")
    void createEntityWithTwoListsAndMetaLoginInfo(EntityWithList entity,
            @ASerializeListToJson("list1") List<ListElement> list,
            @ASerializeListToJson("list2") List<ListElement2> list2);
}
