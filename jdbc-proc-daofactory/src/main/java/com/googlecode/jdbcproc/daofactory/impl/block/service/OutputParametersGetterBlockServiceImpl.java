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
import com.googlecode.jdbcproc.daofactory.impl.block.impl.OutputParametersGetterHasReturnBlock;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import org.springframework.util.Assert;

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

  public IOutputParametersGetterBlock create(ParameterConverterService converterService, Method daoMethod, StoredProcedureInfo procedureInfo) {

    // List.getAll();
    if (BlockFactoryUtils.isGetAllMethod(daoMethod, procedureInfo)) {
      return null;

      // CREATE ENTITY
    } else if (isCreateEntityMethod(daoMethod, procedureInfo)) {
      int entityParameterIndex = findEntityParameterIndex(daoMethod);
      List<EntityPropertySetter> setters = new LinkedList<>();
      Class entityClass = daoMethod.getParameterTypes()[entityParameterIndex];
      for (StoredProcedureArgumentInfo argumentInfo : procedureInfo.getArguments()) {
        if (argumentInfo.isOutputParameter()) {
          Method getterMethod = BlockFactoryUtils.findGetterMethod(entityClass, argumentInfo);
          Method setterMethod = BlockFactoryUtils.findSetterMethod(entityClass, getterMethod);
          IParameterConverter paramConverter = converterService
              .getConverter(argumentInfo.getDataType(), getterMethod.getReturnType());
          setters.add(new EntityPropertySetter(setterMethod, paramConverter
              , argumentInfo.getColumnName(), argumentInfo.getStatementArgument(), argumentInfo.getDataType()));
        }
      }
      return setters.size() > 0 ? new OutputParametersGetterBlockEntity(setters, entityParameterIndex) : null;

      // RETURN ONE OUTPUT PARAMETER
    } else if (BlockFactoryUtils.isOneOutputHasReturn(daoMethod, procedureInfo)) {
        return createOutputParameterHasReturn(converterService, daoMethod, procedureInfo);

      // DEFAULT NO     
    } else {
      return null;
    }
  }

  private int findEntityParameterIndex(Method daoMethod) {
    int entityParameterIndex = -1;
    for (int i = 0; i < daoMethod.getParameterCount(); i++) {
      if (!BlockFactoryUtils.isSimpleOrListType(daoMethod.getParameterTypes()[i])) {
        entityParameterIndex = i;
        break;
      }
    }
    Assert.isTrue(entityParameterIndex >= 0, "Did not find an entity parameter, although this is an entity (probably with lists)");
    return entityParameterIndex;
  }

  private boolean isCreateEntityMethod(Method daoMethod, StoredProcedureInfo procedureInfo) {
    boolean ok = false;
    if (procedureInfo.getArgumentsCounts() >= 1) {
      if (daoMethod.getParameterTypes().length == 1 && !BlockFactoryUtils.isSimpleOrListType(daoMethod.getParameterTypes()[0])) {
        ok = true;
      } else {
        if (daoMethod.getParameterTypes().length > 1) {
          // maybe it's entity + lists
          int entities = 0;
          int lists = 0;
          for (int i = 0; i < daoMethod.getParameterTypes().length; i++) {
            if (!BlockFactoryUtils.isSimpleOrListType(daoMethod.getParameterTypes()[i])) {
              entities++;
            }
            if (BlockFactoryUtils.isListType(daoMethod.getParameterTypes()[i])) {
              lists++;
            }
          }
          if (entities == 1 && lists > 0) {
            // yes, it is
            ok = true;
          }
        }
      }
      return ok;
    }
    return procedureInfo.getArgumentsCounts() >= 1
        && daoMethod.getParameterTypes().length == 1
        && !BlockFactoryUtils.isSimpleType(daoMethod.getParameterTypes()[0]);
  }

    private OutputParametersGetterHasReturnBlock createOutputParameterHasReturn(
        ParameterConverterService converterService, Method daoMethod,
        StoredProcedureInfo procedureInfo) {
      Class methodReturnType = daoMethod.getReturnType();
      StoredProcedureArgumentInfo procedureReturn = getOutputParameter(procedureInfo.getArguments());
      return new OutputParametersGetterHasReturnBlock(
          converterService.getConverter(procedureReturn.getDataType(), methodReturnType)
          , procedureReturn.getStatementArgument());
    }

    private StoredProcedureArgumentInfo getOutputParameter(
        List<StoredProcedureArgumentInfo> arguments) {
      StoredProcedureArgumentInfo info = null;
      for (StoredProcedureArgumentInfo argument : arguments) {
        if (argument.isOutputParameter()) {
          if (info == null) {
            info = argument;
          } else {
            throw new IllegalStateException("Procedure must have only one output parameter");
          }
        }
      }
      return info;
    }

}
