CREATE OR REPLACE FUNCTION tx_table_test()
RETURNS void AS'
	BEGIN
	    insert into tx_table(pk_id) values (4);
	    insert into tx_table(pk_id) values (1);
	    insert into tx_table(pk_id) values (1);
	    insert into tx_table(pk_id) values (2);
	END;'
language 'plpgsql';