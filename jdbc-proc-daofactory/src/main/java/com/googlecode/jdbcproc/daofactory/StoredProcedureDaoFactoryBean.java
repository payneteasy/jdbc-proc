package com.googlecode.jdbcproc.daofactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Proxy;

import com.googlecode.jdbcproc.daofactory.impl.StoredProcedureDaoInvocationHandler;

/**
 * Creates dao based on stored procedure
 */
public class StoredProcedureDaoFactoryBean implements FactoryBean {

    /**
     * Creates proxy object
     * @return instance of #setInterface
     * @throws Exception on error
     */
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                , new Class[] {theInterface}
                , new StoredProcedureDaoInvocationHandler(theInterface, theJdbcTemplate, theDaoMethodInfoFactory)
        );
    }

    /**
     * Returns #setInterface
     * @return interface
     */
    public Class getObjectType() {
        return theInterface;
    }

    /**
     * Always true
     * @return true
     */
    public boolean isSingleton() {
        return true;
    }

    /** Interface class to be proxied */
    public void setInterface(Class aInterface) { theInterface = aInterface ; }

    /** JdbcTemplate */
    public void setJdbcTemplate(JdbcTemplate aJdbcTemplate) { theJdbcTemplate = aJdbcTemplate ; }

    /** DaoMethodInfoFactory */
    public void setDaoMethodInfoFactory(DaoMethodInfoFactory aDaoMethodInfoFactory) { theDaoMethodInfoFactory = aDaoMethodInfoFactory ; }

    public String toString() {
        return "StoredProcedureDaoFactoryBean{" +
                "theDaoMethodInfoFactory=" + theDaoMethodInfoFactory +
                ", theJdbcTemplate=" + theJdbcTemplate +
                ", theInterface=" + theInterface +
                '}';
    }

    /** DaoMethodInfoFactory */
    private DaoMethodInfoFactory theDaoMethodInfoFactory ;
    /** JdbcTemplate */
    private JdbcTemplate theJdbcTemplate ;
    /** Interface class to be proxied */
    private Class theInterface ;

}
