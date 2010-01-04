package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import java.sql.ResultSet;
import java.util.List;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * One To Many Link
 */
public class OneToManyLink {

    public OneToManyLink(Class aEntityClass
            , List<EntityPropertySetter> aEntityPropertySetters
            , List<OneToOneLink> aOneToOneLinks
            , Method aOneToManyMethod
    ) {
        theEntityClass = aEntityClass;
        theEntityPropertySetters = aEntityPropertySetters;
        theOneToOneLinks = aOneToOneLinks;
        theOneToManyMethod = aOneToManyMethod;
    }

    public Object createEmptyEntity() {
        return ResultSetConverterBlockUtil.createEntityObject(theEntityClass);
    }

    public Object loadEntity(ResultSet aResultSet) {
        Object entity = ResultSetConverterBlockUtil.createEntity(aResultSet, theEntityClass, theEntityPropertySetters, theOneToOneLinks);
        setChildren(entity, Collections.EMPTY_LIST);
        return entity;
    }

    public void setChildren(Object aEntity, List<Object> aChildren) {
        if(theOneToManyMethod!=null) {
            try {
                theOneToManyMethod.invoke(aEntity, aChildren);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Error while setting OneToMany list: "+e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Error while setting OneToMany list: "+e.getMessage(), e);
            }
        }
    }


    public String toString() {
        return "OneToManyLink{" +
                "theEntityClass=" + theEntityClass +
                ", theEntityPropertySetters=" + theEntityPropertySetters +
                ", theOneToOneLinks=" + theOneToOneLinks +
                ", theOneToManyMethod=" + theOneToManyMethod +
                '}';
    }

    private final Class theEntityClass;
    private final List<EntityPropertySetter> theEntityPropertySetters;
    private final List<OneToOneLink> theOneToOneLinks;
    private final Method theOneToManyMethod;
}
