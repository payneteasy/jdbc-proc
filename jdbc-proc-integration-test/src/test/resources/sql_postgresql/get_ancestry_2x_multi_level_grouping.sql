CREATE TYPE ancestry_2x_multi_level_row AS (
	name varchar
	, son_name varchar
	, son_son_name varchar
);

CREATE OR REPLACE FUNCTION get_ancestry_2x_multi_level_grouping ()
	RETURNS SETOF ancestry_2x_multi_level_row as' 
	DECLARE r ancestry_2x_multi_level_row%rowtype;
	BEGIN
		FOR r in select
		   g.name as name,
		   f.name as son_name,
		   b.name as son_son_name
		from grandfather g
		   join father f on f.grandfather_id = g.grandfather_id
		   join boy b on b.father_id = f.father_id
		order by g.grandfather_id, f.father_id, b.boy_id loop
		return next r;
		end loop;
		return;
		
	end;'
	language 'plpgsql';

--insert into resultset_info (specific_name, routine_resultset)
--values ('get_ancestry_2x_multi_level_grouping', 'name varchar, son_name varchar, son_son_name varchar');