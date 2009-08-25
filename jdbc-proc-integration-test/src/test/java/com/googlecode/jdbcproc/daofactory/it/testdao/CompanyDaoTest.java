package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.CompanyWithEmployees;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.EmployeeOnly;

/**
 *
 */
public class CompanyDaoTest extends DatabaseAwareTest {

    public void testCreateCompany() {
        Company company = new Company();
        company.setName("first");
        theCompanyDao.createCompany(company);
        assertNotNull(company.getId());
    }

    /**
     * Test for @OneToMany annotation
     */
    public void testGetCompanyWithEmployees() {
        Company company = new Company();
        company.setName("onetomanytest");
        theCompanyDao.createCompany(company);

        for(int i=0; i<3; i++) {
            Employee employee = new Employee();
            employee.setCompany(company);
            employee.setFirstname("firstname-"+i);
            employee.setLastname("lastname-"+i);
            theEmployeeDao.createEmployee(employee);
        }

        CompanyWithEmployees companyWithEmployees = theCompanyDao.getCompanyWithEmployeesById(company.getId());
        assertEquals(company.getId(), companyWithEmployees.getId());
        assertEquals(company.getName(), companyWithEmployees.getName());
        assertNotNull(companyWithEmployees.getEmployees());
        assertEquals(3, companyWithEmployees.getEmployees().size());
        for(int i=0; i<3; i++) {
            EmployeeOnly employee = companyWithEmployees.getEmployees().get(i);
            assertEquals("firstname-"+i, employee.getFirstname());
            assertEquals("lastname-"+i, employee.getLastname());
        }
        System.out.println("companyWithEmployees = " + companyWithEmployees);
    }

    /** Company dao */
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
