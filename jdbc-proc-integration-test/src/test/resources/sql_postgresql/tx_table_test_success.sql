CREATE OR REPLACE FUNCTION tx_table_test_success()
RETURNS void AS'
  BEGIN
    insert into tx_table(pk_id) values (3);
  END;'
language 'plpgsql';
