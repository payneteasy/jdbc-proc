package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Entity argument getter for one to one property
 */
public class EntityArgumentGetterOneToOne implements IEntityArgumentGetter {

    public EntityArgumentGetterOneToOne(Method aOneToOneEntityMethodGetter, IEntityArgumentGetter aEntityArgumentGetter) {
        theOneToOneEntityMethodGetter = aOneToOneEntityMethodGetter;
        theEntityArgumentGetter = aEntityArgumentGetter;
    }

    public void setParameter(Object aEntity, ICallableStatementSetStrategy aStmt) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object oneToOneEntity = getOneToOneEntity(aEntity);
        theEntityArgumentGetter.setParameter(oneToOneEntity, aStmt);
    }

    private Object getOneToOneEntity(Object aEntity) throws IllegalAccessException, InvocationTargetException {
        Object oneToOneEntity = theOneToOneEntityMethodGetter.invoke(aEntity);
        if(oneToOneEntity==null) {
            try {
                oneToOneEntity = theOneToOneEntityMethodGetter.getReturnType().newInstance();
            } catch (InstantiationException e) {
                throw new IllegalStateException("Could not instantiate class "+theOneToOneEntityMethodGetter.getReturnType().getSimpleName());
            }
        }
        return oneToOneEntity;
    }

    public String getColumnNameForInsertQuery() {
        return theEntityArgumentGetter.getColumnNameForInsertQuery();
    }

    private Method theOneToOneEntityMethodGetter;
    private IEntityArgumentGetter theEntityArgumentGetter;
}
