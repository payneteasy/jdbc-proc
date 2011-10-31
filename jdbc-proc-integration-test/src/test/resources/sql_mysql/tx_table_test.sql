drop procedure if exists tx_table_test;
delimiter $$
create procedure tx_table_test()
 main_sql:
  begin
    insert into tx_table(pk_id) values (4);
    insert into tx_table(pk_id) values (1);
    insert into tx_table(pk_id) values (1);
    insert into tx_table(pk_id) values (2);
  end
$$
delimiter ;
