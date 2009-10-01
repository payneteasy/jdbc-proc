package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.TypeNameUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

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


    public String toString() {
        return "ParametersSetterBlockNull1{" +
                "theDataType=" + TypeNameUtil.getName(theDataType) +
                '}';
    }

    private final int theDataType;
}