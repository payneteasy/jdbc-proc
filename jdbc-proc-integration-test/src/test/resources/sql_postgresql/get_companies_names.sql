CREATE TYPE company_name_row AS (
  name varchar
);

CREATE OR REPLACE FUNCTION get_companies_names ()
RETURNS SETOF company_name_row as'
DECLARE r company_name_row;
BEGIN
	FOR r in select name from company order by name LOOP
	return next r;
	end loop;
	return;	
END;'
language 'plpgsql';

--insert into resultset_info (specific_name, routine_resultset) values ('get_companies_names', 'name varchar');