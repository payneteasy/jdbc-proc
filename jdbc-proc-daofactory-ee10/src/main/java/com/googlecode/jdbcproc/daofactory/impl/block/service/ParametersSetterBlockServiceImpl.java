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

import com.googlecode.jdbcproc.daofactory.IMetaLoginInfoService;
import com.googlecode.jdbcproc.daofactory.annotation.AConsumerKey;
import com.googlecode.jdbcproc.daofactory.annotation.AMetaLoginInfo;
import com.googlecode.jdbcproc.daofactory.annotation.ASerializeListToJson;
import com.googlecode.jdbcproc.daofactory.impl.block.BlockFactoryUtils;
import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ArgumentGetter;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.EntityArgumentGetter;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.EntityArgumentGetterOneToOne;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.EntityArgumentGetterOneToOneJoinColumn;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.IEntityArgumentGetter;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockArgument;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockArguments;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockEntity;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockJson;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockList;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockListAggregator;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockMetaLoginInfo;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ParametersSetterBlockNull1;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author esinev
 * @author dmk
 * @version 1.00 Apr 28, 2010 12:16:19 PM
 */
public class ParametersSetterBlockServiceImpl implements ParametersSetterBlockService {

    private final Logger LOG = LoggerFactory.getLogger(ParametersSetterBlockServiceImpl.class);

    public List<IParametersSetterBlock> create(
              JdbcTemplate jdbcTemplate
            , ParameterConverterService converterService
            , Method method
            , StoredProcedureInfo procedureInfo
            , IMetaLoginInfoService aMetaLoginInfoService
    ) {
        if (method.isAnnotationPresent(AMetaLoginInfo.class)) {
          if (aMetaLoginInfoService == null) {
            throw new IllegalStateException(AMetaLoginInfo.class.getName() 
                + " annotation is present but no implementation for " 
                + IMetaLoginInfoService.class.getName() + " was bound");
          }

          // is entity, e.g. void saveProcessor(TransactionProcessor) (meta login supported)
          if (procedureInfo.getArgumentsCounts() > method.getParameterTypes().length
              && method.getParameterTypes().length == 1
              && !BlockFactoryUtils.isSimpleType(method.getParameterTypes()[0])
              && !BlockFactoryUtils.isCollectionAssignableFrom(method.getParameterTypes()[0])) {
            return createSaveMethod(converterService, method, procedureInfo, aMetaLoginInfoService);

            // is entity with lists after it (meta login supported)
          } else if (isEntityWithListsAndMetaLogin(method, procedureInfo)) {

            return createSaveMethodWithLists(jdbcTemplate, converterService, method, procedureInfo,
                    aMetaLoginInfoService);

            // METHOD @AMetaLoginInfo
            // METHOD no parameter
            // PROCEDURE 2 parameters
          } else if (method.getParameterTypes().length == 0 && procedureInfo.getInputArgumentsCount() == 2) {
            return Collections.singletonList((IParametersSetterBlock) new ParametersSetterBlockMetaLoginInfo(aMetaLoginInfoService
                    , getArgument(procedureInfo, aMetaLoginInfoService.getUsernameParameterName())
                    , getArgument(procedureInfo, aMetaLoginInfoService.getRoleParameterName()
            )));

            // if AMetaLoginInfo and parameters are simply put to procedure
          } else if (method.getParameterTypes().length + 2 == procedureInfo.getInputArgumentsCount()) {
            return createSimpleParametersWithMetaLoginInfo(aMetaLoginInfoService, converterService, method, procedureInfo);

            // if both list and arguments parameters provides with MetaLoginInfo
          } else if (areListAndNonListParametersProvidesWithMetaLoginInfo(method, procedureInfo)) {
            return createListAndArgumentsWithMetaLoginInfo(jdbcTemplate,
                    converterService, method, procedureInfo,
                    aMetaLoginInfoService);

            // else not supported
          } else {
            throw new IllegalStateException(method + " is Unsupported");
          }

        } else if (method.isAnnotationPresent(AConsumerKey.class)) {
          if (!String.class.equals(method.getParameterTypes()[0])) {
            throw new IllegalStateException(AConsumerKey.class.getName()  + " annotation is present but first argument is not String type");
          }
          // is entity, e.g. void saveProcessor(consumerKey, TransactionProcessor) (meta login not supported)
          if (procedureInfo.getArgumentsCounts() > method.getParameterTypes().length
              && method.getParameterTypes().length == 2
              && !BlockFactoryUtils.isSimpleType(method.getParameterTypes()[1])
              && !BlockFactoryUtils.isCollectionAssignableFrom(method.getParameterTypes()[1])) {
            return createSaveMethodConsumer(converterService, method, procedureInfo);

            // if parameters is simple putted to procedure
//          } else if (method.getParameterTypes().length == procedureInfo.getInputArgumentsCount()) {
//            return createSimpleParameters(converterService, method, procedureInfo);
             // if both list and arguments parameters provides with ConsumerKey 
//          } else if (areListAndNonListParametersProvidesWithConsumer(method, procedureInfo)) {
//              return createListAndArgumentsWithConsumer(jdbcTemplate, converterService, method, procedureInfo);
          } else {
            throw new IllegalStateException(method + " is Unsupported");
          }
        } else {
          // List getAll()
          if (BlockFactoryUtils.isGetAllMethod(method, procedureInfo)) {
            return createGetAll(procedureInfo);

            // is entity, e.g. void saveProcessor(TransactionProcessor) (meta login not supported)
          } else if (procedureInfo.getArgumentsCounts() >= method.getParameterTypes().length
              && method.getParameterTypes().length == 1
              && !BlockFactoryUtils.isSimpleType(method.getParameterTypes()[0])
              && !BlockFactoryUtils.isCollectionAssignableFrom(method.getParameterTypes()[0])) {
            return createSaveMethod(converterService, method, procedureInfo);

            // is entity with lists after it (meta login not supported)
          } else if (isEntityWithLists(method, procedureInfo)) {

            return createSaveMethodWithLists(jdbcTemplate, converterService, method, procedureInfo);

            // if no parameters
          } else if (method.getParameterTypes().length == 0 && procedureInfo.getInputArgumentsCount() == 0) {
            return Collections.emptyList();

            // if parameters are simply put to procedure
          } else if (method.getParameterTypes().length == procedureInfo.getInputArgumentsCount()) {
            return createSimpleParameters(converterService, method, procedureInfo);

            // if all parameters is list and no arguments
          } else if (areAllParametersListAndNoArguments(method, procedureInfo)) {
            return createAllList(jdbcTemplate, converterService, method, procedureInfo);

            // if both list and arguments parameters provides
          } else if (areListAndNonListParametersProvides(method, procedureInfo)) {
            return createListAndArguments(0, jdbcTemplate, converterService, method, procedureInfo);

            // else not supported
          } else {
            throw new IllegalStateException(method + " is Unsupported");
          }
        }
    }

