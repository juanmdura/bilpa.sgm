package app.server.persistencia;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Marca;

public class MarcaDao extends AbstractDao<Marca, Integer>{

	public Marca buscarMarca(String nombre){
		return get(Restrictions.eq("nombre", nombre));
	}
}
