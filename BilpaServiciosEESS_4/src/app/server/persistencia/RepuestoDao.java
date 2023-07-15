package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Repuesto;
import app.client.dominio.TipoRepuesto;

public class RepuestoDao extends AbstractDao<Repuesto, Integer>{
	
	public List <Repuesto> obtenerRepuestosActivos(){
		return find(Restrictions.eq("inactiva", false));
	}

	public List<Repuesto> obtenerRepuestos(TipoRepuesto tipo) {
		return find(Restrictions.eq("tipoRepuesto", tipo));
	}
}