    private boolean isEntityWithListsAndMetaLogin(Method method, StoredProcedureInfo procedureInfo) {
        // method parameter 0: entity
        // method parameters [1..N]: lists
        boolean ok = procedureInfo.getArgumentsCounts() - 2 >= method.getParameterTypes().length - 1
                && method.getParameterTypes().length > 1
                && !BlockFactoryUtils.isSimpleType(method.getParameterTypes()[0])
                && !BlockFactoryUtils.isCollectionAssignableFrom(method.getParameterTypes()[0]);
        for (int i = 1; i < method.getParameterCount(); i++) {
            ok &= BlockFactoryUtils.isCollectionAssignableFrom(method.getParameterTypes()[i]);
        }
        return ok;
    }

    private boolean isEntityWithLists(Method method, StoredProcedureInfo procedureInfo) {
        // method parameter 0: entity
        // method parameters [1..N]: lists
        boolean ok = procedureInfo.getArgumentsCounts() >= method.getParameterTypes().length - 1
                && method.getParameterTypes().length > 1
                && !BlockFactoryUtils.isSimpleType(method.getParameterTypes()[0])
                && !BlockFactoryUtils.isCollectionAssignableFrom(method.getParameterTypes()[0]);
        for (int i = 1; i < method.getParameterCount(); i++) {
            ok &= BlockFactoryUtils.isCollectionAssignableFrom(method.getParameterTypes()[i]);
        }
        return ok;
    }

    private StatementArgument getArgumentConsumerKey(StoredProcedureInfo aProcedureInfo) {
        StoredProcedureArgumentInfo info = aProcedureInfo.getArgumentInfo("consumer_key");
        if(info==null) throw new IllegalStateException("Argument consumer_key not found in "+aProcedureInfo.getProcedureName());
        return info.getStatementArgument();
    }
  
