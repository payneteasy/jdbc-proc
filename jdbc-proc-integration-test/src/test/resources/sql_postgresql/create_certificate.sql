CREATE OR REPLACE FUNCTION create_certificate (
     out o_id bigint
   , i_name varchar(60)
   , i_employee_id bigint
) AS'
begin
    insert into certificate( name, employee_id ) values (i_name, i_employee_id);
    select last_value into o_id from certificate_seq;
end;'	
language 'plpgsql';
