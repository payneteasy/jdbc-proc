package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import java.util.List;

import com.googlecode.jdbcproc.daofactory.CloseableIterator;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.CompanyWithEmployees;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Certificate2x;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Company2x;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.onetomany2x.Grandfather;

/**
 * Test dao
 */
public interface ICompanyDao {

    /**
     * Creates company
     *
     * @param aCompany new company
     */
    @AStoredProcedure(name = "create_company")
    void createCompany(Company aCompany);

    /**
     * Creates company
     *
     * @param aName company name
     * @return company id
     */
    @AStoredProcedure(name = "create_company")
    long createCompany(String aName);

    /**
     * Gets company with employees by id
     *
     * @param aCompanyId new company
     * @return company
     */
    @AStoredProcedure(name = "get_company_employees")
    CompanyWithEmployees getCompanyWithEmployeesById(long aCompanyId);

    /**
     * Gets all companies with employees
     * 
     * @return companies
     */
    @AStoredProcedure(name = "get_company_employees")
    List<CompanyWithEmployees> getAllCompaniesWithEmployees();

    /**
     * Gets iterator for companies with employees
     *
     * @return companies
     */
    @AStoredProcedure(name = "get_companies")
    CloseableIterator<Company> getAllCompanies();

    /**
     * Gets companies' names
     *
     * @return companies's names
     */
    @AStoredProcedure(name = "get_companies_names")
    List<String> getCompaniesNames();

    /**
     * Gets companies' names
     *
     * @return companies's names
     */
    @AStoredProcedure(name = "get_companies_names")
    CloseableIterator<String> getCompaniesNamesIterator();

    /**
     * Gets companies, employees and certificates with OneToMany Links
     * @return companies
     */
    @AStoredProcedure(name = "get_company_2x")
    List<Company2x> getCompanies2x();
    
    /**
     * Gets grandfathers, fathers and sons with OneToMany Links with
     * tricky multi-level grouping which requires grouper to check not only
     * parents but all ancestors, too.
     * @return companies
     */
    @AStoredProcedure(name = "get_ancestry_2x_multi_level_grouping")
    List<Grandfather> getGrandfathers2xMultiLevelGrouping();
    
    /**
     * Creates certificate for employee
     * @param aCertificate certificate
     */
    @AStoredProcedure(name = "create_certificate")
    void createCertificate(Certificate2x aCertificate);
}