    private StatementArgument getArgument(StoredProcedureInfo aProcedureInfo, String aArgumentName) {
        if(aArgumentName.startsWith("i_")) {
            aArgumentName = aArgumentName.substring(2);
        }

        StoredProcedureArgumentInfo info = aProcedureInfo.getArgumentInfo(aArgumentName);
        if(info==null) throw new IllegalStateException("Argument "+aArgumentName+ " not found in "+aProcedureInfo.getProcedureName());
        return info.getStatementArgument();
    }

    protected List<IParametersSetterBlock> createListAndArgumentsWithMetaLoginInfo(
            JdbcTemplate jdbcTemplate,
            ParameterConverterService converterService, Method method,
            StoredProcedureInfo procedureInfo,
            IMetaLoginInfoService aMetaLoginInfoService) {
        return createListAndArgumentsWithMetaLoginInfo(createListAndArguments(2, jdbcTemplate, converterService, method, procedureInfo), aMetaLoginInfoService, procedureInfo);
    }

//    protected List<IParametersSetterBlock> createListAndArgumentsWithConsumer(
//        JdbcTemplate jdbcTemplate,
//        ParameterConverterService converterService,
//        Method method,
//        StoredProcedureInfo procedureInfo) {
//      return createListAndArgumentsWithConsumer(createListAndArguments(1, jdbcTemplate, converterService, method, procedureInfo), converterService, method, procedureInfo);
//    }

    /**
     * if both list and arguments parameters provides
     */
    private List<IParametersSetterBlock> createListAndArguments(int numSkipArguments, JdbcTemplate jdbcTemplate, 
      ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo) {
        List<IParametersSetterBlock> parametersSetterBlocks = new LinkedList<IParametersSetterBlock>();
        Class<?>[] parameters = method.getParameterTypes();

        List<IParametersSetterBlock> list = new LinkedList<IParametersSetterBlock>();
        int[] tmpNonList = new int[parameters.length];
        int[] tmpList = new int[parameters.length];
        int nonListArgumentsLength = 0;
        int listArgumentsLength = 0;
        for (int i = 0; i < parameters.length; i++) {
            Class clazz = parameters[i];
            if (BlockFactoryUtils.isCollectionAssignableFrom(clazz)) {
                IParametersSetterBlock block
                        = createParametersSetterBlockList(jdbcTemplate, converterService, method, i, procedureInfo);
                list.add(block);
                tmpList[listArgumentsLength] = i;
                listArgumentsLength++;
            } else {
                tmpNonList[nonListArgumentsLength] = i;
                nonListArgumentsLength++;
            }
        }
        final int[] nonListArgumentIndexes;
        final int[] listArgumentIndexes;
        if (nonListArgumentsLength == parameters.length) {
            nonListArgumentIndexes = tmpNonList;
            listArgumentIndexes = new int[0];
        } else {
            nonListArgumentIndexes = new int[nonListArgumentsLength];
            System.arraycopy(tmpNonList, 0, nonListArgumentIndexes, 0, nonListArgumentsLength);
            listArgumentIndexes = new int[listArgumentsLength];
            System.arraycopy(tmpList, 0, listArgumentIndexes, 0, listArgumentsLength);
        }
      
        parametersSetterBlocks.add(new ParametersSetterBlockListAggregator(list, listArgumentIndexes));
      
        if (procedureInfo.getInputArgumentsCount() > nonListArgumentIndexes.length 
          && (procedureInfo.getInputArgumentsCount() != nonListArgumentIndexes.length + numSkipArguments
              || (nonListArgumentIndexes.length == 1
                && isEntity(numSkipArguments, procedureInfo, parameters[nonListArgumentIndexes[0]], nonListArgumentIndexes, method)))) {
            // if entity argument and list argument(s)
            Assert.isTrue(nonListArgumentIndexes.length == 1
                    , "Method " + method.getName() + " non List<?> parameters count must be equals to 1");
            Class entityClass = method.getParameterTypes()[nonListArgumentIndexes[0]];
            parametersSetterBlocks.add(createEntityBlock(converterService, procedureInfo, entityClass, nonListArgumentIndexes));
        } else if (nonListArgumentIndexes.length + numSkipArguments == procedureInfo.getInputArgumentsCount()) {
            List<ArgumentGetter> getters = new LinkedList<ArgumentGetter>();
            int index = 0;
            for (int i = numSkipArguments; i < procedureInfo.getArgumentsCounts(); i++) {
              StoredProcedureArgumentInfo argumentInfo = procedureInfo.getArguments().get(i);
              if (argumentInfo.isInputParameter()) {

                  IParameterConverter paramConverter;
                  try {
                      paramConverter = converterService.getConverter(
                              argumentInfo.getDataType()
                              , method.getParameterTypes()[nonListArgumentIndexes[index]]
                      );

                  } catch (IllegalArgumentException e) {
                      throw new IllegalStateException("Can't find converter for "+argumentInfo+" and "+method.getParameterTypes()[nonListArgumentIndexes[index]], e  );
                  }

                  getters.add(new ArgumentGetter(paramConverter, argumentInfo.getStatementArgument()));

                  index++;
              }
            }
            parametersSetterBlocks.add(new ParametersSetterBlockArguments(getters, nonListArgumentIndexes));
        }
        return parametersSetterBlocks;
    }

