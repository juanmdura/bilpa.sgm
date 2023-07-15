package app.server.persistencia;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.Corregido;
import app.client.dominio.ItemChequeado;
import app.client.dominio.Pendiente;


public class ItemChequeadoDao extends AbstractDao<ItemChequeado, Integer> {

	public List<ItemChequeado> getByPendiente(Pendiente pendiente) {
		Criteria c = getSession().createCriteria(ItemChequeado.class, "ic");
		c.createAlias("ic.listaDePendientes", "pendiente");
		c.add(Restrictions.eq("pendiente.id", pendiente.getId()));
		return c.list();
	}
	
	public List<ItemChequeado> getByCorregido(Corregido corregido) {
		Criteria c = getSession().createCriteria(ItemChequeado.class, "ic");
		c.createAlias("ic.listaDeCorregidos", "corregido");
		c.add(Restrictions.eq("corregido.id", corregido.getId()));
		return c.list();
	}
}
