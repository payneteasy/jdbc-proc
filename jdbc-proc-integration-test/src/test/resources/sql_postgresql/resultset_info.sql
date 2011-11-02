DROP TABLE if EXISTS resultset_info;

DROP SEQUENCE if EXISTS resultset_seq;
CREATE SEQUENCE resultset_seq;

create table resultset_info (
     rs_id          bigint not null DEFAULT nextval('resultset_seq')
   , specific_name     varchar(255)
   , routine_resultset varchar(255)

   , constraint pk_rs primary key (rs_id)
);

