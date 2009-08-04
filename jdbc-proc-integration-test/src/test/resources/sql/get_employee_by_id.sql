delimiter $$

create procedure get_employee_by_id (
     in i_id int(10)
)
begin
    select employee_id, firstname, lastname
      from employee
     where employee_id = i_id;
end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_employee_by_id', 'employee_id int, firstname varchar, lastname varchar');