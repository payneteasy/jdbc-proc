package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockEntity;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.EntityPropertySetter;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockListEntity;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.ResultSetConverterBlockSimpleType;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.ResultSetColumnInfo;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterManager;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;

import org.springframework.util.Assert;

/**
 * Creates IResultSetConverter
 */
public class ResultSetConverterBlockFactory {

    
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
        List<EntityPropertySetter> list = new LinkedList<EntityPropertySetter>();
        for (ResultSetColumnInfo resultSetColumnInfo : aProcedureInfo.getResultSetColumns()) {
            Method getterMethod = BlockFactoryUtils.findGetterMethod(aType, resultSetColumnInfo.getColumnName());
            Method setterMethod = BlockFactoryUtils.findSetterMethod(aType, getterMethod);
            IParameterConverter paramConverter = aConverterManager.findConverter(resultSetColumnInfo.getDataType(), getterMethod.getReturnType());
            list.add(new EntityPropertySetter(setterMethod, paramConverter
                    , resultSetColumnInfo.getColumnName(), resultSetColumnInfo.getDataType()));
        }
        return new ResultSetConverterBlockEntity(aType, list);
    }
}
