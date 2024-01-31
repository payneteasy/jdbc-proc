package com.googlecode.jdbcproc.daofactory.internal;

import java.math.BigDecimal;

public interface Row {

    String[] columns();

    String[] columnTypes();

    boolean isNull(String name);

    boolean getBoolean(int index);

    boolean getBoolean(String name);

    int getInt(int index);

    int getInt(String name);

    long getLong(int index);

    long getLong(String name);

    double getDouble(int index);

    double getDouble(String name);

    String getString(int index);

    String getString(String name);

    BigDecimal getDecimal(int index);

    BigDecimal getDecimal(String name);
}
