package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.ComentarioChequeo;
import app.client.dominio.ItemChequeado;

public class ComentarioChequeoDao extends AbstractDao<ComentarioChequeo, Integer>{

	public List<ComentarioChequeo> getByItemChequeado(ItemChequeado itemChequeado) {
		return find(Restrictions.eq("itemChequeado", itemChequeado), Restrictions.eq("activo", true));
	}
}
