package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.IMetaLoginInfoService;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/**
 * Gets login info and sets to procedure
 */
public class ParametersSetterBlockMetaLoginInfo implements IParametersSetterBlock {

    public ParametersSetterBlockMetaLoginInfo(IMetaLoginInfoService aMetaLoginInfoService) {
        Assert.notNull(aMetaLoginInfoService, "IMetaLoginInfoService is not setted to factory (DaoMethodInfoFactory.MetaLoginInfoService or DaoMethodInfoGuice.init)");

        theMetaLoginInfoService = aMetaLoginInfoService;
        theUsernameParameterName = aMetaLoginInfoService.getUsernameParameterName();
        theRoleParameterName = aMetaLoginInfoService.getRoleParameterName();
    }

    public void setParameters(CallableStatement aStmt, Object[] aArgs) throws DataAccessException, SQLException {
        aStmt.setString(theUsernameParameterName, theMetaLoginInfoService.getUsername());
        aStmt.setString(theRoleParameterName    , theMetaLoginInfoService.getRole());
    }
    
    public void cleanup(CallableStatement aStmt) throws DataAccessException,
            SQLException {
    }

    private final IMetaLoginInfoService theMetaLoginInfoService;
    private final String theUsernameParameterName;
    private final String theRoleParameterName;
}
