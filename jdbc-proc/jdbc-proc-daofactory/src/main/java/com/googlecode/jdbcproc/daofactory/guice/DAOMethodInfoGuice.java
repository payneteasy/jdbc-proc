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

import com.google.inject.Inject;
import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.impl.DaoMethodInvoker;
import com.googlecode.jdbcproc.daofactory.impl.block.BlockFactoryUtils;
import com.googlecode.jdbcproc.daofactory.impl.block.service.CallableStatementExecutorBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.OutputParametersGetterBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.ParametersSetterBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.RegisterOutParametersBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.ResultSetConverterBlockService;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.IStoredProcedureInfoManager;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * @version 1.00 Apr 28, 2010 1:17:09 PM
 *
 * @author crazydmk
 */
public class DAOMethodInfoGuice implements DAOMethodInfo {

  private final Logger LOG = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;
  private final ParameterConverterService parameterConverterService;
  private final CallableStatementExecutorBlockService callableStatementExecutorBlockService;
  private final OutputParametersGetterBlockService outputParametersGetterBlockService;
  private final ParametersSetterBlockService parametersSetterBlockService;
  private final RegisterOutParametersBlockService registerOutParametersBlockService;
  private final ResultSetConverterBlockService resultSetConverterBlockService;
  private final IStoredProcedureInfoManager storedProcedureInfoManager;

  @Inject
  public DAOMethodInfoGuice(JdbcTemplate jdbcTemplate,
      ParameterConverterService parameterConverterService,
      CallableStatementExecutorBlockService callableStatementExecutorBlockService,
      OutputParametersGetterBlockService outputParametersGetterBlockService,
      ParametersSetterBlockService parametersSetterBlockService,
      RegisterOutParametersBlockService registerOutParametersBlockService,
      ResultSetConverterBlockService resultSetConverterBlockService,
      IStoredProcedureInfoManager storedProcedureInfoManager) throws Exception {
    this.jdbcTemplate = jdbcTemplate;
    this.parameterConverterService = parameterConverterService;
    this.callableStatementExecutorBlockService = callableStatementExecutorBlockService;
    this.outputParametersGetterBlockService = outputParametersGetterBlockService;
    this.parametersSetterBlockService = parametersSetterBlockService;
    this.registerOutParametersBlockService = registerOutParametersBlockService;
    this.resultSetConverterBlockService = resultSetConverterBlockService;
    this.storedProcedureInfoManager = storedProcedureInfoManager;
  }

  /**
   * Creates method info for bets performance
   * @param daoMethod method
   * @return method info
   */
  public DaoMethodInvoker createDaoMethodInvoker(Method daoMethod) {
    AStoredProcedure procedureAnnotation = daoMethod.getAnnotation(AStoredProcedure.class);
    Assert.notNull(procedureAnnotation, "Method must have @AStoredProcedure annotation");

    String procedureName = procedureAnnotation.name();
    Assert.hasText(procedureName, "Method " + daoMethod.toString()
        + " has empty name() parameter in @AStoredProcedure annotation");

    StoredProcedureInfo procedureInfo = storedProcedureInfoManager.getProcedureInfo(procedureName);
    if(LOG.isDebugEnabled()) {
      LOG.debug("      Found procedure info: "+procedureInfo);
    }
    Assert.notNull(procedureInfo, "There are no procedure '" + procedureName + "' in database");

    String callString = createCallString(procedureInfo);

    boolean isReturnIterator = BlockFactoryUtils.isReturnIterator(daoMethod);

    return new DaoMethodInvoker(procedureInfo.getProcedureName(), callString
        , registerOutParametersBlockService.create(procedureInfo)
        , parametersSetterBlockService.create(jdbcTemplate, parameterConverterService, daoMethod
            , procedureInfo)
        , callableStatementExecutorBlockService.create(daoMethod, procedureInfo)
        , outputParametersGetterBlockService.create(parameterConverterService, daoMethod
            , procedureInfo)
        , resultSetConverterBlockService.create(daoMethod, procedureInfo, parameterConverterService)
        , isReturnIterator
    );
  }

  /**
   * Create call query string.
   * <p/>
   * For example:
   * <code>
   * { call create_processors( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) }
   * </code>
   *
   * @param procedureInfo procedure info
   * @return call string
   */
  private String createCallString(StoredProcedureInfo procedureInfo) {
    StringBuilder sb = new StringBuilder();
    sb.append("{ call ");
    sb.append(procedureInfo.getProcedureName());
    sb.append("(");
    for (int i = 0; i < procedureInfo.getArgumentsCounts(); i++) {
      if (i > 0) {
        sb.append(", ? ");
      } else {
        sb.append(" ? ");
      }
    }
    sb.append(") }");
    return sb.toString();
  }

  public String toString() {
    return "DaoMethodInfoFactory{" +
        "jdbcTemplate=" + jdbcTemplate +
        ", parameterConverterService=" + parameterConverterService +
        ", storedProcedureInfoManager=" + storedProcedureInfoManager +
        ", callableStatementExecutorBlockService=" + callableStatementExecutorBlockService +
        ", outputParametersGetterBlockService=" + outputParametersGetterBlockService +
        ", parametersSetterBlockService=" + parametersSetterBlockService +
        ", registerOutParametersBlockService=" + registerOutParametersBlockService +
        ", resultSetConverterBlockService=" + resultSetConverterBlockService +
        '}';
  }
}
