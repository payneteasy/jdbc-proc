
create procedure get_companies ()
begin

    select company_id, name from company;

end

/

insert into resultset_info (specific_name, routine_resultset)
values ('get_companies'
       , 'company_id int, name varchar');