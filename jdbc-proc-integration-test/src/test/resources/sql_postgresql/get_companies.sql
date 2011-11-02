CREATE TYPE companies_row AS (
  company_id bigint,
  name varchar
);

CREATE OR REPLACE FUNCTION get_companies ()
RETURNS SETOF companies_row as'
DECLARE
	r companies_row%rowtype;
	BEGIN
		for r in select company_id, name from company loop
		return next r;
		end loop;
		return;
	END;'
LANGUAGE 'plpgsql';


--insert into resultset_info (specific_name, routine_resultset)
--values ('get_companies'
--       , 'company_id bigint, name varchar');