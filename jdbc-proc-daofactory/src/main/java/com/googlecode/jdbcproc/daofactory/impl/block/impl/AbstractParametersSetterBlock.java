package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractParametersSetterBlock implements IParametersSetterBlock {
        
    Object[] skipCollectionArguments(Object[] args) {
        List<Object> arguments = new ArrayList<Object>(args.length);
        for (Object argument : args) {
            if (!(argument instanceof Collection)) {
                arguments.add(argument);
            }
        }
        return arguments.toArray();
    }
    
    Object[] skipNonCollectionArguments(Object[] args) {
        List<Object> arguments = new ArrayList<Object>(args.length);
        for (Object argument : args) {
            if (argument instanceof Collection) {
                arguments.add(argument);
            }
        }
        return arguments.toArray();
    }
}