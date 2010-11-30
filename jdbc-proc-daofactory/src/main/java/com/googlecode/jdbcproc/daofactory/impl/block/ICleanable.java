package com.googlecode.jdbcproc.daofactory.impl.block;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

/**
 * Interface which is able to cleanup after it finishes its work on a
 * {@link CallableStatement}.
 */
public interface ICleanable {
    /**
     * 
     * 
     * @param aStmt
     * @param aArgs
     * @throws DataAccessException
     * @throws SQLException
     */
    void cleanup(CallableStatement aStmt) throws DataAccessException, SQLException;
}
