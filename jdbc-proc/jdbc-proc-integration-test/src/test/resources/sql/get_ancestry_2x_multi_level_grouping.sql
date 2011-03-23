delimiter $$

create procedure get_ancestry_2x_multi_level_grouping ()
begin

	select
	   g.name name,
	   f.name son_name,
	   b.name son_son_name
	from grandfather g
	   join father f on f.grandfather_id = g.grandfather_id
	   join boy b on b.father_id = f.father_id
	order by g.grandfather_id, f.father_id;

end
$$

insert into resultset_info (specific_name, routine_resultset)
values ('get_ancestry_2x_multi_level_grouping'
       , 'name varchar, son_name varchar, son_son_name varchar');