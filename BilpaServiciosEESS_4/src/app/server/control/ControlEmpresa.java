package app.server.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Canio;
import app.client.dominio.Comentario;
import app.client.dominio.EmailEmpresa;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Orden;
import app.client.dominio.Pendiente;
import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Sello;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
import app.client.dominio.Tecnico;
import app.client.dominio.data.ActivoData;
import app.client.dominio.data.DatoListadoActivos;
import app.client.dominio.data.EmailEmpresaData;
import app.client.dominio.data.EstacionDataList;
import app.server.persistencia.ActivoDao;
import app.server.persistencia.ActivoGenericoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.EmailEmpresaDao;
import app.server.persistencia.EstacionDao;
import app.server.persistencia.FotoDao;
import app.server.persistencia.PendienteDao;
import app.server.persistencia.ReparacionDao;
import app.server.persistencia.SelloDao;

public class ControlEmpresa {

	private static ControlEmpresa instancia = null;

	public static ControlEmpresa getInstancia() {
		if(instancia == null){
			instancia = new ControlEmpresa();
		}
		return instancia;
	}

	public static void setInstancia(ControlEmpresa instancia) {
		ControlEmpresa.instancia = instancia;
	}

	private ControlEmpresa (){

	}

	public ArrayList<Estacion> obtenerEmpresasConOrdenesActivas(){

		ArrayList<Estacion> todosLasEstacionesConOrdenesActivas = new ArrayList<Estacion>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			EstacionDao dao = new EstacionDao();

			List<Estacion> lista = dao.obtenerEstacionesConOrdenesActivas();

			for (Estacion e : lista) {
				todosLasEstacionesConOrdenesActivas.add(e);
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}

		return todosLasEstacionesConOrdenesActivas;
	}

