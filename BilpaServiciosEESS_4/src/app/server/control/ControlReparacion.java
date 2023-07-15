package app.server.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import app.client.dominio.Activo;
import app.client.dominio.Calibre;
import app.client.dominio.Comentario;
import app.client.dominio.Contador;
import app.client.dominio.DestinoDelCargo;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.FallaTecnica;
import app.client.dominio.Foto;
import app.client.dominio.Orden;
import app.client.dominio.Pendiente;
import app.client.dominio.Pico;
import app.client.dominio.Precinto;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.Surtidor;
import app.client.dominio.Tarea;
import app.client.dominio.Tecnico;
import app.client.dominio.data.ActivoReparacionData;
import app.client.dominio.data.SolucionData;
import app.client.dominio.json.RepuestoLineaCorregidoJson;
import app.client.dominio.json.SolucionJson;
import app.server.exception.ReparacionException;
import app.server.persistencia.ActivoDao;
import app.server.persistencia.CalibreDao;
import app.server.persistencia.ComentarioDao;
import app.server.persistencia.ContadorDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.OrdenDao;
import app.server.persistencia.PendienteDao;
import app.server.persistencia.PicoDao;
import app.server.persistencia.PrecintoDao;
import app.server.persistencia.ReparacionActivoGenericoDao;
import app.server.persistencia.ReparacionBombaDao;
import app.server.persistencia.ReparacionDao;
import app.server.persistencia.ReparacionSurtidorDao;
import app.server.persistencia.ReparacionTanqueDao;
import app.server.persistencia.RepuestoDao;
import app.server.persistencia.RepuestoLineaDao;
import app.server.persistencia.SolucionDao;
import app.server.propiedades.PropiedadEntorno;
import app.server.propiedades.PropiedadUrlFoto;

public class ControlReparacion {

	private static ControlReparacion instancia = null;

	public static ControlReparacion getInstancia() {
		if(instancia == null){
			instancia = new ControlReparacion();
		}
		return instancia;
	}

	public static void setInstancia(ControlReparacion instancia) {
		ControlReparacion.instancia = instancia;
	}

	private ControlReparacion (){}

