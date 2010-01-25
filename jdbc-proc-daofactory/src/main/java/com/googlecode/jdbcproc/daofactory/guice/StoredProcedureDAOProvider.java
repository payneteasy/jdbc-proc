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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.impl.StoredProcedureDaoInvocationHandler;

import java.lang.reflect.Proxy;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @version 1.00 Jan 25, 2010 12:17:03 PM
 *
 * @author esinev
 * @author dmk
 */
@Singleton
public class StoredProcedureDAOProvider<T> implements Provider<T> {

  private final Class<T> interfaze;
  private final JdbcTemplate jdbcTemplate;
  private final DAOMethodInfo daoMethodInfo;

  @Inject
  public StoredProcedureDAOProvider(Class<T> interfaze, JdbcTemplate jdbcTemplate, 
                                    DAOMethodInfo daoMethodInfo) {
    this.interfaze = interfaze;
    this.jdbcTemplate = jdbcTemplate;
    this.daoMethodInfo = daoMethodInfo;
  }

  @SuppressWarnings("unchecked")
  public T get() {
    try {
      return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
          , new Class[] {interfaze}
          , new StoredProcedureDaoInvocationHandler(interfaze, jdbcTemplate, daoMethodInfo)
      );
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
}
