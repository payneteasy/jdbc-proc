package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

import com.googlecode.jdbcproc.daofactory.impl.TypeNameUtil;
import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;

/**
 * Sets null to first argument
 */
public class ParametersSetterBlockNull1 implements IParametersSetterBlock {

    public ParametersSetterBlockNull1(int aDataType) {
        theDataType = aDataType;
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException, SQLException {
        aStmt.setNull(1, theDataType);
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
}