CREATE TYPE company_employees_row AS(
  company_id bigint
  , name varchar
  , employee_employee_id bigint
  , employee_firstname varchar
  , employee_lastname varchar
);

CREATE OR REPLACE FUNCTION get_company_employees (
     in i_company_id bigint
) RETURNS SETOF company_employees_row as'
  DECLARE r company_employees_row%rowtype;
begin

    if i_company_id is not null then
        FOR r in select 
               comp.company_id as company_id
               , comp.name       as name
               , em.employee_id  as employee_employee_id
               , em.firstname    as employee_firstname
               , em.lastname     as employee_lastname
          from employee em
          left join company comp on em.company_company_id = comp.company_id
         where comp.company_id = i_company_id loop
         return next r;
         end loop;
         return;
    else
        FOR r in select 
               comp.company_id as company_id
               , comp.name       as name
               , em.employee_id  as employee_employee_id
               , em.firstname    as employee_firstname
               , em.lastname     as employee_lastname
          from employee em
          left join company comp on em.company_company_id = comp.company_id
          order by comp.company_id, em.employee_id loop
          return next r;
          end loop;
          return;
    end if;
    
end;'
language 'plpgsql';

--insert into resultset_info (specific_name, routine_resultset)
--values ('get_company_employees', 'company_id bigint, name varchar, employee_employee_id bigint, employee_firstname varchar, employee_lastname varchar');