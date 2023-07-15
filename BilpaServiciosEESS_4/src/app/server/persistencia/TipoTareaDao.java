package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.TipoTarea;

public class TipoTareaDao extends AbstractDao<TipoTarea, Integer>{

	public List<TipoTarea> obtenerTareasActivas() {
		return find(Restrictions.eq("inactiva", false));
	}
	
}
