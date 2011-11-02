CREATE OR REPLACE FUNCTION upload_harnesses()
RETURNS void AS' 
  BEGIN

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
  END;'
language 'plpgsql';