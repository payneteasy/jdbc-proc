jdbc-proc provides mappings from POJOs to stored procedures.

Create stored procedure and map it to DAO interface method.
```
public interface CompanyDao {

    @AStoredProcedure( name = "create_company")
    void createCompany(Company aCompany);

}
```

[Read more about this example](ExampleCreateCompany.md)

### Main features ###
  * uses mapping specification of JPA with annotations: Entity, Column, OneToOne, ManyToOne, OneToMany, JoinColumn
  * creates stored procedures mapping code on the fly
  * integrated with [spring framework](http://www.springsource.org) and [Google Guice](http://google-guice.googlecode.com)

For step-by-step instructions, see [Getting Started](GettingStarted.md) guide

### Supported databases ###
  * [MySQL](http://mysql.com) 5.0.x, 5.1.x, 5.5.x, 5.6.x
  * [PostgreSQL](http://postgresql.org)
  * [Microsoft SQL Server](http://www.microsoft.com/sqlserver)

### Gratitudes ###
Many thanks to [JProfiler](http://www.ej-technologies.com/products/jprofiler/overview.html)

### News ###
**2013 May 13** Added patched mysql jdbc driver with improved performance https://jdbc-proc.googlecode.com/files/mysql-connector-java-5.1.22-3-bin.jar

**2011 Nov 02** new version 1.1.1 released. Main feature: added postgresql support

**2011 Nov 01** migrated from svn to git

**2010 May 05** jdbc-proc artifacts are deployed on the Central Repository. Thanks Sonatype!

### They already use jdbc-proc Framework in production environments: ###

  * http://payneteasy.com
  * http://sellbycell.ru
