drop procedure if exists json_create_entity_with_null_list;
delimiter $$
create procedure json_create_entity_with_null_list(
                                       out o_id     int(10),
                                       i_username   varchar(32),
                                       i_principal  varchar(32),
                                       i_name       varchar(255),
                                       i_prop3      varchar(255),
                                       i_prop4      varchar(255),
                                       i_prop5      varchar(255),
                                       i_list       json
                                      )
 main_sql:
  begin

    set o_id = 999;

    if i_list is not null then
      set @v_ddl_statement = concat('call `|ERROR: illegal i_list value|', i_list, '|`');
      prepare v_stmt from @v_ddl_statement;
      execute v_stmt;
      deallocate prepare v_stmt;
    end if;

  end
$$
delimiter ;