package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IOutputParametersGetterBlock;

import java.sql.SQLException;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import org.springframework.dao.DataAccessException;

/**
 *
 */
public class OutputParametersGetterHasReturnBlock implements IOutputParametersGetterBlock {

    public OutputParametersGetterHasReturnBlock(IParameterConverter aConverter, StatementArgument aStatementArgument) {
        theStatementArgument = aStatementArgument;
        theConverter = aConverter;
    }

    public void fillOutputParameters(ICallableStatementGetStrategy aStmt, Object[] aArgs) throws DataAccessException {
        // do nothing
    }

    public boolean hasReturn() {
        return true;
    }

    public Object getReturnValue(ICallableStatementGetStrategy aCallableStatementStrategy) throws SQLException {
        return theConverter.getOutputParameter(aCallableStatementStrategy, theStatementArgument);
    }

    private final IParameterConverter theConverter;
    private final StatementArgument theStatementArgument;

}
