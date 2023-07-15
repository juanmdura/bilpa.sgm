package app.server.persistencia;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.EmailEmpresa;
import app.client.dominio.Estacion;
import app.client.dominio.Sello;


public class EstacionDao extends AbstractDao<Estacion, Integer>{

	public List <Estacion> obtenerEstacionesConOrdenesActivas(){
		return find(Restrictions.eq("estado", 1));
	}

	public List<Estacion> obtenerEmpresasOrdenadas( ) 
	{
		Criteria criteria = getSession( ).createCriteria(Estacion.class);
		criteria.addOrder(Order.asc( "nombre" ));

		return criteria.list();
	}
	
	public List <Estacion> estacionesPorSello(Sello sello){
		return find(Restrictions.eq("sello", sello));
	}
	
	public List<Estacion> estacionesPorZona(Sello sello, int zona){
		return find(Restrictions.eq("sello", sello),Restrictions.eq("zona", zona));
	}
	
	public Estacion estacionPorNombre(String nombre){
		return get(Restrictions.eq("nombre", nombre));
	}
	
	public Estacion estacionPorFiltroVisita(String nombre, Sello sello, int zona){
		List<Estacion> estaciones;
		if(zona==0){
			estaciones = find(Restrictions.eq("nombre", nombre),Restrictions.eq("sello", sello));
		}else if(nombre==null){
			estaciones = find(Restrictions.eq("sello", sello),Restrictions.eq("zona", zona));
		}else{
			estaciones = find(Restrictions.eq("nombre", nombre),Restrictions.eq("sello", sello),Restrictions.eq("zona", zona));
		}
		 return !estaciones.isEmpty()? estaciones.get(0) : null;
	}
	
	public List <Estacion> estacionesPorLocalidad(String localidad){
		return find(Restrictions.eq("localidad", localidad));
	}
	
	public List<String> getLocalidades(){
		
		ArrayList<String> localidades = new ArrayList<String>();
		
		Criteria criteria = getSession( ).createCriteria(Estacion.class);
		
		ArrayList<Estacion> estaciones = ((ArrayList<Estacion>) criteria.list());

		for (Estacion e : estaciones){
			localidades.add(e.getLocalidad());
		}
		
		return localidades;
	}

	public List<Estacion> obtenerEstacionesPorSello(Sello sello) {
		return find(Restrictions.eq("sello", sello), Restrictions.eq("inactiva", false));
	}

	public List<EmailEmpresa> obtenerEmailsEmpresa(Estacion estacion) {
		// TODO Auto-generated method stub
		return null;
	}
}
