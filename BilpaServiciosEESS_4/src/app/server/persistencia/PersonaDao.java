package app.server.persistencia;

import org.hibernate.criterion.Restrictions;
import app.client.dominio.Persona;

public class PersonaDao extends AbstractDao<Persona, Integer>{
	
	public Persona obtenerUsuario(String usuario){
		return get(Restrictions.eq("nombreDeUSuario", usuario));
	}

}
