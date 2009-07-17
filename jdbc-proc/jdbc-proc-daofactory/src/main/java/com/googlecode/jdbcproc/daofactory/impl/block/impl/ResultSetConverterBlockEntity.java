package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Converts to Entity
 */
public class ResultSetConverterBlockEntity implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntity(Class aEntityType, List<EntityPropertySetter> aEntityPropertySetters) {
        theEntityPropertySetters = aEntityPropertySetters;
        theEntityType = aEntityType;
    }

    public Object convertResultSet(ResultSet aResultSet) throws SQLException {
        Assert.notNull(aResultSet, "ResultSet is null");
        if(aResultSet.next()) {
            Object entity = createEntity(aResultSet);
            if(aResultSet.next()) {
                throw new IllegalStateException("ResultSet returns more than 1 record") ;
            } else {
                return entity;
            }
        } else {
            // not found
            return null;
        }
    }

    protected Object createEntity(ResultSet aResultSet) {
        Object entity = createEntityObject();
        // sets
        for (EntityPropertySetter propertySetter : theEntityPropertySetters) {
            try {
                propertySetter.fillProperty(entity, aResultSet);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to set property: "+e.getMessage(), e);
            }
        }
        return entity;
    }

    private Object createEntityObject() {
        try {
            return theEntityType.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private final List<EntityPropertySetter> theEntityPropertySetters;
    private final Class theEntityType;

}