    private boolean isEntity(int numSkipArguments, StoredProcedureInfo procedureInfo, Class<?> parameter, int[] nonListArgumentIndexes, Method method) {
        return procedureInfo.getInputArgumentsCount() == nonListArgumentIndexes.length + numSkipArguments
                && (!BlockFactoryUtils.isSimpleType(parameter) || BlockFactoryUtils.isOneOutput(method, procedureInfo));
    }

    /**
     * if both list and arguments parameters provides with MetaLoginInfo
     */
    private List<IParametersSetterBlock> createListAndArgumentsWithMetaLoginInfo(List<IParametersSetterBlock> originalBlock, 
      IMetaLoginInfoService aMetaLoginInfoService, StoredProcedureInfo procedureInfo) {
        List<IParametersSetterBlock> list = new LinkedList<IParametersSetterBlock>();

        // add username and principal
        list.add(new ParametersSetterBlockMetaLoginInfo(aMetaLoginInfoService
                , getArgument(procedureInfo, aMetaLoginInfoService.getUsernameParameterName())
                , getArgument(procedureInfo, aMetaLoginInfoService.getRoleParameterName())
        ));

        // add simple parameters
        list.addAll(originalBlock);
        return list;
    }

//  private List<IParametersSetterBlock> createListAndArgumentsWithConsumer(List<IParametersSetterBlock> originalBlock,
//      ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo) {
//    List<IParametersSetterBlock> list = new LinkedList<IParametersSetterBlock>();
//
//    StoredProcedureArgumentInfo argumentInfo = procedureInfo.getInputArguments().get(0);
//    IParameterConverter paramConverter = converterService.getConverter(argumentInfo.getDataType(), method.getParameterTypes()[0]);
//
//    ArgumentGetter consumerGetter = new ArgumentGetter(paramConverter, getArgumentConsumerKey(procedureInfo));
//
//    list.add(new ParametersSetterBlockArgument(consumerGetter)); // add consumer key
//    list.addAll(originalBlock); // add other parameters
//    return list;
//  }

    /**
     * if all parameters is list and no arguments
     */
    protected List<IParametersSetterBlock> createAllList(JdbcTemplate jdbcTemplate, ParameterConverterService converterService, Method method, StoredProcedureInfo aProcedureInfo) {
        if (method.getParameterTypes().length == 1) {
            // only one argument
            return Collections.singletonList(createParametersSetterBlockList(jdbcTemplate, converterService, method, 0, aProcedureInfo));
        } else {
            // many arguments
            Class<?>[] parameters = method.getParameterTypes();
            List<IParametersSetterBlock> list = new ArrayList<>();
            for (int i = 0; i < parameters.length; i++) {
                IParametersSetterBlock block
                        = createParametersSetterBlockList(jdbcTemplate, converterService, method, i, aProcedureInfo);
                list.add(block);
            }
            return Collections.singletonList(new ParametersSetterBlockListAggregator(list));
        }
    }

    private boolean isMetaLoginInfoPresented(Method aMethod) {
        return aMethod.isAnnotationPresent(AMetaLoginInfo.class);
    }

    private boolean isConsumerKeyPresented(Method aMethod) {
        return aMethod.isAnnotationPresent(AConsumerKey.class);
    }

    /**
     * if parameters is simple puted to procedure
     * @param converterService converter
     * @param method method
     * @param procedureInfo procedure
     * @return list of setter blocks
     */
    private List<IParametersSetterBlock> createSimpleParameters(ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo) {
        IParametersSetterBlock block = createSimpleParameters(0, converterService, method, procedureInfo);
        return Collections.singletonList(block);
    }

