package app.server.persistencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import app.client.dominio.Estacion;
import app.client.dominio.Orden;
import app.client.dominio.Sello;
import app.client.dominio.Tecnico;
import app.client.dominio.data.OrdenData;
import app.client.utilidades.utilObjects.filter.orden.ListaOrdenesData;

public class OrdenDao extends AbstractDao<Orden, Integer>{

	public List <Orden> obtenerOrdenesActivas(){
		return find(Restrictions.eq("estadoOrden", 1), Restrictions.eq("estadoOrden", 6));
	}

	/*public List <Orden> buscarOrden(int numero){
		return find(Restrictions.eq("numero", numero));
	}*/
	
	public Orden buscarOrden(int numero){
		return get(Restrictions.eq("numero", numero));
	}

	public List <Orden> obtenerOrdenesAsignadas(){
		return find(Restrictions.eq("estadoOrden", 2));
	}

	public List <Orden> obtenerOrdenesCerradas(){
		return find(Restrictions.eq("estadoOrden", 3));
	}

	public List <Orden> ordenesActivasTecnico(Tecnico tecnico){
		return find(Restrictions.eq("tecnicoAsignado", tecnico),Restrictions.in("estadoOrden", Arrays.asList(1,2,5,6,7)));
	}

	public List <Orden> ordenesActivasEmpresa(Estacion empresa){
		return find(Restrictions.eq("empresa", empresa),Restrictions.in("estadoOrden", Arrays.asList(1,2)));
	}

	public List <Orden> ordenesEmpresa(Estacion empresa){
		return find(Restrictions.eq("empresa", empresa));
	}

	public List <Orden> historicoOrdenesFinalizadasEmpresa(Estacion empresa){
		return find(Restrictions.eq("empresa", empresa),Restrictions.eq("estadoOrden",3));
	}

	public List <Orden> ordenesAsiganadasTecnico(Tecnico tecnico){
		return find(Restrictions.eq("tecnicoAsignado", tecnico),Restrictions.eq("estadoOrden", 3));
	}

	public List<Orden> ordenesPorFecha(Estacion e, Date desde, Date hasta)
	{
		return find(Restrictions.eq("empresa", e), Restrictions.between("fechaFin", desde, hasta),Restrictions.eq("estadoOrden", 3));
	}


	//funcion para cerrar petrobras y ducsa
	public List<Orden> ordenesFinalizadasCierrePetroleras(Sello sello, Date desde, Date hasta){
		return find(
				Restrictions.eq("estadoOrden", 3), 
				Restrictions.between("inicioService", desde, hasta));
	}

	public List<Orden> ordenesAbiertasCierrePetroleras(Sello sello, Date desde, Date hasta){
		return find(
				Restrictions.or(
						Restrictions.or(Restrictions.eq("estadoOrden", 1), 
								Restrictions.eq("estadoOrden", 2)),
								Restrictions.eq("estadoOrden", 5)
						),
						Restrictions.between("fechaInicio", desde, hasta));
	}

	public List <Orden> ordenesAsiganadasTecnicoTiempo(Tecnico tecnico, Date inicio, Date fin){
		return find(Restrictions.eq("tecnicoAsignado", tecnico), Restrictions.between("fechaFin", inicio, fin), Restrictions.eq("estadoOrden", 5));
	}

	public List <Orden> ordenesFinalizadasPorTiempo(Estacion e, Date inicio, Date fin){
		return find(Restrictions.eq("empresa", e), Restrictions.between("fechaFin", inicio, fin),Restrictions.eq("estadoOrden", 3));
	}

	public List <Orden> obtenerOrdenesInactivas(){
		return find(Restrictions.in("estadoOrden", Arrays.asList(3,4)));
	}

	public List <Orden> obtenerTodasOrdenesActivas(){
		return find(Restrictions.in("estadoOrden", Arrays.asList(1,2,5,6)));
	}

	public List <Orden> obtenerTodasOrdenesActivas(Sello sello, Date desde, Date hasta){
		return find(Restrictions.in("estadoOrden", Arrays.asList(1,2,5,6)));
	}

	public List <Orden> ultimasOrdenes(int cantidad){
		Order desc = Order.asc("fechaFin");
		Criteria c = createCriteria();
		c.addOrder(desc);
		c.add(Restrictions.in("estadoOrden", Arrays.asList(3,4)));

		List<Orden> lista = c.list();

		int hasta = lista.size();
		int desde = hasta - cantidad;

		if(desde < 0){
			desde = 0;
		}

		return lista.subList(desde, hasta);
	}

	public int obtenerCantidadOrdenesInactivas() 
	{
		return obtenerOrdenesInactivas().size();
	}

