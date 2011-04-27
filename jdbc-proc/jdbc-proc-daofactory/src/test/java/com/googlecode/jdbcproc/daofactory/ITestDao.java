package com.googlecode.jdbcproc.daofactory;

import java.util.List;

import com.googlecode.jdbcproc.daofactory.annotation.AMetaLoginInfo;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.model.TEntry;

/**
 * Test interface
 */
public interface ITestDao {
    @AStoredProcedure(name = "save_list_not_entity")
    void saveListNotEntity(List<TEntry> entries);
    
    @AStoredProcedure(name = "call_with_list")
    @AMetaLoginInfo
    void callWithList(List<TEntry> entries);
}