    private List<IParametersSetterBlock> createConsumerList(IParametersSetterBlock aOriginalBlock, StoredProcedureInfo procedureInfo, ParameterConverterService converterService, Method method) {
        List<IParametersSetterBlock> list = new LinkedList<IParametersSetterBlock>();

        StoredProcedureArgumentInfo argumentInfo = procedureInfo.getInputArguments().get(0);
        IParameterConverter paramConverter = converterService.getConverter(argumentInfo.getDataType(), method.getParameterTypes()[0]);

        ArgumentGetter consumerGetter = new ArgumentGetter(paramConverter, getArgumentConsumerKey(procedureInfo));

        list.add(new ParametersSetterBlockArgument(consumerGetter)); // add consumer key
        list.add(aOriginalBlock); // add other parameters
        return list;
    }

    private List<IParametersSetterBlock> createMetaLoginList(IParametersSetterBlock aOriginalBlock, IMetaLoginInfoService aMetaLoginInfoService, StoredProcedureInfo procedureInfo) {
        List<IParametersSetterBlock> list = new LinkedList<IParametersSetterBlock>();

        // add username and principal
        list.add(new ParametersSetterBlockMetaLoginInfo(aMetaLoginInfoService
                , getArgument(procedureInfo, aMetaLoginInfoService.getUsernameParameterName())
                , getArgument(procedureInfo, aMetaLoginInfoService.getRoleParameterName())
        ));


        // add simple parameters
        list.add(aOriginalBlock);
        return list;
    }

    /**
     * if parameters is simple puted to procedure
     * @param converterService converter
     * @param method method
     * @param procedureInfo procedure
     * @return list of setter blocks
     */
    private List<IParametersSetterBlock> createSimpleParametersWithMetaLoginInfo(IMetaLoginInfoService aMetaLoginInfoService, ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo) {
       return createMetaLoginList(createSimpleParameters(2, converterService, method, procedureInfo), aMetaLoginInfoService, procedureInfo);
    }

    /**
     * if parameters is simple puted to procedure
     *
     * @param converterService converter
     * @param method method
     * @param procedureInfo procedure
     * @return list of setter blocks
     */
    private IParametersSetterBlock createSimpleParameters(int aFromProcedureArgument, ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo) {
        final LinkedList<ArgumentGetter> getters = new LinkedList<ArgumentGetter>();

        int methodIndex = 0;

        for(int procedureIndex = aFromProcedureArgument; procedureIndex < procedureInfo.getInputArguments().size(); procedureIndex++) {
            StoredProcedureArgumentInfo argumentInfo = procedureInfo.getInputArguments().get(procedureIndex);
            if( argumentInfo.isInputParameter() ) {
                IParameterConverter paramConverter = converterService.getConverter(argumentInfo.getDataType(), method.getParameterTypes()[methodIndex]);
                getters.add(new ArgumentGetter(paramConverter, argumentInfo.getStatementArgument()));
            }

            methodIndex++;
        }
        return new ParametersSetterBlockArguments(getters);
    }

    /**
     * is entity, e.g. saveProcessor(TransactionProcessor)
     */
    private List<IParametersSetterBlock> createSaveMethod(ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo, IMetaLoginInfoService aMetaLoginInfoService) {
        Assert.isTrue(method.getParameterTypes().length == 1
                , "Method " + method.getName() + " parameters count must be equal to 1");

        return doCreateEntityBlocks(converterService, method, procedureInfo, aMetaLoginInfoService);
    }

    private List<IParametersSetterBlock> createSaveMethodConsumer(ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo) {
        Assert.isTrue(method.getParameterTypes().length == 2, "Method " + method.getName() + " parameters count must be equal to 2");
        return doCreateEntityBlocksConsumer(converterService, method, procedureInfo);
    }

    private List<IParametersSetterBlock> doCreateEntityBlocksConsumer(ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo) {
        Class entityClass = method.getParameterTypes()[1];
        final IParametersSetterBlock block = createEntityBlock(converterService, procedureInfo, entityClass, new int[] {1});

        if(isConsumerKeyPresented(method)) {
            return createConsumerList(block, procedureInfo, converterService, method);
        } else {
            return Collections.singletonList(block);
        }
    }

  private List<IParametersSetterBlock> doCreateEntityBlocks(ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo, IMetaLoginInfoService aMetaLoginInfoService) {
        Class entityClass = method.getParameterTypes()[0];
        final IParametersSetterBlock block = createEntityBlock(converterService, procedureInfo, entityClass, null);

        if(isMetaLoginInfoPresented(method)) {
            return createMetaLoginList(block, aMetaLoginInfoService, procedureInfo);
        } else {
            return Collections.singletonList(block);
        }
    }

