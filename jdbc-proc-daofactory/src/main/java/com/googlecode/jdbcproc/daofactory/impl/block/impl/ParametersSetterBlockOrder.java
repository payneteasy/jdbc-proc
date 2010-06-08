package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

import java.util.Map;
import java.util.HashMap;

public enum ParametersSetterBlockOrder {
    
    PSB_META_LOGIN_INFO(ParametersSetterBlockMetaLoginInfo.class, 0),
    PSB_LIST_AGGREGATOR(ParametersSetterBlockListAggregator.class, 1),
    PSB_LIST(ParametersSetterBlockList.class, 2),
    PSB_ENTITY(ParametersSetterBlockEntity.class, 3),
    PSB_ARGUMENTS(ParametersSetterBlockArguments.class, 4),
    PSB_NULL(ParametersSetterBlockNull1.class, 5);
    
    private final Class<? extends IParametersSetterBlock> clazz;
    private final int index;
    
    private final static Map<Class<? extends IParametersSetterBlock>, ParametersSetterBlockOrder> enums = 
        new HashMap<Class<? extends IParametersSetterBlock>, 
            ParametersSetterBlockOrder>(ParametersSetterBlockOrder.values().length);
    
    static {
        for (ParametersSetterBlockOrder order : ParametersSetterBlockOrder.values()) {
            enums.put(order.clazz(), order);
        }
    }
    
    private ParametersSetterBlockOrder(Class<? extends IParametersSetterBlock> clazz, int index) {
        this.clazz = clazz;
        this.index = index;
    }

    public Class<? extends IParametersSetterBlock> clazz() {
        return clazz;
    }

    public int index() {
        return index;
    }
    
    public static ParametersSetterBlockOrder find(Class<? extends IParametersSetterBlock> clazz) {
        ParametersSetterBlockOrder orderEnum = enums.get(clazz);
        if (orderEnum == null) {
            throw new IllegalArgumentException("Could not find order enum for class [" + clazz + "].");
        }
        return orderEnum;
    }
}