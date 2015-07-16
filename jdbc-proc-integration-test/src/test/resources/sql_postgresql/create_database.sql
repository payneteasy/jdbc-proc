--REVOKE CONNECT ON DATABASE jdbcprocdb FROM PUBLIC, postgres;
--SELECT pg_terminate_backend(procpid) FROM pg_stat_activity WHERE procpid <> pg_backend_pid();

drop database if exists jdbcprocdb;

CREATE USER jdbcproc WITH PASSWORD 'jdbcproc';
CREATE DATABASE jdbcprocdb encoding 'utf8';
GRANT ALL PRIVILEGES ON DATABASE jdbcprocdb TO jdbcproc WITH GRANT OPTION;
ALTER DATABASE jdbcprocdb OWNER to jdbcproc;
ALTER USER jdbcproc CREATEDB;