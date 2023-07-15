package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.FallaReportada;
import app.client.dominio.FallaTecnica;
import app.client.dominio.TipoFallaReportada;
import app.client.dominio.TipoFallaTecnica;



public class FallaReportadaDao extends AbstractDao<FallaReportada, Integer>{
	
	public List <FallaReportada> obtenerFallasPorTipo(){
		return find(Restrictions.eq("tipo", 2));
	}
	
	public List <FallaReportada> obtenerFallasReportadasActivas(){
		return find(Restrictions.eq("inactiva", false));
	}
	
	public List <FallaReportada> obtenerFallasReportadasActivas(int idTipo){
		Criterion c1 = Restrictions.eq("inactiva", false);
		if (idTipo > 0){
			Criterion c2 = Restrictions.eq("subTipo", idTipo);
			return find(c1, c2);
		} else {
			return find(c1);
		}
	}
}
