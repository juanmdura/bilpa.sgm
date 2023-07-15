package app.server.persistencia;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.QR;


public class QrDao extends AbstractDao<QR, Integer>{

	public QR get(String codigo) {
		return get(Restrictions.eq("codigo", codigo));
	}
	
}
