jdbc-proc provides mappings from POJOs to stored procedures.

### 10 seconds introduction ###
Mapping a stored procedure to Java DAO:
```java
public interface CompanyDao {

    @AStoredProcedure( name = "create_company")
    void createCompany(Company aCompany);

}
```

[Read more about this example](https://github.com/payneteasy/jdbc-proc/wiki/ExampleCreateCompany)

### Main features ###
  * uses mapping specification of JPA with annotations: Entity, Column, OneToOne, ManyToOne, OneToMany, JoinColumn
  * creates stored procedures mapping code on the fly
  * integrated with [spring framework](http://www.springsource.org) and [Google Guice](https://github.com/google/guice)

For step-by-step instructions, see [Getting Started](https://github.com/payneteasy/jdbc-proc/wiki/GettingStarted) guide

### Supported databases ###
  * [MySQL](http://mysql.com) ≥ 5.0
  * [MariaDB](https://mariadb.org) ≥ 10.0 
  * [PostgreSQL](http://postgresql.org)
  * [Microsoft SQL Server](http://www.microsoft.com/sqlserver)

### Gratitudes ###
Many thanks to [JProfiler](http://www.ej-technologies.com/products/jprofiler/overview.html)

### News ###
**2015 Sep 02** Migrated to github

**2013 May 13** Added patched mysql jdbc driver with improved performance [mysql-connector-java-5.1.22-3-bin.jar](https://github.com/payneteasy/jdbc-proc/blob/master/mysql-driver/mysql-connector-java-5.1.22-3-bin.jar?raw=true)

**2011 Nov 02** new version 1.1.1 released. Main feature: added postgresql support

**2011 Nov 01** migrated from svn to git

**2010 May 05** jdbc-proc artifacts are deployed on the Central Repository. Thanks Sonatype!

### They already use jdbc-proc Framework in production environments: ###

  * https://payneteasy.com
