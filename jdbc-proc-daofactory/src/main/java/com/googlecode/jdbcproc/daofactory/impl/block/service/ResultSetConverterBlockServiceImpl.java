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
import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.EntityPropertySetter;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.OneToManyLink;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.OneToOneLink;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockEntity;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockEntityIterator;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockEntityList;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockEntityOneToMany2x;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockEntityOneToMany2xList;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockOutputParameterHasReturn;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockSimpleType;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockSimpleTypeIterator;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockSimpleTypeList;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterService;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.ResultSetColumnInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Creates IResultSetConverter
 * 
 * @version 1.00 Apr 27, 2010 9:32:34 PM
 *
 * @author esinev
 * @author dmk
 */
public class ResultSetConverterBlockServiceImpl implements ResultSetConverterBlockService {

  private static final Logger LOG = LoggerFactory
      .getLogger(ResultSetConverterBlockServiceImpl.class);


  public IResultSetConverterBlock create(Method daoMethod, StoredProcedureInfo procedureInfo,
      ParameterConverterService converterService) {
    Class returnType = daoMethod.getReturnType();
    if (returnType.equals(void.class)) {
      // void
      return null;

      // OUTPUT PARAMETER HAS RETURN
    } else if (BlockFactoryUtils.isOneOutputHasReturn(daoMethod, procedureInfo)) {
      return createOutputParameterHasReturn(converterService, daoMethod, procedureInfo);

    } else if (BlockFactoryUtils.isSimpleType(returnType)) {
      // simple type from result set
      return createBlockSimpleType(converterService, returnType, procedureInfo);

    } else if (returnType.isAssignableFrom(List.class)) {
      // LIST
      Class entityClass = getEntityClass(daoMethod);
      if (BlockFactoryUtils.isSimpleType(entityClass)) {
        // simple type for list
        return createBlockSimpleTypeList(converterService, entityClass, procedureInfo);

      } else if (isOneToManyPresent(entityClass)) {
        // @OneToMany Annotation support
        return new ResultSetConverterBlockEntityOneToMany2xList(
            createEntityBlockOneToMany2xList(converterService, entityClass, procedureInfo));

      } else {
        // Without @OneToMany Annotation
        ResultSetConverterBlockEntity blockEntity = createEntityBlock(converterService, entityClass,
            procedureInfo);
        return new ResultSetConverterBlockEntityList(blockEntity);
      }

    } else if (BlockFactoryUtils.isReturnIterator(daoMethod)) {
      // Iterator
      Class entityClass = getEntityClass(daoMethod);
      if (BlockFactoryUtils.isSimpleType(entityClass)) {
        // simple type for iterator
        return createBlockSimpleTypeIterator(converterService, entityClass, procedureInfo);

      } else if (isOneToManyPresent(entityClass)) {
        // @OneToMany Annotation not supported
        throw new IllegalStateException("Iterator with OneToMany is unsupported");
      } else {
        // Without @OneToMany Annotation
        ResultSetConverterBlockEntity blockEntity = createEntityBlock(converterService, entityClass,
            procedureInfo);
        return new ResultSetConverterBlockEntityIterator(blockEntity);
      }

    } else if (returnType.isAssignableFrom(Collection.class)) {
      // collection
      throw new IllegalStateException(
          "Unsupported return type " + daoMethod.getReturnType().getSimpleName());

    } else {
      // entity may be
      if (isOneToManyPresent(returnType)) {
        // @OneToMany annotation is finded
        return new ResultSetConverterBlockEntityOneToMany2x(
            createEntityBlockOneToMany2xList(converterService, returnType, procedureInfo));
      } else {
        return createEntityBlock(converterService, returnType, procedureInfo);
      }
    }
  }

