drop procedure if exists save_single_property_entity_with_security;
delimiter $$
create procedure save_single_property_entity_with_security(
                                       out o_id     int(10),
                                       i_username   varchar(32),
                                       i_principal  varchar(32)
                                      )
 main_sql:
  begin
    set o_id = 99;
  end
$$
delimiter ;