package com.googlecode.jdbcproc.daofactory.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.sql.Types;
import java.lang.reflect.Field;

/**
 * Show java.sql.Types field name
 */
public class TypeNameUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TypeNameUtil.class);

    private final static Map<Integer, String> NAMES;

    static {
        NAMES = new HashMap<Integer, String>();

        for (Field field : Types.class.getFields()) {
            String name = field.getName();
            try {
                int value = field.getInt(null);
                NAMES.put(value, name);
            } catch (IllegalAccessException e) {
                LOG.error("Error getting field value for java.sql.Types."+name, e);
            }
        }
    }

    /**
     * Get java.sql.Types types name
     *
     * @param aTypeId type
     * @return name of type
     */
    public static String getName(int aTypeId) {
        String name = NAMES.get(aTypeId);
        return name!=null ? name : String.valueOf(aTypeId);
    }
}
