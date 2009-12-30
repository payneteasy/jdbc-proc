delimiter $$

create procedure create_certificate (
     out o_id int(10)
   , i_name varchar(60)
   , i_employee_id int(10)
)
begin
    insert into certificate( name, employee_id ) values (i_name, i_employee_id);
    set o_id = last_insert_id();
end
$$
