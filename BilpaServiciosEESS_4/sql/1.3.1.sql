/* 
select e.nombre as 'Nombre EE.SS' , e.localidad as 'Localidad', ma.nombre as 'Marca', mo.nombre as 'Modelo', s.serialNumber as 'Serie', 
ms.cantidadDePicos as 'Cantidad de mangueras', p.numero_en_la_estacion as 'Número de manguera', pr.nombre as 'Producto'
from empresas e
inner join empresas_activos ea on ea.empresa = e.id 
inner join surtidores s on s.id = ea.id
inner join modelos mo on mo.id = s.modelo
inner join marcas ma on ma.id = mo.marca
inner join picos p on p.surtidor = s.id
inner join productos pr on pr.id = p.producto
inner join modelos_surtidores ms on ms.modelo = mo.id
where ea.tipoActivo = 1
and e.sello = 2

select e.nombre as 'Nombre EE.SS' , e.localidad as 'Localidad', t.capacidad as 'Capacidad', pr.nombre as 'Producto'
from empresas e
inner join empresas_activos ea on ea.empresa = e.id 
inner join tanques t on t.id = ea.id
inner join productos pr on pr.id = t.producto
where ea.tipoActivo = 2
and e.sello = 2
*/

insert into sellos values (6, 'ANCAP CONTRATOS');
insert into tipos_activos values (6, 'Genericos');
insert into tipos_reparaciones values (6, 'Reparacion Activo Generico');
