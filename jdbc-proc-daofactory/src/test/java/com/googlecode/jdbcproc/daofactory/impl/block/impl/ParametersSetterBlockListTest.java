package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementSetStrategyIndexImpl;
import com.mockobjects.sql.MockStatement;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author rpuch
 */
public class ParametersSetterBlockListTest {
    @Test
    public void testSingleInsert() throws SQLException {
        String insertQuery = "insert into temp_table values (?, ?, ?)";
        String clearTableQuery = "delete from temp_table";

        final AtomicInteger insertQueryCount = new AtomicInteger();
        final AtomicInteger prepareInsertCount = new AtomicInteger();

        CallableStatement callbacleSt = mock(CallableStatement.class);
        Connection con = mock(Connection.class);
        final PreparedStatement insertSt = mock(PreparedStatement.class);
        when(callbacleSt.getConnection()).thenReturn(con);
        when(con.createStatement()).thenReturn(new MockStatement()); // clear statement
        Answer<PreparedStatement> preparedStatementAnswer = new Answer<PreparedStatement>() {
            public PreparedStatement answer(InvocationOnMock invocation) throws Throwable {
                prepareInsertCount.incrementAndGet();
                return insertSt;
            }
        };
        when(con.prepareStatement(insertQuery)).then(preparedStatementAnswer);
        Answer<Void> registerInsertQueryAnswer = new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) throws Throwable {
                insertQueryCount.incrementAndGet();
                return null;
            }
        };
        when(insertSt.executeUpdate()).then(registerInsertQueryAnswer);
        when(insertSt.executeBatch()).then(registerInsertQueryAnswer);

        ICallableStatementSetStrategy setStrategy = new CallableStatementSetStrategyIndexImpl(callbacleSt);
        IEntityArgumentGetter argumentGetter = new IEntityArgumentGetter() {
            public void setParameter(Object aEntity,
                    ICallableStatementSetStrategy aStmt) throws InvocationTargetException, IllegalAccessException, SQLException {
                aStmt.setString(new StatementArgument("name", 1), (String) aEntity);
            }

            public String getColumnNameForInsertQuery() {
                return "name";
            }
        };
        ParametersSetterBlockList setter = new ParametersSetterBlockList(insertQuery,
                Collections.singletonList(argumentGetter), clearTableQuery, 0);

        setter.setParameters(setStrategy, new Object[]{Arrays.asList("abc", "def")});

        Assert.assertEquals("Only one prepare must be called", 1, prepareInsertCount.get());
        Assert.assertEquals("Only one insert must be called", 1, insertQueryCount.get());
    }
}
