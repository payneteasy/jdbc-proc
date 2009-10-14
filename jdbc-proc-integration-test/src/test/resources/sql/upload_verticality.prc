drop procedure if exists upload_verticality;
delimiter $$
create procedure upload_verticality(i_upload_date datetime)
 main_sql:
  begin
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

    truncate table carabiners;
    truncate table dynamic_ropes;
  end
$$
delimiter ;