CREATE OR REPLACE FUNCTION create_company_secured_consumer_key(
        i_consumer_key VARCHAR(60)
  , OUT o_company_id   BIGINT
  ,     i_name         VARCHAR(60))
AS '
BEGIN INSERT INTO company (name) VALUES (i_name);
  SELECT
    last_value
  INTO o_company_id
  FROM company_seq;
END;'
LANGUAGE plpgsql;
