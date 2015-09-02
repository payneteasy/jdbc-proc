How to store Company simple entity to database

### Create Company entity class ###

<img src='http://yuml.me/diagram/scruffy/class/[Company%7Cid;name;].png' />

```
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Company
 */
public class Company {
    /**
     * Idendificator
     */
    @Id
    @Column(name = "company_id")
    public long getId() {
        return theId;
    }

    public void setId(long aId) {
        theId = aId;
    }

    /**
     * Company name
     */
    @Column(name = "name")
    public String getName() {
        return theName;
    }

    public void setName(String aName) {
        theName = aName;
    }

    private String theName;
    private long theId;

}
```

### Create ICompanyDao ###
```

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;

import java.util.List;

/**
 * Test dao
 */
public interface ICompanyDao {

    /**
     * Creates company
     *
     * @param aCompany company to create
     */
    @AStoredProcedure(name = "create_company")
    void createCompany(Company aCompany);
}
```

### Create mysql stored procedure ###
```
delimiter $$

create procedure create_company (
     out o_company_id int(10)
   , i_name varchar(60)
)
begin
    insert into company( name ) values (i_name);
    set o_company_id = last_insert_id();
end
$$

```


### Register in spring-dao.xml ###

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx ="http://www.springframework.org/schema/tx"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
         http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
         http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
       ">

    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url"
                  value="jdbc:mysql://localhost/jdbcprocdb?characterEncoding=utf8"/>
        <property name="username" value="jdbcproc"/>
        <property name="password" value="jdbcproc"/>
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <bean id="daoMethodInfoFactory" class="com.googlecode.jdbcproc.daofactory.DaoMethodInfoFactory">
      <property name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>

    <!-- Company data access -->
    <bean id="companyDao" parent="abstractDao">
        <property name="interface" value="com.googlecode.jdbcproc.daofactory.it.testdao.dao.ICompanyDao"/>
    </bean>

</beans>
```

### Use it ###
```
public class CompanyDaoTest {
 
   public CompanyDaoTest(ICompanyDao aCompanyDao) {
       theCompanyDao = aCompanyDao;
   }

   public void testCreateCompany() {
        Company company = new Company();
        company.setName("first");

        theCompanyDao.createCompany(company);

        System.out.println("Company id = " + company.getid());
    }
    
    private final ICompanyDao theCompanyDao;
}
```