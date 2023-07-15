package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Corregido;
import app.client.dominio.Solucion;

public class SolucionDao extends AbstractDao<Solucion, Integer>{

	public List<Solucion> obtenerPorCorregido(Corregido corregido) {
		return find(Restrictions.eq("corregido", corregido));
	}

}
