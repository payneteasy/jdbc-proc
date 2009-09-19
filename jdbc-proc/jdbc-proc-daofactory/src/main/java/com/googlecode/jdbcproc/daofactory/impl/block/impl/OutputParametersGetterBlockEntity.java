package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IOutputParametersGetterBlock;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.util.List;

/**
 * Gets output parameter and sets it to Entity 
 */
public class OutputParametersGetterBlockEntity implements IOutputParametersGetterBlock {

    public OutputParametersGetterBlockEntity(List<EntityPropertySetter> aEntityPropertySetters) {
        theEntityPropertySetters = aEntityPropertySetters;
    }

    public void fillOutputParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException {
        Assert.notNull(aArgs         , "Argument aArgs must not be null"   );
        Assert.isTrue(aArgs.length==1, "Count of argument must be equals 1");

        Object entity = aArgs[0];

        for(EntityPropertySetter setter : theEntityPropertySetters) {
            try {
                setter.fillOutputParameter(entity, aStmt);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to set output paremter: "+e.getMessage(), e);
            }
        }
    }

    public String toString() {
        return "OutputParametersGetterBlockEntity{" +
                "theEntityPropertySetters=" + theEntityPropertySetters +
                '}';
    }

    private final List<EntityPropertySetter> theEntityPropertySetters;
}
