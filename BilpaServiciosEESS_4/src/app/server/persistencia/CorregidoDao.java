package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Corregido;
import app.client.dominio.Preventivo;

public class CorregidoDao extends AbstractDao<Corregido, Integer>{

	public List<Corregido> getCorregidos(int idPreventivo) {
		return find(Restrictions.eq("preventivo", new Preventivo(idPreventivo)));
	}
}