    /**
     * is entity with a list, e.g. saveProcessor(TransactionProcessor, List properties)
     */
    private List<IParametersSetterBlock> createSaveMethodWithLists(JdbcTemplate jdbcTemplate,
            ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo,
            IMetaLoginInfoService aMetaLoginInfoService) {
        Assert.isTrue(method.getParameterTypes().length > 1
                , "Method " + method.getName() + " parameters count must be at least 2");

        List<IParametersSetterBlock> blocks = doCreateEntityBlocks(converterService, method, procedureInfo,
                aMetaLoginInfoService);
        return addListBlocks(jdbcTemplate, converterService, method, procedureInfo, blocks);
    }

    /**
     * is entity with a list, e.g. saveProcessor(TransactionProcessor, List properties)
     */
    private List<IParametersSetterBlock> createSaveMethodWithLists(JdbcTemplate jdbcTemplate,
            ParameterConverterService converterService, Method method, StoredProcedureInfo procedureInfo) {
        Assert.isTrue(method.getParameterTypes().length > 1
                , "Method " + method.getName() + " parameters count must be at least 2");


        List<IParametersSetterBlock> blocks = doCreateSaveMethod(converterService, method, procedureInfo);
        return addListBlocks(jdbcTemplate, converterService, method, procedureInfo, blocks);
    }

    private List<IParametersSetterBlock> addListBlocks(JdbcTemplate jdbcTemplate, ParameterConverterService converterService, Method method,
            StoredProcedureInfo procedureInfo, List<IParametersSetterBlock> blocks) {
        List<IParametersSetterBlock> result = new ArrayList<>(blocks);
        for (int i = 1; i < method.getParameterCount(); i++) {
            result.add(createParametersSetterBlockList(jdbcTemplate, converterService, method, i, procedureInfo));
        }
//        return Collections.unmodifiableList(result);
        return result;
    }

    protected List<IParametersSetterBlock> createSaveMethod(ParameterConverterService converterService,
        Method method, StoredProcedureInfo procedureInfo) {
        Assert.isTrue(method.getParameterTypes().length == 1
                , "Method " + method.getName() + " parameters count must be equals to 1");
        return doCreateSaveMethod(converterService, method, procedureInfo);
    }

    private List<IParametersSetterBlock> doCreateSaveMethod(ParameterConverterService converterService, Method method,
            StoredProcedureInfo procedureInfo) {
        Class entityClass = method.getParameterTypes()[0];

        final IParametersSetterBlock block = createEntityBlock(converterService, procedureInfo, entityClass, null);
        return Collections.singletonList(block);
    }

    /**
     * List getAll()
     */
    private List<IParametersSetterBlock> createGetAll(StoredProcedureInfo procedureInfo) {
        StoredProcedureArgumentInfo argumentInfo = procedureInfo.getArguments().get(0);
        return Collections.singletonList((IParametersSetterBlock) new ParametersSetterBlockNull1(
                argumentInfo.getStatementArgument(), argumentInfo.getDataType()));
    }

    /**
     * Creates entity block
     *
     * @param converterService converter manager
     * @param procedureInfo    procedure into
     * @param entityClass      entity class
     * @return block
     */
    private IParametersSetterBlock createEntityBlock(ParameterConverterService converterService,
      StoredProcedureInfo procedureInfo, Class entityClass, final int[] nonListArgumentIndexes) {
        List<EntityArgumentGetter> getters = new LinkedList<EntityArgumentGetter>();
        for (Method getterMethod : entityClass.getMethods()) {
            if (getterMethod.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = getterMethod.getAnnotation(Column.class);
                StoredProcedureArgumentInfo argumentInfo = procedureInfo
                        .getArgumentInfo(columnAnnotation.name());
                if (argumentInfo == null) {
                    throw new IllegalStateException("Column " + columnAnnotation.name()
                            + " was not found in " + procedureInfo.getProcedureName());
                }
                if (argumentInfo.getColumnType() == 1) {
                    IParameterConverter paramConverter = converterService
                            .getConverter(argumentInfo.getDataType(), getterMethod.getReturnType());
                    getters.add(new EntityArgumentGetter(getterMethod, paramConverter
                            , argumentInfo.getStatementArgument()));
                }
            } else if (getterMethod.isAnnotationPresent(OneToOne.class)
                    || getterMethod.isAnnotationPresent(ManyToOne.class)) {

                if (getterMethod.isAnnotationPresent(JoinColumn.class)) {
                    JoinColumn joinColumn = getterMethod.getAnnotation(JoinColumn.class);

                    if(StringUtils.hasText(joinColumn.name())) {
                        StoredProcedureArgumentInfo argumentInfo = procedureInfo.getArgumentInfo(joinColumn.name());

                        if (argumentInfo == null) {
                            throw new IllegalStateException("Column " + joinColumn.name()
                                    + " was not found in " + procedureInfo.getProcedureName());
                        }
                        getters.add(new EntityArgumentGetterOneToOneJoinColumn(getterMethod, argumentInfo.getStatementArgument()));
                    } else {
                        throw new IllegalStateException("@JoinColumn.name is empty for "+ entityClass.getSimpleName() + "." + getterMethod.getName() + "()");
                    }
                } else {
                    throw new IllegalStateException("No @JoinColumn annotation was found in "
                            + entityClass.getSimpleName() + "." + getterMethod.getName() + "()");
                }
            }
        }
        return new ParametersSetterBlockEntity(getters, nonListArgumentIndexes);
    }

