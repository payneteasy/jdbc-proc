drop procedure if exists json_create_entity_with_two_lists_and_metalogin_info;
delimiter $$
create procedure json_create_entity_with_two_lists_and_metalogin_info(
                                       out o_id     int(10),
                                       i_username   varchar(32),
                                       i_principal  varchar(32),
                                       i_name       varchar(255),
                                       i_prop3      varchar(255),
                                       i_prop4      varchar(255),
                                       i_prop5      varchar(255),
                                       i_list1      json,
                                       i_list2      json
                                      )
 main_sql:
  begin
    declare v_count int(10);

    set o_id = 999;

    if json_length(i_list1) != 2 then
      set @v_ddl_statement = concat('call `|ERROR: illegal i_list1 value|', i_list1, '|`');
      prepare v_stmt from @v_ddl_statement;
      execute v_stmt;
      deallocate prepare v_stmt;
    end if;

    if json_length(i_list2) != 2 then
      set @v_ddl_statement = concat('call `|ERROR: illegal i_list2 value|', i_list2, '|`');
      prepare v_stmt from @v_ddl_statement;
      execute v_stmt;
      deallocate prepare v_stmt;
    end if;

  end
$$
delimiter ;