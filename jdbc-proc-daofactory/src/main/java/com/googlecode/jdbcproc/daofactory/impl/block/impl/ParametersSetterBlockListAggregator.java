package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
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

    public void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters) throws DataAccessException, SQLException {
        Assert.notNull(aMethodParameters, "Argument aArgs must not be null");
        
        if (theListArgumentIndexes != null) {
            for(int i = 0; i < theListArgumentIndexes.length; i++) {
                Object argument = aMethodParameters[theListArgumentIndexes[i]];
                IParametersSetterBlock block = theList.get(i);
                block.setParameters(aStmt, new Object[] {argument});
            }
        } else {
            for(int i = 0; i < aMethodParameters.length; i++) {
                Object argument = aMethodParameters[i];
                IParametersSetterBlock block = theList.get(i);
                block.setParameters(aStmt, new Object[] {argument});
            }
        }
    }
    
    public void cleanup(CallableStatement aStmt) throws DataAccessException,
            SQLException {
        for (IParametersSetterBlock block : theList) {
            block.cleanup(aStmt);
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
