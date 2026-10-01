delimiter $$

create procedure get_list_elements_failing ()
begin

    signal sqlstate '45000' set message_text = 'get_list_elements_failing: intentional failure';

end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_list_elements_failing'
       , 'name varchar, value varchar');

commit;
