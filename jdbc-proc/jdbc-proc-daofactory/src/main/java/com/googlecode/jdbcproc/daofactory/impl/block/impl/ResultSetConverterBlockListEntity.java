package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Collections;

import org.springframework.util.Assert;

/**
 * Creates list of entity
 */
public class ResultSetConverterBlockListEntity implements IResultSetConverterBlock {

    public ResultSetConverterBlockListEntity(ResultSetConverterBlockEntity aBlockEntity) {
        theBlockEntity = aBlockEntity;
    }

    public Object convertResultSet(ResultSet aResultSet) throws SQLException {
        Assert.notNull(aResultSet, "ResultSet is null");
        LinkedList list = new LinkedList();
        while(aResultSet.next()) {
            Object entity = theBlockEntity.createEntity(aResultSet);
            list.add(entity);
        }
        return Collections.unmodifiableList(list);
    }

    private final ResultSetConverterBlockEntity theBlockEntity;
}
