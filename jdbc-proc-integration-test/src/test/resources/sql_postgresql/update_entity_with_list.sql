DROP FUNCTION if exists update_entity_with_list();

CREATE OR REPLACE FUNCTION update_entity_with_list(
    i_username   varchar(32),
    i_principal  varchar(32),
    i_id         bigint,
    i_name       varchar(255)
) RETURNS void  AS' 
declare v_count bigint;
BEGIN

  select count(*) from list_elements into v_count;
  if v_count <> 2 then
    RAISE EXCEPTION''raise exception'';
  end if;
END;'
language 'plpgsql';