package com.googlecode.jdbcproc.daofactory.internal;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class RowImpl implements Row {

    private static class Indexer {
        private final int shift;
        private final int length;
        private final int[] indexes;
        private final String[] columns;

        Indexer(int expectedSize) {
            length = Integer.highestOneBit(expectedSize) << 1;
            shift = 32 - Integer.numberOfTrailingZeros(length);
            indexes = new int[length];
            columns = new String[length];
        }

        int getIndex(String column) {
            int i = (column.hashCode() * MAGIC) >>> shift;
            String k;
            while (!column.equals(k = columns[i])) {
                if (k == null) {
                    throw new IndexOutOfBoundsException("There is no such column '" + column + "'");
                }
                if (i == 0) {
                    i = length;
                }
                i--;
            }
            return indexes[i];
        }

        void register(String column, int index) {
            int i = (column.hashCode() * MAGIC) >>> shift;
            String k;
            while (!column.equals(k = columns[i])) {
                if (k == null) {
                    columns[i] = column;
                    indexes[i] = index;
                    return;
                }
                if (i == 0) {
                    i = length;
                }
                i--;
            }
            indexes[i] = index;
        }
    }

    private static final int MAGIC = 0xB46394CD;

    private static final Map<Class<?>, Set<String>> TYPES = new HashMap<>();

    static {
        add(boolean.class, "CHAR");
        add(boolean.class, "VARCHAR");
        add(boolean.class, "INTEGER");
        add(int.class, "INTEGER");
        add(long.class, "INTEGER");
        add(long.class, "BIGINT");
        add(double.class, "DECIMAL");
        add(double.class, "DOUBLE");
        add(double.class, "NUMERIC");
        add(double.class, "REAL");
        add(BigDecimal.class, "DECIMAL");
        add(BigDecimal.class, "NUMERIC");
        add(BigDecimal.class, "REAL");
        add(Date.class, "DATE");
        add(Date.class, "TIME");
        add(Date.class, "TIMESTAMP");
    }

    private static void add(Class<?> clazz, String sqlType) {
        Set<String> s = TYPES.get(clazz);
        if (s == null) {
            s = new HashSet<>();
            TYPES.put(clazz, s);
        }
        s.add(sqlType);
    }

    private final Indexer indexer;
    private final String[] columns;
    private final String[] types;
    private final String[] values;

    RowImpl(String[] columns, String[] types) {
        this.columns = columns;
        this.types = types;
        indexer = new Indexer(columns.length);
        values = new String[columns.length];
    }

    @Override public String[] columns() {
        return Arrays.copyOf(columns, columns.length);
    }

    @Override public String[] columnTypes() {
        return Arrays.copyOf(types, types.length);
    }

    @Override public boolean isNull(String name) {
        return getValue(indexer.getIndex(name)) == null;
    }

    @Override public boolean getBoolean(int index) {
        String v = checkType(index, boolean.class);
        if ("INTEGER".equals(types[index])) {
            return "1".equals(v);
        } else {
            return "Y".equalsIgnoreCase(v);
        }
    }

    @Override public boolean getBoolean(String name) {
        return getBoolean(indexer.getIndex(name));
    }

    @Override public int getInt(int index) {
        return Integer.parseInt(checkType(index, int.class));
    }

    @Override public int getInt(String name) {
        return getInt(indexer.getIndex(name));
    }

    @Override public long getLong(int index) {
        return Long.parseLong(checkType(index, long.class));
    }

    @Override public long getLong(String name) {
        return getLong(indexer.getIndex(name));
    }

    @Override public double getDouble(int index) {
        return Double.parseDouble(checkType(index, double.class));
    }

    @Override public double getDouble(String name) {
        return getDouble(indexer.getIndex(name));
    }

    @Override public String getString(int index) {
        return getValue(index);
    }

    @Override public String getString(String name) {
        return getString(indexer.getIndex(name));
    }

    @Override public BigDecimal getDecimal(int index) {
        return new BigDecimal(checkType(index, BigDecimal.class));
    }

    @Override public BigDecimal getDecimal(String name) {
        return getDecimal(indexer.getIndex(name));
    }

    void addColumn(String column, int index, String value) {
        if (index >= values.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid index '" + index + "' for column '" + column + "'");
        }
        values[index] = value;
        indexer.register(column, index);
    }

    private String checkType(int index, Class clazz) {
        String obj = getValue(index);
        Set<String> sqlTypes = TYPES.get(clazz);
        if (sqlTypes == null || !sqlTypes.contains(types[index])) {
            throw new IllegalStateException(String.format("Value '%s' is not of type '%s'", obj, clazz));
        }
        return obj;
    }

    private String getValue(int index) {
        if (index >= values.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid index '" + index + "'");
        }
        return values[index];
    }
}
