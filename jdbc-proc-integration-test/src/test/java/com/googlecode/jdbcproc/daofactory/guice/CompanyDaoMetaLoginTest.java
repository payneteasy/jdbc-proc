/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.jdbcproc.daofactory.guice;

import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.IMetaLoginInfoService;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyMetaLoginDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.service.impl.SimpleMetaLoginInfoServiceImpl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class CompanyDaoMetaLoginTest extends AbstractDatabaseTest {

  private Injector injector;

  @Override public void setUp() {
    injector = Guice.createInjector(new SimpleModule() {
      @Override protected void configure() {
        super.configure();
        bind(IMetaLoginInfoService.class).to(SimpleMetaLoginInfoServiceImpl.class);
      }

      @Provides
      @Singleton
      ICompanyMetaLoginDao provideCompanyMetaLoginDao(JdbcTemplate jdbcTemplate,
          DAOMethodInfo daoMethodInfo) {
        return new StoredProcedureDaoProvider<ICompanyMetaLoginDao>(ICompanyMetaLoginDao.class,
            jdbcTemplate, daoMethodInfo).get();
      }
    });
  }

  public void testCreateCompanySecured() {
    final ICompanyMetaLoginDao companyMetaLoginDao = injector.getInstance(ICompanyMetaLoginDao.class);

    Company company = new Company();
    company.setName("first-secured");
    companyMetaLoginDao.createCompanySecured(company);
    assertNotNull(company.getId());

    long id = companyMetaLoginDao.createCompanySecured("second-secured");
    assertTrue(id!=0);
  }

  public void testGetCompaniesNamesSecured() {
    final ICompanyDao companyDao = injector.getInstance(ICompanyDao.class);
    final ICompanyMetaLoginDao companyMetaLoginDao = injector.getInstance(ICompanyMetaLoginDao.class);

    // creates companies
    for(int i=0; i<10; i++) {
      Company company = new Company();
      company.setName("company-"+i);
      companyDao.createCompany(company);
    }

    List<String> names = companyMetaLoginDao.getCompaniesNamesSecured();
    assertEquals(10, names.size());
    for(int i=0; i<10; i++) {
      assertEquals("company-"+i, names.get(i));
    }
  }
}
