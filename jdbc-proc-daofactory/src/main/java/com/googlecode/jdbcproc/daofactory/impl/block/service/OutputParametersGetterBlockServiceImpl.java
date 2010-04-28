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

import com.googlecode.jdbcproc.daofactory.impl.block.BlockFactoryUtils;
import com.googlecode.jdbcproc.daofactory.impl.block.IOutputParametersGetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.EntityPropertySetter;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.OutputParametersGetterBlockEntity;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @version 1.00 Apr 28, 2010 12:02:46 PM
 *
 * @author esinev
 * @author dmk
 */
public class OutputParametersGetterBlockServiceImpl implements OutputParametersGetterBlockService {

  public IOutputParametersGetterBlock create(ParameterConverterService converterService,
      Method daoMethod, StoredProcedureInfo procedureInfo) {
    // List.getAll();
    if (BlockFactoryUtils.isGetAllMethod(daoMethod, procedureInfo)) {
      return null;

      // CREATE ENTITY
    } else if (isCreateEntityMethod(daoMethod, procedureInfo)) {
      List<EntityPropertySetter> setters = new LinkedList<EntityPropertySetter>();
      Class entityClass = daoMethod.getParameterTypes()[0];
      for (StoredProcedureArgumentInfo argumentInfo : procedureInfo.getArguments()) {
        if (argumentInfo.isOutputParameter()) {
          Method getterMethod = BlockFactoryUtils.findGetterMethod(entityClass, argumentInfo);
          Method setterMethod = BlockFactoryUtils.findSetterMethod(entityClass, getterMethod);
          IParameterConverter paramConverter = converterService
              .getConverter(argumentInfo.getDataType(), getterMethod.getReturnType());
          setters.add(new EntityPropertySetter(setterMethod, paramConverter
              , argumentInfo.getColumnName(), argumentInfo.getDataType()));
        }
      }
      return setters.size() > 0 ? new OutputParametersGetterBlockEntity(setters) : null;

      // return one output parameter
    } else if (BlockFactoryUtils.isOneOutputHasReturn(daoMethod, procedureInfo)) {
      // see ResultSetConverterBlockOutputParameterHasReturn
      return null;

      // DEFAULT NO     
    } else {
      return null;
    }
  }

  private boolean isCreateEntityMethod(Method daoMethod, StoredProcedureInfo procedureInfo) {
    return procedureInfo.getArgumentsCounts() > 1 
        && daoMethod.getParameterTypes().length == 1
        && !BlockFactoryUtils.isSimpleType(daoMethod.getParameterTypes()[0]);
  }
}
