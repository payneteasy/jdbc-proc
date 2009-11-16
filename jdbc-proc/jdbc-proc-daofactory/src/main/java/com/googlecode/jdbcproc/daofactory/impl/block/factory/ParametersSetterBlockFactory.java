package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.block.IParametersSetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.*;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterManager;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Creates IParametersSetterBlock
 */
public class ParametersSetterBlockFactory {

    private final Logger LOG = LoggerFactory.getLogger(ParametersSetterBlockFactory.class);

    public List<IParametersSetterBlock> create(JdbcTemplate aJdbcTemplate
            , ParameterConverterManager aConverterManager, Method aMethod, StoredProcedureInfo aProcedureInfo) {
        if(BlockFactoryUtils.isGetAllMethod(aMethod, aProcedureInfo)) {
            // List getAll()
            return Collections.singletonList(
                (IParametersSetterBlock)new ParametersSetterBlockNull1(aProcedureInfo.getArguments().get(0).getDataType()));

        } else if(aProcedureInfo.getArgumentsCounts() > aMethod.getParameterTypes().length) {
            // is entity, e.g. saveProcessor(TransactionProcessor)
            Assert.isTrue(aMethod.getParameterTypes().length==1, "Method "+aMethod.getName()+" parameters count must be equals to 1");
            Class entityClass = aMethod.getParameterTypes()[0];

            return Collections.singletonList(createEntityBlock(aConverterManager, aProcedureInfo, entityClass));

        // if no parameters
        } else if(aMethod.getParameterTypes().length==0 && aProcedureInfo.getArgumentsCounts()==0) {
            return Collections.emptyList();

        // if parameters is simple puted to procedure
        } else if(aMethod.getParameterTypes().length == aProcedureInfo.getArgumentsCounts()) {
            List<ArgumentGetter> getters = new LinkedList<ArgumentGetter>();
            int index = 0 ;
            for (StoredProcedureArgumentInfo argumentInfo : aProcedureInfo.getArguments()) {
                if(argumentInfo.isInputParameter()) {
                    IParameterConverter paramConverter = aConverterManager.findConverter(argumentInfo.getDataType(), aMethod.getParameterTypes()[index]);
                    getters.add(new ArgumentGetter(paramConverter, argumentInfo.getColumnName())) ;
                }
                index++;
            }
            return Collections.singletonList((IParametersSetterBlock)new ParametersSetterBlockArguments(getters));

        // if all parameters is list and no arguments
        } else if (areAllParametersListAndNoArguments(aMethod, aProcedureInfo)) {
            if(aMethod.getParameterTypes().length==1) {
                // only one argument
                return Collections.singletonList((IParametersSetterBlock)createParametersSetterBlockList(aJdbcTemplate, aConverterManager, aMethod, 0));
            } else {
                // many arguments
                Class<?>[] parameters = aMethod.getParameterTypes();
                List<ParametersSetterBlockList> list = new LinkedList<ParametersSetterBlockList>();
                for(int i=0; i<parameters.length; i++) {
                    ParametersSetterBlockList block = createParametersSetterBlockList(aJdbcTemplate, aConverterManager, aMethod, i);
                    list.add(block);
                }
                return Collections.singletonList((IParametersSetterBlock)new ParametersSetterBlockListAggregator(list));
            }
            
        // if both list and arguments parameters provides
        } else if (areListAndNonListParametersProvides(aMethod, aProcedureInfo)) {
            List<IParametersSetterBlock> parametersSetterBlocks = new LinkedList<IParametersSetterBlock>();
            Class<?>[] parameters = aMethod.getParameterTypes();

            List<ParametersSetterBlockList> list = new LinkedList<ParametersSetterBlockList>();
            List<Integer> argumentIndexes = new LinkedList<Integer>();
            for (int i = 0; i < parameters.length; i++) {
                Class clazz = parameters[i];
                if (clazz.equals(List.class)) {
                    ParametersSetterBlockList block = createParametersSetterBlockList(aJdbcTemplate, 
                        aConverterManager, aMethod, i);
                    list.add(block);
                } else {
                    argumentIndexes.add(i);
                }
            }
            parametersSetterBlocks.add(new ParametersSetterBlockListAggregator(list));
            
            if (aProcedureInfo.getArgumentsCounts() > argumentIndexes.size()) {
                Assert.isTrue(argumentIndexes.size() == 1, 
                    "Method " + aMethod.getName() + " non List<?> parameters count must be equals to 1");
                Class entityClass = aMethod.getParameterTypes()[argumentIndexes.get(0)];
                parametersSetterBlocks.add(createEntityBlock(aConverterManager, aProcedureInfo, entityClass));
            } else if (argumentIndexes.size() == aProcedureInfo.getArgumentsCounts()) {
                List<ArgumentGetter> getters = new LinkedList<ArgumentGetter>();
                int index = 0 ;
                for (StoredProcedureArgumentInfo argumentInfo : aProcedureInfo.getArguments()) {
                    if(argumentInfo.isInputParameter()) {
                        IParameterConverter paramConverter = aConverterManager.findConverter(
                            argumentInfo.getDataType(), aMethod.getParameterTypes()[argumentIndexes.get(index)]);
                        getters.add(new ArgumentGetter(paramConverter, argumentInfo.getColumnName())) ;
                    }
                    index++;
                }
                parametersSetterBlocks.add(new ParametersSetterBlockArguments(getters));
            }
            
            return parametersSetterBlocks;
            
        // else not supported
        } else {
            throw new IllegalStateException(aMethod+" is Unsupported");
        }
    }

