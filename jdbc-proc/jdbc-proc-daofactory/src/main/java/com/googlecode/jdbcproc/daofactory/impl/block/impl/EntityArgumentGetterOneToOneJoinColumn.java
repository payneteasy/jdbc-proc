package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverter_INTEGER_long;

import javax.persistence.Id;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * Argument getter for OneToOne or ManyToOne link
 */
public class EntityArgumentGetterOneToOneJoinColumn extends EntityArgumentGetter {

    public EntityArgumentGetterOneToOneJoinColumn(Method aOneToOneEntityGetterMethod, String aParameterName) {
        super(aOneToOneEntityGetterMethod, new ParameterConverter_INTEGER_long(), aParameterName);
        theIdMethod = findIdMethod(aOneToOneEntityGetterMethod.getReturnType());
    }

    private static Method findIdMethod(Class<?> aClass) {
        for (Method method : aClass.getMethods()) {
            if(method.isAnnotationPresent(Id.class)) {
                return method;
            }
        }
        try {
            return aClass.getMethod("getId");
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Method with @Id or method getId() found in " + aClass.getSimpleName(), e);
        }
    }

    public void setParameter(Object aEntity, CallableStatement aStmt) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object oneToOneObject = theMethod.invoke(aEntity);
        Long   id = (Long) (oneToOneObject!=null ? theIdMethod.invoke(oneToOneObject) : null);
        theParameterConverter.setValue(id, aStmt, theParameterName);
    }

    public void setParameterByIndex(Object aEntity, PreparedStatement aStmt, int aIndex) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object oneToOneObject = theMethod.invoke(aEntity);
        Long   id = (Long) (oneToOneObject!=null ? theIdMethod.invoke(oneToOneObject) : null);
        theParameterConverter.setValue(id, aStmt, aIndex);
    }

    public String toString() {
        return "EntityArgumentGetterOneToOneJoinColumn{" +
                "theIdMethod=" + theIdMethod +
                '}';
    }

    private final Method theIdMethod ;
}