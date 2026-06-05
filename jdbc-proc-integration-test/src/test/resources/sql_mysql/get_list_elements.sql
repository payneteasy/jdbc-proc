delimiter $$

create procedure get_list_elements ()
begin

    select name, value from list_elements;

end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_list_elements'
       , 'name varchar, value varchar');

commit;
