CREATE OR REPLACE FUNCTION create_employee (
     out o_employee_id bigint
   , i_firstname varchar(60)
   , i_lastname varchar(60)
   , i_company_company_id bigint
) as '
begin
    insert into employee( firstname, lastname, company_company_id ) values (i_firstname, i_lastname, i_company_company_id);
    SELECT last_value into o_employee_id from employee_seq;
end;'
language 'plpgsql';