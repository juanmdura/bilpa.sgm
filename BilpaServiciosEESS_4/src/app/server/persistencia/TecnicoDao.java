package app.server.persistencia;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Tecnico;

public class TecnicoDao extends AbstractDao<Tecnico, Integer> {

	public Tecnico obtenerTecnicoPorNombreCompleto(String nombre,
			String apellido) {
		return get(Restrictions.eq("nombre", nombre), Restrictions.eq("apellido", apellido));
	}
}
