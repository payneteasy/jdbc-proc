package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.util.Collections;
import java.util.List;

/**
 * Sets parameters to procedure from JPA Entity
 */
public class ParametersSetterBlockEntity extends AbstractParametersSetterBlock {

    public ParametersSetterBlockEntity(List<EntityArgumentGetter> aArgumentsGetters) {
        theArgumentsGetters = Collections.unmodifiableList(aArgumentsGetters);
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException {
        Assert.notNull(aArgs         , "Argument aArgs must not be null"   );
        final Object[] arguments = skipCollectionArguments(aArgs);
        Assert.isTrue(arguments.length==1, "Count of argument must be equals 1");

        Object entity = arguments[0];

        for(IEntityArgumentGetter getter : theArgumentsGetters) {
            try {
                getter.setParameter(entity, aStmt);
            } catch (Exception e) {
                throw new IllegalStateException("Can not set parameter: "+e.getMessage(), e);
            }
        }
    }

    public String toString() {
        return "ParametersSetterBlockEntity{" +
                "theArgumentsGetters=" + theArgumentsGetters +
                '}';
    }

    private final List<EntityArgumentGetter> theArgumentsGetters ;
}
