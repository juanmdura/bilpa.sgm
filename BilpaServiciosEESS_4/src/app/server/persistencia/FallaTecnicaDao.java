package app.server.persistencia;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.FallaTecnica;
import app.client.dominio.TipoFallaTecnica;

public class FallaTecnicaDao extends AbstractDao<FallaTecnica, Integer>{

	public List <FallaTecnica> obtenerFallasTecnicasActivas(){
		return find(Restrictions.eq("inactiva", false));
	}
	
	public List <FallaTecnica> obtenerFallasTecnicasActivas(TipoFallaTecnica tipo){
		Criterion c1 = Restrictions.eq("inactiva", false);
		Criterion c2 = Restrictions.eq("subTipo", tipo.getId());
		//Criterion c3 = Restrictions.eq("tipoFalla", 1);
		
		return find(c1, c2);
	}
}
