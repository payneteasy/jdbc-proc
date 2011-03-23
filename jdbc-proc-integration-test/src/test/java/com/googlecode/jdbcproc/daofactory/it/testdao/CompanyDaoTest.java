package com.googlecode.jdbcproc.daofactory.it.testdao;

import java.util.Iterator;
import java.util.List;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.CompanyWithEmployees;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;
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

    public void testCreateCompany2() {
        long id = theCompanyDao.createCompany("second");
        assertTrue("Company Id must not be 0", 0!=id);

        Iterator<Company> companies = theCompanyDao.getAllCompanies();

        while (companies.hasNext()) {
            Company company = companies.next();
            assertEquals(id, company.getId());
            assertEquals("second", company.getName());
        }
    }

    public void testGetCompaniesNames() {
        // creates companies
        for(int i=0; i<10; i++) {
            Company company = new Company();
            company.setName("company-"+i);
            theCompanyDao.createCompany(company);
        }

        List<String> names = theCompanyDao.getCompaniesNames();
        assertEquals(10, names.size());
        for(int i=0; i<10; i++) {
            assertEquals("company-"+i, names.get(i));
        }
    }

    public void testGetCompaniesNamesIterator() {
        // creates companies
        for(int i=0; i<10; i++) {
            Company company = new Company();
            company.setName("company-"+i);
            theCompanyDao.createCompany(company);
        }

        CloseableIterator<String> names = theCompanyDao.getCompaniesNamesIterator();
        try {
            int i = 0;
            while (names.hasNext()) {
                String name = names.next();
                assertEquals("company-"+i, name);
                i++;

            }
        } finally {
            names.close();
        }
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

    public void testGetAllCompaniesWithEmployees() {
        for(int c=0; c<4; c++) {
            Company company = new Company();
            company.setName("company-"+c);
            theCompanyDao.createCompany(company);

            for(int i=0; i<3; i++) {
                Employee employee = new Employee();
                employee.setCompany(company);
                employee.setFirstname(c+"-firstname-"+i);
                employee.setLastname(c+"-lastname-"+i);
                theEmployeeDao.createEmployee(employee);
            }
        }

        List<CompanyWithEmployees> companies = theCompanyDao.getAllCompaniesWithEmployees();
        assertNotNull(companies);
        assertEquals(4, companies.size());

        for(int c=0; c<4; c++) {
            CompanyWithEmployees company = companies.get(c);
            assertEquals("company-"+c, company.getName());
            assertNotNull(company.getEmployees());
            assertEquals(3, company.getEmployees().size());

            for(int i=0; i<3; i++) {
                EmployeeOnly employee = company.getEmployees().get(i);
                assertEquals(c+"-firstname-"+i, employee.getFirstname());
                assertEquals(c+"-lastname-"+i, employee.getLastname());
            }
        }

        System.out.println("companies = " + companies);
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

    @Override protected String[] getConfigLocations() {
        return new String[] {"/spring/test-datasource.xml"
          , "/spring/test-dao.xml"
        };
    }

    /**
     * Employee dao
     */
    private IEmployeeDao theEmployeeDao;
    /**
     * Company dao
     */
    protected ICompanyDao theCompanyDao;

}