    private IParametersSetterBlock createParametersSetterBlockList(
            JdbcTemplate jdbcTemplate, ParameterConverterService converterService,
            Method method, int listParameterIndex, StoredProcedureInfo aStoredProcedureInfo) {
        Parameter parameter = method.getParameters()[listParameterIndex];
        if (parameter.isAnnotationPresent(ASerializeListToJson.class)) {
            String listName = parameter.getAnnotation(ASerializeListToJson.class).value();
            StoredProcedureArgumentInfo argumentInfo = aStoredProcedureInfo.getArgumentInfo(listName);
            IParameterConverter paramConverter = converterService.getConverter(argumentInfo.getDataType(), String.class);
            return new ParametersSetterBlockJson(new ArgumentGetter(paramConverter, argumentInfo.getStatementArgument()), listParameterIndex);
        }

        Class entityClass = getListEntityClass(method.getGenericParameterTypes()[listParameterIndex]);
        String tableName = getListTableName(entityClass);

        if (!tableName.matches("^[a-zA-Z0-9_]+$")) {
            // protecting ourselves from injection
            throw new IllegalStateException("Wrong temp table name: '" + tableName + "'");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Getting metadata for table {}...", tableName);
        }
        Map<String, Integer> types = createTypes(jdbcTemplate, tableName);
        List<IEntityArgumentGetter> getters = createListGetters("", entityClass, types, converterService, 0 /* FROM FIRST PARAMETER */);
        String insertQuery = createListInsertQuery(getters, tableName);
        if (LOG.isDebugEnabled()) {
            LOG.debug("insert query: {}", insertQuery);
        }
        String clearTableQuery = "delete from " + tableName;
        return new ParametersSetterBlockList(insertQuery, getters, clearTableQuery, listParameterIndex);
    }

    private Map<String, Integer> createTypes(JdbcTemplate jdbcTemplate, final String tableName) {
        return jdbcTemplate.execute(new StatementCallback<Map<String, Integer>>() {
            public Map<String, Integer> doInStatement(Statement stmt)
                    throws SQLException, DataAccessException {
                ResultSet rs = stmt.executeQuery("select * from " + tableName);
                try {
                    ResultSetMetaData meta = rs.getMetaData();
                    Map<String, Integer> types = new HashMap<String, Integer>();
                    int count = meta.getColumnCount();
                    for (int i = 1; i <= count; i++) {
                        String name = meta.getColumnName(i);
                        int type = meta.getColumnType(i);
                        types.put(name, type);
                    }
                    return types;
                } finally {
                    rs.close();
                }
            }
        });
    }

