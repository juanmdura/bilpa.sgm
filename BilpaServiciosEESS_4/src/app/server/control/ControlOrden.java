package app.server.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import app.client.dominio.Activo;
import app.client.dominio.CantidadOrdenes;
import app.client.dominio.Chequeo;
import app.client.dominio.ChequeoPico;
import app.client.dominio.Comentario;
import app.client.dominio.Corregido;
import app.client.dominio.DestinoDelCargo;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Firma;
import app.client.dominio.HistoricoOrden;
import app.client.dominio.Orden;
import app.client.dominio.Organizacion;
import app.client.dominio.Pendiente;
import app.client.dominio.Persona;
import app.client.dominio.Pico;
import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Sello;
import app.client.dominio.Solucion;
import app.client.dominio.Tecnico;
import app.client.dominio.TipoChequeo;
import app.client.dominio.TipoTrabajo;
import app.client.dominio.Visita;
import app.client.dominio.data.DatoConsultaHistoricoOrdenes;
import app.client.dominio.data.DatoConsultaHistoricoOrdenesTecnico;
import app.client.dominio.data.DatoOrdenesActivasEmpresa;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.dominio.data.EstacionDataList;
import app.client.dominio.data.OrdenData;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.TecnicoData;
import app.client.dominio.json.CorrectivoJson;
import app.client.utilidades.UtilFechas;
import app.client.utilidades.UtilOrden;
import app.client.utilidades.utilObjects.filter.orden.ListaOrdenesData;
import app.server.control.visita.mail.ControlEmailVisita;
import app.server.persistencia.ActivoDao;
import app.server.persistencia.CantidadOrdenesDao;
import app.server.persistencia.ComentarioDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.DestinoDelCargoDao;
import app.server.persistencia.EstacionDao;
import app.server.persistencia.FallaReportadaDao;
import app.server.persistencia.HistoricoOrdenDao;
import app.server.persistencia.OrdenDao;
import app.server.persistencia.PendienteDao;
import app.server.persistencia.PersonaDao;
import app.server.persistencia.RepuestoLineaDao;
import app.server.persistencia.SolucionDao;
import app.server.persistencia.TecnicoDao;
import app.server.persistencia.TipoTrabajoDao;
import app.server.persistencia.VisitaDao;
import app.server.propiedades.PropiedadEntorno;
import app.server.propiedades.PropiedadMailPreventivos;
import app.server.propiedades.PropiedadUrlFirma;

public class ControlOrden {

	private static ControlOrden instancia = null;

	public static ControlOrden getInstancia() {
		if (instancia == null) {
			instancia = new ControlOrden();
		}
		return instancia;
	}

	public static void setInstancia(ControlOrden instancia) {
		ControlOrden.instancia = instancia;
	}

	private ControlOrden() {
	}

	public List<Activo> obtenerActivosXEmpresa(int empresaId) {

		Estacion e = ControlEmpresa.getInstancia().buscarEstacion(empresaId);

		List<Activo> todosLosActivos = new ArrayList<Activo>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			ActivoDao dao = new ActivoDao();

			List<Activo> lista = dao.obtenerActivosEmpresa(e);


			for (Activo a : lista) {
				todosLosActivos.add(a.copiarTodo());
			}

			return todosLosActivos;

		} catch (Exception ex) {

			tx.rollback();
			ex.printStackTrace();

		} finally {

			tx.close();
		}

