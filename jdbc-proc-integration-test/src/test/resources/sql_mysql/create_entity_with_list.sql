drop procedure if exists create_entity_with_list;
delimiter $$
create procedure create_entity_with_list(
                                       out o_id     int(10),
                                       i_username   varchar(32),
                                       i_principal  varchar(32),
                                       i_name       varchar(255)
                                      )
 main_sql:
  begin
    declare v_count int(10);

    set o_id = 999;

    select count(*) from list_elements into v_count;
    if v_count <> 1 then
        call raise_application_error();
    end if;
  end
$$
delimiter ;