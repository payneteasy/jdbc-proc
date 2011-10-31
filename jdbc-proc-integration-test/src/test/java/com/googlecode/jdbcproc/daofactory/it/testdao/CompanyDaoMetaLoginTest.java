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

package com.googlecode.jdbcproc.daofactory.it.testdao;

import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyMetaLoginDao;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;

import java.util.List;

public class CompanyDaoMetaLoginTest extends CompanyDaoTest {
  
    public void testCreateCompanySecured() {
        Company company = new Company();
        company.setName("first-secured");
        theCompanyMetaLoginDao.createCompanySecured(company);
        assertNotNull(company.getId());

        long id = theCompanyMetaLoginDao.createCompanySecured("second-secured");
        assertTrue(id!=0);
    }
  
    public void testGetCompaniesNamesSecured() {
      // creates companies
      for(int i=0; i<10; i++) {
          Company company = new Company();
          company.setName("company-"+i);
          theCompanyDao.createCompany(company);
      }

      List<String> names = theCompanyMetaLoginDao.getCompaniesNamesSecured();
      assertEquals(10, names.size());
      for(int i=0; i<10; i++) {
          assertEquals("company-"+i, names.get(i));
      }
    }
  
  
    @Override protected String[] getConfigLocations() {
      return new String[] {"/spring/test-mysql-datasource.xml"
        , "/spring/test-dao-metalogin.xml"
      };
    }

    public void setTheCompanyDao(ICompanyDao theCompanyDao) {
        this.theCompanyDao = theCompanyDao;
    }

    public void setTheCompanyMetaLoginDao(ICompanyMetaLoginDao theCompanyMetaLoginDao) {
        this.theCompanyMetaLoginDao = theCompanyMetaLoginDao;
    }

    private ICompanyMetaLoginDao theCompanyMetaLoginDao;
}
