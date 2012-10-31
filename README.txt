1. build command
$ mvn clean install

2. integration test for mysql
$ mvn test -Pintegration-test

3. integration test for postgresql
$ mvn test -Pintegration-test -Ddialect=postgresql

4. prepare mysql for integration test
GRANT ALL PRIVILEGES ON jdbcprocdb.* TO 'jdbcproc'@'localhost' IDENTIFIED BY 'jdbcproc' WITH GRANT OPTION;

5. prepare postgresql for integration test
CREATE USER jdbcproc WITH PASSWORD 'jdbcproc';
GRANT ALL PRIVILEGES ON DATABASE jdbcprocdb TO jdbcproc WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON DATABASE jdbcproc TO jdbcproc WITH GRANT OPTION;
ALTER DATABASE jdbcprocdb OWNER to jdbcproc;
ALTER USER jdbcproc CREATEDB;


// revoke select on mysql.proc from 'jdbcproc'@'localhost';
// grant  select on mysql.proc to 'jdbcproc'@'localhost';
// set global event_scheduler='off';