package com.googlecode.jdbcproc.daofactory;

/**
 *
 */
public interface IMetaLoginInfoService {

    /**
     * Username parameter name in stored procedure. eg: i_username
     * @return parameter name
     */
    String getUsernameParameterName();

    /**
     * Role parameter name in stored procedure. eg: i_principal
     * @return parameter name
     */
    String getRoleParameterName();

    /**
     * Current username
     * @return username
     */
    String getUsername();

    /**
     * Current role
     * @return role
     */
    String getRole();
}
