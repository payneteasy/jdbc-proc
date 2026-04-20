delimiter $$

create procedure sleep_proc (
    i_seconds int
)
begin
    declare v int default 0;
    -- SELECT ... INTO so that max_statement_time (used by MariaDB driver to
    -- implement JDBC setQueryTimeout) can actually interrupt the call.
    select sleep(i_seconds) into v;
end
$$
