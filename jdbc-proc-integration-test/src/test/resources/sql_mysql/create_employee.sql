delimiter $$

create procedure create_employee (
     out o_employee_id int(10)
   , i_firstname varchar(60)
   , i_lastname varchar(60)
   , i_company_company_id int(10)
)
begin
    insert into employee( firstname, lastname, company_company_id ) values (i_firstname, i_lastname, i_company_company_id);
    set o_employee_id = last_insert_id();
end
$$
