1. jdbc url

new parameters:
**useInformationSchema=true** noAccessToProcedureBodies=true

```
jdbc:mysql://localhost/jdbcprocdb?autoReconnect=false&amp;characterEncoding=utf8&amp;useInformationSchema=true&amp;noAccessToProcedureBodies=true
```

2. add new strategy
```
    <bean id="daoMethodInfoFactory" class="com.googlecode.jdbcproc.daofactory.DaoMethodInfoFactory">
      <property name="jdbcTemplate"         ref="jdbcTemplate" />
      <property name="metaLoginInfoService" ref="metaLoginInfoService" />
      <property name="callableStatementGetStrategyFactory" value="#{ new com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementGetStrategyFactoryIndexImpl() }" />
      <property name="callableStatementSetStrategyFactory" value="#{ new com.googlecode.jdbcproc.daofactory.impl.dbstrategy.impl.CallableStatementSetStrategyFactoryIndexImpl() }" />
    </bean>
```

3. jdbc-proc version >=1.1-2

4. mysql driver 5.1.22-1

  * driver - http://jdbc-proc.googlecode.com/files/mysql-connector-java-5.1.22-1.jar

  * patch - http://jdbc-proc.googlecode.com/files/mysql-5.1.22-1.patch