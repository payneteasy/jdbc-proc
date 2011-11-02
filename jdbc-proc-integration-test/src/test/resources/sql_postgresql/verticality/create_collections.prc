CREATE OR REPLACE FUNCTION create_collections()
RETURNS void as'
  BEGIN
	  if (select count(*) from information_schema.tables where 
            table_catalog = CURRENT_CATALOG and table_schema = CURRENT_SCHEMA
            and table_name = ''carabiners'')=0 
      then
        CREATE TEMP TABLE carabiners(
          name             varchar(64),
          weight           decimal(5, 2), 
          bs_major_axis    integer,
          bs_open_gate     integer,
          bs_cross_loaded  integer,
          gate_opening     integer
          );
    end if;
          
	  if not exists(select * from information_schema.tables where 
            table_catalog = CURRENT_CATALOG and table_schema = CURRENT_SCHEMA
            and table_name = ''dynamic_ropes'') 
      then
        CREATE TEMPORARY TABLE dynamic_ropes(
          name         varchar(64),
          diameter     decimal(5,2),
          lengths      integer,
          type         varchar(64),
          weight       decimal(5,2)
          );
    end if;
            
    if not exists(select * from information_schema.tables where 
            table_catalog = CURRENT_CATALOG and table_schema = CURRENT_SCHEMA
            and table_name = ''harnesses'') 
      then
        CREATE TEMPORARY TABLE harnesses(
          name      varchar(64),
          weight    decimal(5, 2), 
          color     varchar(32),
          size      varchar(32)
          );
    end if;      

    if not exists(select * from information_schema.tables where 
            table_catalog = CURRENT_CATALOG and table_schema = CURRENT_SCHEMA
            and table_name = ''list_elements'') 
      then
        CREATE TEMPORARY TABLE list_elements(
          id        bigint,
          name      varchar(32),
          value     varchar(32)
          );
    end if;      

    RETURN;
  END;'
  language 'plpgsql';

/*
  CREATE TEMP TABLE carabiners(
          name             varchar(64),
          weight           decimal(5, 2), 
          bs_major_axis    integer,
          bs_open_gate     integer,
          bs_cross_loaded  integer,
          gate_opening     integer
          );
    
        CREATE TEMPORARY TABLE dynamic_ropes(
          name         varchar(64),
          diameter     decimal(5,2),
          lengths      integer,
          type         varchar(64),
          weight       decimal(5,2)
          );
    
        CREATE TEMPORARY TABLE harnesses(
          name      varchar(64),
          weight    decimal(5, 2), 
          color     varchar(32),
          size      varchar(32)
          );
          */