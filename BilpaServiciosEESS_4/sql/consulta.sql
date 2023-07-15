use bilpa;

-- Correctivos que fueron llamados de Ducsa en Julio
select o.*
from empresas e
inner join ordenes o on o.empresa = e.id
where e.sello = 2 -- DUCSA
and o.tipoTrabajo <> 2 -- PREVENTIVOS
and o.inicioService >= '2017-07-01 00:00:00'
and o.finService <= '2017-07-31 23:59:59'

-- preventivos
select c.id, f.descripcion as 'Falla Detectada', t.descripcion as 'Tarea', r.descripcion as 'Repuesto', c.comentario as 'Comentario', v.fecha_realizada  as 'Fecha Cierre'
from corregidos c
inner join preventivos p on p.id = c.preventivo
inner join visitas v on v.id = p.visita
inner join empresas e on e.id = v.estacion
inner join tareas t on t.id = c.tarea
inner join fallas f on f.id = c.falla_tecnica 
inner join repuestos_corregidos rc on rc.corregido = c.id
inner join repuestos r on r.id = rc.repuesto
where e.sello = 2 -- Ducsa
and v.fecha_realizada >= '2019-09-01 00:00:00'
and v.fecha_realizada <= '2019-09-30 00:00:00'
;

-- correctivos
select s.id, fr.descripcion as 'Falla Reportada', fd.descripcion as 'Falla Detectada', t.descripcion as 'Tarea', rep.descripcion as 'Repuesto', c.texto as 'Comentario', o.finService as 'Fecha Cierre'
from soluciones s
inner join comentarios c on c.id = s.comentario
inner join reparaciones r on r.id = s.reparacion
inner join ordenes o on o.numero = r.orden
inner join empresas e on e.id = o.empresa
inner join tareas t on t.id = s.tarea
inner join fallas fd on fd.id = s.fallaTecnica 
inner join fallas fr on fr.id = r.fallaReportada
inner join orden_repuestos orep on orep.orden = o.numero
inner join repuestos rep on rep.id = orep.repuesto
where e.sello = 2 -- Ducsa
and o.inicioService >= '2019-09-01 00:00:00'
and o.finService <= '2019-09-30 00:00:00'
;