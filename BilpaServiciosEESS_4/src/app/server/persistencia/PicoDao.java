package app.server.persistencia;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.Pico;
import app.client.dominio.QR;

public class PicoDao extends AbstractDao<Pico, Integer>{

	public List<Pico> getPicos(int idSurtidor) {
		Order desc = Order.asc("numeroPico");
		Criteria c = createCriteria();
		c.addOrder(desc);
		c.add(Restrictions.eq("surtidor.id", idSurtidor));
		
		return c.list();
	}
	
	public Pico get(int idSurtidor, int numeroPico) {
		return get(Restrictions.eq("surtidor.id", idSurtidor), Restrictions.eq("numeroPico", numeroPico));
	}

	public Pico get(QR qr) {
		return get(Restrictions.eq("qr", qr));
	}
	
}
