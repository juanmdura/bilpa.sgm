use bilpa;
select distinct e.localidad, e.nombre as 'estacion', 
ea.id as 'idActivo', ma.nombre as 'marca', m.nombre as 'modeloSurtidor',  s.serialNumber, 
v.id as 'idVisita', v.estado as 'estadoVisita', v.fecha_realizada/*, 
ieos.texto as 'Chequeo', cc.texto as 'Comentario'*/, 
pr.nombre, pi.numeroPico, pi.numero_en_la_estacion,
/*cal.calibre1, cal.calibre2, cal.calibre3, 
pre.remplazado, pre.numero, pre.numeroViejo,
cal.calibre4, cal.calibre5, cal.calibre6, */
cp.caudal,
cp.totalizador_mecanico_inicial, cp.totalizador_mecanico_final, cp.totalizador_electronico_inicial, cp.totalizador_electronico_final
from visitas v
inner join empresas e on e.id = v.estacion
inner join empresas_activos ea on ea.empresa = e.id
inner join surtidores s on s.id = ea.id
inner join modelos m on m.id = s.modelo
inner join marcas ma on ma.id = m.marca
inner join preventivos p on p.visita = v.id
inner join chequeos c on c.id = p.chequeo
inner join chequeos_surtidores cs on cs.id = c.id
inner join chequeos_picos cp on cp.chequeo_surtidor = cs.id
inner join picos pi on pi.id = cp.pico
inner join productos pr on pr.id = pi.producto
-- left join precintos pre on pre.id = cp.precinto
-- left join calibres cal on cal.id = cp.calibre
-- inner join items_chequeados iados on iados.chequeo = c.id
-- inner join items_chequeos ieos on ieos.id = iados.item_chequeo
-- left join comentarios_chequeos cc on cc.itemChequeado = iados.id
where v.fecha_realizada >= '2019-07-01'
and v.fecha_realizada < '2019-08-01'
and ea.tipoActivo = 1 -- Surtidores
and e.sello = 2 -- ANCAP
order by ea.id asc
