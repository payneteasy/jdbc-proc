package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;
import org.springframework.dao.DataAccessException;

import com.googlecode.jdbcproc.daofactory.impl.TypeNameUtil;
import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

/**
 * Sets null to first argument
 */
public class ParametersSetterBlockNull1 implements IParametersSetterBlock {

    public ParametersSetterBlockNull1(StatementArgument aStatementArgument, int aDataType) {
        theDataType = aDataType;
        theStatementArgument = aStatementArgument;
    }

    public void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters) throws DataAccessException, SQLException {
        aStmt.setNull(theStatementArgument, theDataType);
    }

    public void cleanup(CallableStatement aStmt) throws DataAccessException,
            SQLException {
    }

    public String toString() {
        return "ParametersSetterBlockNull1{" +
                "theDataType=" + TypeNameUtil.getName(theDataType) +
                '}';
    }

    private final int theDataType;
    private final StatementArgument theStatementArgument;

}