package com.googlecode.jdbcproc.daofactory.it.testdao.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Test employee service
 */
@Transactional
public interface IEmployeeService {

    /**
     * Test transaction manager
     *
     * @param aCompanyName       name of company
     * @param aEmployeeFirstname firstname
     * @param aEmployeeLastname  lastname
     */
    void createEmployeeWithCompany(String aCompanyName, String aEmployeeFirstname, String aEmployeeLastname);

}
