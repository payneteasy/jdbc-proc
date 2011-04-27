package com.googlecode.jdbcproc.daofactory.impl.block.service;

import java.util.List;

import com.googlecode.jdbcproc.daofactory.annotation.AMetaLoginInfo;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;

public interface IMappingTestDao {
    @AStoredProcedure(name = "save_list_not_entity")
    void saveListNotEntity(List<TEntry> entries);
    
    @AStoredProcedure(name = "call_with_list")
    @AMetaLoginInfo
    void callWithList(List<TEntry> entries);
}
