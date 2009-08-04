package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.service.IEmployeeService;

/**
 * Tests for IEmployeeService
 */
public class EmployeeServiceTest extends DatabaseAwareTest {

    public void test() {
        theEmployeeService.createEmployeeWithCompany("test-company-1", "emp-firstname-1", "emp-lastname-1");
        theEmployeeService.createEmployeeWithCompany("test-company-2", "emp-firstname-2", "emp-lastname-2");
    }

    /**
     * IEmployeeService
     */
    public void setEmployeeService(IEmployeeService aEmployeeService) {
        theEmployeeService = aEmployeeService;
    }

    /**
     * IEmployeeService
     */
    private IEmployeeService theEmployeeService;

}
