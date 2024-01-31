package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OneToOneLink {
	
	public OneToOneLink(ResultSetConverterBlockEntity aBlock, Method aSetterMethod) {
		theBlock = aBlock;
		theSetterMethod = aSetterMethod;
	}
	
	public ResultSetConverterBlockEntity getBlock() {
		return theBlock;
	}
	
	public void fillProperty(Object aParentEntity, Object aArgumentEntity) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		theSetterMethod.invoke(aParentEntity, aArgumentEntity);
	}

    public String toString() {
        return "OneToOneLink{" +
                "theBlock=" + theBlock +
                ", theSetterMethod=" + theSetterMethod +
                '}';
    }

    private final ResultSetConverterBlockEntity theBlock;
	private final Method theSetterMethod;
}