	public ArrayList<Reparacion> obtenerTodosLasReparaciones(Orden orden) {
		ArrayList<Reparacion> todosLasReparaciones = new ArrayList<Reparacion>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ReparacionDao dao = new ReparacionDao();

			List<Reparacion> lista = dao.obtenerTodosLasReparaciones(orden);
			for (Reparacion r : lista) {
				todosLasReparaciones.add(r.copiarTodo());
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return todosLasReparaciones;
	}

	public SolucionData obtenerSolucion(int idSolucion) {
		SolucionData solucionData = new SolucionData();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			SolucionDao dao = new SolucionDao();
			Solucion solucion = dao.get(idSolucion);
			Orden orden = new OrdenDao().buscarOrden(solucion.getReparacion().getOrden().getNumero());
			Activo activo = solucion.getReparacion().getActivo();

			Solucion solucionCopia = solucion.copiarTodo();
			getSolucionData(solucionData, solucionCopia, orden, activo);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return solucionData;
	}

	private void getSolucionData(SolucionData solucionData, Solucion solucion, Orden orden, Activo activo) {
		solucion.setReparacion(null);
		solucionData.setId(solucion.getId());
		solucionData.setTarea(solucion.getTarea());
		solucionData.setFallaTecnica(solucion.getFallaTecnica());
		solucionData.setTelefonica(solucion.isTelefonica());
		solucionData.setDestinoDelCargo(solucion.getDestinoDelCargo());
		if (solucion.getComentario() != null) solucionData.setComentario(solucion.getComentario().copiarMinimo());

		List<RepuestoLinea> repuestosLinea = new ArrayList<RepuestoLinea>();
		List<RepuestoLinea> repuestosLineaData = new ArrayList<RepuestoLinea>();
		repuestosLinea.addAll(new RepuestoLineaDao().obtenerRepuestosLinea(solucion));

		for (RepuestoLinea repuestoLinea : repuestosLinea) {
			RepuestoLinea rl = repuestoLinea.copiar();
			rl.setSolucion(null);
			rl.setOrden(null);
			rl.setActivo(null);
			repuestosLineaData.add(rl);
		}
		solucionData.setRepuestos(repuestosLineaData);

		List<Contador> contadores = new ContadorDao().obtenerContadores(solucion);
		List<Contador> contadoresCopia = new ArrayList<Contador>();
		for (Contador contador : contadores) {
			contadoresCopia.add(contador.copiarMinimo());
		}
		solucionData.setContadores(contadoresCopia);

		if (contadores.size() > 0){
			solucionData.setPico(contadores.get(0).getPico().getPicoData());
		}

		if (solucion.getCalibre() != null){
			solucionData.setCalibre(solucion.getCalibre().copiar());
		}

		if (solucion.getPrecinto() != null){
			solucionData.setPrecinto(solucion.getPrecinto().getPrecintoData());
		}
		solucion.setCalibre(null);
		solucion.setPrecinto(null);

		if (solucion.getFoto() != null) solucionData.setUrlFoto(solucion.getFoto().getUrl());
		if (solucion.getFoto2() != null) solucionData.setUrlFoto2(solucion.getFoto2().getUrl());

	}

	public List<SolucionData> obtenerReparacionesDeActivoEnOrden(int numero, int idActivo) throws Exception {
		//ArrayList<SolucionData> reparacionesData = new ArrayList<SolucionData>();
		List<SolucionData> solucionesData = new ArrayList<SolucionData>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ReparacionDao dao = new ReparacionDao();
			Activo activo = new ActivoDao().get(idActivo);
			Orden orden = new OrdenDao().buscarOrden(numero);

			List<Reparacion> reparacionesDeActivoEnOrden = dao.obtenerReparacionesDeActivoEnOrden(numero, activo);
			for (Reparacion r : reparacionesDeActivoEnOrden) {
				Reparacion reparacion = r.copiarMinimo();
				reparacion.getActivo().setEmpresa(null);

				if (reparacion.getActivo().getTipo() == 1){
					((Surtidor)reparacion.getActivo()).setPicos(null);
				}

				for (Solucion solucion : reparacion.getSoluciones()) {
					SolucionData solucionData = new SolucionData();
					getSolucionData(solucionData, solucion, orden, activo);
					solucionesData.add(solucionData);
				}
				//ReparacionData reparacionData = new ReparacionData();

				//reparacion.setSoluciones(null);
				//reparacionData.setReparacion(reparacion);
				//reparacionData.setSoluciones(solucionesData);

				//reparacionesData.add(reparacionData);
			}
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			tx.close();
		}
		return solucionesData;
	}

	public ArrayList<ActivoReparacionData> obtenerActivosConFallasReportadas(int numero) {
		ArrayList<ActivoReparacionData> activoReparacionDataList = new ArrayList<ActivoReparacionData>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ReparacionDao dao = new ReparacionDao();
			PendienteDao daoPendiente = new PendienteDao();

			List<Reparacion> lista = dao.obtenerReparacionesConFallaReportada(numero);
			for (Reparacion r : lista) {
				List<Pendiente> pendientesDeActivo = daoPendiente.obtenerPendientesActivosDeActivo(r.getActivo(), EstadoPendiente.INICIADO);
				boolean tienePendientes = pendientesDeActivo != null && pendientesDeActivo.size() > 0;

				List<Reparacion> reparacionDeActivo = new ReparacionDao().obtenerReparacionPorOrdenActivo(new Orden(numero), r.getActivo());
				boolean tieneReparaciones = reparacionDeActivo != null && reparacionDeActivo.size() > 0 && reparacionDeActivo.get(0).getSoluciones().size() > 0;
				
				ActivoReparacionData data = new ActivoReparacionData();
				data.setReparacion(r.copiar());
				data.setActivo(r.getActivo().copiar());

				//no precisamos retornar estos atributos
				data.getActivo().setEmpresa(null);
				data.getReparacion().setActivo(null);
				data.getReparacion().setOrden(null);
				data.getReparacion().setSoluciones(null);

				data.getActivo().setTienePendientes(tienePendientes);
				data.getActivo().setTieneReparaciones(tieneReparaciones);
				activoReparacionDataList.add(data);
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return activoReparacionDataList;
	}

	public void actualizarActivoDeFallaReportada(Orden orden, Activo activoReportado, Activo activoCorregido) throws ReparacionException{
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ReparacionDao dao = new ReparacionDao();

			List<Reparacion> reparaciones = dao.obtenerReparacionPorOrdenActivo(orden, activoReportado);

			if(reparaciones == null || reparaciones.size() == 0){		
				throw new ReparacionException("No existe reparación para la orden " + orden.getNumero() + " y activo " + activoReportado.getId());	
			}
			Reparacion reparacion = reparaciones.get(0);
			reparacion.setActivo(activoCorregido);
			dao.merge(reparacion);
			tx.commit();

		} catch(ReparacionException rex){
			throw rex;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			tx.close();
		}
	}

