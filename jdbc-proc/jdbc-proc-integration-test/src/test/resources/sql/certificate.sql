create table certificate (

     certificate_id   int(10) unsigned not null auto_increment
   , name             varchar(60)
   , employee_id      int(10)

   , constraint pk_certificate primary key (certificate_id)
) engine = innodb ;

