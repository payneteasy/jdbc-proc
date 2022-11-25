drop procedure if exists json_create_with_list;
delimiter $$
create procedure json_create_with_list(
                                       i_name       varchar(255),
                                       i_param1     varchar(255),
                                       i_list       json
                                      )
 main_sql:
  begin

    if json_length(i_list) != 2 then
      set @v_ddl_statement = concat('call `|ERROR: illegal i_list value|', i_list, '|`');
      prepare v_stmt from @v_ddl_statement;
      execute v_stmt;
      deallocate prepare v_stmt;
    end if;

  end
$$
delimiter ;