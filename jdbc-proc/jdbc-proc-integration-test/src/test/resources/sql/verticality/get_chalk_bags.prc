drop procedure if exists get_chalk_bags;
delimiter $$
create procedure get_chalk_bags()
  begin
    select chalk_bag_id, chalk_bag_name, color, materials
      from chalk_bags;
  end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_chalk_bags', 'chalk_bag_id bigint, chalk_bag_name varchar, color varchar, materials varchar');
