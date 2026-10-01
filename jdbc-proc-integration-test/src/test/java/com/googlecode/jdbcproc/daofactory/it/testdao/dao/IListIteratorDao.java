package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.internal.RowIterator;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.lists.ListElement;

import java.util.List;

/**
 * Methods that BOTH take a {@code List} parameter (passed via the
 * {@code list_elements} temp table) AND return a streaming iterator.
 * This is the combination that triggered the MariaDB OOM: the temp-table
 * cleanup must be deferred to the iterator's {@code close()} instead of
 * running while the streaming cursor is still open.
 */
public interface IListIteratorDao {

    @AStoredProcedure(name = "get_list_elements")
    CloseableIterator<ListElement> getListElements(List<ListElement> list);

    /**
     * Same as {@link #getListElements(List)}, but the procedure always fails
     * (SIGNAL). Used to check that the temp table is still cleared when the
     * call fails before any iterator, whose {@code close()} would run the
     * deferred cleanup, is created.
     */
    @AStoredProcedure(name = "get_list_elements_failing")
    CloseableIterator<ListElement> getListElementsFailing(List<ListElement> list);

    /**
     * Same rows as {@link #getListElements(List)}, returned through the
     * dynamic-columns {@link RowIterator} (the first row describes the columns).
     */
    @AStoredProcedure(name = "get_list_elements_report")
    RowIterator getListElementsReport(List<ListElement> list);
}
