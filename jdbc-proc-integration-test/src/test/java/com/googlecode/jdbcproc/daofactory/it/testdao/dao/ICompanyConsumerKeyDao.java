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

package com.googlecode.jdbcproc.daofactory.it.testdao.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AConsumerKey;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.it.testdao.domain.Company;

public interface ICompanyConsumerKeyDao {
  
    /**
     * Creates company with secured info
     * @param aCompany company
     */
    @AStoredProcedure(name = "create_company_secured_consumer_key")
    @AConsumerKey        
    void createCompanySecured(String consumerKey, Company aCompany);
}
