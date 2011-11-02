CREATE TYPE company_2x_row AS(
  id bigint
  , name varchar
  , employee_id bigint
  , employee_firstname varchar
  , employee_lastname varchar
  , employee_certificate_name varchar
  , employee_certificate_id bigint
  , employee_certificate_employee_id bigint
);
  
CREATE OR REPLACE FUNCTION get_company_2x (
     in i_company_id integer
) RETURNS SETOF company_2x_row AS'
DECLARE r company_2x_row%rowtype;
begin
  
    if i_company_id is not null then
        FOR r in select 
               comp.company_id as id
               , comp.name       as name
               , em.employee_id  as employee_id
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
               comp.company_id     as id
               , comp.name           as name
               , em.employee_id      as employee_id
               , em.firstname        as employee_firstname
               , em.lastname         as employee_lastname
               , cert.name           as employee_certificate_name
               , cert.certificate_id as employee_certificate_id
               , em.employee_id      as employee_certificate_employee_id
          from certificate cert
          right join employee   em on cert.employee_id = em.employee_id
          right join company  comp on em.company_company_id = comp.company_id
          order by comp.company_id, em.employee_id, cert.certificate_id loop
          return next r;
          end loop;
          return;
    end if;  

end;'
language 'plpgsql';

--insert into resultset_info (specific_name, routine_resultset)
--values ('get_company_2x' , 'id bigint, name varchar, employee_id int, employee_firstname varchar, employee_lastname varchar, employee_certificate_name varchar, employee_certificate_id int, employee_certificate_employee_id int');