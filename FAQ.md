#FAQ
### java.sql.SQLException: No access to parameters by name when connection has been configured not to access procedure bodies ###

Use Index strategy to fill arguments
for Guice
```
        install(new InitJdbcProcModule() {
            @Override
            protected void bindCallableStatementSetStrategyFactory(AnnotatedBindingBuilder<ICallableStatementSetStrategyFactory> aBuilder) {
                aBuilder.to(CallableStatementSetStrategyFactoryIndexImpl.class);
            }
        });

```

### How to optimise mysql-jdbc drivers calls ###

  * Add additional parameters to jdbc url:
  1. useInformationSchema = true
  1. noAccessToProcedureBodies = true
  1. useLocalSessionState = true

For example:
```
jdbc:mysql://localhost:3306/database
 ?logger=Slf4JLogger
 &autoReconnect=false
 &characterEncoding=utf8
 &useInformationSchema=true
 &noAccessToProcedureBodies=true
 &useLocalSessionState=true
 &profileSQL=false
```

  * Download https://jdbc-proc.googlecode.com/files/mysql-connector-java-5.1.22-3-bin.jar
  * Install this file to maven repository:
```
mvn install:install-file -DgroupId=mysql -DartifactId=mysql-connector-java \
    -Dversion=5.1.22-3  -Dpackaging=jar \
    -Dfile=mysql-connector-java-5.1.22-3-bin.jar
```

This driver uses patches:
  * https://jdbc-proc.googlecode.com/files/mysql-5.1.22-1.patch
  * https://jdbc-proc.googlecode.com/files/disable-autocommit.patch