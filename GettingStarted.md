### Create mysql user and grants all privileges to jdbcprocdb database ###

```
GRANT ALL PRIVILEGES ON jdbcprocdb.* TO 'jdbcproc'@'localhost' IDENTIFIED BY 'jdbcproc' WITH GRANT OPTION;
```

### For postgresql ###
```
CREATE USER jdbcproc WITH PASSWORD 'jdbcproc';
GRANT ALL PRIVILEGES ON DATABASE jdbcprocdb TO jdbcproc WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON DATABASE jdbcproc TO jdbcproc WITH GRANT OPTION;
ALTER DATABASE jdbcprocdb OWNER to jdbcproc;
ALTER USER jdbcproc CREATEDB;
```
### Create maven project ###

```
mvn archetype:generate -B                               \
    -DarchetypeGroupId=com.googlecode.jdbc-proc         \
    -DarchetypeArtifactId=jdbc-proc-archetype           \
    -DarchetypeVersion=1.0-5                            \
                                                        \
    -DgroupId=my.groupid                                \
    -DartifactId=my-artifactId                          \
    -Dversion=1.0-1-SNAPSHOT                            \
```

### Use ###

Change into the project directory and type `mvn clean install`