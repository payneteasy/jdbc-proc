CREATE TYPE employer_row AS (
	employee_id bigint
	, firstname varchar
	, lastname varchar
	, company_company_id bigint
	, company_name varchar
);

CREATE OR REPLACE FUNCTION get_employee_by_id (
     in i_id bigint
) RETURNS SETOF employer_row as'
	declare r employer_row%rowtype;
	begin
		for r in select 
			em.employee_id as employee_id
			, em.firstname as firstname
			, em.lastname as lastname
			, comp.company_id as company_company_id
			, comp.name as company_name
	      from employee em
	      left join company comp on em.company_company_id = comp.company_id
	     where em.employee_id = i_id loop
	     return next r;
	     end loop;
	     return;
	     
	end;'
language 'plpgsql';

--insert into resultset_info (specific_name, routine_resultset)
--values ('get_employee_by_id', 'employee_id bigint, firstname varchar, lastname varchar, company_company_id bigint, company_name varchar');