	public ArrayList<Estacion> obtenerEmpresas(){
		ArrayList<Estacion> todosLasEstaciones = new ArrayList<Estacion>();

		DaoTransaction tx = new DaoTransaction();

		try
		{
			tx.begin();

			EstacionDao dao = new EstacionDao();

			List<Estacion> lista = dao.obtenerEmpresasOrdenadas();

			for (Estacion e : lista) {
				if (!e.isInactiva())
				{//http://opensource.atlassian.com/projects/hibernate/browse/HHH-1401
					todosLasEstaciones.add(e.copiarTodoSinOrdenes());					
				}
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();

		}finally{
			tx.close();
		}

		return todosLasEstaciones;
	}
	
	public List<Estacion> obtenerEstaciones(int idSello) {
		List<Estacion> estacionesPorSello = new ArrayList<Estacion>();
		DaoTransaction tx = new DaoTransaction();
		try{
			tx.begin();
			List<Estacion> estacionesTmp = new ArrayList<Estacion>();
			estacionesTmp = new EstacionDao().obtenerEstacionesPorSello(new Sello(idSello));
			
			for(Estacion estacion : estacionesTmp){
				estacionesPorSello.add(estacion.copiar());
			}
			
			return estacionesPorSello;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return estacionesPorSello;
	}
	
	public ArrayList<EstacionDataList> obtenerEmpresasDataList()
	{
		ArrayList<EstacionDataList> todosLasEstaciones = new ArrayList<EstacionDataList>();

		DaoTransaction tx = new DaoTransaction();
		try
		{
			tx.begin();
			EstacionDao dao = new EstacionDao();
			List<Estacion> lista = dao.obtenerEmpresasOrdenadas();

			for (Estacion e : lista) 
			{
				if (!e.isInactiva()) 
				{
					EstacionDataList auxiliar = new EstacionDataList();
					copiarEstacionDataList(e, auxiliar);		
					todosLasEstaciones.add(auxiliar);
				}
			}
		} 
		catch (Exception e) 
		{
			tx.rollback();
			e.printStackTrace();

		}
		finally
		{
			tx.close();
		}
		return todosLasEstaciones;
	}

	private void copiarEstacionDataList(Estacion e, EstacionDataList auxiliar) {
		if(e != null && auxiliar != null) {
			auxiliar.setId(e.getId());
			auxiliar.setNombre(e.getNombre());
			auxiliar.setNumeroSerie(e.getNumeroSerie());
			if (e.getSello() != null) auxiliar.setSello(e.getSello().getNombre());
		}
	}

	public ArrayList<String> obtenerTiposDeActivos(int idEstacion, boolean incluirDummy){
		ArrayList<String> tiposActivos = new ArrayList<String>();
		DaoTransaction tx = new DaoTransaction();

		
		try {
			tx.begin();
			List<ActivoGenerico> activosGenericos = new ActivoGenericoDao().getByEstacion(idEstacion);

			if (incluirDummy){
				tiposActivos.add("Todos los tipos");
			}
			tiposActivos.add("Surtidor");
			tiposActivos.add("Tanque");
			tiposActivos.add("Bombas Sumergibles");
			
			List<Integer> idTiposActivosAgregados = new ArrayList<Integer>();
			for(ActivoGenerico ag : activosGenericos){
				if (!idTiposActivosAgregados.contains(ag.getTipoActivoGenerico().getId())){
					idTiposActivosAgregados.add(ag.getTipoActivoGenerico().getId());
					tiposActivos.add(ag.getTipoActivoGenerico().getNombre());
				}
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}
		return tiposActivos;
	}

	public ArrayList<DatoListadoActivos> obtenerDataActivosPorTipo(Estacion e, Date desde, Date hasta, Tecnico tecnico, int tipoActivo)
	{
		ArrayList<DatoListadoActivos> activosRetorno = new ArrayList<DatoListadoActivos>();
		ArrayList<Activo> todosLosActivosDeLaEstacion = obtenerActivosDeEstacion(e, tipoActivo);
		
		for (Activo a : todosLosActivosDeLaEstacion)
		{
			ArrayList<Reparacion> todasLasReparacionesDelActivo = ControlActivo.getInstancia().obtenerTodosLasReparacionesDeUnActivo(a);
			
			ArrayList<Reparacion> reparacionesFiltradasDelActivo = new ArrayList<Reparacion>();
			ArrayList<RepuestoLinea> repuestosTEMPDelActivo = new ArrayList<RepuestoLinea>();
			ArrayList<RepuestoLinea> repuestosFinalesDelActivo = new ArrayList<RepuestoLinea>();
			
			ArrayList<Comentario> comentariosFinalesDelActivo = new ArrayList<Comentario>();
			
			for (Reparacion r : todasLasReparacionesDelActivo)
			{
				if (filtrarTecnico(tecnico, r) && filtrarFechas(desde, hasta, r))
				{
					reparacionesFiltradasDelActivo.add(r);
					repuestosTEMPDelActivo = ControlRepuesto.getInstancia().obtenerTodosLosRepuestosLinea(r.getOrden(), a);					
					for (RepuestoLinea rl : repuestosTEMPDelActivo)
					{
						repuestosFinalesDelActivo.add(rl);
					}
					/*
					comentariosTEMPDelActivo = ControlOrden.getInstancia().obtenerComentariosImprimibles();
					//comentariosTEMPDelActivo = ControlOrden.getInstancia().obtenerComentarios(r.getOrden());
					
					for (Comentario cd : comentariosTEMPDelActivo)
					{
						comentariosFinalesDelActivo.add(cd);
					}*/
				}
			}
			
			if (reparacionesFiltradasDelActivo.size()>0)
			{
				activosRetorno.add(new DatoListadoActivos(reparacionesFiltradasDelActivo, repuestosFinalesDelActivo, comentariosFinalesDelActivo));
			}
		}
		return activosRetorno;
	}

	private boolean filtrarFechas(Date desde, Date hasta, Reparacion r) 
	{
		return (desde != null && hasta != null && r.getOrden().getInicioService() != null && r.getOrden().getFinService() != null && r.getOrden().getInicioService().after(desde)) 
				&& r.getOrden().getFinService().before(hasta);
	}

	private boolean filtrarTecnico(Tecnico tecnico, Reparacion r) 
	{
		return tecnico.getId() <= 0 //no hay filtro tecnico
				|| (r.getOrden() != null && r.getOrden().getTecnicoAsignado() != null && 
					r.getOrden().getTecnicoAsignado().getId() == tecnico.getId());
	}
	
	public ArrayList<ActivoData> obtenerDataActivosPorTipo(Estacion estacion, int tipoActivo){

		ArrayList<ActivoData> activosRetorno = new ArrayList<ActivoData>();
		ArrayList<Activo> todosLosActivosDeLaEstacion = obtenerActivosDeEstacion(estacion, tipoActivo);

			for (Activo a : todosLosActivosDeLaEstacion)
			{
				String descripcion="";
				String nombreGenerico = null;
				if (a.getTipo() == 1)//Surtidor
				{
					Surtidor s = (Surtidor)a;
					descripcion = s.toString();
				}				
				else if (a.getTipo() == 2)//Tanque
				{
					Tanque s = (Tanque)a;
					descripcion = s.toString();
				}
				else if (a.getTipo() == 3)//Canio
				{
					Canio s = (Canio)a;
					descripcion = s.toString();
				}
				else if (a.getTipo() == 4)//Bomba
				{
					BombaSumergible s = (BombaSumergible)a;
					descripcion = s.toString();
				}
				else if (a.getTipo() == 6)//Generico
				{
					ActivoGenerico ag = (ActivoGenerico)a;
					descripcion = ag.toString();
					nombreGenerico = ag.getTipoActivoGenerico().getNombre();
				}
				
				activosRetorno.add(new ActivoData(a.getId(), String.valueOf(a.getTipo()), descripcion, nombreGenerico));
			}	
			return activosRetorno;
	}
	
	public ArrayList<Activo> obtenerActivosPorTipo(Estacion empresa, int tipoActivo){

		ArrayList<Activo> activosRetorno = new ArrayList<Activo>();
		Estacion estacion = empresa;
		ArrayList<Activo> todosLosActivosDeLaEstacion = obtenerActivosDeEstacion(estacion, tipoActivo);

		if (tipoActivo == 0)
		{
			return todosLosActivosDeLaEstacion;
		}
		else
		{
			for (int i = 0; i < todosLosActivosDeLaEstacion.size(); i++) 
			{
				Activo activo = todosLosActivosDeLaEstacion.get(i);

				if(activo.getTipo() == tipoActivo)
				{

					activosRetorno.add(activo);
				}
			}	
			return activosRetorno;
		}
	}
	
	public ArrayList<Activo> obtenerActivos(int idEstacion){
		ArrayList<Activo> todosLosActivosDeLaEstacion = new ArrayList<Activo>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ActivoDao dao = new ActivoDao();
			List<Activo> lista = dao.obtenerActivosEmpresa(new Estacion(idEstacion));
			for (Activo a : lista) {
					todosLosActivosDeLaEstacion.add(a.copiar());
			}
			return todosLosActivosDeLaEstacion;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return todosLosActivosDeLaEstacion;
	}

	public ArrayList<Estacion> estacionesPorSello(Sello sello){

		ArrayList<Estacion> estaciones = new ArrayList<Estacion>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			EstacionDao dao = new EstacionDao();

			List<Estacion> lista = dao.estacionesPorSello(sello);

			for (Estacion e : lista) {
				estaciones.add(e.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}

		return estaciones;
	}
	
	public List<Activo> obtenerActivosXEmpresa(Estacion e, int numero){
		List<Activo> activos = new ArrayList<Activo>();
		
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			ActivoDao dao = new ActivoDao();
			
			List<Activo> lista = dao.obtenerActivosEmpresa(e);

			for (Activo a : lista) {
				Activo activo = a.copiar();
				activo.setEmpresa(null);
				activos.add(activo);
				
				List<Pendiente> pendientesDeActivo = new PendienteDao().obtenerPendientesActivosDeActivo(activo, EstadoPendiente.INICIADO);
				boolean tienePendientes = pendientesDeActivo != null && pendientesDeActivo.size() > 0;
				activo.setTienePendientes(tienePendientes);
				
				List<Reparacion> reparacionDeActivo = new ReparacionDao().obtenerReparacionPorOrdenActivo(new Orden(numero), activo);
				boolean tieneReparaciones = reparacionDeActivo != null && reparacionDeActivo.size() > 0 && reparacionDeActivo.get(0).getSoluciones().size() > 0;
				activo.setTieneReparaciones(tieneReparaciones);
			}
			return activos;

		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
		} finally{
			tx.close();
		}
		return activos;
	}

	public ArrayList<Activo> obtenerActivosDeEstacion(Estacion estacion, int tipoActivo){

		ArrayList<Activo> todosLosActivosDeLaEstacion = new ArrayList<Activo>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			ActivoDao dao = new ActivoDao();
			EstacionDao daoEstacion = new EstacionDao();
			estacion = daoEstacion.get(estacion.getId());
			
			List<Activo> lista = dao.obtenerActivosEmpresa(estacion);

			for (Activo a : lista) {

				if(a.getTipo() == tipoActivo || tipoActivo == 0)
				{
					todosLosActivosDeLaEstacion.add(a.copiarTodo());
				}
			}
			return todosLosActivosDeLaEstacion;

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return todosLosActivosDeLaEstacion;
	}

	public Estacion buscarEstacion(int id) {

		Estacion estacion = null;

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			EstacionDao dao = new EstacionDao();

			estacion = dao.get(id);

			estacion = estacion.copiarTodoSinOrdenes();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			tx.close();
		}

		return estacion;
	}

	public boolean agregarEstacion(Estacion estacion)
	{
		if (estacion != null)
		{
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				EstacionDao dao = new EstacionDao();
				setFotoDeSello(estacion);
				dao.save(estacion);
				tx.commit();
				return true;

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}		finally{
				tx.close();
			}
		}
		return false;
	}
	
	public boolean actualizarEstacion(Estacion estacion)
	{
		if(estacion != null){
			estacion.setListaOrdenes(ControlOrden.getInstancia().obtenerOrdenesEstacion(estacion));
			
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				EstacionDao dao = new EstacionDao();
				setFotoDeSello(estacion);
				
				dao.update(estacion);

				tx.commit();
				return true;

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
				tx.close();
			}
		}
		return false;
	}

