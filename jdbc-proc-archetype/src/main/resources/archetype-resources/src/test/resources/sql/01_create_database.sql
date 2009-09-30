drop database if exists jdbcprocdb

/

create database if not exists jdbcprocdb character set utf8 collate utf8_general_ci

/

use jdbcprocdb

/

GRANT ALL PRIVILEGES ON jdbcprocdb.* TO 'jdbcproc'@'localhost' IDENTIFIED BY 'jdbcproc' WITH GRANT OPTION

