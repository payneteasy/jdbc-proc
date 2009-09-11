package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.List;

/**
 * Converts to Entity
 */
public class ResultSetConverterBlockEntity implements IResultSetConverterBlock {

    public ResultSetConverterBlockEntity(Class aEntityType, List<EntityPropertySetter> aEntityPropertySetters
    		, List<OneToOneLink> aOneToOneLinks) {
        theEntityPropertySetters = aEntityPropertySetters;
        theEntityType = aEntityType;
        theOneToOneLinks = aOneToOneLinks;
    }

    public Object convertResultSet(ResultSet aResultSet, CallableStatement aStmt) throws SQLException {
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
        return ResultSetConverterBlockUtil.createEntity(aResultSet, theEntityType, theEntityPropertySetters, theOneToOneLinks);
    }
    
    private final List<EntityPropertySetter> theEntityPropertySetters;
    private final Class theEntityType;
    private final List<OneToOneLink> theOneToOneLinks;

}
