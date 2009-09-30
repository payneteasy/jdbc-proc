package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;
import java.math.BigDecimal;

/**
 *  CHAR - String
 */
public class ParameterConverter_CHAR_String extends ParameterConverter_VARCHAR_String {

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.CHAR, String.class);
    }

    public String toString() {
        return "ParameterConverter_CHAR_String{}";
    }
}