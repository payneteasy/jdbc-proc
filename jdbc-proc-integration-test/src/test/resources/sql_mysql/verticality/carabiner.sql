drop table if exists storage_carabiners;

create table storage_carabiners(
  carabiner_id     int(10) auto_increment,
  name             varchar(64),
  weight           decimal(5, 2), 
  bs_major_axis    int(10),
  bs_open_gate     int(10),
  bs_cross_loaded  int(10),
  gate_opening     int(10),
  upload_date      datetime,
  constraint pk_storage_carabiners primary key (carabiner_id)
  )
  engine = innodb;
