package com.googlecode.jdbcproc.daofactory.it.testdao;

import java.util.List;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Certificate2x;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Company2x;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Employee2x;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Father;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Grandfather;

/**
 * Test for OneToMany 2x links
 */
public class Company2xDaoTest extends DatabaseAwareTest {

    public void testGetCompanies2xOneRow() {
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
            setName("certificate-1");
            setEmployeeId(employee.getId());
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

        // certificates
        List<Certificate2x> certificates = employee2x.getCertificates();
        assertNotNull(certificates);
        assertEquals(1, certificates.size());

        // certificate
        Certificate2x certificate2x = certificates.get(0);
        assertNotNull(certificate2x);
        assertEquals(certificate.getId(), certificate2x.getId());
        assertEquals(certificate.getName(), certificate2x.getName());
        assertEquals(certificate.getEmployeeId(), certificate2x.getEmployeeId());

    }


    public void testGetCompanies2xManyRow() {
        int companyIndex = 0;
        int employeeIndex = 0;
        int certificateIndex = 0 ;
        for(int c=0; c<3; c++) {
            companyIndex++;
            Company company = new Company();
            company.setName("company-"+companyIndex);
            theCompanyDao.createCompany(company);
            // employee
            for(int e=0; e<4; e++) {
                employeeIndex++;
                Employee employee = new Employee();
                employee.setCompany(company);
                employee.setFirstname("firstname-"+employeeIndex);
                employee.setLastname("lastname-"+employeeIndex);
                theEmployeeDao.createEmployee(employee);

                // certificate
                for(int cert=0; cert<5; cert++) {
                    certificateIndex++;

                    Certificate2x certificate = new Certificate2x();
                    certificate.setName("certificate-"+certificateIndex);
                    certificate.setEmployeeId(employee.getId());
                    theCompanyDao.createCertificate(certificate);
                }
            }
        }

        List<Company2x> companies = theCompanyDao.getCompanies2x();
        assertEquals(3, companies.size());

        companyIndex = 0 ;
        employeeIndex = 0;
        certificateIndex = 0;
        
        for (Company2x company : companies) {
            // company
            companyIndex++;
            assertEquals("company-"+companyIndex, company.getName());
            assertEquals(companyIndex, company.getId());

            // employee
            List<Employee2x> employees = company.getEmployees();
            assertEquals(4, employees.size());
            for (Employee2x employee : employees) {
                employeeIndex++;
                assertEquals(employeeIndex, employee.getId());
                assertEquals("firstname-"+employeeIndex, employee.getFirstname());
                assertEquals("lastname-"+employeeIndex, employee.getLastname());

                // certificate
                List<Certificate2x> certificates = employee.getCertificates();
                assertEquals(5, certificates.size());

                for (Certificate2x certificate : certificates) {
                    certificateIndex++;
                    assertEquals(certificateIndex, certificate.getId());
                    assertEquals("certificate-"+certificateIndex, certificate.getName());
                    assertEquals(employee.getId(), (long)certificate.getEmployeeId());
                }
            }
        }

    }

    public void testGetCompanies2xManyRowWithEmptyCertificates() {
        int companyIndex = 0;
        int employeeIndex = 0;
        for(int c=0; c<3; c++) {
            companyIndex++;
            Company company = new Company();
            company.setName("company-"+companyIndex);
            theCompanyDao.createCompany(company);
            // employee
            for(int e=0; e<4; e++) {
                employeeIndex++;
                Employee employee = new Employee();
                employee.setCompany(company);
                employee.setFirstname("firstname-"+employeeIndex);
                employee.setLastname("lastname-"+employeeIndex);
                theEmployeeDao.createEmployee(employee);
            }
        }

        List<Company2x> companies = theCompanyDao.getCompanies2x();
        assertEquals(3, companies.size());

        companyIndex = 0 ;
        employeeIndex = 0;

        for (Company2x company : companies) {
            // company
            companyIndex++;
            assertEquals("company-"+companyIndex, company.getName());
            assertEquals(companyIndex, company.getId());

            // employee
            List<Employee2x> employees = company.getEmployees();
            assertEquals(4, employees.size());
            for (Employee2x employee : employees) {
                employeeIndex++;
                assertEquals(employeeIndex, employee.getId());
                assertEquals("firstname-"+employeeIndex, employee.getFirstname());
                assertEquals("lastname-"+employeeIndex, employee.getLastname());

                // certificate
                List<Certificate2x> certificates = employee.getCertificates();
                assertNotNull(certificates);
                assertEquals(0, certificates.size());
            }
        }

    }


