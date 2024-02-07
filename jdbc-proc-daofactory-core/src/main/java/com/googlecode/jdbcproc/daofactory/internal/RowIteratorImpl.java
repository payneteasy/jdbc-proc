package com.googlecode.jdbcproc.daofactory.internal;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

final class RowIteratorImpl implements RowIterator {

    private final java.sql.ResultSet resultSet;
    private final CallableStatement statement;
    private final DataSource dataSource;

    private Row next;
    private boolean ready = false;
    private boolean first = true;
    private boolean closed = false;
    private String[] columns;
    private String[] columnTypes;

    public RowIteratorImpl(java.sql.ResultSet resultSet, CallableStatement statement, DataSource dataSource) {
        this.resultSet = resultSet;
        this.statement = statement;
        this.dataSource = dataSource;
    }

    @Override public void close() throws IOException {
        if (!closed) {
            try {
                doClose();
            } catch (Exception e) {
                throw new IllegalStateException("Unable to close ResultSet or CallableStatement: " + e.getMessage(), e);
            }
        }
    }

    @Override public boolean hasNext() {
        return ready || (!closed && tryNext());
    }

    @Override public Row next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        ready = false;
        return next;
    }

    private void doClose() throws SQLException {
        closed = true;
        try {
            resultSet.close();
        } finally {
            DataSourceUtils.releaseConnection(statement.getConnection(), dataSource);
            statement.close();
        }
    }

    private Row one() throws SQLException {
        RowImpl row = new RowImpl(Arrays.copyOf(columns, columns.length), Arrays.copyOf(columnTypes, columnTypes.length));
        int i = 0;
        for (String column : columns) {
            row.addColumn(column, i++, resultSet.getString(column));
        }
        return row;
    }

    private boolean tryNext() {
        try {
            if (first && resultSet.next()) {
                String tmp = resultSet.getString("columns");
                if (tmp == null) {
                    throw new SQLException("There is no system column 'columns' in result set.");
                }
                final int offset = 2;
                final int numOfColumns = Integer.parseInt(tmp);
                columns = new String[numOfColumns];
                columnTypes = new String[numOfColumns];
                for (int i = 0; i < numOfColumns; i++) {
                    String val = resultSet.getString(i + offset);
                    int separator = val.indexOf(':');
                    columns[i] = val.substring(0, separator);
                    columnTypes[i] = val.substring(separator + 1).toUpperCase().trim();
                }
                first = false;
                return tryNext();
            } else if (resultSet.next()) {
                next = one();
                ready = true;
                return true;
            }
            next = null;
            ready = false;
            doClose();
        } catch (SQLException ex) {
            throw new RuntimeException("Error in Row iterator occurred: " + ex.getMessage(), ex);
        }
        return false;
    }
}