  private IResultSetConverterBlock createOutputParameterHasReturn(
      ParameterConverterService converterService, Method daoMethod,
      StoredProcedureInfo procedureInfo) {
    Class methodReturnType = daoMethod.getReturnType();
    StoredProcedureArgumentInfo procedureReturn = getOutputParameter(procedureInfo.getArguments());
    return new ResultSetConverterBlockOutputParameterHasReturn(
        converterService.getConverter(procedureReturn.getDataType(), methodReturnType)
        , procedureReturn.getColumnName());
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

  private boolean isOneToManyPresent(Class clazz) {
    return BlockFactoryUtils.findOneToManyMethod(clazz) != null;
  }

  /**
   * Creates ResultSetConverterBlockSimpleType
   *
   * @param converterService converter manager
   * @param type             type
   * @param procedureInfo    procedure info
   * @return ResultSetConverterBlockSimpleType
   */
  private ResultSetConverterBlockSimpleType createBlockSimpleType(
      ParameterConverterService converterService, Class type, StoredProcedureInfo procedureInfo) {

    Assert.isTrue(1 == procedureInfo.getResultSetColumns().size(), "Count of columns in result set must be equals 1");

    ResultSetColumnInfo columnInfo = procedureInfo.getResultSetColumns().get(0);
    return new ResultSetConverterBlockSimpleType( converterService.getConverter(columnInfo.getDataType(), type), columnInfo.getColumnName() );
  }

  /**
   * Creates ResultSetConverterBlockSimpleTypeList
   *
   * @param converterService converter manager
   * @param type             type
   * @param procedureInfo    procedure info
   * @return ResultSetConverterBlockSimpleTypeList
   */
  private ResultSetConverterBlockSimpleTypeList createBlockSimpleTypeList(
      ParameterConverterService converterService, Class type, StoredProcedureInfo procedureInfo) {
    Assert.isTrue(1 == procedureInfo.getResultSetColumns().size(),
        "Count of columns in result set must be equals 1");

    ResultSetColumnInfo columnInfo = procedureInfo.getResultSetColumns().get(0);
    return new ResultSetConverterBlockSimpleTypeList(
        converterService.getConverter(columnInfo.getDataType(), type), columnInfo.getColumnName());
  }

  /**
   * Creates ResultSetConverterBlockSimpleTypeIterator
   *
   * @param converterService converter manager
   * @param type             type
   * @param procedureInfo    procedure info
   * @return ResultSetConverterBlockSimpleTypeIterator
   */
  private ResultSetConverterBlockSimpleTypeIterator createBlockSimpleTypeIterator(
      ParameterConverterService converterService, Class type, StoredProcedureInfo procedureInfo) {
    Assert.isTrue(1 == procedureInfo.getResultSetColumns().size()
        , "Count of columns in result set must be equals 1");

    ResultSetColumnInfo columnInfo = procedureInfo.getResultSetColumns().get(0);
    return new ResultSetConverterBlockSimpleTypeIterator(
        converterService.getConverter(columnInfo.getDataType(), type), columnInfo.getColumnName());
  }

  /**
   * Gets List&lt;Entity&gt; class
   * @param daoMethod method
   * @return entity class
   */
  private Class getEntityClass(Method daoMethod) {
    Type type = daoMethod.getGenericReturnType();
    ParameterizedType parameterizedType = (ParameterizedType) type;
    return (Class) parameterizedType.getActualTypeArguments()[0];
  }

  private ResultSetConverterBlockEntity createEntityBlock(
      ParameterConverterService converterService, Class type, StoredProcedureInfo procedureInfo) {
    // finds simple setters
    List<EntityPropertySetter> propertySetters = createEntityPropertySetters(converterService
        , type, procedureInfo, "");
    // finds OneToOne and ManyToOne links
    List<OneToOneLink> oneToOneLinks = createOneToOneLinks("", converterService, type
        , procedureInfo);
    return new ResultSetConverterBlockEntity(type, propertySetters, oneToOneLinks);
  }

  /**
   * One to Many 2x
   * @param converterService converter manager
   * @param type type
   * @param procedureInfo procedure info
   * @return One to Many converter
   */
  private List<OneToManyLink> createEntityBlockOneToMany2xList(
      ParameterConverterService converterService, Class type, StoredProcedureInfo procedureInfo) {
    List<OneToManyLink> oneToManyLinks = new LinkedList<OneToManyLink>();
    Class clazz = type;
    Method otmMethodGetter = BlockFactoryUtils.findOneToManyMethod(clazz);
    String childPrefix = "";

    for (int i = 0; i < 100; i++) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Creating OneToManyLink for class {}, otm getter method {} and prefix '{}'"
            , new Object[] {clazz, otmMethodGetter, childPrefix});
      }
      // finds simple setters
      List<EntityPropertySetter> propertySetters = createEntityPropertySetters(converterService
          , clazz, procedureInfo, childPrefix);
      // finds OneToOne and ManyToOne links
      List<OneToOneLink> oneToOneLinks = createOneToOneLinks(childPrefix, converterService, clazz
          , procedureInfo);

      Method otmMethodSetter = otmMethodGetter != null 
          ? BlockFactoryUtils.findSetterMethod(clazz, otmMethodGetter) : null;

