drop table if exists chalk_bags;

create table chalk_bags(
  chalk_bag_id     bigint(20) auto_increment,
  chalk_bag_name   varchar(64),
  color            varchar(64),
  materials        varchar(128),
  constraint pk_chalk_bags primary key (chalk_bag_id)
  )
  engine = innodb;
