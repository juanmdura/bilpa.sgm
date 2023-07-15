use bilpa;

select distinct e.localidad, e.nombre as 'estacion', ea.id as 'idActivo', t.descripcion, pr.nombre, t.capacidad, v.id as 'idVisita', 
v.estado as 'estadoVisita', v.fecha_realizada, p.id
, td.nombre as 'tipoDeDescarga', ct.medida_del_agua, cc.texto as 'Comentario'
from visitas v
inner join preventivos p on p.visita = v.id
inner join empresas e on e.id = v.estacion
inner join empresas_activos ea on ea.empresa = v.estacion and ea.id = p.activo
inner join tanques t on t.id = p.activo
inner join productos pr on pr.id = t.producto
inner join chequeos c on c.id = p.chequeo
inner join chequeos_tanques ct on ct.id = c.id
left join tipos_descarga td on td.id = ct.tipo_de_descarga
inner join items_chequeados iados on iados.chequeo = c.id
inner join items_chequeos ieos on ieos.id = iados.item_chequeo
inner join comentarios_chequeos cc on cc.itemChequeado = iados.id
where v.fecha_realizada >= '2019-08-01'
and v.fecha_realizada < '2019-09-01'
and ea.tipoActivo = 2 -- Tanques
and e.sello = 2 -- ANCAP
order by v.fecha_realizada desc;