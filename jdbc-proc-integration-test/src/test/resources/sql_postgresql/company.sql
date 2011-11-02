DROP TABLE if EXISTS company;

DROP SEQUENCE if EXISTS company_seq;
CREATE SEQUENCE company_seq;

create table company (
     company_id   bigint not null default nextval('company_seq')
   , name         varchar(60)
   
   , constraint pk_company primary key (company_id)
);
