drop table if exists chalk_bags;

DROP SEQUENCE if EXISTS chalk_bags_seq;
CREATE SEQUENCE chalk_bags_seq;

create table chalk_bags(
  chalk_bag_id     bigint default nextval('chalk_bags_seq'),
  chalk_bag_name   varchar(64),
  color            varchar(64),
  materials        varchar(128),
  constraint pk_chalk_bags primary key (chalk_bag_id)
  );
