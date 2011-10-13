drop procedure if exists update_entity_with_list;
delimiter $$
create procedure update_entity_with_list(
                                       i_username   varchar(32),
                                       i_principal  varchar(32),
                                       i_id         int(10),
                                       i_name       varchar(255)
                                      )
 main_sql:
  begin

  end
$$
delimiter ;