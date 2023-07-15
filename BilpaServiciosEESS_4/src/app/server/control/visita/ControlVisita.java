package app.server.control.visita;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.client.dominio.ComentarioChequeo;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoVisita;
import app.client.dominio.Firma;
import app.client.dominio.ItemChequeado;
import app.client.dominio.Organizacion;
import app.client.dominio.Preventivo;
import app.client.dominio.Sello;
import app.client.dominio.Tecnico;
import app.client.dominio.Visita;
import app.client.dominio.data.ActivoData;
import app.client.dominio.data.EstadoPreventivoData;
import app.client.dominio.data.VisitaData;
import app.client.dominio.data.VisitaDataList;
import app.server.control.ArchivoUtil;
import app.server.control.ControlEmpresa;
import app.server.control.ControlOrden;
import app.server.control.ControlPendiente;
import app.server.control.ControlPersona;
import app.server.control.visita.mail.ControlEmailVisita;
import app.server.control.visita.reporte.web.ControlVisitaReporteWebOperador;
import app.server.persistencia.ComentarioChequeoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.EstacionDao;
import app.server.persistencia.PreventivoDao;
import app.server.persistencia.VisitaDao;
import app.server.propiedades.PropiedadEntorno;
import app.server.propiedades.PropiedadMailPreventivos;
import app.server.propiedades.PropiedadUrlFirma;

public class ControlVisita {

	private static ControlVisita instancia = null;

	public static ControlVisita getInstancia() {
		if (instancia == null) {
			instancia = new ControlVisita();
		}
		return instancia;
	}

	public static void setInstancia(ControlVisita instancia) {
		ControlVisita.instancia = instancia;
	}

	private ControlVisita() {

	}

