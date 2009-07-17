package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;

import javax.persistence.Column;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
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

    public static boolean isGetAllMethod(Method aDaoMethod, StoredProcedureInfo aProcedureInfo) {
        return aProcedureInfo.getArgumentsCounts()==1 && aDaoMethod.getReturnType().isAssignableFrom(List.class);
    }


    /**
     * Is simple type not entity class
     * @param aType class
     * @return true if simple java type
     */
    public static boolean isSimpleType(Class aType) {
        return SIMPLE_TYPES.contains(aType);
    }

}
