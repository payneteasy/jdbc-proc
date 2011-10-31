package com.googlecode.jdbcproc.daofactory.impl.block;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementCloser;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Converts result ser to
 */
public interface IResultSetConverterBlock {
    /**
     * Converts result set to dao method return type
     * 
     * @param aResultSet result set
     * @param aStmt callable statement
     * @return dao method return type instance
     * @throws java.sql.SQLException exception on error
     */
    Object convertResultSet(ResultSet aResultSet, StatementCloser aStmt) throws SQLException;
}
