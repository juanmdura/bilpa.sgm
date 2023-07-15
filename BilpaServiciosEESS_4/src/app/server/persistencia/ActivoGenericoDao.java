package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.ActivoGenerico;
import app.client.dominio.Estacion;

public class ActivoGenericoDao extends AbstractDao<ActivoGenerico, Integer>{
	public List<ActivoGenerico> getBySerie(String serie) {
		return find(Restrictions.eq("serie", serie));
	}

	public List<ActivoGenerico> getByEstacion(int idEstacion) {
		return find(Restrictions.eq("empresa", new Estacion(idEstacion)));
	}

	public List<ActivoGenerico> getById(int id) {
		return find(Restrictions.eq("id", id));
	}
}