    public void testGetCompanies2xManyRowWithOneEmployeeWithCertificate() {
        int companyIndex = 0;
        int employeeIndex = 0;
        int certificateIndex = 0 ;
        for(int c=0; c<3; c++) {
            companyIndex++;
            Company company = new Company();
            company.setName("company-"+companyIndex);
            theCompanyDao.createCompany(company);
            // employee
            for(int e=0; e<4; e++) {
                employeeIndex++;
                Employee employee = new Employee();
                employee.setCompany(company);
                employee.setFirstname("firstname-"+employeeIndex);
                employee.setLastname("lastname-"+employeeIndex);
                theEmployeeDao.createEmployee(employee);

                if(employeeIndex==6) {
                    // certificate
                    for(int cert=0; cert<5; cert++) {
                        certificateIndex++;

                        Certificate2x certificate = new Certificate2x();
                        certificate.setName("certificate-"+certificateIndex);
                        certificate.setEmployeeId(employee.getId());
                        theCompanyDao.createCertificate(certificate);
                    }
                }
            }
        }

        List<Company2x> companies = theCompanyDao.getCompanies2x();
        assertEquals(3, companies.size());

        companyIndex = 0 ;
        employeeIndex = 0;
        certificateIndex = 0;

        for (Company2x company : companies) {
            // company
            companyIndex++;
            assertEquals("company-"+companyIndex, company.getName());
            assertEquals(companyIndex, company.getId());

            // employee
            List<Employee2x> employees = company.getEmployees();
            assertEquals(4, employees.size());
            for (Employee2x employee : employees) {
                employeeIndex++;
                assertEquals(employeeIndex, employee.getId());
                assertEquals("firstname-"+employeeIndex, employee.getFirstname());
                assertEquals("lastname-"+employeeIndex, employee.getLastname());

                // certificate
                List<Certificate2x> certificates = employee.getCertificates();
                if(employeeIndex==6) {
                    assertEquals(5, certificates.size());

                    for (Certificate2x certificate : certificates) {
                        certificateIndex++;
                        assertEquals(certificateIndex, certificate.getId());
                        assertEquals("certificate-"+certificateIndex, certificate.getName());
                        assertEquals(employee.getId(), (long)certificate.getEmployeeId());
                    }

                } else {
                    assertEquals(0, certificates.size());
                }
            }
        }

    }
    
    public void testAncestry() {
        List<Grandfather> grandfathers = theCompanyDao.getGrandfathers2xMultiLevelGrouping();
        assertEquals(2, grandfathers.size());
        Grandfather tom = grandfathers.get(0);
        Grandfather sam = grandfathers.get(1);
        assertEquals("Grand Tom", tom.getName());
        assertEquals("Grand Sam", sam.getName());
        assertEquals(1, tom.getSons().size());
        assertEquals(1, sam.getSons().size());
        Father john1 = tom.getSons().get(0);
        Father john2 = sam.getSons().get(0);
        assertEquals("John", john1.getName());
        assertEquals("John", john2.getName());
        assertEquals(2, john1.getSons().size());
        assertEquals("Little Jimmy", john1.getSons().get(0).getName());
        assertEquals("Little Timmy", john1.getSons().get(1).getName());
        assertEquals(2, john2.getSons().size());
        assertEquals("Little Jacky", john2.getSons().get(0).getName());
        assertEquals("Little Stanny", john2.getSons().get(1).getName());
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
