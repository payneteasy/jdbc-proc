DROP FUNCTION if exists create_entity_with_two_lists();

CREATE OR REPLACE FUNCTION create_entity_with_two_lists(
    out o_id     bigint,
    i_name       varchar(255),
    i_prop3      varchar(255),
    i_prop4      varchar(255),
    i_prop5      varchar(255)
) AS'
DECLARE v_count bigint;
BEGIN
  o_id:=999;

  select count(*) from list_elements into v_count;
  if v_count <> 2 then
    RAISE EXCEPTION''raise exception'';
  end if;

  select count(*) from list_elements2 into v_count;
  if v_count <> 2 then
    RAISE EXCEPTION''raise exception'';
  end if;

END;'
language 'plpgsql';