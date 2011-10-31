package com.googlecode.jdbcproc.daofactory.impl.block;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementStrategy;
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
    void fillOutputParameters(ICallableStatementStrategy aStmt, Object[] aArgs) throws DataAccessException;

    boolean hasReturn();

    Object getReturnValue(ICallableStatementStrategy aCallableStatementStrategy) throws SQLException;
}
