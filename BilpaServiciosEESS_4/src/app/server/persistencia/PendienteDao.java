package app.server.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.Activo;
import app.client.dominio.Corregido;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Organizacion;
import app.client.dominio.Pendiente;
import app.client.dominio.Preventivo;
import app.client.dominio.Sello;
import app.client.dominio.Solucion;

public class PendienteDao extends AbstractDao<Pendiente, Integer> {

	//		public List<Pendiente> pendientesVisibles(Date desde, Date hasta, List<Organizacion> organizaciones){
	//			return find(
	//					Restrictions.or(Restrictions.eq("organizacion", Organizacion.Ducsa), 
	//					Restrictions.eq("organizacion", Organizacion.Operador)),				
	//					Restrictions.eq("comentarioVisible", true), 
	//					Restrictions.between("fechaCreado", desde, hasta));
	//		}

	public List<Pendiente> pendientesVisibles(Date desde, Date hasta, List<Organizacion> organizaciones){

		Criteria c = createCriteria();
		Organizacion[] nombreOrganizaciones = new Organizacion[organizaciones.size()];
		int i =0;

		for(Organizacion o : organizaciones){
			nombreOrganizaciones[i]=o;
			i++;
		}

		if (nombreOrganizaciones.length > 0){
			c.add(Restrictions.in("organizacion", Arrays.asList(nombreOrganizaciones)));
		} else {
			return new ArrayList<Pendiente>();
		}
		c.add(Restrictions.eq("comentarioVisible", true));
		c.add(Restrictions.between("fechaCreado", desde, hasta));

		List<Pendiente> lista = c.list();

		return lista;

	}

	public List<Pendiente> obtenerPendientesDeActivo(Activo activo) {
		Criteria criteria = getSession( ).createCriteria(Pendiente.class);
		criteria.add(Restrictions.eq("activo", activo));
		criteria.add(Restrictions.eq("estado", EstadoPendiente.INICIADO));
		criteria.addOrder(Order.asc( "estado" ));
		// criteria.setMaxResults(20);

		return criteria.list();
	}

	public List<Pendiente> obtenerPendientesActivosDeActivo(Activo activo, EstadoPendiente estado) {
		Criteria criteria = getSession( ).createCriteria(Pendiente.class);

		criteria.add(Restrictions.eq("activo", activo));
		if (estado != null){
			criteria.add(Restrictions.eq("estado", estado));
		}
		criteria.addOrder(Order.asc( "id" ));
		criteria.setMaxResults(20);

		return criteria.list();
	}

	public Pendiente obtenerPendienteDeSolucion(Solucion solucion) {
		return get(Restrictions.eq("solucion", solucion));			
	}

	public List<Pendiente> obtenerPendienteDeCorregido(Corregido corregido) {
		return find(Restrictions.eq("corregido", corregido));
	}
	
	public List<Pendiente> obtenerPendientesPorSello(Sello sello, EstadoPendiente estadoPendiente) {
		Criteria criteria = createCriteria();
		criteria.createAlias("activo", "a");
		criteria.createAlias("a.empresa", "e");

		criteria.add(Restrictions.eq("e.sello", sello));
		criteria.add(Restrictions.eq("estado", estadoPendiente));
		// criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

}
