package com.googlecode.jdbcproc.daofactory.it.testdao.service.impl;

import com.googlecode.jdbcproc.daofactory.it.testdao.service.ICompanyService;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.CloseableIterator;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Company service implementation
 */
@Transactional
public class CompanyServiceImpl implements ICompanyService {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public void createCompany(Company aCompany) {
        theCompanyDao.createCompany(aCompany);
    }

    public void iterateCompanies() {
        CloseableIterator<Company> iterator = theCompanyDao.getAllCompanies();
        try {
            while (iterator.hasNext()) {
                Company company = iterator.next();
                LOG.info("company = " + company);
            }
        } finally {
            iterator.close();
        }
    }

    /** ICompanyDao */
    public void setCompanyDao(ICompanyDao aCompanyDao) { theCompanyDao = aCompanyDao ; }

    /** ICompanyDao */
    private ICompanyDao theCompanyDao ;
}
