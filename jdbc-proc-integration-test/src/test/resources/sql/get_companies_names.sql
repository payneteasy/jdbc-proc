delimiter $$

create procedure get_companies_names ()
begin

    select name from company order by name;

end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_companies_names'
       , 'name varchar');