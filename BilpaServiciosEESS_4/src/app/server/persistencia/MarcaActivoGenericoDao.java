package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.TipoActivoGenerico;
import app.client.dominio.data.TipoActivoGenericoData;

public class MarcaActivoGenericoDao extends AbstractDao<MarcaActivoGenerico, Integer>{
	public List<MarcaActivoGenerico> getByTipoData(TipoActivoGenericoData tipo) {
		return find(Restrictions.eq("tipo", new TipoActivoGenerico(tipo.getId())), Restrictions.eq("activo", true));
	}
	
	public List<MarcaActivoGenerico> getByTipo(TipoActivoGenerico tipo) {
		return find(Restrictions.eq("tipo", tipo), Restrictions.eq("activo", true));
	}

	public List<MarcaActivoGenerico> getByNombre(MarcaActivoGenerico param) {
		return find(Restrictions.eq("nombre", param.getNombre()), Restrictions.eq("activo", true));
	}
}
