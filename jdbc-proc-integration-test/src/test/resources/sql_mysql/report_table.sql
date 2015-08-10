create table report (
     report_id      int(10) unsigned not null auto_increment
   , firstname      varchar(60)
   , lastname       varchar(60)
   , amount         decimal(10, 4)
   , constraint pk_report primary key (report_id)
) engine = innodb ;

insert into report (firstname, lastname, amount)
     values ('firstnam1', 'lastname1', '10.0'),
            ('firstnam2', 'lastname2', '20.0');

commit;
