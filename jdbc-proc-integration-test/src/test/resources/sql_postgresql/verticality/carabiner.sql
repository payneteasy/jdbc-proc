drop table if exists storage_carabiners;

DROP SEQUENCE if EXISTS carabiners_seq;
CREATE SEQUENCE carabiners_seq;

create table storage_carabiners(
  carabiner_id     bigint default nextval('carabiners_seq'),
  name             varchar(64),
  weight           decimal(5, 2), 
  bs_major_axis    bigint,
  bs_open_gate     bigint,
  bs_cross_loaded  bigint,
  gate_opening     bigint,
  upload_date      date,
  constraint pk_storage_carabiners primary key (carabiner_id)
  );