	private static final int idFotoAncap = 1441;
	private static final int idFotoChicaAncap = 1443;
	private static final int idFotoPetrobras = 1442;
	private static final int idFotoChicaPetrobras = 1444;
	
	private void setFotoDeSello(Estacion estacion) {
		if (estacion.getSello().getId() == Sello.ANCAP || estacion.getSello().getId() == Sello.ANCAP_CONTRATOS){
			estacion.setFoto(new FotoDao().get(idFotoAncap));
			estacion.setFotoChica(new FotoDao().get(idFotoChicaAncap));
		
		} else if (estacion.getSello().getId() == Sello.PETROBRAS){
			estacion.setFoto(new FotoDao().get(idFotoPetrobras));
			estacion.setFotoChica(new FotoDao().get(idFotoChicaPetrobras));
		}
	}

	public ArrayList<Sello> obtenerSellos() {
		ArrayList<Sello> sellos = new ArrayList<Sello>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			SelloDao dao = new SelloDao();

			List<Sello> lista = dao.list();

			for (Sello s : lista) {
				sellos.add(s.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();

		}finally{
			tx.close();
		}

		return sellos;
	}

	public boolean validarNuevaEstacion(Estacion nueva)
	{
		List<Estacion> estaciones = obtenerEmpresas();

		for (Estacion e : estaciones)
		{
			if (e.getNumeroSerie().equals(nueva.getNumeroSerie()) || 
					e.getNombre().equals(nueva.getNombre()))
			{
				return false;
			}
		}
		return true;

	}

	public boolean eliminarEstacion(Estacion estacion){
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			EstacionDao dao = new EstacionDao();
			estacion.setInactiva(true);
			dao.merge(estacion);

			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			tx.close();
		}
	}