    /**
     * Creates entity block
     * @param aConverterManager converter manager
     * @param aProcedureInfo procedure into
     * @param aEntityClass entity class
     * @return block
     */
    private IParametersSetterBlock createEntityBlock(ParameterConverterManager aConverterManager
            , StoredProcedureInfo aProcedureInfo, Class aEntityClass) {

        List<EntityArgumentGetter> getters = new LinkedList<EntityArgumentGetter>();
        for (Method getterMethod : aEntityClass.getMethods()) {
            if(getterMethod.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = getterMethod.getAnnotation(Column.class);
                StoredProcedureArgumentInfo argumentInfo = aProcedureInfo.getArgumentInfo(columnAnnotation.name());
                if(argumentInfo==null) {
                    throw new IllegalStateException("Column "+columnAnnotation.name()+" was not found in "+aProcedureInfo.getProcedureName());
                }
                if(argumentInfo.getColumnType() == 1) {
                    IParameterConverter paramConverter = aConverterManager.findConverter(argumentInfo.getDataType(), getterMethod.getReturnType());
                    getters.add(new EntityArgumentGetter(getterMethod, paramConverter, argumentInfo.getColumnName())) ;
                }
            } else if(getterMethod.isAnnotationPresent(OneToOne.class) || getterMethod.isAnnotationPresent(ManyToOne.class)) {
                if(getterMethod.isAnnotationPresent(JoinColumn.class)) {
                    JoinColumn joinColumn = getterMethod.getAnnotation(JoinColumn.class);
                    StoredProcedureArgumentInfo argumentInfo = aProcedureInfo.getArgumentInfo(joinColumn.name());
                    if(argumentInfo==null) {
                        throw new IllegalStateException("Column "+joinColumn.name()+" was not found in "+aProcedureInfo.getProcedureName());
                    }
                    getters.add(new EntityArgumentGetterOneToOneJoinColumn(getterMethod, argumentInfo.getColumnName()));
                } else {
                    throw new IllegalStateException("No @JoinColumn annotation was found in "+aEntityClass.getSimpleName()+"."+getterMethod.getName()+"()");
                }
            }
        }

//        for (StoredProcedureArgumentInfo argumentInfo : aProcedureInfo.getArguments()) {
//            if(argumentInfo.getColumnType() == 1) {
//                Method getterMethod = BlockFactoryUtils.findGetterMethod(aEntityClass, argumentInfo);
//                IParameterConverter paramConverter = aConverterManager.findConverter(argumentInfo.getDataType(), getterMethod.getReturnType());
//                getters.add(new IEntityArgumentGetter(getterMethod, paramConverter, argumentInfo.getColumnName())) ;
//            }
//        }
        return new ParametersSetterBlockEntity(getters);
    }

    private ParametersSetterBlockList createParametersSetterBlockList(JdbcTemplate aJdbcTemplate
            , ParameterConverterManager aConverterManager
            , Method aMethod
            , int aParameterIndex  
    ) {
        Class entityClass = getListEntityClass(aMethod.getGenericParameterTypes()[aParameterIndex]);
        String tableName = getListTableName(entityClass);
        if(LOG.isDebugEnabled()) {
            LOG.debug("Getting metadata for table {}...", tableName);
        }
        Map<String, Integer> types = createTypes(aJdbcTemplate, tableName);
        List<IEntityArgumentGetter> getters = createListGetters("", entityClass, types, aConverterManager);
        String insertQuery = createListInsertQuery(getters, tableName);
        if(LOG.isDebugEnabled()) {
            LOG.debug("insert query: {}", insertQuery);
        }
        String truncateTableQuery = "truncate table "+tableName;
        return new ParametersSetterBlockList(insertQuery, getters, truncateTableQuery);
    }

