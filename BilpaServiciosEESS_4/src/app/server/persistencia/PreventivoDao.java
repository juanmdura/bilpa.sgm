package app.server.persistencia;

import java.util.Date;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Preventivo;

public class PreventivoDao extends AbstractDao<Preventivo, Integer> {

	public Preventivo get(int idVisita, int idActivo){
		return get(Restrictions.eq("activo.id", idActivo), Restrictions.eq("visita.id", idVisita));
	}

	public Date getUltimaModificacion(int idVisita, int idActivo){
		Preventivo preventivo = get(idVisita, idActivo);
		if(preventivo != null){
			return preventivo.getUltimaModificacion();
		}
		return null;
	}
}
