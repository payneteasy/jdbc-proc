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
 * Sets parameters to procedure from method's arguments
 */
public class ParametersSetterBlockArguments implements IParametersSetterBlock {

    public ParametersSetterBlockArguments(List<ArgumentGetter> aArgumentsGetters) {
        this(aArgumentsGetters, null);
    }
    
    public ParametersSetterBlockArguments(List<ArgumentGetter> aArgumentsGetters, 
        int[] aNonListArgumentIndexes) {
        theArgumentsGetters = Collections.unmodifiableList(aArgumentsGetters);
        theNonListArgumentIndexes = aNonListArgumentIndexes;
    }

    public void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters) throws DataAccessException, SQLException {
        Assert.notNull(aMethodParameters, "Argument aArgs must not be null"   );

        int index = 0 ;
        if (theNonListArgumentIndexes != null) {
            for(ArgumentGetter getter : theArgumentsGetters) {
                getter.setParameter(aMethodParameters[theNonListArgumentIndexes[index]], aStmt);
                index++;
            }
        } else {
            for(ArgumentGetter getter : theArgumentsGetters) {
                getter.setParameter(aMethodParameters[index], aStmt);
                index++;
            }
        }
    }
    
    public void cleanup(CallableStatement aStmt) throws DataAccessException,
            SQLException {
    }

    public String toString() {
        return "ParametersSetterBlockArguments{" +
                "theArgumentsGetters=" + theArgumentsGetters +
                '}';
    }

    private final List<ArgumentGetter> theArgumentsGetters ;
    private final int[] theNonListArgumentIndexes;
}
