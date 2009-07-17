package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  VARCHAR - Boolean
 */
public class ParameterConverter_CHAR_langBoolean extends ParameterConverter_CHAR_boolean{

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.CHAR, Boolean.class);
    }
}