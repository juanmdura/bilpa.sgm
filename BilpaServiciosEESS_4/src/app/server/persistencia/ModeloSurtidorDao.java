package app.server.persistencia;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.Marca;
import app.client.dominio.Modelo;
import app.client.dominio.ModeloSurtidor;

public class ModeloSurtidorDao extends AbstractDao<ModeloSurtidor, Integer>{

	public Modelo buscarModelo(String nombre, Marca marca){
		return get(Restrictions.eq("nombre", nombre), Restrictions.eq("marca", marca));
	}

	public List<ModeloSurtidor> obtenerModelos(String nombre, int idMarca, int idModeloExistente) {
		if(idModeloExistente > 0){
			return find(Restrictions.eq("nombre", nombre), Restrictions.eq("marca", new Marca(idMarca)), Restrictions.not(Restrictions.eq("id", idModeloExistente)));	
		}
		return find(Restrictions.eq("nombre", nombre), Restrictions.eq("marca", new Marca(idMarca)));
	}
	
	public List<ModeloSurtidor> modelosOrdenados() {
		Criteria criteria = getSession( ).createCriteria(ModeloSurtidor.class);
		criteria.addOrder(Order.asc( "marca" )).addOrder(Order.asc("nombre"));

		return criteria.list();
	}
}

