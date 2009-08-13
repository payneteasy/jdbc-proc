delimiter $$

create procedure get_employee_by_id (
     in i_id int(10)
)
begin
    select em.employee_id as employee_id, em.firstname as firstname, em.lastname as lastname
           , comp.company_id as company_company_id, comp.name as company_name
      from employee em
      left join company comp on em.company_company_id = comp.company_id
     where em.employee_id = i_id;
end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_employee_by_id', 'employee_id int, firstname varchar, lastname varchar, company_company_id int, company_name varchar');