delimiter $$

create procedure get_company_2x (
     in i_company_id int(10)
)
begin

    if i_company_id is not null then
        select   comp.company_id as id
               , comp.name       as name
               , em.employee_id  as employee_id
               , em.firstname    as employee_firstname
               , em.lastname     as employee_lastname
          from employee em
          left join company comp on em.company_company_id = comp.company_id
         where comp.company_id = i_company_id;
    else
        select   comp.company_id as id
               , comp.name       as name
               , em.employee_id  as employee_id
               , em.firstname    as employee_firstname
               , em.lastname     as employee_lastname
          from employee em
          left join company comp on em.company_company_id = comp.company_id
          order by comp.company_id, em.employee_id;
    end if;

end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_company_2x'
       , 'id int, name varchar, employee_id int, employee_firstname varchar, employee_lastname varchar');