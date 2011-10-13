drop procedure if exists save_entity_with_list;
delimiter $$
create procedure save_entity_with_list(
                                       out o_id     int(10),
                                       i_username   varchar(32),
                                       i_principal  varchar(32),
                                       i_name       varchar(255)
                                      )
 main_sql:
  begin
    set o_id = 999;
  end
$$
delimiter ;