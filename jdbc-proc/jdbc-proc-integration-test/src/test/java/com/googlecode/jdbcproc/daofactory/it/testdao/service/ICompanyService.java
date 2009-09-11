package com.googlecode.jdbcproc.daofactory.it.testdao.service;

import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;

/**
 *  Company service
 */
public interface ICompanyService {

    void createCompany(Company aCompany);

    void iterateCompanies();
}
