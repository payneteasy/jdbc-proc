--CREATE TYPE company_name_row AS (
--  name varchar
--);

CREATE OR REPLACE FUNCTION get_companies_names_secured (
          i_username    varchar(60)
   ,      i_principal   varchar(60)
)
RETURNS SETOF company_name_row as'
DECLARE r company_name_row%rowtype;
BEGIN
	FOR r in select name from company order by name LOOP
	return next r;
	end loop;
	return;	
END;'
language 'plpgsql';

--insert into resultset_info (specific_name, routine_resultset) values ('get_companies_names_secured', 'name varchar');