package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementCloser;
import org.springframework.util.Assert;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * OneToMany for one parent entity
 */
public class ResultSetConverterBlockEntityOneToMany2x extends ResultSetConverterBlockEntityOneToMany2xList {
    public ResultSetConverterBlockEntityOneToMany2x(List<OneToManyLink> aOneToManyLinks) {
        super(aOneToManyLinks);
    }

    @Override
    public Object convertResultSet(ResultSet aResultSet, StatementCloser aStmt) throws SQLException {
        List<Object> list = (List<Object>) super.convertResultSet(aResultSet, aStmt);
        if(list==null || list.isEmpty()) {
            return null;
        } else {
            Assert.isTrue(list.size()==1, "List size must be equals 1 not "+list.size());
            return list.get(0);
        }
    }
}
