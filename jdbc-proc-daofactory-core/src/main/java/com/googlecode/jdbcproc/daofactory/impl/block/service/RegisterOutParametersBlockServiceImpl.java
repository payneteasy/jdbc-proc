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

import com.googlecode.jdbcproc.daofactory.impl.block.IRegisterOutParametersBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.IndexDataTypePair;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.RegisterOutParametersBlockImpl;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import java.util.LinkedList;

/**
 * @version 1.00 Apr 28, 2010 12:27:15 PM
 *
 * @author esinev
 * @author dmk
 */
public class RegisterOutParametersBlockServiceImpl implements RegisterOutParametersBlockService {

  public IRegisterOutParametersBlock create(StoredProcedureInfo procedureInfo) {
    LinkedList<IndexDataTypePair> list = new LinkedList<IndexDataTypePair>();
    for (StoredProcedureArgumentInfo argumentInfo : procedureInfo.getArguments()) {
      if (argumentInfo.getColumnType() == StoredProcedureArgumentInfo.OUT) {
        list.add(new IndexDataTypePair(argumentInfo.getStatementArgument().getParameterIndex(), argumentInfo.getDataType()));
      }
    }
    return list.size() > 0 ? new RegisterOutParametersBlockImpl(list) : null;
  }
}
