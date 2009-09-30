create table resultset_info (
     rs_id          int(10) unsigned not null auto_increment
   , specific_name     varchar(255)
   , routine_resultset varchar(255)

   , constraint pk_rs primary key (rs_id)
) engine = innodb 

