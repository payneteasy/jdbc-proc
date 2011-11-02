CREATE OR REPLACE FUNCTION create_chalk_bag(
    out o_chalk_bag_id bigint,
    i_chalk_bag_name varchar(64),
    i_color varchar(64),
    i_materials varchar(128)
)AS'
BEGIN
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

    SELECT last_value into o_chalk_bag_id from chalk_bags_seq;
   
  end;'
language 'plpgsql';