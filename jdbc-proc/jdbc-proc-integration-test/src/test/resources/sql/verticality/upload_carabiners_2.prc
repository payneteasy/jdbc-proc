drop procedure if exists upload_carabiners_2;
delimiter $$
create procedure upload_carabiners_2(i_upload_date datetime, 
                                     i_upload_date_2 datetime)
 main_sql:
  begin
    declare v_last_insert_id                int(10);  
  
    insert into storage_carabiners
          (
             name, weight, bs_major_axis, bs_open_gate, bs_cross_loaded, gate_opening, upload_date
          )
      select name,
             weight, 
             bs_major_axis,
             bs_open_gate,
             bs_cross_loaded,
             gate_opening,
             date(i_upload_date)
        from carabiners;

    set v_last_insert_id = 1;
    
    select v_last_insert_id entity_id from dual;
  end
$$
delimiter ;

insert into resultset_info (specific_name, routine_resultset)
values ('upload_carabiners_2', 'entity_id int');
