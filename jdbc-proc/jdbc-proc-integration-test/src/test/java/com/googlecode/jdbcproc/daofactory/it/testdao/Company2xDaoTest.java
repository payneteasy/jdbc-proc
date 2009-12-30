package com.googlecode.jdbcproc.daofactory.it.testdao;

import junit.framework.TestCase;
import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Certificate2x;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Employee2x;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Company2x;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;

import java.util.List;

/**
 * Test for OneToMany 2x links
 */
public class Company2xDaoTest extends DatabaseAwareTest {

    public void testGetCompanies2x() {
        final Company company = new Company() {{
            setName("company-1");
        }};
        theCompanyDao.createCompany(company);

        final Employee employee = new Employee() {{
            setCompany(company);
            setFirstname("firstname-1");
            setLastname("lastname-1");
        }};
        theEmployeeDao.createEmployee(employee);

        Certificate2x certificate = new Certificate2x() {{
            setEmployee(new Employee2x() {{
                setId(employee.getId());
                setFirstname(employee.getFirstname());
                setLastname(employee.getLastname());
            }});
            setName("certificate-1");
        }};
        theCompanyDao.createCertificate(certificate);

        assertTrue("certificate id must not be equals 0", certificate.getId()!=0);

        // companies
        List<Company2x> companies = theCompanyDao.getCompanies2x();
        assertNotNull(companies);
        assertEquals(1, companies.size());

        // company
        Company2x company2x = companies.get(0);
        assertNotNull(company2x);
        assertEquals(company.getId(), company2x.getId());
        assertEquals(company.getName(), company2x.getName());

        // employees
        List<Employee2x> employees = company2x.getEmployees();
        assertNotNull(employees);
        assertEquals(1, employees.size());

        // employee
        Employee2x employee2x = employees.get(0);
        assertNotNull(employee2x);
        assertEquals(employee.getId(), employee2x.getId());
        assertEquals(employee.getFirstname(), employee2x.getFirstname());
        assertEquals(employee.getLastname(), employee2x.getLastname());

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