    private Map<String, Integer> createTypes(JdbcTemplate aJdbcTemplate, final String aTableName) {
        return (Map<String, Integer>) aJdbcTemplate.execute(new StatementCallback() {
            public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
                ResultSet rs = stmt.executeQuery("select * from "+aTableName);
                try {
                    ResultSetMetaData meta = rs.getMetaData();
                    Map<String, Integer> types = new HashMap<String, Integer>();
                    int count = meta.getColumnCount();
                    for(int i=1; i<=count; i++) {
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
    
    private List<IEntityArgumentGetter> createListGetters(String aColumnPrefix, Class aEntityClass, Map<String, Integer> aTypes, ParameterConverterManager aConverterManager) {
        List<IEntityArgumentGetter> getters = new LinkedList<IEntityArgumentGetter>();
        for (Method method : aEntityClass.getMethods()) {
            if(method.isAnnotationPresent(Column.class)) {
                Column column = method.getAnnotation(Column.class);
                Assert.notNull(column, "Method "+method+" has no Column annotation");
                Assert.hasText(column.name(), "Column annotation has no name parameter in method "+method);
                String columnName = aColumnPrefix + column.name();
                Integer dataType = aTypes.get(columnName);
                Assert.notNull(dataType, "NO information abount column "+columnName+" in method "+method);
                IParameterConverter paramConverter = aConverterManager.findConverter(dataType, method.getReturnType());
                getters.add(new EntityArgumentGetter(method, paramConverter, columnName)) ;
            } else if(method.isAnnotationPresent(OneToOne.class) || method.isAnnotationPresent(ManyToOne.class)) {
                Class oneToOneClass = method.getReturnType();
                if(method.isAnnotationPresent(JoinColumn.class)) {
                    // table name
                    JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
                    Assert.hasText(joinColumn.table(), "JoinColumn annotation has no table parameter in method "+method);
                    String tableName = joinColumn.table();

                    //
                    List<IEntityArgumentGetter> oneToOneClassGetters = createListGetters(tableName+"_",oneToOneClass, aTypes, aConverterManager);
                    for (IEntityArgumentGetter oneToOneClassGetter : oneToOneClassGetters) {
                        EntityArgumentGetterOneToOne oneToOneConverter = new EntityArgumentGetterOneToOne(method, oneToOneClassGetter);
                        getters.add(oneToOneConverter);
                    }
                }

            }
        }
        return getters;
    }

    private String getListTableName(Class aEntityClass) {
        Entity entity = (Entity) aEntityClass.getAnnotation(Entity.class);
        Assert.notNull(entity, "No Entity annotation found in "+aEntityClass.getSimpleName());
        Assert.hasText(entity.name(), "Entity name is empty in Entity annotation for "+aEntityClass.getSimpleName());
        return entity.name();
    }

    private Class getListEntityClass(Type aType) {
        ParameterizedType parameterizedType = (ParameterizedType) aType;
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }

    private String createListInsertQuery(List<IEntityArgumentGetter> aGetters, String aTableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(aTableName).append(" ( ");
        boolean firstPassed = false;
        for (IEntityArgumentGetter getter : aGetters) {
            if(firstPassed) {
                sb.append(", ");
            } else {
                firstPassed = true;
            }
            sb.append(getter.getParameterName());
        }
        sb.append(" ) values ( ");

        firstPassed = false;
        for (IEntityArgumentGetter getter : aGetters) {
            if(firstPassed) {
                sb.append(", ");
            } else {
                firstPassed = true;
            }
            sb.append("?");
        }

        sb.append(" )");
        return sb.toString();
    }

    private boolean areAllParametersListAndNoArguments(Method aMethod, StoredProcedureInfo aProcedureInfo) {
        if(aProcedureInfo.getArgumentsCounts()==0 && aMethod.getParameterTypes().length>0) {
            for (Class<?> parameterClass : aMethod.getParameterTypes()) {
                if(!parameterClass.equals(List.class)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean areListAndNonListParametersProvides(Method method, StoredProcedureInfo procedureInfo) {
        if (method.getParameterTypes().length >= procedureInfo.getArgumentsCounts()) {
            int argumentsCount = 0;
            for (Class<?> parameterClass : method.getParameterTypes()) {
                if(!parameterClass.equals(List.class)) {
                    argumentsCount++;
                }
            }
            return procedureInfo.getArgumentsCounts() == argumentsCount;
        } else {
            return false;
        }
    }
}