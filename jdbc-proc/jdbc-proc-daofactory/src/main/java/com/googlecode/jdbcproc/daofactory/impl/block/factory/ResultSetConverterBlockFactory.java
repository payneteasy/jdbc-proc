package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.*;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterManager;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.ResultSetColumnInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
            // LIST
            Class entityClass = getEntityClass(aDaoMethod);
            if(BlockFactoryUtils.isSimpleType(entityClass)) {
                // simple type for list
                return createBlockSimpleTypeList(aConverterManager, entityClass, aProcedureInfo);

            } else if(isOneToManyPresent(entityClass)) {
                // @OneToMany Annotation support
//                ResultSetConverterBlockEntityOneToMany blockEntity = createEntityBlockOneToMany(aConverterManager, entityClass, aProcedureInfo);
//                return new ResultSetConverterBlockEntityOneToManyList(blockEntity);
                return createEntityBlockOneToMany2xList(aConverterManager, entityClass, aProcedureInfo);

            } else {
                // Without @OneToMany Annotation
                ResultSetConverterBlockEntity blockEntity = createEntityBlock(aConverterManager, entityClass, aProcedureInfo);
                return new ResultSetConverterBlockEntityList(blockEntity);
            }

        } else if(BlockFactoryUtils.isReturnIterator(aDaoMethod)) {
            // Iterator
            Class entityClass = getEntityClass(aDaoMethod);
            if(BlockFactoryUtils.isSimpleType(entityClass)) {
                // simple type for iterator
                return createBlockSimpleTypeIterator(aConverterManager, entityClass, aProcedureInfo);

            } else if(isOneToManyPresent(entityClass)) {
                // @OneToMany Annotation not supported
                throw new IllegalStateException("Iterator with OneToMany is unsupported");
            } else {
                // Without @OneToMany Annotation
                ResultSetConverterBlockEntity blockEntity = createEntityBlock(aConverterManager, entityClass, aProcedureInfo);
                return new ResultSetConverterBlockEntityIterator(blockEntity);
            }

        } else if(returnType.isAssignableFrom(Collection.class)) {
            // collection
            throw new IllegalStateException("Unsupported return type "+aDaoMethod.getReturnType().getSimpleName());

        } else {
            // entity may be
            if(isOneToManyPresent(returnType)) {
                // @OneToMany annotation is finded
                return createEntityBlockOneToMany(aConverterManager, returnType, aProcedureInfo);
            } else {
                return createEntityBlock(aConverterManager, returnType, aProcedureInfo);
            }
        }
    }

    private boolean isOneToManyPresent(Class aClass) {
        return BlockFactoryUtils.findOneToManyMethod(aClass)!=null;
    }

    /**
     * Creates ResultSetConverterBlockSimpleType
     *
     * @param aConverterManager converter manager
     * @param aType             type
     * @param aProcedureInfo    procedure info
     * @return ResultSetConverterBlockSimpleType
     */
    private ResultSetConverterBlockSimpleType createBlockSimpleType(ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        Assert.isTrue(1==aProcedureInfo.getResultSetColumns().size(), "Count of columns in result set must equals 1");

        ResultSetColumnInfo columnInfo = aProcedureInfo.getResultSetColumns().get(0);
        return new ResultSetConverterBlockSimpleType(aConverterManager.findConverter(
                columnInfo.getDataType()
               , aType
        ), columnInfo.getColumnName());
    }

    /**
     * Creates ResultSetConverterBlockSimpleTypeList
     *
     * @param aConverterManager converter manager
     * @param aType             type
     * @param aProcedureInfo    procedure info
     * @return ResultSetConverterBlockSimpleTypeList
     */
    private ResultSetConverterBlockSimpleTypeList createBlockSimpleTypeList(ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        Assert.isTrue(1==aProcedureInfo.getResultSetColumns().size(), "Count of columns in result set must equals 1");

        ResultSetColumnInfo columnInfo = aProcedureInfo.getResultSetColumns().get(0);
        return new ResultSetConverterBlockSimpleTypeList(aConverterManager.findConverter(
                columnInfo.getDataType()
               , aType
        ), columnInfo.getColumnName());
    }

    /**
     * Creates ResultSetConverterBlockSimpleTypeIterator
     *
     * @param aConverterManager converter manager
     * @param aType             type
     * @param aProcedureInfo    procedure info
     * @return ResultSetConverterBlockSimpleTypeIterator
     */
    private ResultSetConverterBlockSimpleTypeIterator createBlockSimpleTypeIterator(ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        Assert.isTrue(1==aProcedureInfo.getResultSetColumns().size(), "Count of columns in result set must equals 1");

        ResultSetColumnInfo columnInfo = aProcedureInfo.getResultSetColumns().get(0);
        return new ResultSetConverterBlockSimpleTypeIterator(aConverterManager.findConverter(
                columnInfo.getDataType()
               , aType
        ), columnInfo.getColumnName());
    }

    /**
     * Gets List&lt;Entity&gt; class
     * @param aDaoMethod method
     * @return entity class
     */
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

    /**
     * One to Many
     * @param aConverterManager converter manager
     * @param aType type
     * @param aProcedureInfo procedure info
     * @return One to Many converter
     */
    private ResultSetConverterBlockEntityOneToMany createEntityBlockOneToMany(ParameterConverterManager aConverterManager
            , Class aType
            , StoredProcedureInfo aProcedureInfo) {
        // finds simple setters
        List<EntityPropertySetter> propertySetters = createEntityPropertySetters(aConverterManager, aType, aProcedureInfo, "");
        // finds OneToOne and ManyToOne links
        List<OneToOneLink> oneToOneLinks = createOneToOneLinks("", aConverterManager, aType, aProcedureInfo);

        // child
        Method otmMethodGetter = BlockFactoryUtils.findOneToManyMethod(aType);
        Method otmMethodSetter = BlockFactoryUtils.findSetterMethod(aType, otmMethodGetter);
        String childPrefix = getTablePrefixFromJoinColumnAnnotation(otmMethodGetter);
        Class  childClass  = getEntityClass(otmMethodGetter);
        List<EntityPropertySetter> childPropertySetters = createEntityPropertySetters(aConverterManager, childClass, aProcedureInfo, childPrefix);
        List<OneToOneLink> childOneToOneLinks = createOneToOneLinks(childPrefix, aConverterManager, childClass, aProcedureInfo);

        return new ResultSetConverterBlockEntityOneToMany(aType, propertySetters, oneToOneLinks, childClass, childPropertySetters, childOneToOneLinks, otmMethodSetter);
    }

    /**
     * One to Many 2x
     * @param aConverterManager converter manager
     * @param aType type
     * @param aProcedureInfo procedure info
     * @return One to Many converter
     */
    private ResultSetConverterBlockEntityOneToMany2xList createEntityBlockOneToMany2xList(ParameterConverterManager aConverterManager
            , Class aType
            , StoredProcedureInfo aProcedureInfo) {


        List<OneToManyLink> oneToManyLinks = new LinkedList<OneToManyLink>();
        Class clazz = aType;
        Method otmMethodGetter = BlockFactoryUtils.findOneToManyMethod(clazz);
        String childPrefix = "" ;
        
        for(int i=0; i<100; i++) {
            if(LOG.isDebugEnabled()) {
                LOG.debug("Creating OneToManyLink for class {}, otm getter method {} and prefix '{}'"
                        , new Object[] {clazz, otmMethodGetter, childPrefix});
            }
            // finds simple setters
            List<EntityPropertySetter> propertySetters = createEntityPropertySetters(aConverterManager, clazz, aProcedureInfo, childPrefix);
            // finds OneToOne and ManyToOne links
            List<OneToOneLink> oneToOneLinks = createOneToOneLinks(childPrefix, aConverterManager, clazz, aProcedureInfo);

            Method otmMethodSetter = otmMethodGetter!=null ? BlockFactoryUtils.findSetterMethod(clazz, otmMethodGetter) : null;

            oneToManyLinks.add(new OneToManyLink(clazz, propertySetters, oneToOneLinks, otmMethodSetter));

            if(otmMethodGetter==null) {
                break;
            } else {
                childPrefix = childPrefix.length()==0 ? getTablePrefixFromJoinColumnAnnotation(otmMethodGetter) : childPrefix+getTablePrefixFromJoinColumnAnnotation(otmMethodGetter);
                clazz  = getEntityClass(otmMethodGetter);
                otmMethodGetter = BlockFactoryUtils.findOneToManyMethod(clazz);
            }
        }

        return new ResultSetConverterBlockEntityOneToMany2xList(oneToManyLinks);

    }

    private List<OneToOneLink> createOneToOneLinks(String aParentTablePrefix, ParameterConverterManager aConverterManager, Class aType, StoredProcedureInfo aProcedureInfo) {
        List<OneToOneLink> oneToOneLinks = new LinkedList<OneToOneLink>();
        for(Method method : aType.getMethods()) {
        	if(method.isAnnotationPresent(OneToOne.class) || method.isAnnotationPresent(ManyToOne.class)) {
                String tablePrefix = aParentTablePrefix + getTablePrefixFromJoinColumnAnnotation(method);
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
    private String getTablePrefixFromJoinColumnAnnotation(Method aMethod) {
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
                if(LOG.isDebugEnabled()) {
                    LOG.debug("         Mapping result set for {}.{}() to {} ...", new String[]{aType.getSimpleName(), getterMethod.getName(), columnName});
                }
                ResultSetColumnInfo resultSetColumnInfo = aProcedureInfo.getResultSetColumn(columnName);
                if(resultSetColumnInfo==null) {
                    throw new IllegalStateException(
                            String.format("For method %s.%s() column '%s' was not found in result set for procedure %s() "
                                    , aType.getSimpleName(), getterMethod.getName(), columnName, aProcedureInfo.getProcedureName()
                              ));
                }
                try {
                    IParameterConverter paramConverter = aConverterManager.findConverter(resultSetColumnInfo.getDataType(), getterMethod.getReturnType());
                    list.add(new EntityPropertySetter(setterMethod, paramConverter
                            , resultSetColumnInfo.getColumnName(), resultSetColumnInfo.getDataType()));
                } catch (IllegalStateException e) {
                    throw new IllegalStateException(
                            String.format("Converter was not found for method %s.%s() [ column '%s' in procedure %s()]"
                                    , aType.getSimpleName(), getterMethod.getName(), columnName, aProcedureInfo.getProcedureName()
                              ), e);
                }
            }

        }
        return list;
    }
}
