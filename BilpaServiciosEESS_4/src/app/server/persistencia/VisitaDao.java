package app.server.persistencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import app.client.dominio.Estacion;
import app.client.dominio.EstadoActivo;
import app.client.dominio.EstadoVisita;
import app.client.dominio.Preventivo;
import app.client.dominio.Sello;
import app.client.dominio.Tecnico;
import app.client.dominio.Visita;
import app.client.dominio.data.VisitaDataList;
import app.server.control.ControlEmpresa;
import app.server.control.ControlPersona;

public class VisitaDao extends AbstractDao<Visita, Integer> {

	public List<Visita> obtenerVisitasAsignadas(Tecnico tecnico) {
		List<Visita> visitas = find(Restrictions.eq("inactiva", false), Restrictions.eq("tecnico", tecnico), Restrictions.or(Restrictions.eq("estado", EstadoVisita.PENDIENTE ),Restrictions.eq("estado", EstadoVisita.INICIADA)));	
		for (Visita v : visitas) {
			setFechaUltimaVisita(v);
		}
		return visitas;
	}

	public List<Visita> obtenerVisitas(int sello) {
		List<Visita> visitas = new ArrayList<Visita>();
		List<Estacion> estacionesPorSello = new ArrayList<Estacion>();

		DaoTransaction tx = new DaoTransaction();
		try{
			tx.begin();
			visitas = find(Restrictions.eq("inactiva", false));

			estacionesPorSello = new EstacionDao().find(Restrictions.eq("sello", new Sello(sello)), Restrictions.eq("inactiva", false));

			loadVisitasDummies(visitas, estacionesPorSello, sello);
			return visitas;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			tx.close();
		}
		return visitas;
	}

