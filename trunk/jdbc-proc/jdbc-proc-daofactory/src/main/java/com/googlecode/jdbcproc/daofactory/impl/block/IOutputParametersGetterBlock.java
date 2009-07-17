package com.googlecode.jdbcproc.daofactory.impl.block;

import org.springframework.dao.DataAccessException;

import java.sql.CallableStatement;

/**
 * Output parameters getter
 */
public interface IOutputParametersGetterBlock {

    /**
     * Fills output parameters
     * @param aStmt callable statement
     * @param aArgs arguments
     * @throws org.springframework.dao.DataAccessException on error
     */
    void fillOutputParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException;
}
