package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IOutputParametersGetterBlock;

import java.sql.CallableStatement;

import org.springframework.dao.DataAccessException;

/**
 *
 */
public class OutputParametersGetterHasReturnBlock implements IOutputParametersGetterBlock {

    public void fillOutputParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException {
        // do nothing
    }
}
