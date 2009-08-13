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
import javax.persistence.JoinColumn;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
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
        List<EntityPropertySetter> propertySetters = createEntityPropertySetters(aConverterManager, aType, aProcedureInfo, "");
        // finds OneToOne and ManyToOne links
        List<OneToOneLink> oneToOneLinks = createOneToOneLinks("", aConverterManager, aType, aProcedureInfo);
        return new ResultSetConverterBlockEntity(aType, propertySetters, oneToOneLinks);
    }

    private List<OneToOneLink> createOneToOneLinks(String aParentTablePrefix, ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        List<OneToOneLink> oneToOneLinks = new LinkedList<OneToOneLink>();
        for(Method method : aType.getMethods()) {
        	if(method.isAnnotationPresent(OneToOne.class) || method.isAnnotationPresent(ManyToOne.class)) {
                String tablePrefix = aParentTablePrefix + getTablePrefixForOneToOneLink(method);
                Class oneToOneClass = method.getReturnType();
                if(LOG.isDebugEnabled()) {
                    LOG.debug("        Finded {}.{}",aType.getSimpleName(), oneToOneClass.getSimpleName());
                }
                Method oneToOneSetterMethod = BlockFactoryUtils.findSetterMethod(aType, method);
                List<EntityPropertySetter> oneToOnePropertySetters = createEntityPropertySetters(aConverterManager, oneToOneClass, aProcedureInfo, tablePrefix);
                ResultSetConverterBlockEntity oneToOneBlock = new ResultSetConverterBlockEntity(
                          oneToOneClass
                        , oneToOnePropertySetters
                        , createOneToOneLinks(tablePrefix, aConverterManager, oneToOneClass, aProcedureInfo)
                );
        		oneToOneLinks.add(new OneToOneLink(oneToOneBlock, oneToOneSetterMethod));
        	}
        }
        return oneToOneLinks;
    }

    /**
     * Gets table prefix from @JoinColumn.name()
     * @param aMethod getter method with OneToOne annotation and possibly JoinColumn
     * @return @JoinColumn.name() + "_" or empty string ("") 
     */
    private String getTablePrefixForOneToOneLink(Method aMethod) {
        String name = "";
        if(aMethod.isAnnotationPresent(JoinColumn.class)) {
            JoinColumn joinColumn = aMethod.getAnnotation(JoinColumn.class);
            if(StringUtils.hasText(joinColumn.table())) {
                name = joinColumn.table() + "_";
            }
        }
        return name;
    }

    private List<EntityPropertySetter> createEntityPropertySetters(ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo, String aTablePrefix) {
        List<EntityPropertySetter> list = new LinkedList<EntityPropertySetter>();
        for (Method getterMethod : aType.getMethods()) {
            Column columnAnnotation = getterMethod.getAnnotation(Column.class);
            if(columnAnnotation!=null) {
                Method setterMethod = BlockFactoryUtils.findSetterMethod(aType, getterMethod);
                String columnName = aTablePrefix + columnAnnotation.name();
                ResultSetColumnInfo resultSetColumnInfo = aProcedureInfo.getResultSetColumn(columnName);
                if(resultSetColumnInfo==null) {
                    throw new IllegalStateException(
                            String.format("For method %s.%s() column '%s' was not found in result set for procedure %s() "
                                    , aType.getSimpleName(), getterMethod.getName(), columnName, aProcedureInfo.getProcedureName()
                              ));
                }
                if(LOG.isDebugEnabled()) {
                    LOG.debug("         Mapped result set for {}.{}() to {}", new String[]{aType.getSimpleName(), getterMethod.getName(), columnName});
                }
                IParameterConverter paramConverter = aConverterManager.findConverter(resultSetColumnInfo.getDataType(), getterMethod.getReturnType());
                list.add(new EntityPropertySetter(setterMethod, paramConverter
                        , resultSetColumnInfo.getColumnName(), resultSetColumnInfo.getDataType()));
            }

        }
        return list;
    }
}
