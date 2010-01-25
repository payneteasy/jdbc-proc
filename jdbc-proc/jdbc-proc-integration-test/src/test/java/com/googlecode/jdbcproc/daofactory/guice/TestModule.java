/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.googlecode.jdbcproc.daofactory.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

 public class TestModule extends AbstractModule {
  
  @Override
  protected void configure() {
    bind(DAOMethodInfo.class).to(DAOMethodInfoGuice.class);
  }
  
  @Provides
  @Singleton
  DataSource provideDataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/jdbcprocdb");
    dataSource.setUsername("jdbcproc");
    dataSource.setPassword("jdbcproc");
    dataSource.setDefaultAutoCommit(true);
    dataSource.setValidationQuery("call create_collections()");
    
    return dataSource;
  }
  
  @Provides
  JdbcTemplate provideJDBCTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
  
  @Provides
  @Singleton
  ICompanyDao provideCompanyDAO(JdbcTemplate jdbcTemplate, DAOMethodInfo daoMethodInfo) {
    return new StoredProcedureDAOProvider<ICompanyDao>(ICompanyDao.class, 
        jdbcTemplate, daoMethodInfo).get();
  }
  
  @Provides
  @Singleton
  IEmployeeDao provideEmployeeDAO(JdbcTemplate jdbcTemplate, DAOMethodInfo daoMethodInfo) {
    return new StoredProcedureDAOProvider<IEmployeeDao>(IEmployeeDao.class, 
        jdbcTemplate, daoMethodInfo).get();
  }
}
