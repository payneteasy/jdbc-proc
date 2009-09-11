package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.CompanyWithEmployees;
import com.googlecode.jdbcproc.daofactory.CloseableIterator;

import java.util.List;
import java.util.Iterator;

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
}
