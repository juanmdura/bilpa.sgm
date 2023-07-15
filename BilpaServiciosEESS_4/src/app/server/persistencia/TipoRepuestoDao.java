package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.TipoRepuesto;

public class TipoRepuestoDao extends AbstractDao<TipoRepuesto, Integer>{

	public List<TipoRepuesto> obtenerRepuestosActivos() {
		return find(Restrictions.eq("inactivo", false));
	}
}
