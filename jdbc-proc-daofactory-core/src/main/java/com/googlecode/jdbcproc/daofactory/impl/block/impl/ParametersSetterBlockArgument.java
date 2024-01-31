package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/**
 * Sets parameters to procedure from method's arguments
 */
public class ParametersSetterBlockArgument implements IParametersSetterBlock {

    public ParametersSetterBlockArgument(ArgumentGetter aArgumentsGetter) {
        theArgumentsGetter = aArgumentsGetter;
    }

    public void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters) throws DataAccessException, SQLException {
        Assert.notNull(aMethodParameters, "Argument aArgs must not be null"   );
        theArgumentsGetter.setParameter(aMethodParameters[0], aStmt);
    }
    
    public void cleanup(CallableStatement aStmt) throws DataAccessException,
            SQLException {
    }

    public String toString() {
        return "ParametersSetterBlockArguments{" +
                "theArgumentsGetters=" + theArgumentsGetter +
                '}';
    }

    private final ArgumentGetter theArgumentsGetter ;
}
