delimiter $$

create procedure create_employee (
     out o_employee_id int(10)
   , i_firstname varchar(60)
   , i_lastname varchar(60)
)
begin
    insert into employee( firstname, lastname ) values (i_firstname, i_lastname);
    set o_employee_id = last_insert_id();
end
$$
