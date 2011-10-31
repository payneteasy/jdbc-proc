drop table if exists storage_dynamic_ropes;

create table storage_dynamic_ropes(
  dynamic_rope_id  int(10) auto_increment,
  name             varchar(64),
  diameter         decimal(5,2),
  lengths          int(10),
  type             varchar(64),
  weight           decimal(5,2),
  upload_date      datetime,
  constraint pk_storage_dynamic_ropes primary key (dynamic_rope_id)
  )
  engine = innodb;
