delimiter $$

create procedure get_list_elements_report ()
begin

    select '2'              as columns,
           'name:varchar'   as name,
           'value:varchar'  as value
    union all
    select null as columns,
           name,
           value
      from list_elements;

end
$$

commit;
