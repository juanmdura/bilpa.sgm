package app.server.persistencia;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.CantidadOrdenes;
import app.client.dominio.Persona;

public class CantidadOrdenesDao  extends AbstractDao<CantidadOrdenes, Integer>{

	public CantidadOrdenes obtenerCantidadOrdenes(Persona usuario){
		return get(Restrictions.eq("usuario", usuario));
	}
}
