package com.googlecode.jdbcproc.daofactory.it.testdao.service.impl;

import com.googlecode.jdbcproc.daofactory.IMetaLoginInfoService;

/**
 * Simple IMetaLoginInfoService implementation
 */
public class SimpleMetaLoginInfoServiceImpl implements IMetaLoginInfoService {

    public String getRole() {
        return "admin-role";
    }

    public String getRoleParameterName() {
        return "i_principal";
    }

    public String getUsername() {
        return "admin-user";
    }

    public String getUsernameParameterName() {
        return "i_username";
    }
}