	@SuppressWarnings("unchecked")
	public List<VisitaDataList> obtenerVisitasPorFiltro(VisitaDataList visitaData, int sello) {
		List<Visita> visitasRetorno = new ArrayList<Visita>();
		List<Object[]> visitas = new ArrayList<Object[]>();
		List<Estacion> estacionesPorSello = new ArrayList<Estacion>();

		Estacion empresa = null;
		Tecnico tecnico = null;
		String zona = null;
		EstadoVisita estado = null;

		if (filtroEstacion(visitaData)){
			empresa = ControlEmpresa.getInstancia().obtenerEstacion(visitaData.getEstacionData().getNombre());
		}

		if (filtroTecnico(visitaData)){
			tecnico = ControlPersona.getInstancia().obtnerTecnicoPorNombre(visitaData.getTecnicoData().getNombre(), visitaData.getTecnicoData().getApellido());
		}

		if (filtroZona(visitaData)){			
			zona = visitaData.getEstacionData().getZona();	
		}

		if (filtroEstado(visitaData)){			
			estado = visitaData.getEstado();	
		}

		List<VisitaDataList> result = new ArrayList<VisitaDataList>();
		DaoTransaction tx = new DaoTransaction();
		try
		{
			tx.begin();

			EstacionDao daoEstacion = new EstacionDao();
			Criteria criteriaEstacion =  daoEstacion.createCriteria();
			criteriaEstacion.add(Restrictions.eq("sello", new Sello(sello)));
			criteriaEstacion.add(Restrictions.eq("inactiva", false));

			if (sello == Sello.ANCAP){
				criteriaEstacion.createAlias("listaDeActivos", "activoDeEstacion");
				criteriaEstacion.add(Restrictions.eq("activoDeEstacion.estado", EstadoActivo.ABONO));
				criteriaEstacion.add(Restrictions.eq("activoDeEstacion.tipo", 1));
				criteriaEstacion.add(Restrictions.eq("sello", new Sello(sello)));
				/*criteriaEstacion.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("nombre"), "nombre") ))
						.setResultTransformer(Transformers.aliasToBean(Estacion.class));*/
			}

			Criteria criteriaVisita = createCriteria();
			criteriaVisita.add(Restrictions.eq("inactiva", false));

			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("id"));
			projList.add(Projections.property("estacion"));
			projList.add(Projections.property("tecnico"));
			projList.add(Projections.property("fechaRealizada"));
			projList.add(Projections.property("fechaProximaVisita"));
			projList.add(Projections.property("estado"));

			criteriaVisita.setProjection(projList);
			criteriaVisita.addOrder(Order.desc("id"));

			if (empresa != null){
				criteriaVisita.add(Restrictions.eq("estacion", empresa));
				criteriaEstacion.add(Restrictions.eq("nombre", empresa.getNombre()));
			}

			boolean avoidDummies = false;
			if (tecnico != null){	
				criteriaVisita.add(Restrictions.eq("tecnico", tecnico));	
				avoidDummies = true;
			}

			if (estado != null){	
				criteriaVisita.add(Restrictions.eq("estado", estado));	
				if (!estado.equals(EstadoVisita.SIN_VISITAS)){
					avoidDummies = true;
				}
			}

			Criteria criteriaEstacionVisita = criteriaVisita.createAlias("estacion", "e");

			if (zona != null){	
				criteriaEstacionVisita.add(Restrictions.eq("e.zona", Integer.valueOf(zona)));
				criteriaEstacion.add(Restrictions.eq("zona", Integer.valueOf(zona)));
			}

			criteriaEstacionVisita.add(Restrictions.eq("e.sello", new Sello(sello)));

			visitas = criteriaVisita.list();

			List<Integer> idEstaciones = new ArrayList<Integer>();
			for(Object[] visitaObject : visitas){
				Estacion estacion = (Estacion)(visitaObject[1]);

				if (!idEstaciones.contains(estacion.getId())){
					idEstaciones.add(estacion.getId());
					Visita visita = new Visita();
					visita.setId(Integer.valueOf(visitaObject[0].toString()));
					visita.setEstacion(estacion);
					if (visitaObject[2] != null) visita.setTecnico((Tecnico)(visitaObject[2]));
					if (visitaObject[3] != null) visita.setFechaRealizada((Date)visitaObject[3]);
					if (visitaObject[4] != null) visita.setFechaProximaVisita((Date)visitaObject[4]);
					visita.setEstado((EstadoVisita)visitaObject[5]);

					visitasRetorno.add(visita);
				}
			}

			/*if(!avoidDummies){
				estacionesPorSello = criteriaEstacion.list();
				loadVisitasDummies(visitasRetorno, estacionesPorSello, sello);
			}*/

			result = visitasToVisitasDataList(visitasRetorno);
		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			tx.close();
		}
		return result;
	}

	private void loadVisitasDummies(List<Visita> visitas, List<Estacion> estacionesPorSello, int sello) {
		for(Estacion estacion : estacionesPorSello){
			if (!estacionTieneVisita(estacion, visitas)){
				Visita visitaDummy = new Visita();
				estacion.setSello(new Sello(sello));
				visitaDummy.setEstacion(estacion);
				visitaDummy.setEstado(EstadoVisita.SIN_VISITAS);
				visitas.add(visitaDummy);
			}
		}
	}

	private boolean filtroZona(VisitaDataList visitaData) {
		return visitaData != null && visitaData.getEstacionData() != null && visitaData.getEstacionData().getZona() != null && !visitaData.getEstacionData().getZona().isEmpty();
	}

	private boolean filtroEstado(VisitaDataList visitaData) {
		return visitaData != null && visitaData.getEstado() != null;
	}

	private boolean filtroTecnico(VisitaDataList visitaData) {
		return visitaData != null && visitaData.getTecnicoData() != null && !visitaData.getTecnicoData().getNombreCompleto().isEmpty();
	}

	private boolean filtroEstacion(VisitaDataList visitaData) {
		return visitaData != null && visitaData.getEstacionData() != null && visitaData.getEstacionData().getNombre() != null && !visitaData.getEstacionData().getNombre().isEmpty();
	}

	public List<VisitaDataList> obtenerVisitasDataList(int sello) {
		return visitasToVisitasDataList(obtenerVisitas(sello));
	}

	private boolean estacionTieneVisita(Estacion estacion, List<Visita> visitas) {
		for(Visita visita : visitas){
			if(visita.getEstacion().getId() == estacion.getId()){
				return true;
			}
		}
		return false;
	}

	public List<VisitaDataList> obtenerVisitasPorEmpresa(String nombreEmpresa) {
		Estacion empresa = obtenerEmpresa(nombreEmpresa);
		List<Visita> visitas = find(Restrictions.eq("estacion", empresa), Restrictions.eq("inactiva", false));
		return visitasToVisitasDataList(visitas);
	}

	@SuppressWarnings("unchecked")
	private void setFechaUltimaVisita(Visita visita) {
		/*try
		{
			Criteria c = createCriteria();
			c.addOrder(Order.desc("fechaRealizada"));
			c.add(Restrictions.eq("estacion", visita.getEstacion()));
			c.add(Restrictions.eq("estado", EstadoVisita.REALIZADA));
			c.add(Restrictions.eq("inactiva", false));
			List<Visita> visitasEstacion = c.list();

			Date fechaUltimaVisita = null;
			if(visitasEstacion.size() > 0){
				fechaUltimaVisita = visitasEstacion.get(0).getFechaRealizada();
				visita.setFechaUltimaVisita(fechaUltimaVisita);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}*/
	}

	private Estacion obtenerEmpresa(String nombreEmpresa) {
		EstacionDao estacionDao = new EstacionDao();
		return estacionDao.estacionPorNombre(nombreEmpresa);
	}

	private List<VisitaDataList> visitasToVisitasDataList(List<Visita> visitas) {
		List<VisitaDataList> visitaDataList = new ArrayList<VisitaDataList>();
		VisitaDataList visitaData;

		for (Visita v : visitas) {
			visitaData = new VisitaDataList();

			setFechaUltimaVisita(v);
			// v.setFechaUltimaVisita(new Date());

			visitaData = v.getVisitaDataList();
			visitaData.setDiasSinVisita(app.server.UtilFechas.getDiasSinVisitas(v.getEstacion().getFechaUltimaVisita()));

			visitaDataList.add(visitaData);
		}

		// Collections.sort(visitaDataList);
		return visitaDataList;
	}

	public List<Visita> reporteVisitas(Sello selloSeleccionado, Date inicio, Date fin) {
		Criteria criteria = createCriteria();
		criteria.createAlias("estacion", "e");

		criteria.add(Restrictions.eq("e.sello", selloSeleccionado));
		criteria.add(Expression.le("fechaFin",fin));
		criteria.add(Expression.ge("fechaInicio",inicio));
		criteria.add(Restrictions.isNotNull("fechaFin"));
		criteria.addOrder(Order.asc( "fechaFin" ));
		return criteria.list();
	}
}
