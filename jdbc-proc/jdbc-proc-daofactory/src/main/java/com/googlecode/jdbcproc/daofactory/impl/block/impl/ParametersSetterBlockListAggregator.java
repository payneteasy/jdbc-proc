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
public class ParametersSetterBlockListAggregator extends AbstractParametersSetterBlock {

    public ParametersSetterBlockListAggregator(List<IParametersSetterBlock> aList) {
        theList = Collections.unmodifiableList(aList);
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException, SQLException {
        Assert.notNull(aArgs         , "Argument aArgs must not be null"   );
        final Object[] arguments = skipNonCollectionArguments(aArgs);
        for(int i=0; i<arguments.length; i++) {
            Object argument = arguments[i];
            IParametersSetterBlock block = theList.get(i);
            block.setParameters(aStmt, new Object[] {argument});
        }
    }

    public String toString() {
        return "ParametersSetterBlockListAggregator{" +
                "theList=" + theList +
                '}';
    }

    private final List<IParametersSetterBlock> theList ;
}
