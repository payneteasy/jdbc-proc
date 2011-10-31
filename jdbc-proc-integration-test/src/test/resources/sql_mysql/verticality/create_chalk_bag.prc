drop procedure if exists create_chalk_bag;
delimiter $$
create procedure create_chalk_bag(out o_chalk_bag_id bigint(20),
                                        i_chalk_bag_name varchar(64),
                                        i_color varchar(64),
                                        i_materials varchar(128)
)
  begin
    insert into chalk_bags
          (
             chalk_bag_name,
             color,
             materials
          )
    values (
             i_chalk_bag_name,
             i_color,
             i_materials
           );

    set o_chalk_bag_id = last_insert_id();
    commit;

    select 'OK' status, null error_message;
  end
$$
delimiter ;