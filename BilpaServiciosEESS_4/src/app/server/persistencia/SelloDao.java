package app.server.persistencia;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Sello;

public class SelloDao extends AbstractDao<Sello, Integer>{
	
	public Sello obtenerObjSello(String nombreSello){
		return get(Restrictions.eq("nombre", nombreSello));
	}
 
}
