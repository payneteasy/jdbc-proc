package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.internal.RowIterator;

public interface RowIteratorDao {

    @AStoredProcedure(name = "report")
    RowIterator report(String procedureName);
}
