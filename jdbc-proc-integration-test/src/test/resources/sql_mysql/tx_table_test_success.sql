drop procedure if exists tx_table_test_success;
delimiter $$
create procedure tx_table_test_success()
 main_sql:
  begin
    insert into tx_table(pk_id) values (3);
  end
$$
delimiter ;
