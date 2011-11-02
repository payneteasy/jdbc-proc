CREATE OR REPLACE FUNCTION create_company_secured(
		i_username    varchar(60)
   ,	i_principal   varchar(60)
   ,	out o_company_id bigint
   , 	i_name varchar(60))
	as '
BEGIN
    insert into company (name) values (i_name);    
   	SELECT last_value into o_company_id from company_seq;
END;'
language plpgsql;
