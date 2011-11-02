DROP TABLE if EXISTS tx_table;

DROP SEQUENCE if EXISTS tx_table_seq;
CREATE SEQUENCE tx_table_seq;

create table tx_table (
  	pk_id       integer default nextval('tx_table_seq')
  , constraint pk_paynet_versions primary key (pk_id)
);
