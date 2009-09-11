package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.service.ICompanyService;

/**
 * Tests for iterator
 */
public class IteratorTest extends DatabaseAwareTest {

    public void testIterator() {

        // creates companies
        for (int i = 0; i < 10; i++) {
            Company company = new Company();
            company.setName("company-" + i);
            theCompanyService.createCompany(company);
        }

        theCompanyService.iterateCompanies();
    }


    /** ICompanyService */
    public void setCompanyService(ICompanyService aCompanyService) { theCompanyService = aCompanyService ; }

    /** ICompanyService */
    private ICompanyService theCompanyService ;
}
