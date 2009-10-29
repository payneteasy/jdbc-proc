drop table if exists storage_harnesses;

create table storage_harnesses(
  harness_id       int(10) auto_increment,
  name             varchar(64),
  weight           decimal(5, 2), 
  color            varchar(32),
  size             varchar(32),
  constraint pk_storage_harnesses primary key (harness_id)
  )
  engine = innodb;
