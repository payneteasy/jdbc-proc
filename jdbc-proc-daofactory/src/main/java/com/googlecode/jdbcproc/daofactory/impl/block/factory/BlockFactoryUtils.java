package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.math.BigDecimal;

/**
 * Utils for factories
 */
public class BlockFactoryUtils {

    private static final Set<Class> SIMPLE_TYPES ;
    static {
        SIMPLE_TYPES = new HashSet<Class>();
        SIMPLE_TYPES.add(int.class);      SIMPLE_TYPES.add(Integer.class);
        SIMPLE_TYPES.add(long.class);     SIMPLE_TYPES.add(Long.class);
        SIMPLE_TYPES.add(boolean.class);  SIMPLE_TYPES.add(Boolean.class);
        SIMPLE_TYPES.add(float.class);    SIMPLE_TYPES.add(Float.class);
        SIMPLE_TYPES.add(double.class);   SIMPLE_TYPES.add(Double.class);
        SIMPLE_TYPES.add(BigDecimal.class);
        SIMPLE_TYPES.add(java.util.Date.class);
        SIMPLE_TYPES.add(java.sql.Date.class);
        SIMPLE_TYPES.add(java.sql.Timestamp.class);
        SIMPLE_TYPES.add(java.sql.Time.class);
        SIMPLE_TYPES.add(String.class);
    }
    /**
     * Iterates throw the methods and find @Column annotation
     * @param aEntityClass  entity class
     * @param aArgumentInfo argument info
     * @return method
     */
    public static  Method findGetterMethod(Class aEntityClass, StoredProcedureArgumentInfo aArgumentInfo) {
        return findGetterMethod(aEntityClass, aArgumentInfo.getColumnName());
    }
    
    public static  Method findGetterMethod(Class aEntityClass, String aColumnName) {
        for (Method method : aEntityClass.getMethods()) {
            Column columnAnnotation = method.getAnnotation(Column.class);
            if(columnAnnotation!=null) {
                if(aColumnName.equals("i_"+columnAnnotation.name())
                     || aColumnName.equals("o_"+columnAnnotation.name())
                     || aColumnName.equals(columnAnnotation.name())
                        ) {
                    return method;
                }
            }
        }
        throw new IllegalStateException("In class "+aEntityClass.getSimpleName()
                + " where are no method with Column annotation and Column.name() == "+aColumnName);
    }

    public static Method findSetterMethod(Class aClass, Method aGetter) {
        String name = aGetter.getName().startsWith("is")
            ? "set"+aGetter.getName().substring(2)
            : "s"+aGetter.getName().substring(1);
        try {
            return aClass.getMethod(name, aGetter.getReturnType());
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No such method: "+e.getMessage(),e) ;
        }
    }

    /**
     * If in java no arguments in function but in sql there are 1 argument
     *
     * For example:
     * ICompanyDao.getAllCompanies() and get_company(id)
     *
     * @param aDaoMethod     method
     * @param aProcedureInfo procedure information
     * @return true, if method is getAll()
     */
    public static boolean isGetAllMethod(Method aDaoMethod, StoredProcedureInfo aProcedureInfo) {
        return aProcedureInfo.getArgumentsCounts()==1
                && (aDaoMethod.getParameterTypes()==null || aDaoMethod.getParameterTypes().length==0)
                && aDaoMethod.getReturnType().isAssignableFrom(List.class);
    }

    /**
     * Is method return Iterator
     * @param aDaoMethod dao method
     * @return true if method is return Iterator
     */
    public static boolean isReturnIterator(Method aDaoMethod) {
        Class returnType = aDaoMethod.getReturnType();
        return Iterator.class.isAssignableFrom(returnType);
    }

    /**
     * Is simple type not entity class
     * @param aType class
     * @return true if simple java type
     */
    public static boolean isSimpleType(Class aType) {
        return SIMPLE_TYPES.contains(aType);
    }

    public static Method findOneToManyMethod(Class aClass) {
        Method ret = null;
        for (Method method : aClass.getMethods()) {
            if(method.isAnnotationPresent(OneToMany.class)) {
                if(ret!=null) {
                    throw new IllegalStateException("There are more than one @OneToMany annotations in "+aClass.getSimpleName());
                } else {
                    ret =  method;
                }
            }
        }
        return ret;
    }
}