	public void eliminarSolucion(int idSolucion) throws Exception{
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			RepuestoLineaDao daoRepuestos = new RepuestoLineaDao();
			ContadorDao daoContadores = new ContadorDao();
			SolucionDao dao = new SolucionDao();

			Solucion solucion = dao.get(idSolucion);
			if(solucion !=null){		
				List<Contador> contadores = daoContadores.obtenerContadores(solucion);
				for (Contador contador : contadores) {
					daoContadores.delete(contador);
				}

				List<RepuestoLinea> repuestosLinea = daoRepuestos.obtenerRepuestosLinea(solucion);
				for (RepuestoLinea repuestoLinea : repuestosLinea) {
					daoRepuestos.delete(repuestoLinea);
				}

				PendienteDao pendienteDao = new PendienteDao();
				Pendiente pendiente = pendienteDao.obtenerPendienteDeSolucion(solucion);
				if (pendiente != null) {
					pendiente.setEstado(EstadoPendiente.INICIADO);
					pendiente.setSolucion(null);
					pendiente.setFechaReparado(null);
					pendiente.setOrdenReparado(null);

					pendienteDao.save(pendiente);
					tx.getSession().flush();
				}			

				dao.delete(solucion);
				tx.commit();
			}
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			tx.close();
		}
	}

	public int guardarSolucion(SolucionJson solucionJson) throws Exception {
		DaoTransaction tx = new DaoTransaction();
		Solucion solucion = new Solucion();
		try {
			tx.begin();
			SolucionDao dao = new SolucionDao();
			if (solucionJson.getId() > 0){
				solucion = dao.get(solucionJson.getId());
			}

			Solucion solucionParam = getSolucionAuxiliar(solucionJson);
			solucion.merge(solucionParam);

			if (solucionJson.getId() > 0){
				dao.update(solucion);

			} else {
				dao.save(solucion);
			}

			Activo activo = new ActivoDao().get(solucionJson.getIdActivo());

			saveContador(solucionJson, solucion, activo);

			saveFotos(solucionJson, solucion, dao);

			saveRepuestos(solucionJson, solucion, activo);

			updatePendiente(solucionJson, solucion);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			tx.close();
		}
		return solucion.getId();
	}

	private void updatePendiente(SolucionJson solucionJson, Solucion solucion) {
		if (solucionJson.getIdPendiente() > 0){
			PendienteDao pendienteoDao = new PendienteDao();
			Pendiente pendiente = pendienteoDao.get(solucionJson.getIdPendiente());

			if(pendiente != null) {
				pendiente.setEstado(EstadoPendiente.REPARADO);
				pendiente.setSolucion(solucion);
				pendiente.setOrdenReparado(new OrdenDao().get(solucionJson.getNumero()));
				pendiente.setFechaReparado(new Date());
				pendienteoDao.save(pendiente);
			}
		}
	}

	private void saveRepuestos(SolucionJson solucionJson, Solucion solucion, Activo activo) {
		Set<RepuestoLinea> rlNuevos = new HashSet<RepuestoLinea>();

		OrdenDao ordenDao = new OrdenDao();
		Orden orden = ordenDao.get(solucionJson.getNumero());
		if (solucionJson.getRepuestosLineaCorregidos() != null){
			for (RepuestoLineaCorregidoJson rlj : solucionJson.getRepuestosLineaCorregidos()) {
				RepuestoLinea rl = new RepuestoLinea();
				rl.setActivo(activo);
				rl.setCantidad(rlj.getCantidad());
				rl.setNuevo(rlj.isNuevo());
				rl.setOrden(orden);
				rl.setSolucion(solucion);
				rl.setRepuesto(new RepuestoDao().get(rlj.getIdRepuesto()));
				rlNuevos.add(rl);
			}
		}

		RepuestoLineaDao rlDao = new RepuestoLineaDao();
		List<RepuestoLinea> rlViejos = rlDao.obtenerRepuestosLinea(solucion);

		for (RepuestoLinea rl : rlViejos) {
			rlDao.delete(rl);
		}

		rlDao.getSession().flush();

		for (RepuestoLinea repuestoLinea : rlNuevos) {
			rlDao.save(repuestoLinea);
		}

		/*
		orden.setRepuestosLineas(rlNuevos);
		ordenDao.update(orden);
		 */
	}

	private void saveFotos(SolucionJson solucionJson, Solucion solucion, SolucionDao dao) throws IOException {
		String fotoStr = solucionJson.getFotoBytes();
		String foto2Str = solucionJson.getFoto2Bytes();

		byte[] fotoBytes = (fotoStr != null && fotoStr.length() > 0) ? new Base64().decode(fotoStr.getBytes()) : null;
		byte[] foto2Bytes = (foto2Str != null && foto2Str.length() > 0) ? new Base64().decode(foto2Str.getBytes()) : null;

		if (fotoBytes != null) {
			String path = agregarImagen(fotoBytes, solucion.getId(), 1);
			setFoto(solucion, path);
			dao.merge(solucion);
		}

		if (foto2Bytes != null) {
			String path2 = agregarImagen(foto2Bytes, solucion.getId(), 2);
			setFoto2(solucion, path2);
			dao.merge(solucion);
		}
	}

	private void setFoto(Solucion solucion, String path) {
		String entorno = new PropiedadEntorno().getEntorno().equals("produccion") ? "produccion" : "testing";
		String url = "http://179.27.66.44:6912/" + entorno + "/fotos_correctivo/" + ArchivoUtil.getNombreArchivo(path);

		if (!path.isEmpty()) {
			if (solucion.getFoto() != null) {
				solucion.getFoto().setPath(path);
				solucion.getFoto().setUrl(url);
			} else {
				Foto foto = new Foto();
				foto.setPath(path);
				foto.setUrl(url);
				solucion.setFoto(foto);
			}
			solucion.getFoto().setUrl(url);
		}
	}

	private void setFoto2(Solucion solucion, String path) {
		String entorno = new PropiedadEntorno().getEntorno().equals("produccion") ? "produccion" : "testing";
		String url = "http://179.27.66.44:6912/" + entorno + "/fotos_correctivo/" + ArchivoUtil.getNombreArchivo(path);

		if (!path.isEmpty()) {
			if (solucion.getFoto2() != null) {
				solucion.getFoto2().setPath(path);
				solucion.getFoto2().setUrl(url);
			} else {
				Foto foto = new Foto();
				foto.setPath(path);
				foto.setUrl(url);
				solucion.setFoto2(foto);
			}
			solucion.getFoto2().setUrl(url);
		}
	}

	private String agregarImagen(byte[] bytes, int idSolucion, int numero) throws IOException {

		if (bytes != null) {
			DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			String path = new PropiedadUrlFoto().getURLPropertie(true);

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String numeroStr = numero == 2 ? "2_" : "";
			String pathRelative = idSolucion + "_" + numeroStr + format.format(new Date()) + ".png";
			String pathAbsolute = path + pathRelative;
			File foto = new File(pathAbsolute);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(foto);
				fos.write(bytes);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw e;
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
			return pathAbsolute;
		}
		return "";

	}

	private void saveContador(SolucionJson solucionJson, Solucion solucion, Activo activo) {
		ContadorDao contadorDao = new ContadorDao();

		Pico pico = new PicoDao().get(solucionJson.getIdPico());

		if (activo.getTipo() == 1 && solucionJson.getIdPico() > 0 && solucionJson.getContador() != null){
			ReparacionSurtidor reparacionSurtidor = new ReparacionSurtidorDao().get(solucion.getReparacion().getId());

			Contador contador;
			if (solucionJson.getContador().getId() > 0){
				contador = contadorDao.get(solucionJson.getContador().getId());

			} else {
				contador = new Contador(reparacionSurtidor, solucion, pico);
			}

			if (contador != null){
				contador.setInicio(solucionJson.getContador().getInicio());
				contador.setFin(solucionJson.getContador().getFin());
				contador.setReparacion(reparacionSurtidor);
				contador.setPico(pico);
				contador.setSolucion(solucion);

				if (solucionJson.getContador().getId() > 0){
					contadorDao.update(contador);
				} else {
					contadorDao.save(contador);	
				}
			}
		}
	}

	private Solucion getSolucionAuxiliar(SolucionJson solucionJson) {
		CalibreDao calibreDao = new CalibreDao();
		PrecintoDao precintoDao = new PrecintoDao();
		ComentarioDao comentarioDao = new ComentarioDao();
		OrdenDao ordenDao = new OrdenDao();
		PicoDao picoDao = new PicoDao();

		Activo activo = new ActivoDao().get(solucionJson.getIdActivo());

		Orden orden = ordenDao.get(solucionJson.getNumero());
		Reparacion reparacion;
		if (solucionJson.getIdReparacion() > 0){//si es un update o una solucion a una reparacion que ya existe
			reparacion = getReparacion(solucionJson.getIdReparacion(), activo.getTipo());
		} else {
			reparacion = Reparacion.getReparacion(activo);
			reparacion.set(null, activo, activo.getTipo(), orden);
		}
		
		if (solucionJson.getIdPendiente() > 0){
			reparacion.setPendiente(new Pendiente(solucionJson.getIdPendiente()));
		}
		
		Solucion solucionAuxiliar = new Solucion(
				new Tarea(solucionJson.getIdTarea()), 
				new FallaTecnica(solucionJson.getIdFalla()), 
				false, 
				reparacion, 
				null, 
				null);

		solucionAuxiliar.setId(solucionJson.getId());
		solucionAuxiliar.setDestinoDelCargo( new DestinoDelCargo(solucionJson.getIdDestinoDelCargo()));

		if (solucionJson.getComentario() != null){
			Comentario comentario = new Comentario();
			if (solucionJson.getComentario().getId() > 0){
				comentario = comentarioDao.get(solucionJson.getComentario().getId());
			}
			comentario.merge(solucionJson.getComentario());
			comentario.setUsuario(new Tecnico(solucionJson.getIdTecnico()));
			comentario.setOrden(new Orden (solucionJson.getNumero()));
			solucionAuxiliar.setComentario(comentario);
		}

		Pico pico = picoDao.get(solucionJson.getIdPico());
		if (solucionJson.getCalibre() != null){
			Calibre calibre = new Calibre();
			if (solucionJson.getCalibre().getId() > 0){
				calibre = calibreDao.get(solucionJson.getCalibre().getId());
			}
			calibre.merge(solucionJson.getCalibre(), pico);
			solucionAuxiliar.setCalibre(calibre);
		}

		if (solucionJson.getPrecinto() != null && solucionJson.getIdPico() > 0){
			Precinto precinto = new Precinto();
			if (solucionJson.getPrecinto().getId() > 0){
				precinto = precintoDao.get(solucionJson.getPrecinto().getId());
			}
			precinto.merge(solucionJson.getPrecinto(), pico);
			solucionAuxiliar.setPrecinto(precinto);
		}

		/* else {
			solucionParam.setReparacion(new Reparacion(null, activo, activo.getTipo()));
		}*/
		return solucionAuxiliar;
	}

	private Reparacion getReparacion(int id, int tipo) {
		if (tipo == 1){
			return new ReparacionSurtidorDao().get(id);

		} else if (tipo == 2){
			return new ReparacionTanqueDao().get(id);

		} else if (tipo == 4 ){
			return new ReparacionBombaDao().get(id);

		} else if (tipo == 6 ){
			return new ReparacionActivoGenericoDao().get(id);
		}
		
		return null;
	}

	public Reparacion obtenerReparacion(int idSolucion) {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			return new ReparacionDao().obtenterReparacion(idSolucion);
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return null;
	}

	public ReparacionSurtidor obtenerReparacionSurtidor(int id) {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			return new ReparacionSurtidorDao().get(id).copiarTodo();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return null;
	}

	public Reparacion obtenerReparacionDeActivoEnOrden(int numero, int idActivo, int idPendiente) throws Exception {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			Reparacion reparacion = null;
			if (idPendiente > 0){
				reparacion = new ReparacionDao().obtenerReparacionPorPendiente(idPendiente);
			
			} else {
				List<Reparacion> reparaciones = new ReparacionDao().obtenerReparacionPorOrdenActivo(new Orden(numero), new ActivoDao().get(idActivo));
				if (reparaciones != null && reparaciones.size() > 0){
					reparacion = reparaciones.get(0);
				}
			}
			
			if (reparacion != null){
				return reparacion.copiar();
			}
			return null;
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			tx.close();
		}
	}
}