		return todosLosActivos;
	}

	public OrdenData buscarCorrectivo(int numero) throws ParseException {

		Orden o = buscarOrden(numero);
		OrdenData auxiliar = new OrdenData();
		copiarOrdenes(o, auxiliar);
		return auxiliar;
	}

	public Orden buscarOrden(int numero) {

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			Orden orden = new OrdenDao().buscarOrden(numero);
			if (orden != null) {
				return orden.copiarTodo();
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return null;
	}

	public boolean validarNumeroDUCSA(String numeroDucsa) {

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();

			List<Orden> lista = dao.list();

			for (Orden o : lista) {
				if (o.getNumeroParteDucsa() == numeroDucsa) {
					return false;
				}
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Orden> obtenerTodasLasOrdenesActivas() {

		ArrayList<Orden> todosLasOrdenesActivas = new ArrayList<Orden>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();

			List<Orden> lista = dao.obtenerOrdenesActivas();
			List<Orden> lista2 = dao.obtenerOrdenesAsignadas();

			for (Orden o : lista2) {
				todosLasOrdenesActivas.add(o.copiarTodo());
			}

			for (Orden o2 : lista) {
				todosLasOrdenesActivas.add(o2.copiarTodo());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		Collections.sort(todosLasOrdenesActivas);

		return todosLasOrdenesActivas;
	}

	public Orden obtenerNuevaOrdenSinGuardarEnBase(Estacion empresa,
			Persona creador, Persona sesion) {
		Orden nuevaOrden = new Orden(new Date(), empresa, 1);// estado 1 =
		// Iniciada
		nuevaOrden.setFechaInicio(new Date());
		nuevaOrden.setEmpresa(empresa);
		nuevaOrden.setPrioridad("Normal");
		nuevaOrden.setCreador(creador);
		// nuevaOrden = this.guardarOrden(nuevaOrden, sesion);
		return nuevaOrden;
	}

	public ArrayList<String> obtenerPrioridades() {
		ArrayList<String> prioridades = new ArrayList<String>();
		prioridades.add("Normal");
		prioridades.add("Alta");
		prioridades.add("Baja");

		return prioridades;
	}

	public DestinoDelCargo buscarDestinoDelCargo(int id) {

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			DestinoDelCargoDao dao = new DestinoDelCargoDao();

			DestinoDelCargo ddc = dao.get(id);
			return ddc;

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return null;
	}

	public ArrayList<DestinoDelCargoData> obtenerDestinosDelCargo() {
		ArrayList<DestinoDelCargoData> retorno = new ArrayList<DestinoDelCargoData>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			DestinoDelCargoDao dao = new DestinoDelCargoDao();

			List<DestinoDelCargo> lista = dao.list();

			for (DestinoDelCargo destinoDelCargo : lista) {

				int id = destinoDelCargo.getId();

				String nombre = destinoDelCargo.getNombre();

				DestinoDelCargoData d = new DestinoDelCargoData(id, nombre);
				retorno.add(d);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();

		} finally {
			tx.close();
		}
		return retorno;

	}

	public TipoTrabajo obtenerTipoDeTrabajo(int id) {
		TipoTrabajo tipoTrabajo = null;
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();

			tipoTrabajo = new TipoTrabajoDao().get(id);
			if (tipoTrabajo != null) {
				return tipoTrabajo.copiar();
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();

		} finally {
			tx.close();
		}

		return tipoTrabajo;

	}

	public ArrayList<TipoTrabajo> obtenerTiposDeTrabajo() {
		ArrayList<TipoTrabajo> todosLosTipoTrabajo = new ArrayList<TipoTrabajo>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			TipoTrabajoDao dao = new TipoTrabajoDao();

			List<TipoTrabajo> lista = dao.list();
			todosLosTipoTrabajo = (ArrayList<TipoTrabajo>) lista;

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();

		} finally {
			tx.close();
		}

		return todosLosTipoTrabajo;
	}

	private void notificarPorMail(Orden nuevaOrden, String estadoOrden) {

		if (estadoOrden.equalsIgnoreCase("alta")) {
			ControlPersona.getInstancia().notificarEncargados(nuevaOrden);
		}

		if (estadoOrden.equalsIgnoreCase("seguimientoInicial")) {
			if (nuevaOrden.getTecnicoAsignado() != null) {
				ControlPersona.getInstancia().notificarTecnico(nuevaOrden);
			}
		}
		if (estadoOrden.equalsIgnoreCase("reparado")) {
			ControlPersona.getInstancia().notificarEncargadosReparado(
					nuevaOrden);
		}
	}

	public ArrayList<OrdenData> ordenesActivasTecnicoData(Tecnico tecnico) {
		ArrayList<OrdenData> retorno = new ArrayList<OrdenData>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.ordenesActivasTecnico(tecnico);

			Collections.sort(lista);

			for (Orden o : lista) {
				OrdenData auxiliar = new OrdenData();
				copiarOrdenes(o, auxiliar);
				retorno.add(auxiliar);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public ArrayList<Orden> obtenerOrdenesCerradas() {
		ArrayList<Orden> retorno = new ArrayList<Orden>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.obtenerOrdenesCerradas();

			for (Orden o : lista) {
				Orden o2 = new Orden();
				o2 = o.copiarTodo();
				retorno.add(o2);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public ArrayList<DatoConsultaHistoricoOrdenesTecnico> obtenerHistoricoOrdenesTecnicos() {
		ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();
		tecnicos = ControlPersona.getInstancia().listaTecnicos();

		ArrayList<DatoConsultaHistoricoOrdenesTecnico> retorno = new ArrayList<DatoConsultaHistoricoOrdenesTecnico>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			OrdenDao dao = new OrdenDao();
			for (Tecnico tecnico : tecnicos) {

				List<Orden> lista = dao.ordenesAsiganadasTecnico(tecnico);
				DatoConsultaHistoricoOrdenesTecnico dt = new DatoConsultaHistoricoOrdenesTecnico(
						tecnico.toString(), lista.size());
				retorno.add(dt);
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public ArrayList<DatoConsultaHistoricoOrdenesTecnico> obtenerHistoricoOrdenesTecnicosDelimitado(
			Date inicio, Date fin) {
		ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();
		tecnicos = ControlPersona.getInstancia().listaTecnicos();

		ArrayList<DatoConsultaHistoricoOrdenesTecnico> retorno = new ArrayList<DatoConsultaHistoricoOrdenesTecnico>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			OrdenDao dao = new OrdenDao();
			for (Tecnico tecnico : tecnicos) {

				List<Orden> lista = dao.ordenesAsiganadasTecnicoTiempo(tecnico,
						inicio, fin);
				DatoConsultaHistoricoOrdenesTecnico dt = new DatoConsultaHistoricoOrdenesTecnico(
						tecnico.toString(), lista.size());
				retorno.add(dt);
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}

		Collections.sort(retorno);
		Collections.reverse(retorno);
		return retorno;
	}

	public boolean validarOrdenesUsuario(int idUsuario) {
		boolean retorno = true;
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.list();

			for (Orden orden : lista) {
				if (orden.getAnulador() != null) {
					if (orden.getAnulador().getId() == idUsuario) {
						return false;
					}
				}
				if (orden.getTecnicoAsignado() != null) {
					if (orden.getTecnicoAsignado().getId() == idUsuario) {
						return false;
					}
				}
				if (orden.getCreador() != null) {
					if (orden.getCreador().getId() == idUsuario) {
						return false;
					}
				}
				if (orden.getContacto() != null) {
					if (orden.getContacto().getId() == idUsuario) {
						return false;
					}
				}

			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public ArrayList<DatoOrdenesActivasEmpresa> ordenesActivasDeUnaEmpresa(Estacion empresa) {
		ArrayList<DatoOrdenesActivasEmpresa> retorno = new ArrayList<DatoOrdenesActivasEmpresa>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			EstacionDao daoEstacion = new EstacionDao();
			empresa = daoEstacion.get(empresa.getId());

			List<Orden> lista = dao.ordenesActivasEmpresa(empresa);

			for (Orden orden : lista) {
				DatoOrdenesActivasEmpresa dato = new DatoOrdenesActivasEmpresa();
				dato.setAutor(orden.getCreador().toString());
				dato.setNumero(orden.getNumero() + "");
				dato.setEstado(orden.getEstadoOrden());
				dato.setFecha(new SimpleDateFormat(UtilFechas.DATE_FORMAT).format(orden.getFechaInicio()) + "");
				if (orden.getTecnicoAsignado() != null) {
					dato.setTecnico(orden.getTecnicoAsignado().toString());
				} else {
					dato.setTecnico("Sin Asignar");
				}
				retorno.add(dato);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public ArrayList<DatoConsultaHistoricoOrdenes> historicoOrdenesFinalizadasEmpresa(
			Estacion empresa) {
		ArrayList<DatoConsultaHistoricoOrdenes> ordenesDatoRetorno = new ArrayList<DatoConsultaHistoricoOrdenes>();
		DaoTransaction tx = new DaoTransaction();
		// aca
		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			EstacionDao daoEstacion = new EstacionDao();
			empresa = daoEstacion.get(empresa.getId());

			List<Orden> listaOrdenes = dao
					.historicoOrdenesFinalizadasEmpresa(empresa);

			ArrayList<Orden> ultimas3 = this.ultimasOrdenes(listaOrdenes, 3);
			for (Orden o : ultimas3) {
				DatoConsultaHistoricoOrdenes ordenDato = new DatoConsultaHistoricoOrdenes();
				ordenDato.setFecha(o.getFechaFin());
				ordenDato.setNro(o.getNumero() + "");
				for (Reparacion r : o.getReparaciones()) {
					ordenDato.getReparaciones().add(r.copiarTodo());
				}
				ordenesDatoRetorno.add(ordenDato);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return ordenesDatoRetorno;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Orden> ultimasOrdenes(List<Orden> ordenes, int cantidad) {
		ArrayList<Orden> retorno = new ArrayList<Orden>();
		if (ordenes != null && ordenes.size() > 0 && cantidad > 0) {
			Collections.sort(ordenes);

			if (ordenes.size() >= cantidad) {
				for (int i = 0; i < cantidad; i++) {
					Orden auxiliar = (Orden) ordenes.get(i);
					retorno.add(auxiliar);
				}
			} else {
				for (int i = 0; i < ordenes.size(); i++) {
					Orden auxiliar = (Orden) ordenes.get(i);
					retorno.add(auxiliar);
				}
			}
		}
		return retorno;
	}

	public boolean esEstacionSinOrdenes(Estacion estacion) {
		DaoTransaction tx = new DaoTransaction();
		// retorna true si no tiene ordenes.
		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.list();

			for (Orden orden : lista) {
				if (orden.getEmpresa().getId() == estacion.getId()) {
					return false;
				}
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return true;
	}

	// ESCRITURA
	public boolean cerrarOrden(Orden orden, Persona sesion) {
		if (orden != null) {
			if (orden.tieneSoluciones()) {

				// actualizarComentarios(orden);

				DaoTransaction tx = new DaoTransaction();

				try {
					tx.begin();
					orden.setEstadoOrden(3);// cerrada
					orden.setFechaFin(new Date());
					OrdenDao dao = new OrdenDao();

					dao.merge(orden);

					tx.commit();
					actulizarHistorico(orden, sesion);
					return true;
				} catch (Exception e) {
					tx.rollback();
					e.printStackTrace();
				} finally {
					tx.close();
				}
			}
		}
		return false;
	}

	public boolean guardarOrden(Orden orden, String estadoOrden, Persona sesion) {
		if (orden != null) {
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();

				OrdenDao dao = new OrdenDao();

				dao.save(orden);
				tx.commit();

				orden.setReparaciones(new HashSet<Reparacion>());
				orden.setRepuestosLineas((new HashSet<RepuestoLinea>()));

				notificarPorMail(orden, estadoOrden);
				actulizarHistorico(orden, sesion);

				return true;

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally {
				tx.close();
			}
		}
		return false;
	}

	public boolean actualizarOrden(CorrectivoJson correctivoJson) throws Exception {
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			Orden orden = dao.buscarOrden(correctivoJson.getNumero());
			if (orden != null) {
				orden.setEstadoOrden(correctivoJson.getEstado());
				if (correctivoJson.getInicio() != null){
					orden.setInicioService(correctivoJson.getInicio());
				}
				if (correctivoJson.getFin() != null){
					orden.setFinService(correctivoJson.getFin());
				}
				if (correctivoJson.getInicioReal() != null){
					orden.setInicioServiceReal(correctivoJson.getInicioReal());
				}
				if (correctivoJson.getFinReal() != null){
					orden.setFinServiceReal(correctivoJson.getFinReal());
				}

				String firmaStr = correctivoJson.getFirma();
				if (firmaStr != null /*&& correctivoJson.getEstado() == 5*/){
					byte[] firmaBytes = (firmaStr != null && firmaStr.length() > 0) ? new Base64().decode(firmaStr.getBytes()) : null;
					agregarFirma(firmaBytes, correctivoJson.getComentarioFirma(), orden);
				}

				dao.merge(orden);
				tx.commit();
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			throw e;
		} finally {
			tx.close();
		}
		return false;
	}

	private void agregarFirma(byte[] bytes, String comentarioFirma, Orden orden) throws IOException {
		if (bytes != null) {
			String path = agregarFirmaFile(bytes, orden.getNumero());
			if (!path.isEmpty()) {
				if (orden.getFirma() != null) {
					orden.getFirma().setPath(path);
					orden.getFirma().setComentario(comentarioFirma);
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					if(new PropiedadEntorno().getEntorno().equals("produccion")){
						orden.getFirma().setUrl("http://179.27.66.44:6912/produccion/firmas_correctivo/" + nombreArchivo);	
					}else{
						orden.getFirma().setUrl("http://179.27.66.44:6912/testing/firmas_correctivo/" + nombreArchivo);
					}
				} else {
					Firma firma = new Firma();
					firma.setPath(path);
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					if(new PropiedadEntorno().getEntorno().equals("produccion")){
						firma.setUrl("http://179.27.66.44:6912/produccion/firmas_correctivo/" + nombreArchivo);	
					}else{
						firma.setUrl("http://179.27.66.44:6912/testing/firmas_correctivo/" + nombreArchivo);
					}
					firma.setComentario(comentarioFirma);
					orden.setFirma(firma);
				}
				if(new PropiedadEntorno().getEntorno().equals("produccion")){
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					orden.getFirma().setUrl("http://179.27.66.44:6912/produccion/firmas_correctivo/" + nombreArchivo);	
					orden.getFirma().setComentario(comentarioFirma);
				}else{
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					orden.getFirma().setUrl("http://179.27.66.44:6912/testing/firmas_correctivo/" + nombreArchivo);
					orden.getFirma().setComentario(comentarioFirma);
				}
			}
		}
	}

	private String agregarFirmaFile(byte[] bytes, int numero) throws IOException {
		if (bytes != null) {
			DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			String path = new PropiedadUrlFirma().getURLPropertie(true);

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String pathRelative = numero + "_" + format.format(new Date()) + ".png";
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

	public boolean actualizarOrden(Orden orden, String estadoOrden,
			Persona sesion) {
		if (orden != null) {

			setearComentarios(orden, sesion);

			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();
				OrdenDao dao = new OrdenDao();
				for(Reparacion rep : orden.getReparaciones()){
					if (rep.getPendiente() != null){
						PendienteDao daoPendiente = new PendienteDao();
						Pendiente pendiente = daoPendiente.get(rep.getPendiente().getId());
						pendiente.setEstado(EstadoPendiente.CORRECTIVO_ASIGNADO);
						pendiente.setOrdenAsignada(rep.getOrden());
						daoPendiente.update(pendiente);
					}
				}
				dao.merge(orden);
				tx.commit();

				notificarPorMail(orden, estadoOrden);
				actulizarHistorico(orden, sesion);
				return true;

			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback();
			} finally {
				tx.close();
			}
		}
		return false;
	}

	private void setearComentarios(Orden orden, Persona sesion) {
		for (Reparacion reparacion : orden.getReparaciones()) {
			for (Solucion solucion : reparacion.getSoluciones()) {
				if (solucion.getComentario() != null) {
					solucion.getComentario().setUsuario(sesion);
					solucion.getComentario().setFecha(obtenerFechaServidor());
					solucion.getComentario().setOrden(orden);
				}
			}
		}
	}

	public boolean removerRepuestosDeOrden(
			ArrayList<RepuestoLinea> repuetosLineaARemover) {
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			RepuestoLineaDao dao = new RepuestoLineaDao();
			for (RepuestoLinea rl : repuetosLineaARemover) {
				dao.delete(rl);
			}
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

	public boolean actualizarOrdenRebotada(Orden orden, Tecnico tecnico,
			Persona sesion) {
		if (orden != null) {
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();
				OrdenDao dao = new OrdenDao();
				dao.merge(orden);
				tx.commit();

				ControlPersona.getInstancia().notificarEncargadosOrdenRebotada(
						orden, tecnico);
				actulizarHistorico(orden, sesion);
				return true;

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally {
				tx.close();
			}
		}
		return false;
	}

	public boolean eliminarOrden(Orden orden, Persona sesion) {
		if (orden != null) {
			DaoTransaction tx = new DaoTransaction();

			try {
				if (eliminarHistoricoOrden(orden)) {
					tx.begin();
					OrdenDao dao = new OrdenDao();
					dao.delete(orden);
					tx.commit();
					return true;
				} else {
					return false;
				}

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally {
				tx.close();
			}
		}
		return false;
	}

	public boolean eliminarHistoricoOrden(Orden orden) {
		if (orden != null) {
			DaoTransaction tx = new DaoTransaction();

			try {
				ArrayList<HistoricoOrden> historicos = obtenerHistorico(orden);

				tx.begin();
				HistoricoOrdenDao daoH = new HistoricoOrdenDao();

				for (HistoricoOrden ho : historicos) {
					daoH.delete(ho);
				}

				tx.commit();

				return true;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally {
				tx.close();
			}
		}
		return false;
	}

	private ArrayList<HistoricoOrden> obtenerHistorico(Orden orden) {
		ArrayList<HistoricoOrden> historicos = new ArrayList<HistoricoOrden>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			HistoricoOrdenDao dao = new HistoricoOrdenDao();

			List<HistoricoOrden> lista = dao.list();
			historicos = (ArrayList<HistoricoOrden>) lista;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return historicos;
	}

	public boolean guardarOrdenAnulada(Orden orden, Persona sesion) {
		if (orden != null && orden.getAnulador() != null
				&& !orden.getComentarioAnulada().equalsIgnoreCase("")) {
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();
				OrdenDao dao = new OrdenDao();

				Orden ordenAAnular = dao.get(orden.getNumero());
				ordenAAnular.setEstadoOrden(4);// anulada
				ordenAAnular.setComentarioAnulada(orden.getComentarioAnulada());
				ordenAAnular.setAnulador(orden.getAnulador());

				dao.merge(ordenAAnular);

				tx.commit();
				actulizarHistorico(orden, sesion);
				return true;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally {
				tx.close();
			}
		}

		return false;
	}

	private void actulizarHistorico(Orden orden, Persona sesion) {
		HistoricoOrden h = new HistoricoOrden(orden, new Date(), sesion);
		guardarHistorico(h);
	}

	public boolean guardarHistorico(HistoricoOrden h) {
		if (h != null) {
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();

				HistoricoOrdenDao dao = new HistoricoOrdenDao();

				dao.save(h);
				tx.commit();

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				return false;
			} finally {
				tx.close();
			}
			return false;
		}
		return true;
	}

	public ArrayList<Orden> obtenerUltimasOrdenesInactivas(int cantidad,
			Persona sesion) {
		ArrayList<Orden> retorno = new ArrayList<Orden>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.ultimasOrdenes(cantidad);

			for (Orden o : lista) {
				Orden o2 = new Orden();
				o2 = o.copiarTodo();
				retorno.add(o2);
			}
			guardarCantidadOrdenes(cantidad, sesion);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	private void guardarCantidadOrdenes(int cantidadNueva, Persona sesion) {
		CantidadOrdenes cantidadBD = obtenerCantidadOrdenes(sesion);

		DaoTransaction tx = new DaoTransaction();
		try {

			tx.begin();
			CantidadOrdenesDao dao = new CantidadOrdenesDao();
			if (cantidadBD == null) {
				dao.save(new CantidadOrdenes(cantidadNueva, sesion));
			} else {
				cantidadBD.setCantidad(cantidadNueva);
				dao.merge(cantidadBD);
			}

			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
	}

	public CantidadOrdenes obtenerCantidadOrdenes(Persona sesion) {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			CantidadOrdenesDao dao = new CantidadOrdenesDao();
			CantidadOrdenes cantOrdenes = dao.obtenerCantidadOrdenes(sesion);
			obtenerCantidadOrdenesInactivas(cantOrdenes);
			cantOrdenes.setUsuario((Persona) sesion.copiar());
			return cantOrdenes;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return null;
	}

	private void obtenerCantidadOrdenesInactivas(CantidadOrdenes cantOrdenes) {
		OrdenDao dao = new OrdenDao();
		cantOrdenes.setCantidadTotalDeOrdenesInactivas(dao
				.obtenerCantidadOrdenesInactivas());
	}

	public ArrayList<OrdenData> ordenesDataInactivas() {
		ArrayList<OrdenData> retorno = new ArrayList<OrdenData>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.obtenerOrdenesInactivas();

			for (Orden o : lista) {
				OrdenData auxiliar = new OrdenData();
				copiarOrdenes(o, auxiliar);
				retorno.add(auxiliar);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public ArrayList<OrdenData> ordenesDataActivas() {
		ArrayList<OrdenData> retorno = new ArrayList<OrdenData>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.obtenerTodasOrdenesActivas();

			Collections.sort(lista);

			for (Orden o : lista) {
				OrdenData auxiliar = new OrdenData();
				copiarOrdenes(o, auxiliar);

				retorno.add(auxiliar);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	private void copiarOrdenes(Orden o, OrdenData auxiliar) throws ParseException {
		if (o != null && auxiliar != null) {
			SimpleDateFormat format = new SimpleDateFormat(
					UtilFechas.DATE_FORMAT);

			Estacion estacion = (Estacion) o.getEmpresa();
			
			auxiliar.setSello(estacion.getSello().getNombre());			
			auxiliar.setEstacion(estacion.getNombre());
			auxiliar.setIdEstacion(estacion.getId());
			if (estacion.getFoto() != null){
				auxiliar.setFotoEstacion(estacion.getFoto().getUrl());
			}
			if (estacion.getFotoChica() != null){
				auxiliar.setFotoEstacionChica(estacion.getFotoChica().getUrl());
			}
			auxiliar.setLocalidad(estacion.getLocalidad());

			auxiliar.setFechaCumplimiento(o.getFechaCumplimiento());
			auxiliar.setEstado(UtilOrden.getEstadoTexto(o.getEstadoOrden()));
			auxiliar.setIdEstado(o.getEstadoOrden());
			auxiliar.setFechaFin(o.getFechaFin());
			auxiliar.setFechaInicio(format.format(o.getFechaInicio()));

			auxiliar.setFechaCumplimiento2(o.getFechaCumplimiento() != null ? format.format(o.getFechaCumplimiento()) : null);
			auxiliar.setFechaFin2(o.getFechaFin() != null ? format.format(o.getFechaFin()) : null);
			auxiliar.setNumeroDucsa(UtilOrden.getNumeroDucsaInt(o.getNumeroParteDucsa()));

			auxiliar.setNumero(o.getNumero());
			auxiliar.setPrioridad(o.getPrioridad());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(o.getFechaInicio());
			calendar.add(Calendar.HOUR, estacion.getTiempoRespuesta());

			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				calendar.add(Calendar.DAY_OF_WEEK, 2);

			} else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				calendar.add(Calendar.DAY_OF_WEEK, 1);
			}

			auxiliar.setPlazo(format.format(calendar.getTime()));

			auxiliar.setInicioServiceReal(o.getInicioServiceReal() != null ? format.format(o.getInicioServiceReal()) : null);
			auxiliar.setFinServiceReal(o.getFinServiceReal() != null ? format.format(o.getFinServiceReal()) : null);
			auxiliar.setInicioServiceUsuario(o.getInicioService() != null ? format.format(o.getInicioService()) : null);
			auxiliar.setFinServiceUsuario(o.getFinService() != null ? format.format(o.getFinService()) : null);

			if (o.getTecnicoAsignado() != null) {
				auxiliar.setTecnico(o.getTecnicoAsignado().toString());
			} else {
				auxiliar.setTecnico("Sin Asignar");
			}
		}
	}

	public ArrayList<Comentario> obtenerComentarios(Solucion solucion) {
		ArrayList<Comentario> comentarios = new ArrayList<Comentario>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			ComentarioDao dao = new ComentarioDao();

			List<Comentario> lista = dao.obtenerComentarios(solucion);
			for (Comentario c : lista) {
				comentarios.add(c.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return comentarios;
	}

	public ArrayList<DatoOrdenesActivasEmpresa> ordenesPorFecha(
			EstacionDataList estacionData, Date desde, Date hasta) {
		ArrayList<DatoOrdenesActivasEmpresa> retorno = new ArrayList<DatoOrdenesActivasEmpresa>();
		DaoTransaction tx = new DaoTransaction();

		try {
			Estacion estacion = new Estacion();
			estacion.setId(estacionData.getId());

			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.ordenesPorFecha(estacion, desde, hasta);

			for (Orden orden : lista) {
				DatoOrdenesActivasEmpresa dato = new DatoOrdenesActivasEmpresa();
				dato.setAutor(orden.getCreador().toString());
				dato.setNumero(orden.getNumero() + "");
				dato.setEstado(orden.getEstadoOrden());
				dato.setFecha(orden.getFechaFin() + "");
				dato.setIdEmpresa(estacionData.getId());

				if (orden.getTecnicoAsignado() != null) {
					dato.setTecnico(orden.getTecnicoAsignado().toString());
				} else {
					dato.setTecnico("Sin Asignar");
				}
				retorno.add(dato);
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public ArrayList<Comentario> obtenerComentariosImprimibles(Solucion solucion) {
		ArrayList<Comentario> comentarios = new ArrayList<Comentario>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			ComentarioDao dao = new ComentarioDao();

			List<Comentario> lista = dao.obtenerComentarios(solucion);
			for (Comentario c : lista) {
				if (c.isImprimible()) {
					comentarios.add(c.copiar());
				}
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return comentarios;
	}

	public List<PendienteData> obtenerTodosLosPendientesVisibles(Date inicio, Date fin, List<Organizacion> organizaciones){
		List<PendienteData> pendientes = new ArrayList<PendienteData>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			PendienteDao dao = new PendienteDao();


			List<Pendiente> list = dao.pendientesVisibles(inicio, fin, organizaciones);
			PendienteData data;

			for(Pendiente p : list){
				data = new PendienteData();
				data.setId(p.getId());
				data.setComentario(p.getComentario());
				data.setPlazo(p.getPlazo());

				if(p.getOrganizacion().toString()!=null){
					data.setDestinatario(p.getOrganizacion().toString()); 
				}

				if(p.getActivo()!=null){
					data.setActivo(p.getActivo().toString());
					if(p.getActivo().getEmpresa()!=null){
						data.setEmpresa(p.getActivo().getEmpresa().toString());
					}	
				}
				pendientes.add(data);
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return pendientes;
	}

	public ArrayList<OrdenData> obtenerTodasLasOrdenesActivasDataExcel(Sello s,
			Date inicio, Date fin) {
		ArrayList<OrdenData> retorno = new ArrayList<OrdenData>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.ordenesAbiertasCierrePetroleras(s, inicio,
					fin);

			Collections.sort(lista);

			for (Orden o : lista) {
				if (o.getEmpresa().getSello().getId() == s.getId()) {
					OrdenData auxiliar = new OrdenData();
					copiarOrdenes(o, auxiliar);

					retorno.add(auxiliar);
				}
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}

		return retorno;
	}

	public Date obtenerFechaServidor() {
		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public Set<Orden> obtenerOrdenesEstacion(Estacion estacion) {
		Set<Orden> ordenes = new HashSet<Orden>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ordenes = new HashSet<Orden>(
					new OrdenDao().ordenesEmpresa(estacion));
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return ordenes;
	}

	public ArrayList<OrdenData> obtenerOrdenes(OrdenData ordenFilter) {

		ArrayList<OrdenData> ordenes = new ArrayList<OrdenData>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			List<Orden> lista = dao.obtenerOrdenes(ordenFilter);

			Collections.sort(lista);

			for (Orden o : lista) {
				OrdenData auxiliar = new OrdenData();
				copiarOrdenes(o, auxiliar);
				ordenes.add(auxiliar);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return ordenes;
	}

	public ListaOrdenesData obtenerListaOrdenesData() {
		ListaOrdenesData data = new ListaOrdenesData();

		setEstados(data);
		setTecnicos(data);
		setLocalidades(data);
		setEstaciones(data);

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();
			dao.setIndicadores(data);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}

		return data;
	}

	private void setEstados(ListaOrdenesData data) {
		// Horrible, se deberia ir a buscarlos a la base de datos y la tabla
		// Estado deberia tener un corte por Activo/ Inactivo.
		ArrayList<String> estadosActivos = new ArrayList<String>();
		ArrayList<String> estadosInactivos = new ArrayList<String>();

		estadosActivos.add("Iniciada");
		estadosActivos.add("Inspección Pendiente");
		estadosActivos.add("Reparada");
		estadosActivos.add("Iniciada Ducsa");

		estadosInactivos.add("Finalizada");
		estadosInactivos.add("Anulada");

		data.setEstadoActivos(estadosActivos);
		data.setEstadoInactivos(estadosInactivos);
	}

	private void setTecnicos(ListaOrdenesData data) {

		ArrayList<TecnicoData> tecData = new ArrayList<TecnicoData>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			TecnicoDao dao = new TecnicoDao();
			List<Tecnico> lista = dao.list();

			for (Tecnico t : lista) {
				TecnicoData aux = new TecnicoData();
				copiarTecnicos(t, aux);
				tecData.add(aux);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}

		data.setTecnicosData(tecData);
	}

	private void setLocalidades(ListaOrdenesData data) {

		ArrayList<String> localidades = new ArrayList<String>();
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			EstacionDao e = new EstacionDao();
			localidades = (ArrayList<String>) e.getLocalidades();

		}

		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		data.setLocalidades(localidades);
	}

	private void setEstaciones(ListaOrdenesData data) {

		ArrayList<EstacionDataList> estData = new ArrayList<EstacionDataList>();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			EstacionDao dao = new EstacionDao();
			List<Estacion> lista = dao.list();

			for (Estacion e : lista) {
				EstacionDataList aux = new EstacionDataList();
				aux = e.copiarEstacionAEstDataList();
				estData.add(aux);
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}

		data.setEstacionesData(estData);

	}

	private void copiarTecnicos(Tecnico t, TecnicoData aux) {

		if (t != null && aux != null) {
			aux.setId(t.getId());
			aux.setNombre(t.getNombre());
			aux.setApellido(t.getApellido());
			aux.setNombreUsuario(t.getNombreDeUSuario());
			aux.setNombreCompleto(t.getNombre(), t.getApellido());
		}

	}

	public void finalizarCorrectivo(CorrectivoJson correctivoJson) throws Exception {
		List<String> mailsExito = new ArrayList<String>();
		List<String> mailsFallidos = new ArrayList<String>();

		Orden orden = ControlOrden.getInstancia().buscarOrden(correctivoJson.getNumero());

		//File pdf1  =  ControlPDFOrden.getInstancia().crearPDF(orden);//parche para arreglar bug de cantidad de paginas.
		boolean viaBilpa = orden.getEmpresa().getSello().getId() == Sello.ANCAP_CONTRATOS;
		File pdf  =  ControlPDFOrden.getInstancia().crearPDF(orden, viaBilpa);

		// File pdf = ControlPDFOrden.getInstancia().crearPDF(orden);
		byte[] pdfBytes = IOUtils.toByteArray(new FileInputStream(pdf));

		int totalMails = enviarReporteOperador(correctivoJson.getMails(), orden.getEmpresa().getNombre(),
				getFormattedDateSubject(orden.getFechaFin()), getFormattedDateBody(orden.getFechaFin()), pdfBytes, mailsExito, mailsFallidos);
		ControlEmpresa.getInstancia().actualizarCorreosEstacion(orden.getEmpresa().getId(), correctivoJson.getMails());

		if (mailsFallidos.size() > 0){
			enviarReporteABilpaPorEnvioFallido(orden.getEmpresa().getNombre(), pdfBytes, mailsExito, mailsFallidos);
		}
	}

	private void enviarReporteABilpaPorEnvioFallido(String estacion, byte[] reporte, List<String> mailsExito, List<String> mailsFallidos)
			throws Exception {
		String textoDireccionesQueSiSeEnvio = "";
		if (mailsExito.size() > 0){
			textoDireccionesQueSiSeEnvio = getTextoDeMails("Para su información, si fue posible notificar a las siguientes direcciones: ", mailsExito);
		}

		if (mailsFallidos.size() > 0){
			String textoDireccionesQueNoSeEnvio = getTextoDeMails("No se pudo hacer entrega del reporte de mantenimiento correctivo adjunto, a la siguientes direcciones: ", mailsFallidos);
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

	private int enviarReporteOperador(List<String> mails, String estacion, String fechaSubject, String fechaBody, byte[] reporte, List<String> mailsExito, List<String> mailsFallidos) {
		int totalMails = 0;
		for(String destinatario : mails) {
			if(!destinatario.isEmpty()) {
				totalMails++;
				try{
					ControlEmailVisita.getInstancia().notificarEnvioExitoso( 
							destinatario, 
							estacion + " | Reporte de mantenimiento correctivo | " + fechaSubject, 
							"Le enviamos adjunto un informe del mantenimiento correctivo realizado el dia " + fechaBody + ".", 
							"Correo enviado desde estación " + estacion + ", al finalizar las tareas de mantenimiento correctivo.", 
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

	public Comentario obtenerComentario(int id) {
		Comentario retorno = new Comentario();
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			ComentarioDao dao = new ComentarioDao();
			retorno = dao.get(id);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return retorno;
	}

	public static String getFormattedDateSubject(Date date){
		SimpleDateFormat format = new SimpleDateFormat(UtilFechas.DATE_FORMAT_SHORT);
		if(date != null){
			if (date.toString().contains(".")){
				return date.toString().substring(0, date.toString().indexOf("."));
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			return format.format(date).toString();
		}
		return null;
	}

	public static String getFormattedDateBody(Date date){
		DateFormatSymbols symbols = new DateFormatSymbols(new Locale("es"));
		String formatStr1 = "EEEE dd";
		String formatStr2 = "MMMM";
		String formatStr3 = "HH:mm";

		SimpleDateFormat format1 = new SimpleDateFormat(formatStr1, symbols);
		SimpleDateFormat format2 = new SimpleDateFormat(formatStr2, symbols);
		SimpleDateFormat format3 = new SimpleDateFormat(formatStr3, symbols);

		if(date != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			return format1.format(date).toString() + " de " + format2.format(date).toString() + ", a las " + format3.format(date).toString();
		}
		return null;
	}

	public Orden crearOrdenReparacionDePendientes(int idCreador, int idEstacion) {
		Orden orden = new Orden();
		orden.setTipoTrabajo(new TipoTrabajo(13));
		orden.setEstadoOrden(2);
		orden.setCreador(new PersonaDao().get(idCreador));
		orden.setEmpresa(new EstacionDao().get(idEstacion));
		orden.setFechaInicio(new Date());
		orden.setPrioridad("Normal");
		return orden;
	}

	public Orden crearOrdenReparacionesDePreventivo(int idVisita, int idEstacion) {
		DaoTransaction tx = new DaoTransaction();
		Orden orden = new Orden();
		try {
			tx.begin();

			VisitaDao daoVisita = new VisitaDao();
			OrdenDao daoOrden = new OrdenDao();
			
			Visita visita = daoVisita.get(idVisita);

			List<Corregido> corregidos = visita.getCorregidos();
			
			if (corregidos == null || corregidos.size() == 0 ){
				return null;
			}
			
			Date fecha = new Date();

			setOrden(idEstacion, orden, daoOrden, visita, fecha);
			
			//Reparaciones y soluciones
			for (Corregido corregido : corregidos){
				Reparacion reparacion = Reparacion.crearReparacion(corregido.getPreventivo().getActivo());
				reparacion.set(null, corregido.getPreventivo().getActivo(), reparacion.getTipo(), orden);

				// new ReparacionDao().save(reparacion);
				Pico pico = null;
				Chequeo chequeo = corregido.getPreventivo().getChequeo();
				if (chequeo.getTipo().equals(TipoChequeo.Pico)){
					ChequeoPico cp = (ChequeoPico)chequeo;
					pico = cp.getPico();
				}

				Solucion solucion = new Solucion();
				// int id = daoSolucion.save(solucion);
				
				Comentario comentario = null;
				if (corregido.getComentario() != null){
					comentario = corregido.getComentarioObj(orden, visita.getTecnico(), fecha);
					//new ComentarioDao().save(comentario);
				}
				
				solucion = reparacion.agregarSolucion(orden, orden.getSoluciones(), corregido.getTarea(), corregido.getFalla(), new HashSet<RepuestoLinea>(), 
						Boolean.FALSE, pico, new DestinoDelCargoData (corregido.getDestinoDelCargo().getId(), corregido.getDestinoDelCargo().getNombre()), 
						comentario);

				solucion.setCorregido(corregido);
				//solucion.setId(id);
				if (comentario!=null){
					solucion.setComentario(comentario);
				}
				solucion.setFoto(corregido.getFoto());
				solucion.setFoto2(corregido.getFoto2());

				// daoSolucion.update(solucion);
				
				orden.agregarReparacion(reparacion);

				Set<RepuestoLinea> rls = corregido.getListaDeRepuestosLinea(orden, solucion);
				for(RepuestoLinea rl : rls){
					orden.getRepuestosLineas().add(rl);
				}
			}
			
			daoOrden.update(orden);

			//si el corregido es una reaparacion de un pendiente, se setea la solucion al pendiente
			PendienteDao daoPendiente = new PendienteDao();
			for(Corregido corregido : corregidos){
				List<Pendiente> pendientes = daoPendiente.obtenerPendienteDeCorregido(corregido);
				if (pendientes != null && pendientes.size() > 0){
					for (Pendiente pendiente : pendientes) {
						List<Solucion> soluciones = new SolucionDao().obtenerPorCorregido(corregido);
						if (soluciones != null && soluciones.size() > 0) {
							pendiente.setSolucion(soluciones.get(0));
							pendiente.setOrdenReparado(orden);
						}
						daoPendiente.update(pendiente);
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return orden;
	}

	private void setOrden(int idEstacion, Orden orden, OrdenDao daoOrden, Visita visita, Date fecha) {
		//orden
		orden.setTipoTrabajo(new TipoTrabajo(2));
		orden.setEstadoOrden(5);
		orden.setCreador(visita.getTecnico());
		orden.setEmpresa(new EstacionDao().get(idEstacion));
		orden.setFechaInicio(fecha);
		orden.setPrioridad("Normal");
		orden.setTecnicoAsignado(visita.getTecnico());
		orden.setVisita(visita);
		
		daoOrden.save(orden);
	}

	public Reparacion crearReparacionDePendiente(Orden orden, PendienteData pd) {
		Activo activo = new ActivoDao().get(pd.getIdActivo());
		Reparacion reparacion = Reparacion.crearReparacion(activo);
		reparacion.setFallaReportada(new FallaReportadaDao().get(UtilOrden.idFallaPendientes));
		reparacion.setComentario(pd.getComentario());
		reparacion.setActivo(activo);
		reparacion.setOrden(orden);
		reparacion.setTipo(2);
		return reparacion;
	}
}
