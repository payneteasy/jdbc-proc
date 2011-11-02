CREATE TYPE chalk_bag_row AS (
	chalk_bag_id bigint
	, chalk_bag_name varchar
	, color varchar
	, materials varchar
);

CREATE OR REPLACE FUNCTION get_chalk_bags()
RETURNS SETOF chalk_bag_row as'
DECLARE r chalk_bag_row%rowtype;
  BEGIN
    FOR r IN select chalk_bag_id, chalk_bag_name, color, materials
      from chalk_bags loop
      return next r;
      end loop;
      return;
  END;'
language 'plpgsql';

--insert into resultset_info (specific_name, routine_resultset)
--values ('get_chalk_bags', 'chalk_bag_id bigint, chalk_bag_name varchar, color varchar, materials varchar');