drop table if exists storage_harnesses;

DROP SEQUENCE if EXISTS storage_harnesses_seq;
CREATE SEQUENCE storage_harnesses_seq;

create table storage_harnesses(
  harness_id       bigint default nextval('storage_harnesses_seq'),
  name             varchar(64),
  weight           decimal(5, 2), 
  color            varchar(32),
  size             varchar(32),
  constraint pk_storage_harnesses primary key (harness_id)
  );
