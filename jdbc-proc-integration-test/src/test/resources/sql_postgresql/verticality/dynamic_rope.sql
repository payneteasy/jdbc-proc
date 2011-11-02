drop table if exists storage_dynamic_ropes;

DROP SEQUENCE if EXISTS storage_dynamic_ropes_seq;
CREATE SEQUENCE storage_dynamic_ropes_seq;

create table storage_dynamic_ropes(
  dynamic_rope_id  bigint default nextval('storage_dynamic_ropes_seq'),
  name             varchar(64),
  diameter         decimal(5,2),
  lengths          bigint,
  type             varchar(64),
  weight           decimal(5,2),
  upload_date      date,
  constraint pk_storage_dynamic_ropes primary key (dynamic_rope_id)
  );
