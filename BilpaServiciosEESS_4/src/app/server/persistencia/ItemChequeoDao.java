package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.ItemChequeo;
import app.client.dominio.TipoActivoGenerico;
import app.client.dominio.TipoChequeo;
import app.client.dominio.data.TipoActivoGenericoData;

public class ItemChequeoDao extends AbstractDao<ItemChequeo, Integer> {

	public ItemChequeo get(String nombre) {
		return get(Restrictions.eq("nombre", nombre));
	}

	public List<ItemChequeo> getPorTipo(TipoChequeo tipoChequeo, TipoActivoGenerico tipoActivoGenerico) {
		if (tipoChequeo.getTipoChequeo().equals(TipoChequeo.Generico.name().toUpperCase()) && tipoActivoGenerico != null){
			return find(Restrictions.eq("tipoChequeo", tipoChequeo), Restrictions.eq("tipoActivoGenerico", tipoActivoGenerico));
		} else {
			return find(Restrictions.eq("tipoChequeo", tipoChequeo));
		}
	}

	public List<ItemChequeo> getByTipoData(TipoActivoGenericoData tipo) {
		return find(Restrictions.eq("tipoActivoGenerico", new TipoActivoGenerico(tipo.getId())), Restrictions.eq("activo", true));
	}

	public List<ItemChequeo> getByNombre(String nombre) {
		return find(Restrictions.eq("nombre", nombre), Restrictions.eq("activo", true));
	}
}
