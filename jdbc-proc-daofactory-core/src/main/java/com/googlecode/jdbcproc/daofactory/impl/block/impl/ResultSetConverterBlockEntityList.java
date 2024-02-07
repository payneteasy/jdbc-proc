package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Creates list of entity
 */
public class ResultSetConverterBlockEntityList implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntityList(ResultSetConverterBlockEntity aBlockEntity) {
        theBlockEntity = aBlockEntity;
    }

    public Object convertResultSet(IResultSetConverterContext aContext) throws SQLException {
        final ResultSet aResultSet = aContext.getResultSet();
        Objects.requireNonNull(aResultSet, "ResultSet is null");
        List list = new ArrayList();
        while (aResultSet.next()) {
            Object entity = theBlockEntity.createEntity(aResultSet);
            list.add(entity);
        }
        return Collections.unmodifiableList(list);
    }

    public String toString() {
        return "ResultSetConverterBlockEntityList{" +
                "theBlockEntity=" + theBlockEntity +
                '}';
    }

    private final ResultSetConverterBlockEntity theBlockEntity;
}
