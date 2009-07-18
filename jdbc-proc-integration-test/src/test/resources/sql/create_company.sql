delimiter $$

create procedure create_company (
     out o_id int(10)
   , i_name varchar(60)
)
begin
    insert into company( name ) values (i_name);
    set o_id = last_insert_id();
end
$$
