package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * For list entity with @OneToMany annotation
 */
public class ResultSetConverterBlockEntityOneToManyList implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntityOneToManyList(ResultSetConverterBlockEntityOneToMany aBlock) {
        theBlock = aBlock;
    }

    public Object convertResultSet(IResultSetConverterContext aContext) throws SQLException {
        ResultSet aResultSet = aContext.getResultSet();
        Objects.requireNonNull(aResultSet, "ResultSet is null");
        List list = new ArrayList();
        Map<Object, List<Object>> map = new HashMap<Object, List<Object>>();
        while(aResultSet.next()) {
            Object parent = theBlock.createParentEntity(aResultSet);
            Object child  = theBlock.createChildEntity(aResultSet);

            List<Object> childsList;
            if(map.containsKey(parent)) {
                childsList = map.get(parent);
            } else {
                childsList = new ArrayList<Object>();
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
