package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;

import java.util.List;

/**
 * Method that BOTH takes a {@code List} parameter (passed via the
 * {@code list_elements} temp table) AND returns a streaming iterator.
 * This is the combination that triggered the MariaDB OOM: the temp-table
 * cleanup must be deferred to {@link CloseableIterator#close()} instead of
 * running while the streaming cursor is still open.
 */
public interface IListIteratorDao {

    @AStoredProcedure(name = "get_list_elements")
    CloseableIterator<ListElement> getListElements(List<ListElement> list);
}
