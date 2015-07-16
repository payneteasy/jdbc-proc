drop procedure if exists create_collections;
delimiter $$
create procedure create_collections()
  begin
	  -- Locking carabiners
    create temporary table if not exists carabiners(
      name             varchar(64),
      weight           decimal(5, 2), 
      bs_major_axis    int(10),
      bs_open_gate     int(10),
      bs_cross_loaded  int(10),
      gate_opening     int(10)
      )
      engine = memory;
      
	  -- Dynamic ropes
    create temporary table if not exists dynamic_ropes(
      name         varchar(64),
      diameter     decimal(5,2),
      lengths      int(10),
      type         varchar(64),
      weight       decimal(5,2)
      )
      engine = memory;

      -- Harnesses
    create temporary table if not exists harnesses(
      name      varchar(64),
      weight    decimal(5, 2), 
      color     varchar(32),
      size      varchar(32)
      )
      engine = memory;

    create temporary table if not exists list_elements(
      id        int(10),
      name      varchar(32),
      value     varchar(32)
    ) engine = memory;

    create temporary table if not exists list_elements2(
      id        int(10),
      name      varchar(32),
      value     varchar(32)
    ) engine = memory;

    select 1;
  end
$$
delimiter ;
