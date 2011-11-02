--REVOKE CONNECT ON DATABASE jdbcprocdb FROM PUBLIC, postgres;
--SELECT pg_terminate_backend(procpid) FROM pg_stat_activity WHERE procpid <> pg_backend_pid();

#drop database if exists jdbcprocdb;

#CREATE DATABASE jdbcprocdb ENCODING 'UTF8';

#DROP ROLE if EXISTS jdbcproc;
#CREATE USER jdbcproc WITH PASSWORD 'jdbcproc';

#GRANT ALL PRIVILEGES ON DATABASE jdbcprocdb TO jdbcproc WITH GRANT OPTION;