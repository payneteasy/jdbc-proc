package com.googlecode.jdbcproc.daofactory.impl;

import com.googlecode.jdbcproc.daofactory.CallableStatementAdapter;
import com.googlecode.jdbcproc.daofactory.ResultSetAdapter;
import com.googlecode.jdbcproc.daofactory.impl.block.ICallableStatementExecutorBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterContext;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collections;

/**
 * When {@link DaoMethodInvoker} runs the parameter-setter cleanup (e.g. clearing
 * the List-parameter temp table), depending on whether the method returns an
 * iterator and whether the call succeeds:
 * <ul>
 *   <li>non-iterator methods: right after the procedure call;</li>
 *   <li>iterator methods, successful call: deferred to the iterator's close()
 *       (the streaming cursor is still open);</li>
 *   <li>iterator methods, failed call: right away, because no iterator whose
 *       close() could run the deferred cleanup will ever exist.</li>
 * </ul>
 */
public class DaoMethodInvokerTest {

    private static final Object[] NO_ARGS = new Object[0];

    private final RecordingSetterBlock setterBlock = new RecordingSetterBlock();
    private final TestResultSet resultSet = new TestResultSet();
    private IResultSetConverterContext capturedContext;

    @Test
    public void testNonIteratorMethodCleansUpRightAfterTheCall() throws Exception {
        DaoMethodInvoker invoker = newInvoker(false, stmt -> resultSet, context -> "result");

        Object result = invoke(invoker);

        Assert.assertEquals("result", result);
        Assert.assertEquals(1, setterBlock.cleanupCount);
        Assert.assertTrue(resultSet.closed);
        Assert.assertNull("non-iterator results get no deferred cleanup", capturedContext.getOnCloseCleanup());
    }

    @Test
    public void testIteratorMethodDefersCleanupToIteratorClose() throws Exception {
        DaoMethodInvoker invoker = newInvoker(true, stmt -> resultSet, context -> "iterator");

        Object result = invoke(invoker);

        Assert.assertEquals("iterator", result);
        Assert.assertEquals("cleanup must not run while the streaming cursor is open", 0, setterBlock.cleanupCount);
        Assert.assertFalse("the iterator owns the result set now", resultSet.closed);

        Runnable onCloseCleanup = capturedContext.getOnCloseCleanup();
        Assert.assertNotNull(onCloseCleanup);
        onCloseCleanup.run();
        Assert.assertEquals(1, setterBlock.cleanupCount);
    }

    @Test
    public void testIteratorMethodCleansUpWhenTheProcedureCallFails() throws Exception {
        DaoMethodInvoker invoker = newInvoker(true,
                stmt -> { throw new SQLException("procedure failed"); },
                context -> { throw new AssertionError("converter must not be called"); });

        try {
            invoke(invoker);
            Assert.fail("SQLException expected");
        } catch (SQLException expected) {
            Assert.assertEquals("procedure failed", expected.getMessage());
        }

        Assert.assertEquals("no iterator will ever be closed, so the cleanup must run right away",
                1, setterBlock.cleanupCount);
    }

    @Test
    public void testIteratorMethodCleansUpWhenSettingParametersFails() throws Exception {
        setterBlock.failOnSet = true;
        DaoMethodInvoker invoker = newInvoker(true,
                stmt -> { throw new AssertionError("procedure must not be called"); },
                context -> { throw new AssertionError("converter must not be called"); });

        try {
            invoke(invoker);
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException expected) {
            // thrown by the setter block
        }

        Assert.assertEquals(1, setterBlock.cleanupCount);
    }

    @Test
    public void testIteratorMethodCleansUpWhenTheIteratorCannotBeCreated() throws Exception {
        DaoMethodInvoker invoker = newInvoker(true, stmt -> resultSet,
                context -> { throw new NullPointerException("ResultSet is null"); });

        try {
            invoke(invoker);
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException expected) {
            // thrown by the converter
        }

        Assert.assertEquals("the iterator never took ownership, so the cleanup must run right away",
                1, setterBlock.cleanupCount);
        Assert.assertTrue(resultSet.closed);
        Assert.assertTrue("the cursor must be closed before the cleanup issues its executeUpdate",
                setterBlock.resultSetClosedOnCleanup);
    }

    @Test
    public void testIteratorMethodSkipsCleanupWhenTheCursorCannotBeClosed() throws Exception {
        resultSet.failOnClose = true;
        DaoMethodInvoker invoker = newInvoker(true, stmt -> resultSet,
                context -> { throw new NullPointerException("ResultSet is null"); });

        try {
            invoke(invoker);
            Assert.fail("NullPointerException expected");
        } catch (NullPointerException expected) {
            // thrown by the converter
        }

        Assert.assertEquals("the streaming cursor may still be open, so a delete on this connection "
                + "could make the driver buffer the whole result set: cleanup must be skipped",
                0, setterBlock.cleanupCount);
    }

    private Object invoke(DaoMethodInvoker invoker) throws SQLException {
        return invoker.createCallableStatementCallback(NO_ARGS, null)
                .doInCallableStatement(new CallableStatementAdapter());
    }

    private DaoMethodInvoker newInvoker(boolean returnIterator, ICallableStatementExecutorBlock executor,
                                        IResultSetConverterBlock converter) {
        IResultSetConverterBlock capturingConverter = context -> {
            capturedContext = context;
            return converter.convertResultSet(context);
        };
        return new DaoMethodInvoker("test_proc", "{ call test_proc() }"
                , null
                , Collections.<IParametersSetterBlock>singletonList(setterBlock)
                , executor
                , null
                , capturingConverter
                , returnIterator
                , stmt -> null
                , stmt -> null
                , 0);
    }

    private final class RecordingSetterBlock implements IParametersSetterBlock {
        private boolean failOnSet;
        private int cleanupCount;
        private boolean resultSetClosedOnCleanup;

        public void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters) {
            if (failOnSet) {
                throw new IllegalStateException("cannot set parameters");
            }
        }

        public void cleanup(CallableStatement aStmt) {
            cleanupCount++;
            resultSetClosedOnCleanup = resultSet.closed;
        }
    }

    private static final class TestResultSet extends ResultSetAdapter {
        private boolean closed;
        private boolean failOnClose;

        @Override
        public void close() throws SQLException {
            if (failOnClose) {
                throw new SQLException("cannot close the cursor");
            }
            closed = true;
        }
    }
}
