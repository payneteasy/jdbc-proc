CREATE TYPE entity_row AS(
  entity_id integer  
);


CREATE OR REPLACE FUNCTION upload_carabiners_2(
    i_upload_date date,
    i_upload_date_2 date
) RETURNS entity_row as'
  DECLARE r entity_row%rowtype;  
  BEGIN
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
    
    select 1 into r;   
    return r;
  end;'
language 'plpgsql';

--insert into resultset_info (specific_name, routine_resultset)
--values ('upload_carabiners_2', 'entity_id integer');