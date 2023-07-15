package app.server.persistencia;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.Activo;
import app.client.dominio.Orden;
import app.client.dominio.Pendiente;
import app.client.dominio.Reparacion;

public class ReparacionDao extends AbstractDao<Reparacion, Integer>{

	public List <Reparacion> obtenerTodosLasReparaciones(Orden o){
		return find(Restrictions.eq("orden", o));
	}

	public List <Reparacion> obtenerTodosLasReparacionesDeUnActivo(Activo a){
		return find(Restrictions.eq("activo", a));
	}

	public List<Reparacion> obtenerReparacionesConFallaReportada(int numero) {
		return find(Restrictions.eq("orden", new Orden(numero)), Restrictions.isNotNull("fallaReportada"));
	}

	public List<Reparacion> obtenerReparacionesDeActivoEnOrden(int numero, Activo activo) {
		return find(Restrictions.eq("orden", new Orden(numero)), Restrictions.eq("activo", activo));
	}

	public List<Reparacion> obtenerReparacionPorOrdenActivo(Orden orden, Activo activo){		
		return find(Restrictions.eq("orden", new Orden(orden.getNumero())), Restrictions.eq("activo", activo));
	}

	public Reparacion obtenterReparacion(int idSolucion) {
		try {
			Criteria c = createCriteria();
			c.createAlias("soluciones", "solucion");
			c.add(Restrictions.eq("solucion.id", idSolucion));
			Object objectReparacion = c.uniqueResult();
			return (Reparacion) objectReparacion;
		} catch (Exception e) {
			return null;
		}
	}

	public Reparacion obtenerReparacionPorPendiente(int idPendiente) {
		return get(Restrictions.eq("pendiente", new Pendiente(idPendiente)));
	}
}
