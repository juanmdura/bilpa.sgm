package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.TipoFallaReportada;



public class TipoFallaReportadaDao extends AbstractDao<TipoFallaReportada, Integer>{
	
	
	public List <TipoFallaReportada> obtenerTipoFallasReportadasActivas(){
		return find(Restrictions.eq("inactiva", false));
	}
	
	
}
