delimiter $$

create procedure get_companies_names_secured (
          i_username    varchar(60)
   ,      i_principal   varchar(60)
)
begin

    select name from company order by name;

end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_companies_names_secured'
       , 'name varchar');