package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  VARCHAR - Boolean
 */
public class ParameterConverter_VARCHAR_langBoolean extends ParameterConverter_CHAR_boolean{

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.VARCHAR, Boolean.class);
    }

    public String toString() {
        return "ParameterConverter_VARCHAR_langBoolean{}";
    }
}