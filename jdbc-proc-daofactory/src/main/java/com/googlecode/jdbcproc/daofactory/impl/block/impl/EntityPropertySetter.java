package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Sets output parameter to entity property
 */
public class EntityPropertySetter {

    public EntityPropertySetter(Method aSetterMethod
            , IParameterConverter aConverter
            , String aParameterName
            , int aSqlType
    ) {
        theConverter = aConverter;
        theSetterMethod = aSetterMethod;
        theParameterName = aParameterName;
        theSqlType = aSqlType;
    }

    public void fillProperty(Object aEntity, ResultSet aResultSet) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object value = theConverter.getFromResultSet(aResultSet, theParameterName);
        setProperty(aEntity, value);
    }

    public void fillOutputParameter(Object aEntity, CallableStatement aStmt) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object value = theConverter.getOutputParameter(aStmt, theParameterName);
        setProperty(aEntity, value);
    }

    private void setProperty(Object aEntity, Object aValue) throws InvocationTargetException, IllegalAccessException {
        try {
            theSetterMethod.invoke(aEntity, aValue);
        } catch (IllegalArgumentException e) {
            String gettedType = aValue!=null ? aValue.getClass().getSimpleName() : "NULL";
            String argumentType = theSetterMethod.getParameterTypes()[0].getSimpleName();
            throw new IllegalStateException(
                    String.format("Unable to set %s of sql type %d to %s.%s(%s)"
                    , gettedType, theSqlType, aEntity.getClass().getSimpleName(), theSetterMethod.getName(), argumentType), e);
        }
    }


    private final IParameterConverter theConverter;
    private final Method theSetterMethod;
    private final String theParameterName;
    private final int theSqlType;
}
