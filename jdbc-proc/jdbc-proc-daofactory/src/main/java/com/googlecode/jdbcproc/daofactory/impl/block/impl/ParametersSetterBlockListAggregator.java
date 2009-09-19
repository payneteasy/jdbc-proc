package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import org.springframework.dao.DataAccessException;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Aggregates ParametersSetterBlockList
 */
public class ParametersSetterBlockListAggregator implements IParametersSetterBlock {

    public ParametersSetterBlockListAggregator(List<ParametersSetterBlockList> aList) {
        theList = Collections.unmodifiableList(aList);
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException, SQLException {
        for(int i=0; i<aArgs.length; i++) {
            Object argument = aArgs[i];
            ParametersSetterBlockList block = theList.get(i);
            block.setParameters(aStmt, new Object[] {argument});
        }
    }

    public String toString() {
        return "ParametersSetterBlockListAggregator{" +
                "theList=" + theList +
                '}';
    }

    private final List<ParametersSetterBlockList> theList ;
}
