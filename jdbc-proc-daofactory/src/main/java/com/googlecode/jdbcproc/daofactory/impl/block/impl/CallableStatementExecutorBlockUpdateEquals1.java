package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.SQLException;

import com.googlecode.jdbcproc.daofactory.impl.block.ICallableStatementExecutorBlock;

/**
 * Executes update and check for result is equals 1
 * always return null
 */
public class CallableStatementExecutorBlockUpdateEquals1 implements ICallableStatementExecutorBlock {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public ResultSet execute(CallableStatement aStmt) throws SQLException {
        int result = aStmt.executeUpdate();
        if(1!=result) {
            LOG.warn("Result must be equals 1 not {}", result);
        }
        return null;
    }
}
