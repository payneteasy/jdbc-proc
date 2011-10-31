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

package com.googlecode.jdbcproc.daofactory;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.impl.DaoMethodInvoker;
import com.googlecode.jdbcproc.daofactory.impl.block.BlockFactoryUtils;
import com.googlecode.jdbcproc.daofactory.impl.block.service.CallableStatementExecutorBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.CallableStatementExecutorBlockServiceImpl;
import com.googlecode.jdbcproc.daofactory.impl.block.service.OutputParametersGetterBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.OutputParametersGetterBlockServiceImpl;
import com.googlecode.jdbcproc.daofactory.impl.block.service.ParametersSetterBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.ParametersSetterBlockServiceImpl;
import com.googlecode.jdbcproc.daofactory.impl.block.service.RegisterOutParametersBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.RegisterOutParametersBlockServiceImpl;
import com.googlecode.jdbcproc.daofactory.impl.block.service.ResultSetConverterBlockService;
import com.googlecode.jdbcproc.daofactory.impl.block.service.ResultSetConverterBlockServiceImpl;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategyFactory;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategyFactory;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementGetStrategyFactoryNameImpl;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementSetStrategyFactoryNameImpl;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterServiceImpl;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.IStoredProcedureInfoManager;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfoManagerInitOnStartup;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * @version 1.00 Apr 28, 2010 1:28:16 PM
 *
 * @author esinev
 */
public class DaoMethodInfoFactory implements InitializingBean, DAOMethodInfo {

  private final Logger LOG = LoggerFactory.getLogger(getClass());

  private JdbcTemplate jdbcTemplate;
  private ParameterConverterService parameterConverterService;
  private CallableStatementExecutorBlockService callableStatementExecutorBlockService;
  private OutputParametersGetterBlockService outputParametersGetterBlockService;
  private ParametersSetterBlockService parametersSetterBlockService;
  private RegisterOutParametersBlockService registerOutParametersBlockService;
  private ResultSetConverterBlockService resultSetConverterBlockService;
  private IStoredProcedureInfoManager storedProcedureInfoManager;
  private ICallableStatementSetStrategyFactory theSetStrategyFactory = new CallableStatementSetStrategyFactoryNameImpl();
  private ICallableStatementGetStrategyFactory theGetStrategyFactory = new CallableStatementGetStrategyFactoryNameImpl();
  /** MetaLoginInfoService */
  private IMetaLoginInfoService theMetaLoginInfoService;


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

      return new DaoMethodInvoker(procedureInfo.getProcedureName()
              , callString
              , registerOutParametersBlockService.create(procedureInfo)
              , parametersSetterBlockService.create(jdbcTemplate, parameterConverterService, daoMethod, procedureInfo, theMetaLoginInfoService)
              , callableStatementExecutorBlockService.create(daoMethod, procedureInfo)
              , outputParametersGetterBlockService.create(parameterConverterService, daoMethod, procedureInfo)
              , resultSetConverterBlockService.create(daoMethod, procedureInfo, parameterConverterService)
              , isReturnIterator
              , theSetStrategyFactory
              , theGetStrategyFactory
      );
  }

  /* 
   * it's not best decision to make services non final, 
   * but esinev wants this =) 
   */
  public void afterPropertiesSet() throws Exception {
    if (parameterConverterService == null) {
      setParameterConverterService(new ParameterConverterServiceImpl());
    }
    if (callableStatementExecutorBlockService == null) {
      setCallableStatementExecutorBlockService(new CallableStatementExecutorBlockServiceImpl());
    }
    if (outputParametersGetterBlockService == null) {
      setOutputParametersGetterBlockService(new OutputParametersGetterBlockServiceImpl());
    }
    if (parametersSetterBlockService == null) {
      setParametersSetterBlockService(new ParametersSetterBlockServiceImpl());
    }
    if (registerOutParametersBlockService == null) {
      setRegisterOutParametersBlockService(new RegisterOutParametersBlockServiceImpl());
    }
    if (resultSetConverterBlockService == null) {
      setResultSetConverterBlockService(new ResultSetConverterBlockServiceImpl());
    }
    if(storedProcedureInfoManager == null) {
      setStoredProcedureInfoManager(new StoredProcedureInfoManagerInitOnStartup(jdbcTemplate));
    }
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void setParameterConverterService(ParameterConverterService parameterConverterService) {
    this.parameterConverterService = parameterConverterService;
  }

  public void setCallableStatementExecutorBlockService(
      CallableStatementExecutorBlockService callableStatementExecutorBlockService) {
    this.callableStatementExecutorBlockService = callableStatementExecutorBlockService;
  }

  public void setOutputParametersGetterBlockService(
      OutputParametersGetterBlockService outputParametersGetterBlockService) {
    this.outputParametersGetterBlockService = outputParametersGetterBlockService;
  }

  public void setParametersSetterBlockService(
      ParametersSetterBlockService parametersSetterBlockService) {
    this.parametersSetterBlockService = parametersSetterBlockService;
  }

  public void setRegisterOutParametersBlockService(
      RegisterOutParametersBlockService registerOutParametersBlockService) {
    this.registerOutParametersBlockService = registerOutParametersBlockService;
  }

  public void setResultSetConverterBlockService(
      ResultSetConverterBlockService resultSetConverterBlockService) {
    this.resultSetConverterBlockService = resultSetConverterBlockService;
  }

  public void setStoredProcedureInfoManager(IStoredProcedureInfoManager storedProcedureInfoManager) {
    this.storedProcedureInfoManager = storedProcedureInfoManager;
  }

    /** CallableStatementGetStrategyFactory */
    public void setCallableStatementGetStrategyFactory(ICallableStatementGetStrategyFactory aCallableStatementGetStrategyFactory) { theGetStrategyFactory = aCallableStatementGetStrategyFactory; }

    /** Set strategy factory */
    public void setCallableStatementSetStrategyFactory(ICallableStatementSetStrategyFactory aCallableStatementSetStrategyFactory) { theSetStrategyFactory = aCallableStatementSetStrategyFactory; }

    /**
     * MetaLoginInfoService to use AMetaLoginInfo annotation in stored procedure
     *
     */
    public void setMetaLoginInfoService(IMetaLoginInfoService aMetaLoginInfoService) { theMetaLoginInfoService = aMetaLoginInfoService; }

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