    private List<IEntityArgumentGetter> createListGetters(String columnPrefix
            , Class entityClass
            , Map<String, Integer> types
            , ParameterConverterService converterService
            , int aParameterIndex
    ) {
        List<IEntityArgumentGetter> getters = new LinkedList<IEntityArgumentGetter>();
        for (Method method : entityClass.getMethods()) {
            if (method.isAnnotationPresent(Column.class)) {

                Column column = method.getAnnotation(Column.class);
                Assert.notNull(column, "Method " + method + " has no Column annotation");
                Assert.hasText(column.name(), "Column annotation has no name parameter in method " + method);

                String columnName = columnPrefix + column.name();

                Integer dataType = types.get(columnName);
                Assert.notNull(dataType, "No information about cPreparedSolumn " + columnName + " in method " + method);

                IParameterConverter paramConverter = converterService.getConverter(dataType, method.getReturnType());

                aParameterIndex++;
                getters.add(new EntityArgumentGetter(method, paramConverter, new StatementArgument(columnName, aParameterIndex)));

            } else if (method.isAnnotationPresent(OneToOne.class) || method.isAnnotationPresent(ManyToOne.class)) {
                Class oneToOneClass = method.getReturnType();
                if (method.isAnnotationPresent(JoinColumn.class)) {

                    // table name
                    JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
                    Assert.hasText(joinColumn.table(), "JoinColumn annotation has no table parameter in method " + method);
                    String tableName = joinColumn.table();

                    //
                    List<IEntityArgumentGetter> oneToOneClassGetters = createListGetters(tableName + "_", oneToOneClass, types, converterService, aParameterIndex);
                    for (IEntityArgumentGetter oneToOneClassGetter : oneToOneClassGetters) {
                        EntityArgumentGetterOneToOne oneToOneConverter = new EntityArgumentGetterOneToOne(method, oneToOneClassGetter);
                        getters.add(oneToOneConverter);
                    }
                }

            }
        }
        return getters;
    }

    @SuppressWarnings("unchecked")
    private String getListTableName(Class entityClass) {
        Entity entity = (Entity) entityClass.getAnnotation(Entity.class);
        Assert.notNull(entity, "No Entity annotation found in " + entityClass.getSimpleName());
        Assert.hasText(entity.name(),
                "Entity name is empty in Entity annotation for " + entityClass.getSimpleName());
        return entity.name();
    }

    private Class getListEntityClass(Type type) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }

    private String createListInsertQuery(List<IEntityArgumentGetter> getters, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(tableName).append(" ( ");
        boolean firstPassed = false;
        for (IEntityArgumentGetter getter : getters) {
            if (firstPassed) {
                sb.append(", ");
            } else {
                firstPassed = true;
            }
            sb.append(getter.getColumnNameForInsertQuery());
        }
        sb.append(" ) values ( ");

        firstPassed = false;
        for (IEntityArgumentGetter getter : getters) {
            if (firstPassed) {
                sb.append(", ");
            } else {
                firstPassed = true;
            }
            sb.append("?");
        }

        sb.append(" )");
        return sb.toString();
    }

    private boolean areAllParametersListAndNoArguments(Method method,
                                                       StoredProcedureInfo procedureInfo) {
        if (procedureInfo.getInputArgumentsCount() == 0 && method.getParameterTypes().length > 0) {
            for (Class<?> parameterClass : method.getParameterTypes()) {
                if (!BlockFactoryUtils.isCollectionAssignableFrom(parameterClass)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean areListAndNonListParametersProvides(Method method,
                                                        StoredProcedureInfo procedureInfo) {
        if (method.getParameterTypes().length >= procedureInfo.getInputArgumentsCount()) {
            int argumentsCount = 0;
            for (Class<?> parameterClass : method.getParameterTypes()) {
                if (!BlockFactoryUtils.isCollectionAssignableFrom(parameterClass)) {
                    argumentsCount++;
                }
            }
            return procedureInfo.getInputArgumentsCount() == argumentsCount;
        } else {
            return false;
        }
    }

    private boolean areListAndNonListParametersProvidesWithMetaLoginInfo(Method method, StoredProcedureInfo procedureInfo) {
        if (method.getParameterTypes().length + 2 >= procedureInfo.getInputArgumentsCount()) {
            int argumentsCount = 0;
            for (Class<?> parameterClass : method.getParameterTypes()) {
                if (!BlockFactoryUtils.isCollectionAssignableFrom(parameterClass)) {
                    argumentsCount++;
                }
            }
            return procedureInfo.getInputArgumentsCount() == argumentsCount + 2;
        } else {
            return false;
        }
    }

//    private boolean areListAndNonListParametersProvidesWithConsumer(Method method, StoredProcedureInfo procedureInfo) {
//        if (method.getParameterTypes().length >= procedureInfo.getInputArgumentsCount()) {
//            int argumentsCount = 0;
//            for (Class<?> parameterClass : method.getParameterTypes()) {
//                if (!BlockFactoryUtils.isCollectionAssignableFrom(parameterClass)) {
//                    argumentsCount++;
//                }
//            }
//            return procedureInfo.getInputArgumentsCount() == argumentsCount && argumentsCount > 1;
//        } else {
//            return false;
//        }
//    }
}
