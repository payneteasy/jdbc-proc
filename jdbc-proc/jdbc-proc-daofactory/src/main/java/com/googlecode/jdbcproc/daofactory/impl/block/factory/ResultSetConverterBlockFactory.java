package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Column;

import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.EntityPropertySetter;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.OneToOneLink;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockEntity;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockListEntity;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockSimpleType;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterManager;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.ResultSetColumnInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

/**
 * Creates IResultSetConverter
 */
public class ResultSetConverterBlockFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ResultSetConverterBlockFactory.class);

    public IResultSetConverterBlock create(Method aDaoMethod, StoredProcedureInfo aProcedureInfo, ParameterConverterManager aConverterManager) {
        Class returnType = aDaoMethod.getReturnType();
        if(returnType.equals(void.class))  {
            // void
            return null;

        } else if(BlockFactoryUtils.isSimpleType(returnType)) {
            // simple type from result set
            return createBlockSimpleType(aConverterManager, returnType, aProcedureInfo);

        } else if(returnType.isAssignableFrom(List.class)) {
            // list 
            Class entityClass = getEntityClass(aDaoMethod);
            ResultSetConverterBlockEntity blockEntity = createEntityBlock(aConverterManager, entityClass, aProcedureInfo);
            return new ResultSetConverterBlockListEntity(blockEntity);

        } else if(returnType.isAssignableFrom(Collection.class)) {
            // collection
            throw new IllegalStateException("Unsupported return type "+aDaoMethod.getReturnType().getSimpleName());

        } else {
            // entity may be
            return createEntityBlock(aConverterManager, returnType, aProcedureInfo);
        }
    }

    private IResultSetConverterBlock createBlockSimpleType(ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        Assert.isTrue(1==aProcedureInfo.getResultSetColumns().size(), "Count of columns in result set must equals 1");

        ResultSetColumnInfo columnInfo = aProcedureInfo.getResultSetColumns().get(0);
        return new ResultSetConverterBlockSimpleType(aConverterManager.findConverter(
                columnInfo.getDataType()
               , aType
        ), columnInfo.getColumnName());
    }

    private Class getEntityClass(Method aDaoMethod) {
        Type type = aDaoMethod.getGenericReturnType();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }

    private ResultSetConverterBlockEntity createEntityBlock(ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        // finds simple setters
        List<EntityPropertySetter> propertySetters = createEntityPropertySetters(aConverterManager, aType, aProcedureInfo);
        // finds OneToOne and ManyToOne links
        List<OneToOneLink> oneToOneLinks = createOneToOneLinks(aConverterManager, aType, aProcedureInfo);
        return new ResultSetConverterBlockEntity(aType, propertySetters, oneToOneLinks);
    }

    private List<OneToOneLink> createOneToOneLinks(ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        List<OneToOneLink> oneToOneLinks = new LinkedList<OneToOneLink>();
        for(Method method : aType.getMethods()) {
        	if(method.isAnnotationPresent(OneToOne.class) || method.isAnnotationPresent(ManyToOne.class)) {
                Class oneToOneClass = method.getReturnType();
                if(LOG.isDebugEnabled()) {
                    LOG.debug("        Finded {}.{}",aType.getSimpleName(), oneToOneClass.getSimpleName());
                }
                Method oneToOneSetterMethod = BlockFactoryUtils.findSetterMethod(aType, method);
                List<EntityPropertySetter> oneToOnePropertySetters = createEntityPropertySetters(aConverterManager, oneToOneClass, aProcedureInfo);
                ResultSetConverterBlockEntity oneToOneBlock = new ResultSetConverterBlockEntity(
                        oneToOneClass, oneToOnePropertySetters, createOneToOneLinks(aConverterManager, oneToOneClass, aProcedureInfo));
        		oneToOneLinks.add(new OneToOneLink(oneToOneBlock, oneToOneSetterMethod));
        	}
        }
        return oneToOneLinks;
    }

    private List<EntityPropertySetter> createEntityPropertySetters(ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        List<EntityPropertySetter> list = new LinkedList<EntityPropertySetter>();
        for (Method getterMethod : aType.getMethods()) {
            Column columnAnnotation = getterMethod.getAnnotation(Column.class);
            if(columnAnnotation!=null) {
                Method setterMethod = BlockFactoryUtils.findSetterMethod(aType, getterMethod);
                ResultSetColumnInfo resultSetColumnInfo = aProcedureInfo.getResultSetColumn(columnAnnotation.name());
                IParameterConverter paramConverter = aConverterManager.findConverter(resultSetColumnInfo.getDataType(), getterMethod.getReturnType());
                list.add(new EntityPropertySetter(setterMethod, paramConverter
                        , resultSetColumnInfo.getColumnName(), resultSetColumnInfo.getDataType()));
            }

        }
        return list;
    }
}
