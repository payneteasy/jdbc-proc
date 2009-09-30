package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Sets parameters to procedure from method's arguments
 */
public class ParametersSetterBlockArguments implements IParametersSetterBlock {

    public ParametersSetterBlockArguments(List<ArgumentGetter> aArgumentsGetters) {
        theArgumentsGetters = Collections.unmodifiableList(aArgumentsGetters);
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException, SQLException {
        Assert.notNull(aArgs         , "Argument aArgs must not be null"   );
        Assert.isTrue(aArgs.length==theArgumentsGetters.size()
                , "Count of procedure arguments must be equals to count of method arguments");

        int index = 0 ;
        for(ArgumentGetter getter : theArgumentsGetters) {
            getter.setParameter(aArgs[index], aStmt);
            index++;
        }

    }

    public String toString() {
        return "ParametersSetterBlockArguments{" +
                "theArgumentsGetters=" + theArgumentsGetters +
                '}';
    }

    private final List<ArgumentGetter> theArgumentsGetters ;
}