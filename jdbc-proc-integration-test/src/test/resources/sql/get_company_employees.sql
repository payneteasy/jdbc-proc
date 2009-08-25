delimiter $$

create procedure get_company_employees (
     in i_company_id int(10)
)
begin
    select   comp.company_id as company_id
           , comp.name       as name
           , em.employee_id  as employee_employee_id
           , em.firstname    as employee_firstname
           , em.lastname     as employee_lastname
      from employee em
      left join company comp on em.company_company_id = comp.company_id
     where comp.company_id = i_company_id;
end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_company_employees'
       , 'company_id int, name varchar, employee_employee_id int, employee_firstname varchar, employee_lastname varchar');