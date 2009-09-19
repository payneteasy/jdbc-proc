package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.*;

import org.springframework.util.Assert;

/**
 * For list entity with @OneToMany annotation
 */
public class ResultSetConverterBlockEntityOneToManyList implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntityOneToManyList(ResultSetConverterBlockEntityOneToMany aBlock) {
        theBlock = aBlock;
    }

    public Object convertResultSet(ResultSet aResultSet, CallableStatement aStmt) throws SQLException {
        Assert.notNull(aResultSet, "ResultSet is null");
        LinkedList list = new LinkedList();
        Map<Object, List<Object>> map = new HashMap<Object, List<Object>>();
        while(aResultSet.next()) {
            Object parent = theBlock.createParentEntity(aResultSet);
            Object child  = theBlock.createChildEntity(aResultSet);

            List<Object> childsList;
            if(map.containsKey(parent)) {
                childsList = map.get(parent);
            } else {
                childsList = new LinkedList<Object>();
                theBlock.setOneToManyList(parent, childsList);
                map.put(parent, childsList);
                list.add(parent);
            }
            childsList.add(child);
        }
        return Collections.unmodifiableList(list);
    }

    public String toString() {
        return "ResultSetConverterBlockEntityOneToManyList{" +
                "theBlock=" + theBlock +
                '}';
    }

    private final ResultSetConverterBlockEntityOneToMany theBlock;
}
