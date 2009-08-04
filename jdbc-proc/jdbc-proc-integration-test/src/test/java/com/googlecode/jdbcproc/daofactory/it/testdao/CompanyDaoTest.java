package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.DatabaseAwareTest;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;

/**
 *
 */
public class CompanyDaoTest extends DatabaseAwareTest {

    public void test() {
        Company company = new Company();
        company.setName("first");
        theCompanyDao.createCompany(company);
        assertNotNull(company.getId());
    }

//    /** Company dao */

    //    public ICompanyDao getCompanyDao() { return theCompanyDao ; }
    public void setCompanyDao(ICompanyDao aCompanyDao) {
        theCompanyDao = aCompanyDao;
    }

    /**
     * Company dao
     */
    private ICompanyDao theCompanyDao;

}
