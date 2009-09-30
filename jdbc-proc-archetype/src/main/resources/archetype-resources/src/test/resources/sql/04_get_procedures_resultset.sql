
create procedure get_procedures_resultset ()
begin
    select specific_name, routine_resultset from resultset_info;
end
