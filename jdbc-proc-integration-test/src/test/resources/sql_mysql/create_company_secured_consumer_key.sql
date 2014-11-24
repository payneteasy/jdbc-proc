DELIMITER $$

CREATE PROCEDURE create_company_secured_consumer_key(i_consumer_key    VARCHAR(60),
                                                     OUT o_company_id  INT(10),
                                                     i_name            VARCHAR(60)
)
BEGIN

  IF i_consumer_key != 'consumer_key'
  THEN
    SET @v_ddl_statement = concat('call `|ERROR: illegal consumer key value|', i_consumer_key, '|`');
    PREPARE v_stmt FROM @v_ddl_statement;
    EXECUTE v_stmt;
    DEALLOCATE PREPARE v_stmt;
  END IF;

  INSERT INTO company (name) VALUES (i_name);
  SET o_company_id = last_insert_id();
END
$$
