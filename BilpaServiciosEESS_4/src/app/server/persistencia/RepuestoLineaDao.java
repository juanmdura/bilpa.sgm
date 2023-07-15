package app.server.persistencia;


import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Activo;
import app.client.dominio.Orden;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;

public class RepuestoLineaDao extends AbstractDao<RepuestoLinea, Integer>{

	public List <RepuestoLinea> obtenerRepuestosOrden(Orden orden){
		return find(Restrictions.eq("orden", orden));
	}

	public List<RepuestoLinea> obtenerRepuestosLinea(Orden o, Activo a) {
		return find(Restrictions.eq("orden", o), Restrictions.eq("activo", a));
	}

	public List<RepuestoLinea> obtenerRepuestosLinea(Solucion s) {
		return find(Restrictions.eq("solucion", s));
	}
}
