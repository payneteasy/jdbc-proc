package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;

/**
 *
 */
public class EmployeeDaoTest extends DatabaseAwareTest {

    public void test() {
        Company company = new Company();
        company.setName("first");
        theCompanyDao.createCompany(company);

        Employee employee = new Employee();
        employee.setFirstname("Ivan");
        employee.setLastname("Petrov");
        employee.setCompany(company);
        theEmployeeDao.createEmployee(employee);

        Employee employeeFromBase = theEmployeeDao.getEmployeeById(employee.getId());
        assertNotNull(employeeFromBase);
        assertEquals(employee, employeeFromBase);
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
    public void setEmployeDao(IEmployeeDao aEmployeeDao) {
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