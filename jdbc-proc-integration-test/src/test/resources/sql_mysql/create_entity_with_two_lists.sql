drop procedure if exists create_entity_with_two_lists;
delimiter $$
create procedure create_entity_with_two_lists(
                                       out o_id     int(10),
                                       i_name       varchar(255),
                                       i_prop3      varchar(255),
                                       i_prop4      varchar(255),
                                       i_prop5      varchar(255)
                                      )
 main_sql:
  begin
    declare v_count int(10);

    set o_id = 999;

    select count(*) from list_elements into v_count;
    if v_count <> 2 then
        call raise_application_error();
    end if;

    select count(*) from list_elements2 into v_count;
    if v_count <> 2 then
        call raise_application_error();
    end if;
  end
$$
delimiter ;