package com.googlecode.jdbcproc.daofactory.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Releases everything a streaming iterator owns once it is done. Shared by
 * {@code CloseableIteratorImpl} and {@code RowIteratorImpl} so the order below
 * is defined in one place.
 */
public final class IteratorResources {

    private IteratorResources() {
    }

    /**
     * Closes the result set, runs the deferred cleanup, closes the statement and
     * only then returns the connection to the pool. The order matters:
     * <ul>
     *   <li>the deferred cleanup (e.g. clearing the List-parameter temp table)
     *       runs after the result set is closed: an executeUpdate while the
     *       streaming cursor is still open forces the MariaDB driver to buffer
     *       the remaining rows into memory. The cleanup never throws (it logs
     *       internally), so it cannot turn end-of-iteration into an error after
     *       all rows have been delivered;</li>
     *   <li>the statement is closed before the connection is released: releasing
     *       first may make the pool close/recycle the underlying connection, after
     *       which {@code stmt.close()} fails with "Connection is closed" (MariaDB
     *       driver is strict about this, old mysql-connector was not);</li>
     *   <li>the connection reference is captured up front because
     *       {@code stmt.getConnection()} is not reliable once the statement is
     *       closed.</li>
     * </ul>
     *
     * @param onCloseCleanup deferred cleanup to run after the result set is
     *                       closed, may be {@code null}
     */
    public static void close(ResultSet resultSet, CallableStatement stmt, DataSource dataSource,
                             Runnable onCloseCleanup) throws SQLException {
        Connection connection = stmt.getConnection();
        try {
            try {
                resultSet.close();
                if (onCloseCleanup != null) {
                    onCloseCleanup.run();
                }
            } finally {
                stmt.close();
            }
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}
