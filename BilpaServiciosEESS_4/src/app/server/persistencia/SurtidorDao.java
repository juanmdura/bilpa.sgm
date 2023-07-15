package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Estacion;
import app.client.dominio.ModeloSurtidor;
import app.client.dominio.Surtidor;

public class SurtidorDao extends AbstractDao<Surtidor, Integer>{

	public Surtidor obtenerSurtidor(String numeroSerie){
		return get(Restrictions.eq("numeroSerie", numeroSerie));
	}
	
	public List<Surtidor> obtenerSurtidoresPorEstacion(Estacion e){
		return find(Restrictions.eq("empresa", e),Restrictions.eq("tipo", 1));
	}

	public List<Surtidor> obtenerSurtidoresPorModelo(int idModelo) {
		return find(Restrictions.eq("modelo", new ModeloSurtidor(idModelo)),Restrictions.eq("tipo", 1));
	}
}

