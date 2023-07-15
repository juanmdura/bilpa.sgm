use bilpa;

set sql_safe_updates = 0;
SET FOREIGN_KEY_CHECKS=1;

update empresas e 
set e.id = 477
where e.id = 0;

SELECT * FROM empresas
order by id desc;

SELECT * FROM empresas
where id = 0;

SELECT * FROM empresas_activos
where empresa = 0;

SELECT * FROM ordenes
where empresa = 0;

SELECT * FROM visitas
where estacion = 0;