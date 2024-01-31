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

package com.googlecode.jdbcproc.daofactory.impl.block.service;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import java.lang.reflect.Method;

/**
 * @version 1.00 Apr 27, 2010 9:31:27 PM
 *
 * @author esinev
 * @author dmk
 */
public interface ResultSetConverterBlockService {
  
  IResultSetConverterBlock create(Method daoMethod, StoredProcedureInfo procedureInfo, 
      ParameterConverterService converterService);
}
