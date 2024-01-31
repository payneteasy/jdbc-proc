package com.googlecode.jdbcproc.daofactory.impl.block;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;

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
    void fillOutputParameters(ICallableStatementGetStrategy aStmt, Object[] aArgs) throws DataAccessException;

    boolean hasReturn();

    Object getReturnValue(ICallableStatementGetStrategy aCallableStatementStrategy) throws SQLException;
}
