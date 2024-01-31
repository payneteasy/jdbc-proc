package com.googlecode.jdbcproc.daofactory.impl.block;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;

/**
 * Sets parameters
 */
public interface IParametersSetterBlock extends ICleanable {

    /**
     * Sets paremeters
     * @param aStmt callable statement
     * @param aMethodParameters
     */
    void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters) throws DataAccessException, SQLException;
}
