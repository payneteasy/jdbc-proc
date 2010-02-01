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
import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.googlecode.jdbcproc.daofactory.impl.DaoMethodInvoker;
import com.googlecode.jdbcproc.daofactory.impl.block.factory.BlockFactoryUtils;
import com.googlecode.jdbcproc.daofactory.impl.block.factory.CallableStatementExecutorBlockFactory;
import com.googlecode.jdbcproc.daofactory.impl.block.factory.OutputParametersGetterBlockFactory;
import com.googlecode.jdbcproc.daofactory.impl.block.factory.ParametersSetterBlockFactory;
import com.googlecode.jdbcproc.daofactory.impl.block.factory.RegisterOutParametersBlockFactory;
import com.googlecode.jdbcproc.daofactory.impl.block.factory.ResultSetConverterBlockFactory;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterManager;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfoManagerInitOnStartup;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.IStoredProcedureInfoManager;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * @version 1.00 Jan 25, 2010 12:22:54 PM
 *
 * @author esinev
 * @author dmk
 */
public class DAOMethodInfoGuice implements DAOMethodInfo {

  private final Logger LOG = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  private final ParameterConverterManager theParameterConverterManager 
      = new ParameterConverterManager();
  private final IStoredProcedureInfoManager theStoredProcedureInfoManager;
  
  // factories
  private final CallableStatementExecutorBlockFactory theCallableStatementExecutorBlockFactory
      = new CallableStatementExecutorBlockFactory();
  private final OutputParametersGetterBlockFactory theOutputParametersGetterBlockFactory
      = new OutputParametersGetterBlockFactory();
  private final ParametersSetterBlockFactory theParametersSetterBlockFactory
      = new ParametersSetterBlockFactory();
  private final RegisterOutParametersBlockFactory theRegisterOutParametersBlockFactory
      = new RegisterOutParametersBlockFactory();
  private final ResultSetConverterBlockFactory theResultSetConverterBlockFactory
      = new ResultSetConverterBlockFactory();

  @Inject
  public DAOMethodInfoGuice(JdbcTemplate jdbcTemplate) throws Exception {
    this.jdbcTemplate = jdbcTemplate;

    // Gets information about all procedures from database
    theStoredProcedureInfoManager = new StoredProcedureInfoManagerInitOnStartup(jdbcTemplate);
  }

  /**
   * Creates method info for bets performance
   *
   * @param aDaoMethod method
   * @return method info
   */
  public DaoMethodInvoker createDaoMethodInvoker(Method aDaoMethod) {
    AStoredProcedure procedureAnnotation = aDaoMethod.getAnnotation(AStoredProcedure.class);
    Assert.notNull(procedureAnnotation, "Method must have @AStoredProcedure annotation");

    String procedureName = procedureAnnotation.name();
    Assert.hasText(procedureName, "Method " + aDaoMethod.toString()
        + " has empty name() parameter in @AStoredProcedure annotation");

    StoredProcedureInfo procedureInfo 
        = theStoredProcedureInfoManager.getProcedureInfo(procedureName);
    if (LOG.isDebugEnabled()) {
      LOG.debug("      Found procedure info: " + procedureInfo);
    }
    Assert.notNull(procedureInfo, "There are no procedure '" + procedureName + "' in database");

    String callString = createCallString(procedureInfo);

    boolean isReturnIterator = BlockFactoryUtils.isReturnIterator(aDaoMethod);

    return new DaoMethodInvoker(
        procedureInfo.getProcedureName()
        , callString
        , theRegisterOutParametersBlockFactory.create(procedureInfo)
        , theParametersSetterBlockFactory.create(jdbcTemplate, theParameterConverterManager, 
            aDaoMethod, procedureInfo)
        , theCallableStatementExecutorBlockFactory.create(aDaoMethod, procedureInfo)
        , theOutputParametersGetterBlockFactory.create(theParameterConverterManager, aDaoMethod, 
            procedureInfo)
        , theResultSetConverterBlockFactory.create(aDaoMethod, procedureInfo, 
            theParameterConverterManager)
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

  @Override
  public String toString() {
    return "DaoMethodInfoFactory{" +
        "jdbcTemplate=" + jdbcTemplate +
        ", theParameterConverterManager=" + theParameterConverterManager +
        ", theStoredProcedureInfoManager=" + theStoredProcedureInfoManager +
        ", theCallableStatementExecutorBlockFactory=" + theCallableStatementExecutorBlockFactory +
        ", theOutputParametersGetterBlockFactory=" + theOutputParametersGetterBlockFactory +
        ", theParametersSetterBlockFactory=" + theParametersSetterBlockFactory +
        ", theRegisterOutParametersBlockFactory=" + theRegisterOutParametersBlockFactory +
        ", theResultSetConverterBlockFactory=" + theResultSetConverterBlockFactory +
        '}';
  }
}
