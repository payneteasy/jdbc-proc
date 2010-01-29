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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.IEmployeeDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Employee;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class EmployeeDAOTest extends AbstractDatabaseTest {

  @Test
  public void test() {
    Injector injector = Guice.createInjector(new SimpleModule());

    final ICompanyDao companyDao = injector.getInstance(ICompanyDao.class);
    final IEmployeeDao employeeDao = injector.getInstance(IEmployeeDao.class);

    Company company = new Company();
    company.setName("first");
    companyDao.createCompany(company);

    Employee employee = new Employee();
    employee.setFirstname("Ivan");
    employee.setLastname("Petrov");
    employee.setCompany(company);
    employeeDao.createEmployee(employee);

    Employee employeeFromBase = employeeDao.getEmployeeById(employee.getId());
    assertNotNull(employeeFromBase);
    assertEquals(employee, employeeFromBase);
  }
}
