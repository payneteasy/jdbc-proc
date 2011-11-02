DROP TABLE if EXISTS certificate;

DROP SEQUENCE if EXISTS certificate_seq;
CREATE SEQUENCE certificate_seq;

create table certificate (

     certificate_id   bigint not null default nextval('certificate_seq')
   , name             varchar(60)
   , employee_id      bigint

   , constraint pk_certificate primary key (certificate_id)
);