	public Estacion obtenerEstacion(int id) {
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			EstacionDao dao = new EstacionDao();
			Estacion estacion = dao.get(id);
			return estacion;
		} catch (Exception e) {
		//	tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}

	public Estacion obtenerEstacion(String nombre) {
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			EstacionDao dao = new EstacionDao();
			Estacion estacion = dao.estacionPorNombre(nombre);
			return estacion;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}

	public List<EmailEmpresaData> obtenerEmails(int idEstacion) {
		
		List<EmailEmpresaData> emails = new ArrayList<EmailEmpresaData>();
		try {
			
			Estacion estacion = buscarEstacion(idEstacion);
			emails = estacion.getEmailsData();
			return emails;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return emails;
	}

	public void actualizarCorreosEstacion(int id, List<String> mails) {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			EstacionDao dao = new EstacionDao();
			Estacion estacion = dao.get(id);
			
			Set<EmailEmpresa> mailsEmpresa = new HashSet<EmailEmpresa>();
			for (String mail : mails){
				EmailEmpresa emailEmpresa = new EmailEmpresa();
				emailEmpresa.setEmail(mail);
				mailsEmpresa.add(emailEmpresa);
			}
			
			EmailEmpresaDao daoEmail = new EmailEmpresaDao();
			for (EmailEmpresa emailEmpresa : estacion.getListaEmail()){
				daoEmail.delete(emailEmpresa);
			}
			estacion.setListaEmail(mailsEmpresa);
			dao.update(estacion);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
	}
	
}