      oneToManyLinks.add(new OneToManyLink(clazz, propertySetters, oneToOneLinks, otmMethodSetter));

      if (otmMethodGetter == null) {
        break;
      } else {
        childPrefix = childPrefix.length() == 0 
            ? getTablePrefixFromJoinColumnAnnotation(otmMethodGetter)
            : childPrefix + getTablePrefixFromJoinColumnAnnotation(otmMethodGetter);
        clazz = getEntityClass(otmMethodGetter);
        otmMethodGetter = BlockFactoryUtils.findOneToManyMethod(clazz);
      }
    }
    return oneToManyLinks;
  }

  private List<OneToOneLink> createOneToOneLinks(String parentTablePrefix,
      ParameterConverterService converterService, Class type, StoredProcedureInfo procedureInfo) {
    List<OneToOneLink> oneToOneLinks = new LinkedList<OneToOneLink>();
    for (Method method : type.getMethods()) {
      if (method.isAnnotationPresent(OneToOne.class) 
          || method.isAnnotationPresent(ManyToOne.class)) {
        String tablePrefix = parentTablePrefix + getTablePrefixFromJoinColumnAnnotation(method);
        Class oneToOneClass = method.getReturnType();
        if (LOG.isDebugEnabled()) {
          LOG.debug("        Finded {}.{}", type.getSimpleName(), oneToOneClass.getSimpleName());
        }
        Method oneToOneSetterMethod = BlockFactoryUtils.findSetterMethod(type, method);
        List<EntityPropertySetter> oneToOnePropertySetters = createEntityPropertySetters(
            converterService, oneToOneClass, procedureInfo, tablePrefix);
        ResultSetConverterBlockEntity oneToOneBlock = new ResultSetConverterBlockEntity(
            oneToOneClass, oneToOnePropertySetters,
            createOneToOneLinks(tablePrefix, converterService, oneToOneClass, procedureInfo));
        oneToOneLinks.add(new OneToOneLink(oneToOneBlock, oneToOneSetterMethod));
      }
    }
    return oneToOneLinks;
  }

  /**
   * Gets table prefix from @JoinColumn.name()
   * @param method getter method with OneToOne annotation and possibly JoinColumn
   * @return @JoinColumn.name() + "_" or empty string ("") 
   */
  private String getTablePrefixFromJoinColumnAnnotation(Method method) {
    String name = "";
    if (method.isAnnotationPresent(JoinColumn.class)) {
      JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
      if (StringUtils.hasText(joinColumn.table())) {
        name = joinColumn.table() + "_";
      }
    }
    return name;
  }

  private List<EntityPropertySetter> createEntityPropertySetters(
      ParameterConverterService converterService, Class type, StoredProcedureInfo procedureInfo,
      String tablePrefix) {
    List<EntityPropertySetter> list = new LinkedList<EntityPropertySetter>();
    for (Method getterMethod : type.getMethods()) {
      Column columnAnnotation = getterMethod.getAnnotation(Column.class);
      if (columnAnnotation != null) {
        Method setterMethod = BlockFactoryUtils.findSetterMethod(type, getterMethod);
        String columnName = tablePrefix + columnAnnotation.name();
        if (LOG.isDebugEnabled()) {
          LOG.debug("         Mapping result set for {}.{}() to {} ...",
              new String[] {type.getSimpleName(), getterMethod.getName(), columnName});
        }
        ResultSetColumnInfo resultSetColumnInfo = procedureInfo.getResultSetColumn(columnName);
        if (resultSetColumnInfo == null) {
          throw new IllegalStateException(String.format(
              "For method %s.%s() column '%s' was not found in result set for procedure %s() ",
              type.getSimpleName(), getterMethod.getName(), columnName,
              procedureInfo.getProcedureName()));
        }
        try {
          IParameterConverter paramConverter = converterService
              .getConverter(resultSetColumnInfo.getDataType(), getterMethod.getReturnType());
          list.add(new EntityPropertySetter(setterMethod, paramConverter,
              resultSetColumnInfo.getColumnName(), resultSetColumnInfo.getDataType()));
        } catch (IllegalStateException e) {
          throw new IllegalStateException(String.format(
              "Converter was not found for method %s.%s() [ column '%s' in procedure %s()]",
              type.getSimpleName(), getterMethod.getName(), columnName,
              procedureInfo.getProcedureName()), e);
        }
      }
    }
    return list;
  }
}
