drop procedure if exists upload_harnesses;
delimiter $$
create procedure upload_harnesses()
 main_sql:
  begin
    insert into storage_harnesses
          (
             name, weight, color, size
          )
      select name,
             weight, 
             color,
             size
        from harnesses;

    truncate table harnesses;
  end
$$
delimiter ;