package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.TipoActivoGenerico;;

public class TipoActivoGenericoDao extends AbstractDao<TipoActivoGenerico, Integer>{

	public List<TipoActivoGenerico> obtenerActivos() {
		return find(Restrictions.eq("activo", true));
	}

	public List<TipoActivoGenerico> getByNombre(String nombre) {
		return find(Restrictions.eq("nombre", nombre), Restrictions.eq("activo", true));
	}

}