	public boolean modificarVisitaWeb(VisitaDataList vd) {
		Tecnico tecnico = null;
		if (vd.getTecnicoData() != null) {
			tecnico = ControlPersona.getInstancia().obtenerTecnico(
					vd.getTecnicoData().getId());
		} 

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			VisitaDao visitaDao = new VisitaDao();
			Visita visita = visitaDao.get(vd.getId());
			if (tecnico == null) {
				visita.setEstado(EstadoVisita.SIN_ASIGNAR);

			} else if (visita.getEstado().equals(EstadoVisita.SIN_ASIGNAR)){
				visita.setEstado(EstadoVisita.PENDIENTE);

			} else {
				visita.setEstado(vd.getEstado());

			}
			visita.setTecnico(tecnico);
			visita.setFechaProximaVisita(vd.getFechaProximaVisita());
			visitaDao.merge(visita);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			tx.close();
		}
		return false;
	}

	public boolean nuevaVisita(VisitaDataList dataVisita) {

		Tecnico tecnico = null;
		Estacion estacion = ControlEmpresa.getInstancia().obtenerEstacion(
				dataVisita.getEstacionData().getId());
		if (dataVisita.getTecnicoData() != null) {
			tecnico = ControlPersona.getInstancia().obtenerTecnico(
					dataVisita.getTecnicoData().getId());
		}
		DaoTransaction tx = new DaoTransaction();
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataVisita.getFechaProximaVisita());
			tx.begin();
			VisitaDao visitaDao = new VisitaDao();
			Visita visita = new Visita();
			visita.setEstacion(estacion);
			visita.setTecnico(tecnico);
			visita.setEstado(dataVisita.getEstado());
			visita.setFechaProximaVisita(dataVisita.getFechaProximaVisita());
			visita.setFechaCreada(new Date());
			visitaDao.save(visita);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return false;
	}

	public boolean modificarVisitaWS(int idVisita, Date fechaInicio,
			Date fechaFin, byte[] bytes, String comentarioFirma, EstadoVisita estadoVisita) throws Exception  {

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			VisitaDao visitaDao = new VisitaDao();

			Visita visita = visitaDao.get(idVisita);

			if (visita != null) {
				agregarFirma(bytes, comentarioFirma, visita);

				visita.setFechaInicio(fechaInicio);
				visita.setFechaFin(fechaFin);

				if (estadoVisita != null) {
					visita.setEstado(estadoVisita);

					if (estadoVisita.equals(EstadoVisita.REALIZADA)){
						visita.setFechaRealizada(new Date());
					}
				}
				visitaDao.save(visita);
				tx.commit();
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			tx.rollback();
			throw e;
		} finally {
			tx.close();
		}
	}

	private void agregarFirma(byte[] bytes, String comentarioFirma, Visita visita) throws IOException {
		if (bytes != null) {
			String path = agregarFirma(bytes, visita.getId());
			if (!path.isEmpty()) {
				if (visita.getFirma() != null) {
					visita.getFirma().setPath(path);
					visita.getFirma().setComentario(comentarioFirma);
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					if(new PropiedadEntorno().getEntorno().equals("produccion")){
						visita.getFirma().setUrl("http://179.27.66.44:6912/produccion/firmas/" + nombreArchivo);	
					}else{
						visita.getFirma().setUrl("http://179.27.66.44:6912/testing/firmas/" + nombreArchivo);
					}
				} else {
					Firma firma = new Firma();
					firma.setPath(path);
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					if(new PropiedadEntorno().getEntorno().equals("produccion")){
						firma.setUrl("http://179.27.66.44:6912/produccion/firmas/" + nombreArchivo);	
					}else{
						firma.setUrl("http://179.27.66.44:6912/testing/firmas/" + nombreArchivo);
					}
					firma.setComentario(comentarioFirma);
					visita.setFirma(firma);
				}
				if(new PropiedadEntorno().getEntorno().equals("produccion")){
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					visita.getFirma().setUrl("http://179.27.66.44:6912/produccion/firmas/" + nombreArchivo);	
					visita.getFirma().setComentario(comentarioFirma);
				}else{
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					visita.getFirma().setUrl("http://179.27.66.44:6912/testing/firmas/" + nombreArchivo);
					visita.getFirma().setComentario(comentarioFirma);
				}
			}
		}
	}

	private String agregarFirma(byte[] bytes, int idVisita) throws IOException {

		if (bytes != null) {
			DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			// String fileName = "/var/SGM2/firmas/" + idVisita + "_" + new
			// Date().toString();
			//String path = "/var/www/html/testing";
			String path = new PropiedadUrlFirma().getURLPropertie(false);

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String pathRelative = idVisita + "_" + format.format(new Date()) + ".png";
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

	public ArrayList<VisitaDataList> obtenerVisitasAsignadas(int idTecnico) {
		ArrayList<VisitaDataList> retorno = new ArrayList<VisitaDataList>();

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();

			VisitaDao dao = new VisitaDao();

			List<Visita> lista = dao.obtenerVisitasAsignadas(new Tecnico(idTecnico));

			for (Visita v : lista) {
				VisitaDataList vdl = v.getVisitaDataList();
				vdl.setDiasSinVisita(app.server.UtilFechas.getDiasSinVisitas(v.getEstacion().getFechaUltimaVisita()));
				retorno.add(vdl);
			}

		} catch (Exception e) {
			// tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public VisitaData obtenerVisitaDataWS(int idVisita) throws Exception {

		VisitaData visitaData = new VisitaData();
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();

			Visita visita = new VisitaDao().get(idVisita);
			if (visita != null) {
				visitaData = visita.getVisitaData();
				visitaData.setFechaInicio(app.server.UtilFechas.getFormattedDate(visita.getFechaInicio()));
				visitaData.setFechaFin(app.server.UtilFechas.getFormattedDate(visita.getFechaFin()));
				String fechaProx = app.server.UtilFechas.getMonthForInt(visita.getFechaProximaVisita());
				visitaData.setFechaProximaVisita(fechaProx);
			}

			PreventivoDao daoPreventivo = new PreventivoDao();
			for (ActivoData activoData : visitaData.getListaActivos()) {

				activoData.setUltimaModificacion(daoPreventivo.getUltimaModificacion(visitaData.getId(), activoData.getId()));
				activoData.setFueModificado();

				Preventivo preventivo = daoPreventivo.get(visita.getId(), activoData.getId());

				activoData.setEstadoPreventivo(EstadoPreventivoData.SIN_INICIAR);

				if(preventivo != null && preventivo.getChequeo() != null && preventivo.getChequeo().estaCompleto(preventivo.getActivo())) {
					activoData.setEstadoPreventivo(EstadoPreventivoData.COMPLETO);
				} else {
					if(activoData.fueModificado()) {
						activoData.setEstadoPreventivo(EstadoPreventivoData.INICIADO);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw e;
		} finally {
			tx.close();
		}
		return visitaData;

	}

	public Visita obtenerVisita(int idVisita) {

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			VisitaDao dao = new VisitaDao();
			return dao.get(idVisita);

		} catch (Exception e) {
			// tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return null;

	}

	public List<VisitaDataList> obtenerListaVisitasDataPorFiltroWeb(VisitaDataList visitaData, int sello){
		List<VisitaDataList> retorno = new ArrayList<VisitaDataList>();

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			retorno =  new VisitaDao().obtenerVisitasPorFiltro(visitaData, sello);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public List<VisitaDataList> obtenerListaVisitasDataWeb(int sello) {
		List<VisitaDataList> retorno = null;

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			retorno = new VisitaDao().obtenerVisitasDataList(sello);
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public List<VisitaDataList> obtenerListaVistasDataEstacionWeb(String nombreEmpresa) {
		List<VisitaDataList> retorno = null;
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			VisitaDao dao = new VisitaDao();
			retorno = dao.obtenerVisitasPorEmpresa(nombreEmpresa);
			return retorno;
		} catch (Exception e) {
			// tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public Boolean eliminarVisita(VisitaDataList dataVisita) {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			VisitaDao dao = new VisitaDao();
			Visita visita = dao.get(dataVisita.getId());
			visita.setInactiva(true);
			dao.update(visita);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return false;
	}

	public boolean modificarEstadoVisita(int idVisita, EstadoVisita estado, boolean notificada, Date fechaRealizada) throws Exception {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			VisitaDao dao = new VisitaDao();
			Visita visita = dao.get(idVisita);
			visita.setEstado(estado);
			visita.setNotificada(notificada);
			visita.setFechaRealizada(fechaRealizada);

			if (estado.equals(EstadoVisita.REALIZADA)){
				visita.getEstacion().setFechaUltimaVisita(fechaRealizada);
			}

			dao.update(visita);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			tx.close();
		}
	}

	public void finalizarVisita(int idVisita, String nombreEstacion, int idEstacion, String fecha, List<String> mails) throws Exception {
		List<String> mailsExito = new ArrayList<String>();
		List<String> mailsFallidos = new ArrayList<String>();
		
		Estacion estacion = ControlEmpresa.getInstancia().buscarEstacion(idEstacion);
		Organizacion organizacion = estacion.getSello().getId() == Sello.ANCAP_CONTRATOS ? Organizacion.Bilpa : Organizacion.Operador;
		byte[] reporte = ControlVisitaReporteWebOperador.getInstancia().crearPDFPreventivosVisitasBytes(idVisita, organizacion);

		int totalMails = enviarReporteOperador(idVisita, mails, nombreEstacion, fecha, reporte, mailsExito, mailsFallidos);
		ControlEmpresa.getInstancia().actualizarCorreosEstacion(idEstacion, mails);
		modificarEstadoVisita(idVisita, mailsFallidos, totalMails, EstadoVisita.REALIZADA);

		ControlOrden.getInstancia().crearOrdenReparacionesDePreventivo(idVisita, idEstacion);
		
		if (mailsFallidos.size() > 0){
			enviarReporteABilpaPorEnvioFallido(nombreEstacion, reporte, mailsExito, mailsFallidos);
		}
	}

	private void enviarReporteABilpaPorEnvioFallido(String estacion, byte[] reporte, List<String> mailsExito, List<String> mailsFallidos)
			throws Exception {
		String textoDireccionesQueSiSeEnvio = "";
		if (mailsExito.size() > 0){
			textoDireccionesQueSiSeEnvio = getTextoDeMails("Para su información, si fue posible notificar a las siguientes direcciones: ", mailsExito);
		}

		if (mailsFallidos.size() > 0){
			String textoDireccionesQueNoSeEnvio = getTextoDeMails("No se pudo hacer entrega del reporte de mantenimiento preventivo adjunto, a la siguientes direcciones: ", mailsFallidos);
			String destinatarios = new PropiedadMailPreventivos().getDestinatarios();
			ControlEmailVisita.getInstancia().notificarEnvioFallido(
					destinatarios,
					"Bilpa SGM | Notificación de envío fallido de reporte a operador de E.S. " + estacion,
					textoDireccionesQueNoSeEnvio,
					textoDireccionesQueSiSeEnvio, 
					reporte, 
					true);
		}
	}

	private String getTextoDeMails(String texto, List<String> mails) {
		String mailsSalida = "";
		for (int i = 0; i < mails.size(); i++){
			String mailExito = mails.get(i);
			if (i == mails.size()-1){//el ultimo
				mailsSalida = mailsSalida.concat(mailExito);
			} else {
				mailsSalida = mailsSalida.concat(mailExito+", ");
			}
		}
		return texto.concat(mailsSalida).concat(".");
	}

	private void modificarEstadoVisita(int idVisita, List<String> mailsFallidos, int totalMails, EstadoVisita estado) throws Exception {
		boolean notificada;
		if (totalMails == mailsFallidos.size()){//fallaron todos los correos
			notificada = false;
		} else {
			notificada = true;
		}

		ControlVisita.getInstancia().modificarEstadoVisita(idVisita, estado, notificada, new Date());
	}

	private int enviarReporteOperador(int idVisita, List<String> mails, String estacion, String fecha, byte[] reporte, List<String> mailsExito, List<String> mailsFallidos) {
		int totalMails = 0;
		for(String destinatario : mails) {
			if(!destinatario.isEmpty()) {
				totalMails++;
				try{
					ControlEmailVisita.getInstancia().notificarEnvioExitoso( 
							destinatario, 
							estacion + " | Reporte de mantenimiento preventivo | " + fecha, 
							"Le enviamos adjunto un informe del mantenimiento preventivo correspondiente a " + fecha + ".", 
							"Correo enviado desde estación " + estacion + ", al finalizar las tareas de mantenimiento preventivo.", 
							reporte,
							true);

					mailsExito.add(destinatario);
				}catch(Exception e){
					mailsFallidos.add(destinatario);
				}
			}
		}
		return totalMails;
	}

	public static String agregarComentarios(ItemChequeado icd, String texto, boolean incluirLosNoImprimibles) {
		List<ComentarioChequeo> comentarios = new ComentarioChequeoDao().getByItemChequeado(icd);
		if (comentarios.size() > 0){
			texto+=" ( ";
			int i = 0;

			List<ComentarioChequeo> comentarios2 = new ArrayList<ComentarioChequeo>();
			if (!incluirLosNoImprimibles){
				for (ComentarioChequeo cc : comentarios){
					if (cc.isImprimible()){
						comentarios2.add(cc);
					}
				}
			} else {
				comentarios2 = comentarios;
			}
			
			for (ComentarioChequeo cc : comentarios2){
				i++;

				texto+= cc.getTexto();

				if (i == comentarios2.size()){
					texto+=" )";
				} else {
					texto+=" | ";
				}
			}
		}
		return texto;
	}
}
