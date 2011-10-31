package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IOutputParametersGetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Gets output parameter and sets it to Entity 
 */
public class OutputParametersGetterBlockEntity implements IOutputParametersGetterBlock {

    public OutputParametersGetterBlockEntity(List<EntityPropertySetter> aEntityPropertySetters, int aEntityParameterIndex) {
        Assert.isTrue(aEntityParameterIndex == 0 || aEntityParameterIndex == 1, "aEntityParameter index can only be 0 or 1");
        theEntityPropertySetters = aEntityPropertySetters;
        theEntityParameterIndex = aEntityParameterIndex;
    }

    public void fillOutputParameters(ICallableStatementGetStrategy aStmt, Object[] aArgs) throws DataAccessException {
        Assert.notNull(aArgs         , "Argument aArgs must not be null"   );
        Assert.isTrue(aArgs.length == 1 || aArgs.length == 2, "Count of arguments must be 1 or 2");

        Object entity = aArgs[theEntityParameterIndex];

        for(EntityPropertySetter setter : theEntityPropertySetters) {
            try {
                setter.fillOutputParameter(entity, aStmt);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to set output parameter: "+e.getMessage(), e);
            }
        }
    }

    public boolean hasReturn() { return false; }

    public Object getReturnValue(ICallableStatementGetStrategy aCallableStatementStrategy) {
        throw new UnsupportedOperationException("getReturnValue() must never be invoked in OutputParametersGetterBlockEntity");
    }

    public String toString() {
        return "OutputParametersGetterBlockEntity{" +
                "theEntityPropertySetters=" + theEntityPropertySetters +
                '}';
    }

    private final List<EntityPropertySetter> theEntityPropertySetters;
    private final int theEntityParameterIndex;
}
