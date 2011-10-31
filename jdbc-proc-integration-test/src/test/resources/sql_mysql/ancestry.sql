drop table if exists boy;
drop table if exists father;
drop table if exists grandfather;

create table grandfather (
     grandfather_id   int(10) unsigned not null auto_increment
   , name             varchar(60)
   , constraint pk_grandfather primary key (grandfather_id)
) engine = innodb;

create table father (
     father_id        int(10) unsigned not null auto_increment
   , name             varchar(60)
   , grandfather_id   int(10) unsigned not null
   , constraint pk_father primary key (father_id)
) engine = innodb ;

create table boy (
     boy_id           int(10) unsigned not null auto_increment
   , name             varchar(60)
   , constraint pk_boy primary key (boy_id)
   , father_id   int(10) unsigned not null
) engine = innodb ;

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