package com.googlecode.jdbcproc.daofactory.it.testdao.service.impl;

import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;
import com.googlecode.jdbcproc.daofactory.it.testdao.service.IEmployeeService;

/**
 * Implementation of IEmployeeService
 */
public class EmployeeServiceImpl implements IEmployeeService {

    public void createEmployeeWithCompany(String aCompanyName, String aEmployeeFirstname, String aEmployeeLastname) {
        Company company = new Company();
        company.setName("first");
        theCompanyDao.createCompany(company);

        Employee employee = new Employee();
        employee.setFirstname("Ivan");
        employee.setLastname("Petrov");
        employee.setCompany(company);
        theEmployeeDao.createEmployee(employee);
    }

    /**
     * Company dao
     */
    public void setCompanyDao(ICompanyDao aCompanyDao) {
        theCompanyDao = aCompanyDao;
    }

    /**
     * Employee dao
     */
    public void setEmployeeDao(IEmployeeDao aEmployeeDao) {
        theEmployeeDao = aEmployeeDao;
    }

    /**
     * Employee dao
     */
    private IEmployeeDao theEmployeeDao;
    /**
     * Company dao
     */
    private ICompanyDao theCompanyDao;

}
