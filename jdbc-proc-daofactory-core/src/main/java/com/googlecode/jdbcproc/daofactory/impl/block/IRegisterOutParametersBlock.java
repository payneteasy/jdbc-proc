package com.googlecode.jdbcproc.daofactory.impl.block;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Register output parameters
 *
 * <pre>
 *   aStmt.registerOutParameter(1, Types.INTEGER);
 * </pre>
 */
public interface IRegisterOutParametersBlock {
    /**
     * Must be registered output parameters
     *
     * @param aStmt callable statement
     * @throws java.sql.SQLException on sql error
     */
    void registerOutParameters(CallableStatement aStmt) throws SQLException;
}
