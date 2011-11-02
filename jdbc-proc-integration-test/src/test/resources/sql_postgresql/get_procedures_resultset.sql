CREATE TYPE myrow AS (
  specific_name VARCHAR,
  routine_resultset VARCHAR
);

CREATE TYPE column_row AS(
	column_name varchar,
	column_type varchar
); 

CREATE OR REPLACE FUNCTION get_procedures_resultset ()
RETURNS SETOF myrow AS
$body$
DECLARE
	function_name varchar;
	rows_count bigint;
	routine_resultset_string varchar;
	query varchar(1000);
	attribute_row column_row%rowtype;
	r myrow%rowtype;
	BEGIN
		for function_name in SELECT  proname
						FROM    pg_catalog.pg_namespace n
						JOIN    pg_catalog.pg_proc p
						ON      pronamespace = n.oid
						WHERE   nspname = 'public'
			 loop
			 	
			 	query:='select count(*) from resultset_info where specific_name = '''||function_name||'''';
			 	EXECUTE query into rows_count;
			 	if(rows_count = 0) then
				
				 	routine_resultset_string := '';	

				 	query:= 'SELECT attname, a.typname	from pg_type t JOIN pg_class on (reltype = t.oid) JOIN pg_attribute on (attrelid = pg_class.oid) JOIN pg_type a on (atttypid = a.oid) WHERE t.typname = (SELECT t.typname FROM pg_catalog.pg_proc p LEFT JOIN pg_catalog.pg_namespace n ON n.oid = p.pronamespace INNER JOIN pg_type t ON p.prorettype = t.oid WHERE n.nspname = ''public'' and proname = '''||function_name||''' ORDER BY proname)';
				 	
				 	for attribute_row in EXECUTE query
						loop
							if(routine_resultset_string != '') then
								routine_resultset_string:=routine_resultset_string||', ';
							end if;
							routine_resultset_string:=routine_resultset_string||attribute_row.column_name||' '||attribute_row.column_type;
						end loop; 
					
					if(routine_resultset_string != '') then
						--check for other types
						routine_resultset_string := REPLACE(routine_resultset_string, 'int8', 'bigint');				
						routine_resultset_string := REPLACE(routine_resultset_string, 'int4', 'integer');
						insert into resultset_info (specific_name, routine_resultset) values (function_name, routine_resultset_string);
					end if;	

				end if;

			 end loop;	 

		for r in select specific_name, routine_resultset from resultset_info loop
		return next r;
		end loop;
		return;
	END;
$body$
LANGUAGE 'plpgsql'
SECURITY INVOKER;

--DROP TYPE column_row;