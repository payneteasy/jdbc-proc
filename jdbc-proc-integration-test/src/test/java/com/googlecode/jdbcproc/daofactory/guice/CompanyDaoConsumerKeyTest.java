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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.IMetaLoginInfoService;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyConsumerKeyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.service.impl.SimpleMetaLoginInfoServiceImpl;

import org.springframework.jdbc.core.JdbcTemplate;

public class CompanyDaoConsumerKeyTest extends AbstractDatabaseTest {

  private Injector injector;

  @Override public void setUp() {
    injector = Guice.createInjector(new SimpleModule() {
      @Override protected void configure() {
        super.configure();
        bind(IMetaLoginInfoService.class).to(SimpleMetaLoginInfoServiceImpl.class);
      }

      @Provides
      @Singleton
      ICompanyConsumerKeyDao provideCompanyConsumerKeyDao(JdbcTemplate jdbcTemplate, DAOMethodInfo daoMethodInfo) {
        return new StoredProcedureDaoProvider<ICompanyConsumerKeyDao>(ICompanyConsumerKeyDao.class, jdbcTemplate, daoMethodInfo).get();
      }
    });
  }

  public void testCreateCompanySecured() {
    final ICompanyConsumerKeyDao companyDao = injector.getInstance(ICompanyConsumerKeyDao.class);

    Company company = new Company();
    company.setName("first-secured");
    companyDao.createCompanySecured("consumer_key", company);
    assertNotNull(company.getId());
  }
}
