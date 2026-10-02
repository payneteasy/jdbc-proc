package com.googlecode.jdbcproc.daofactory.it.internal;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Test-only {@link DataSource} wrapper that counts {@code Statement.executeUpdate(String)}
 * calls made on borrowed connections: the ones that returned normally and, separately,
 * the ones that threw.
 *
 * <p>In the iterator + {@code List}-parameter path the ONLY such call is the
 * temp-table clear in {@code ParametersSetterBlockList.clearTable()}
 * ({@code createStatement().executeUpdate("delete from ...")}). Everything else
 * uses other methods: the batch insert uses {@code PreparedStatement.executeBatch()},
 * the stored-procedure call uses {@code CallableStatement.execute()}, and the DBCP2
 * pool validation ({@code call create_collections()}) uses
 * {@code Statement.executeQuery(String)}. So the count is a precise, deterministic
 * signal of WHEN the cleanup runs: once when the list is inserted, and once more
 * only when the iterator is closed (deferred cleanup).
 *
 * <p>Returned statements are proxied so their {@code getConnection()} keeps
 * returning this proxy connection — that is what makes {@code clearTable}'s
 * {@code aStmt.getConnection().createStatement()} go through the counting proxy.
 */
public class CountingDataSource implements DataSource {

    private final DataSource delegate;
    private final AtomicInteger executeUpdateCount = new AtomicInteger(0);
    private final AtomicInteger failedExecuteUpdateCount = new AtomicInteger(0);

    public CountingDataSource(DataSource delegate) {
        this.delegate = delegate;
    }

    /** Number of {@code Statement.executeUpdate(String)} calls that returned normally since the last reset. */
    public int getExecuteUpdateCount() {
        return executeUpdateCount.get();
    }

    /** Number of {@code Statement.executeUpdate(String)} calls that threw since the last reset. */
    public int getFailedExecuteUpdateCount() {
        return failedExecuteUpdateCount.get();
    }

    public void resetExecuteUpdateCount() {
        executeUpdateCount.set(0);
        failedExecuteUpdateCount.set(0);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return wrapConnection(delegate.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return wrapConnection(delegate.getConnection(username, password));
    }

    private Connection wrapConnection(Connection real) {
        return (Connection) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Connection.class},
                new ConnectionHandler(real));
    }

    private final class ConnectionHandler implements InvocationHandler {
        private final Connection real;

        private ConnectionHandler(Connection real) {
            this.real = real;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = invokeReal(real, method, args);
            // Wrap any returned statement so executeUpdate(String) is counted and
            // getConnection() keeps returning this proxy connection.
            if (result instanceof CallableStatement) {
                return wrapStatement((Statement) result, (Connection) proxy, CallableStatement.class);
            } else if (result instanceof PreparedStatement) {
                return wrapStatement((Statement) result, (Connection) proxy, PreparedStatement.class);
            } else if (result instanceof Statement) {
                return wrapStatement((Statement) result, (Connection) proxy, Statement.class);
            }
            return result;
        }
    }

    private Statement wrapStatement(Statement real, Connection proxyConnection, Class<?> iface) {
        return (Statement) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{iface},
                new StatementHandler(real, proxyConnection));
    }

    private final class StatementHandler implements InvocationHandler {
        private final Statement real;
        private final Connection proxyConnection;

        private StatementHandler(Statement real, Connection proxyConnection) {
            this.real = real;
            this.proxyConnection = proxyConnection;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            if ("getConnection".equals(name)) {
                return proxyConnection;
            }
            if ("executeUpdate".equals(name) && args != null && args.length > 0 && args[0] instanceof String) {
                // Count after the call, so the count means "the delete ran and
                // did not throw", not just "the delete was attempted".
                try {
                    Object result = invokeReal(real, method, args);
                    executeUpdateCount.incrementAndGet();
                    return result;
                } catch (Throwable t) {
                    failedExecuteUpdateCount.incrementAndGet();
                    throw t;
                }
            }
            return invokeReal(real, method, args);
        }
    }

    private static Object invokeReal(Object target, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // --- plain delegation for the rest of DataSource / Wrapper ---

    @Override public PrintWriter getLogWriter() throws SQLException { return delegate.getLogWriter(); }
    @Override public void setLogWriter(PrintWriter out) throws SQLException { delegate.setLogWriter(out); }
    @Override public void setLoginTimeout(int seconds) throws SQLException { delegate.setLoginTimeout(seconds); }
    @Override public int getLoginTimeout() throws SQLException { return delegate.getLoginTimeout(); }
    @Override public Logger getParentLogger() throws SQLFeatureNotSupportedException { return delegate.getParentLogger(); }
    @Override public <T> T unwrap(Class<T> iface) throws SQLException { return delegate.unwrap(iface); }
    @Override public boolean isWrapperFor(Class<?> iface) throws SQLException { return delegate.isWrapperFor(iface); }
}
