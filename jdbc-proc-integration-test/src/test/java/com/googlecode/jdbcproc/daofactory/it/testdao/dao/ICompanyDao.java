package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.CompanyWithEmployees;

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
     * @param aCompanyId new company
     */
    @AStoredProcedure(name = "get_company_employees")
    CompanyWithEmployees getCompanyWithEmployeesById(long aCompanyId);
}
