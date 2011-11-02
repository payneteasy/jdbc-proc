CREATE OR REPLACE FUNCTION upload_carabiners(i_upload_date date)
RETURNS void as'
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

    truncate table carabiners;
  end;'
language 'plpgsql';