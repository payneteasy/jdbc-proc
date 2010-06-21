drop procedure if exists upload_dynamic_ropes_with_meta_login_info;
delimiter $$
create procedure upload_dynamic_ropes_with_meta_login_info(i_username    varchar(60),      
                                                           i_principal   varchar(60),
                                                           i_upload_date datetime)
 main_sql:
  begin

    insert into storage_dynamic_ropes
          (
             name, diameter, lengths, type, weight, upload_date
          )
      select name,
             diameter,
             lengths,
             type,
             weight,
             date(i_upload_date)
        from dynamic_ropes;

    truncate table dynamic_ropes;
  end
$$
delimiter ;
