package app.server.persistencia;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import app.client.dominio.Tarea;
import app.client.dominio.TipoTarea;

public class TareaDao extends AbstractDao<Tarea, Integer>{
	
	public List <Tarea> obtenerTareasActivas(){
		return find(Restrictions.eq("inactiva", false));
	}

	public List<Tarea> obtenerTareas(TipoTarea tipo) {
		return find(Restrictions.eq("tipoTarea", tipo));
	}
}
