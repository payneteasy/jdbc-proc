package com.googlecode.jdbcproc.daofactory.impl.block;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Converts result ser to
 */
public interface IResultSetConverterBlock {
    /**
     * Converts result set to dao method return type
     * @param aResultSet result set
     * @return dao method return type instance
     */
    Object convertResultSet(ResultSet aResultSet) throws SQLException;
}
