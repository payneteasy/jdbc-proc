package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Aggregates ParametersSetterBlockList
 */
public class ParametersSetterBlockListAggregator implements IParametersSetterBlock {

    public ParametersSetterBlockListAggregator(List<IParametersSetterBlock> aList) {
        this(aList, null);
    }

    public ParametersSetterBlockListAggregator(List<IParametersSetterBlock> aList, int[] aListArgumentIndexes) {
        theList = Collections.unmodifiableList(aList);
        theListArgumentIndexes = aListArgumentIndexes;
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException, SQLException {
        Assert.notNull(aArgs, "Argument aArgs must not be null");
        
        if (theListArgumentIndexes != null) {
            for(int i = 0; i < theListArgumentIndexes.length; i++) {
                Object argument = aArgs[theListArgumentIndexes[i]];
                IParametersSetterBlock block = theList.get(i);
                block.setParameters(aStmt, new Object[] {argument});
            }
        } else {
            for(int i = 0; i < aArgs.length; i++) {
                Object argument = aArgs[i];
                IParametersSetterBlock block = theList.get(i);
                block.setParameters(aStmt, new Object[] {argument});
            }
        }
    }

    public String toString() {
        return "ParametersSetterBlockListAggregator{" +
                "theList=" + theList +
                '}';
    }

    private final List<IParametersSetterBlock> theList ;
    private final int[] theListArgumentIndexes;
}
