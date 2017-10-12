drop procedure if exists save_single_property_entity;
delimiter $$
create procedure save_single_property_entity(
                                       out o_id     int(10)
                                      )
 main_sql:
  begin
    set o_id = 99;
  end
$$
delimiter ;