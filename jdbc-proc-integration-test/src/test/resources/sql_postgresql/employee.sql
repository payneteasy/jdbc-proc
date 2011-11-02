DROP TABLE if EXISTS employee;

DROP SEQUENCE if EXISTS employee_seq;
CREATE SEQUENCE employee_seq;

create table employee (
     employee_id   bigint not null default nextval('employee_seq')
   , firstname     varchar(60)
   , lastname      varchar(60)
   , company_company_id   bigint
   
   , constraint pk_employee primary key (employee_id)
);