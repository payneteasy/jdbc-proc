package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.IMetaLoginInfoService;

import java.sql.CallableStatement;
import java.sql.SQLException;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/**
 * Gets login info and sets to procedure
 */
public class ParametersSetterBlockMetaLoginInfo implements IParametersSetterBlock {

    public ParametersSetterBlockMetaLoginInfo(IMetaLoginInfoService aMetaLoginInfoService, StatementArgument aUsernameArgument, StatementArgument aRoleArgument) {
        Assert.notNull(aMetaLoginInfoService, "IMetaLoginInfoService is not set to factory (DaoMethodInfoFactory.MetaLoginInfoService or DaoMethodInfoGuice.init)");

        theMetaLoginInfoService = aMetaLoginInfoService;
        theUsernameArgument = aUsernameArgument;
        theRoleArgument = aRoleArgument;
    }

    public void setParameters(ICallableStatementSetStrategy aStmt, Object[] aMethodParameters) throws DataAccessException, SQLException {
        aStmt.setString(theUsernameArgument, theMetaLoginInfoService.getUsername());
        aStmt.setString(theRoleArgument    , theMetaLoginInfoService.getRole());
    }
    
    public void cleanup(CallableStatement aStmt) throws DataAccessException, SQLException {
    }

    private final IMetaLoginInfoService theMetaLoginInfoService;
    private final StatementArgument theUsernameArgument;
    private final StatementArgument theRoleArgument;
}
