package com.googlecode.jdbcproc.daofactory.impl.block;

import org.springframework.dao.DataAccessException;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Sets parameters
 */
public interface IParametersSetterBlock extends ICleanable {

    /**
     * Sets paremeters
     * @param aStmt callable statement
     * @param aArgs arguments
     */
    void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException, SQLException;
}
