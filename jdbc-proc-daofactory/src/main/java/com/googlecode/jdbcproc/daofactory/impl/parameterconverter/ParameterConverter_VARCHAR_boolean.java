package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  VARCHAR - boolean
 */
public class ParameterConverter_VARCHAR_boolean extends ParameterConverter_CHAR_boolean{

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.VARCHAR, boolean.class);
    }
}