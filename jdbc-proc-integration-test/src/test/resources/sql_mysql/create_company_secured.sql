delimiter $$

create procedure create_company_secured (
          i_username    varchar(60)
   ,      i_principal   varchar(60)
   ,  out o_company_id  int(10)
   ,      i_name        varchar(60)
)
begin
    insert into company( name ) values (i_name);
    set o_company_id = last_insert_id();
end
$$
