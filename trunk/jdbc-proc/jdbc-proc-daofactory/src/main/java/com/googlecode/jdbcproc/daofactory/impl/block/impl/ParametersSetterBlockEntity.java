package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.util.Collections;
import java.util.List;

/**
 * Sets parameters to procedure from JPA Entity
 */
public class ParametersSetterBlockEntity implements IParametersSetterBlock {

    public ParametersSetterBlockEntity(List<EntityArgumentGetter> aArgumentsGetters) {
        theArgumentsGetters = Collections.unmodifiableList(aArgumentsGetters);
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException {
        Assert.notNull(aArgs         , "Argument aArgs must not be null"   );
        Assert.isTrue(aArgs.length==1, "Count of argument must be equals 1");

        Object entity = aArgs[0];

        for(EntityArgumentGetter getter : theArgumentsGetters) {
            try {
                getter.setParameter(entity, aStmt);
            } catch (Exception e) {
                throw new IllegalStateException("Can not set parameter "+e.getMessage(), e);
            }
        }

    }

    private final List<EntityArgumentGetter> theArgumentsGetters ;
}
