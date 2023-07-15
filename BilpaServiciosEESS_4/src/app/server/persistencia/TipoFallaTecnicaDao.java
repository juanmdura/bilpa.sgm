package app.server.persistencia;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.TipoFallaTecnica;

public class TipoFallaTecnicaDao extends AbstractDao<TipoFallaTecnica, Integer>{

	public List <TipoFallaTecnica> obtenerTipoFallasTecnicasActivas(){
		return find(Restrictions.eq("inactiva", false));
	}
	
}
