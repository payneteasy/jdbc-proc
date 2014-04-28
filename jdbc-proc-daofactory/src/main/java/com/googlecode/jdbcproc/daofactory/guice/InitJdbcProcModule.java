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

import com.googlecode.jdbcproc.daofactory.DAOMethodInfo;
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
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementGetStrategyFactoryIndexImpl;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementGetStrategyFactoryNameImpl;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementSetStrategyFactoryIndexImpl;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementSetStrategyFactoryNameImpl;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterServiceImpl;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.IStoredProcedureInfoManager;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfoManagerInitOnStartup;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.binder.AnnotatedBindingBuilder;

public class InitJdbcProcModule extends AbstractModule {
  
  @Override protected void configure() {
    bindDaoMethodInfo                         ( bind( DAOMethodInfo.class                           ));
    bindParameterConverterService             ( bind( ParameterConverterService.class               ));
    bindCallableStatementExecutorBlockService ( bind( CallableStatementExecutorBlockService.class   ));
    bindOutputParametersGetterBlockService    ( bind( OutputParametersGetterBlockService.class      ));
    bindParametersSetterBlockService          ( bind( ParametersSetterBlockService.class            ));
    bindRegisterOutParametersBlockService     ( bind( RegisterOutParametersBlockService.class       ));
    bindResultSetConverterBlockService        ( bind( ResultSetConverterBlockService.class          ));
    bindStoredProcedureInfoManager            ( bind( IStoredProcedureInfoManager.class             ));
    bindCallableStatementGetStrategyFactory   ( bind( ICallableStatementGetStrategyFactory.class    ));
    bindCallableStatementSetStrategyFactory   ( bind( ICallableStatementSetStrategyFactory.class    ));
  }

  protected void bindCallableStatementGetStrategyFactory(
          AnnotatedBindingBuilder<ICallableStatementGetStrategyFactory> aBuilder) {
    aBuilder.to(CallableStatementGetStrategyFactoryNameImpl.class);
  }
  
  protected void bindCallableStatementSetStrategyFactory(
          AnnotatedBindingBuilder<ICallableStatementSetStrategyFactory> aBuilder) {
    aBuilder.to(CallableStatementSetStrategyFactoryNameImpl.class);
  }

  protected void bindDaoMethodInfo(AnnotatedBindingBuilder<DAOMethodInfo> builder) {
    builder.to(DaoMethodInfoGuice.class).in(Singleton.class);
  }
  
  protected void bindParameterConverterService(
      AnnotatedBindingBuilder<ParameterConverterService> builder) {
    builder.to(ParameterConverterServiceImpl.class);
  }
  
  protected void bindCallableStatementExecutorBlockService(
      AnnotatedBindingBuilder<CallableStatementExecutorBlockService> builder) {
    builder.to(CallableStatementExecutorBlockServiceImpl.class);
  }
  
  protected void bindOutputParametersGetterBlockService(
      AnnotatedBindingBuilder<OutputParametersGetterBlockService> builder) {
    builder.to(OutputParametersGetterBlockServiceImpl.class);
  }
  
  protected void bindParametersSetterBlockService(
      AnnotatedBindingBuilder<ParametersSetterBlockService> builder) {
    builder.to(ParametersSetterBlockServiceImpl.class);
  }
  
  protected void bindRegisterOutParametersBlockService(
      AnnotatedBindingBuilder<RegisterOutParametersBlockService> builder) {
    builder.to(RegisterOutParametersBlockServiceImpl.class);
  }
  
  protected void bindResultSetConverterBlockService(
      AnnotatedBindingBuilder<ResultSetConverterBlockService> builder) {
    builder.to(ResultSetConverterBlockServiceImpl.class);
  }
  
  protected void bindStoredProcedureInfoManager(
      AnnotatedBindingBuilder<IStoredProcedureInfoManager> builder) {
    builder.to(StoredProcedureInfoManagerInitOnStartup.class);
  }
}
