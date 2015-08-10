drop procedure if exists report;
delimiter $$
create procedure report(i_procedure_name varchar(64))
  begin

  if i_procedure_name = 'report1' then

    select '4'                  as columns,
           'report_id:integer'  as report_id,
           'lastname:varchar'   as lastname,
           'firstname:varchar'  as firstname,
           'amount:decimal'     as amount 
    union all
    select null as columns,
           report_id,
           lastname,
           firstname,
           amount
      from report;

  else

    select '2'                  as columns,
           'report_id:integer'  as report_id,
           'amount:decimal'     as amount 
    union all
    select null as columns,
           report_id,
           amount
      from report;

  end if;

  end
  
$$

commit;
