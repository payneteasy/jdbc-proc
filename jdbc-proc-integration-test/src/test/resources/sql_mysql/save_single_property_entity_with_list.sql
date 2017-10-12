drop procedure if exists save_single_property_entity_with_list;
delimiter $$
create procedure save_single_property_entity_with_list(
                                       out o_id     int(10)
                                      )
 main_sql:
  begin
    declare v_count int(10);

    set o_id = 99;

    select count(*) from list_elements into v_count;
    if v_count <> 2 then
        call raise_application_error();
    end if;
  end
$$
delimiter ;