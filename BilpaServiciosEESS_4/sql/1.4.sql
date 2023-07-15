set sql_safe_updates = 0;

update pendientes p, pendientes_preventivos pp 
set p.preventivo = pp.preventivo, p.item_chequeado = pp.item_chequeado 
where p.id = pp.id;

drop table pendientes_preventivos;