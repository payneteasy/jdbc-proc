drop table if exists boy;
drop table if exists father;
drop table if exists grandfather;

DROP SEQUENCE if EXISTS grandfather_seq;
CREATE SEQUENCE grandfather_seq;

DROP SEQUENCE if EXISTS father_seq;
CREATE SEQUENCE father_seq;

DROP SEQUENCE if EXISTS boy_seq;
CREATE SEQUENCE boy_seq;

create table grandfather (
     grandfather_id   bigint not null default nextval('grandfather_seq')
   , name             varchar(60)
   , constraint pk_grandfather primary key (grandfather_id)
);

create table father (
     father_id        bigint not null default nextval('father_seq')
   , name             varchar(60)
   , grandfather_id   bigint not null
   , constraint pk_father primary key (father_id)
); 

create table boy (
     boy_id           bigint not null default nextval('boy_seq')
   , name             varchar(60)
   , constraint pk_boy primary key (boy_id)
   , father_id   bigint not null
); 

insert into grandfather (grandfather_id, name) values
    (1, 'Grand Tom'),
    (2, 'Grand Sam')
;

insert into father (father_id, name, grandfather_id) values
    (1, 'John', 1),
    (2, 'John', 2)
;

insert into boy (boy_id, name, father_id) values
    (1, 'Little Jimmy', 1),
    (2, 'Little Timmy', 1),
    (3, 'Little Jacky', 2),
    (4, 'Little Stanny', 2)
;

commit;