package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Gets parameter from Entity and set to Callable Statement
 */
public class EntityArgumentGetter {

    public EntityArgumentGetter(Method aGetterMethod, IParameterConverter aParameterConverter, String aParameterName) {
        theMethod = aGetterMethod;
        theParameterConverter = aParameterConverter;
        theParameterName = aParameterName;
    }

    public void setParameter(Object aEntity, CallableStatement aStmt) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object value = theMethod.invoke(aEntity);
        theParameterConverter.setValue(value, aStmt, theParameterName);
    }

    public String getParameterName() {
        return theParameterName;
    }
    
    private final Method theMethod;
    private final IParameterConverter theParameterConverter;
    private final String theParameterName ;

    public void setParameterByIndex(Object aEntity, PreparedStatement aStmt, int aIndex) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object value = theMethod.invoke(aEntity);
        theParameterConverter.setValue(value, aStmt, aIndex);
    }
}
