package app.server.persistencia;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.ChequeoProducto;
import app.client.dominio.Preventivo;
import app.client.dominio.Producto;


public class ChequeoProductoDao extends AbstractDao<ChequeoProducto, Integer> {

	public ChequeoProducto obtenerChequeoProducto(int idPreventivo, int idProducto) {
		return get(Restrictions.eq("preventivo", new Preventivo(idPreventivo)), Restrictions.eq("producto", new Producto(idProducto)));
	}

}
