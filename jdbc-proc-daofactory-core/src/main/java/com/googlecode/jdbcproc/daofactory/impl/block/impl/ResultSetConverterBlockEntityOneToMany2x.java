package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;

import java.sql.SQLException;
import java.util.List;

import org.springframework.util.Assert;

/**
 * OneToMany for one parent entity
 */
public class ResultSetConverterBlockEntityOneToMany2x extends ResultSetConverterBlockEntityOneToMany2xList {

    public ResultSetConverterBlockEntityOneToMany2x(List<OneToManyLink> aOneToManyLinks) {
        super(aOneToManyLinks);
    }

    @Override public Object convertResultSet(IResultSetConverterContext aContext) throws SQLException {
        List<Object> list = (List<Object>) super.convertResultSet(aContext);
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            Assert.isTrue(list.size() == 1, "List size must be equals 1 not " + list.size());
            return list.get(0);
        }
    }
}
