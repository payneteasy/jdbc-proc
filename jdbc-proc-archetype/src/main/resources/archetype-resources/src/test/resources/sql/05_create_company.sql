create procedure create_company (
     out o_company_id int(10)
   , i_name varchar(60)
)
begin
    insert into company( name ) values (i_name);
    set o_company_id = last_insert_id();
end