	public List<Orden> obtenerOrdenes(OrdenData ordenFilter) {
		// return find(Restrictions.eq("empresa", e), Restrictions.between("fechaFin", desde, hasta),Restrictions.eq("estadoOrden", 3));
		Order desc = Order.asc("fechaFin");
		Criteria c = createCriteria();
		c.addOrder(desc);

		if(ordenFilter.getEstado() != null && !ordenFilter.getEstado().equals("")){//SI HAY FILTRO POR ESTADO
			c.add(Restrictions.eq("estadoOrden", Integer.valueOf(ordenFilter.getEstado())));

		} else if (ordenFilter.isActiva()){//SI NO HAY FILTRO, LAS ACTIVAS (Iniciada, Inspeccion Pendiente, Reparado, Iniciada DUCSA y en Proceso)
			c.add(Restrictions.in("estadoOrden", Arrays.asList(1,2,5,6,7)));

		} else {// O LAS INACTIVAS (Finalizada, Anulada)
			c.add(Restrictions.in("estadoOrden", Arrays.asList(3, 4)));
		}

		if (ordenFilter.getTecnico() != null){
			c.add(Restrictions.eq("tecnicoAsignado", obtenerObjTecnico(ordenFilter.getTecnico())));
		}

		if (ordenFilter.getSello() != null){
			ArrayList <Estacion> lista;
			lista = obtenerListaEstaciones(ordenFilter.getSello());
			c.add(Restrictions.in("empresa", lista));
		}

		if (ordenFilter.getLocalidad() != null){
			c.add(Restrictions.in("empresa", obtenerListEstacionesPorLocalidad(ordenFilter.getLocalidad())));
		}

		if (ordenFilter.getEstacion() != null){
			c.add(Restrictions.eq("empresa", obtenerEstacionPorNombre(ordenFilter.getEstacion())));
		}

		if (ordenFilter.getNumeroBoca() != null) {
			c.add(Restrictions.eq("empresa", obtenerEstacionPorNumBoca(ordenFilter.getNumeroBoca())));
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaInicio = null;
		try {
			fechaInicio = format.parse(ordenFilter.getFechaInicio());
		} catch (ParseException e) {

		} catch (Exception e) {

		}

		if (ordenFilter.getFechaInicio() != null && ordenFilter.getFechaFin() != null && !ordenFilter.getFechaInicio().isEmpty()) {
			c.add(Restrictions.between("fechaInicio", fechaInicio, ordenFilter.getFechaFin()));

		}	else if (fechaInicio != null && ordenFilter.getFechaFin() == null){
			c.add(Restrictions.ge("fechaInicio", fechaInicio));

		}	else if (ordenFilter.getFechaFin() != null && ordenFilter.getFechaInicio() == null){
			c.add(Restrictions.le("fechaInicio", ordenFilter.getFechaFin()));
		}


		return c.list();
	}

	public Tecnico obtenerObjTecnico (String nombreUsu){
		Tecnico t = new Tecnico();

		PersonaDao p = new PersonaDao();
		t = (Tecnico) p.obtenerUsuario(nombreUsu);

		return t;

	}

	public ArrayList<Estacion> obtenerListaEstaciones (String sello){

		SelloDao s = new SelloDao();
		Sello selloBuscado = new Sello();
		selloBuscado = s.obtenerObjSello(sello);

		ArrayList<Estacion> estaciones = new ArrayList<Estacion>();

		EstacionDao e = new EstacionDao();
		estaciones = (ArrayList<Estacion>) e.estacionesPorSello(selloBuscado);

		return estaciones;

	}

	public ArrayList<Estacion> obtenerListEstacionesPorLocalidad (String localidad){

		ArrayList<Estacion> estaciones = new ArrayList<Estacion>();
		EstacionDao e = new EstacionDao();
		estaciones = (ArrayList<Estacion>) e.estacionesPorLocalidad(localidad);

		return estaciones;
	}

	public Estacion obtenerEstacionPorNombre (String nombre){

		Estacion est = new Estacion();
		EstacionDao e = new EstacionDao();
		est = e.get((Restrictions.eq("nombre", nombre)));

		return est;
	}

	public Estacion obtenerEstacionPorNumBoca (String numBoca){
		Estacion est = new Estacion();
		EstacionDao e = new EstacionDao();	
		est = e.get((Restrictions.eq("numeroSerie", numBoca)));

		return est;
	}


	public void setIndicadores(ListaOrdenesData data) {
		data.setIndicadorIniciadas(((Long)getSession().createQuery("select count(*) from app.client.dominio.Orden where estadoOrden = 1").uniqueResult()).intValue());
		data.setIndicadorInspeccionPendiente(((Long)getSession().createQuery("select count(*) from app.client.dominio.Orden where estadoOrden = 2").uniqueResult()).intValue());
		data.setIndicadorFinalizadas(((Long)getSession().createQuery("select count(*) from app.client.dominio.Orden where estadoOrden = 3").uniqueResult()).intValue());
		data.setIndicadorAnuladas(((Long)getSession().createQuery("select count(*) from app.client.dominio.Orden where estadoOrden = 4").uniqueResult()).intValue());
		data.setIndicadorReparadas(((Long)getSession().createQuery("select count(*) from app.client.dominio.Orden where estadoOrden = 5").uniqueResult()).intValue());
		data.setIndicadorIniciadasDUCSA(((Long)getSession().createQuery("select count(*) from app.client.dominio.Orden where estadoOrden = 6").uniqueResult()).intValue());
	}


}
