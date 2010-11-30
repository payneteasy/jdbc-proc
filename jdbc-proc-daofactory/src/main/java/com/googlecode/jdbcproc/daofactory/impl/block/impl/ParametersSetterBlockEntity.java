package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Sets parameters to procedure from JPA Entity
 */
public class ParametersSetterBlockEntity implements IParametersSetterBlock {

    public ParametersSetterBlockEntity(List<EntityArgumentGetter> aArgumentsGetters, 
        int[] aNonListArgumentIndexes) {
        theArgumentsGetters = Collections.unmodifiableList(aArgumentsGetters);
        theNonListArgumentIndexes = aNonListArgumentIndexes;
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException {
        Assert.notNull(aArgs, "Argument aArgs must not be null");

        Object entity;      
        if (theNonListArgumentIndexes != null) {
          Assert.isTrue(theNonListArgumentIndexes.length == 1, "Count of argument must be equals 1");          
          entity = aArgs[theNonListArgumentIndexes[0]];
        } else {
          entity = aArgs[0];
        }
      
        for(EntityArgumentGetter getter : theArgumentsGetters) {
            try {
                getter.setParameter(entity, aStmt);
            } catch (Exception e) {
                throw new IllegalStateException("Can not set parameter: "+e.getMessage(), e);
            }
        }
    }
    
    public void cleanup(CallableStatement aStmt) throws DataAccessException,
            SQLException {
    }

    public String toString() {
        return "ParametersSetterBlockEntity{" +
                "theArgumentsGetters=" + theArgumentsGetters +
                '}';
    }

    private final List<EntityArgumentGetter> theArgumentsGetters ;
    private final int[] theNonListArgumentIndexes;
}
