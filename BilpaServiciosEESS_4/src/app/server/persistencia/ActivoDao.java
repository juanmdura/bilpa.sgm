package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Activo;
import app.client.dominio.Estacion;
import app.client.dominio.QR;


public class ActivoDao extends AbstractDao<Activo, Integer>{
	//public List <Activo> obtenerActivos(){
		//return find(Restrictions.eq("tipo", 1));
	//}
	
	public List <Activo> obtenerActivosEmpresa(Estacion empresa){
		return find(Restrictions.eq("empresa", empresa));
	}

	public Activo get(QR qr) {
		return get(Restrictions.eq("qr", qr));
	}
}
