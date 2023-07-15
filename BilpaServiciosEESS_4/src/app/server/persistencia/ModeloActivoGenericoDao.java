package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.ModeloActivoGenerico;
import app.client.dominio.TipoActivoGenerico;


public class ModeloActivoGenericoDao extends AbstractDao<ModeloActivoGenerico, Integer>{
	public List<ModeloActivoGenerico> getByTipo(TipoActivoGenerico tipo) {
		return find(Restrictions.eq("tipo", tipo), Restrictions.eq("activo", true));
	}
	
	public List<ModeloActivoGenerico> getByMarca(MarcaActivoGenerico marca) {
		return find(Restrictions.eq("marca", marca), Restrictions.eq("activo", true));
	}

	public List<ModeloActivoGenerico> getByNombre(String nombre) {
		return find(Restrictions.eq("nombre", nombre), Restrictions.eq("activo", true));
	}
}
