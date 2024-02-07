package com.googlecode.jdbcproc.daofactory.impl.block;

import java.sql.SQLException;

/**
 * Converts result ser to
 */
public interface IResultSetConverterBlock {
    /**
     * Converts result set to dao method return type
     * 
     * @param aContext context
     * @return dao method return type instance
     * @throws java.sql.SQLException exception on error
     */
    Object convertResultSet(IResultSetConverterContext aContext) throws SQLException;
}
