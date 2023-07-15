package app.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import app.client.GreetingService;
import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Canio;
import app.client.dominio.CantidadOrdenes;
import app.client.dominio.Comentario;
import app.client.dominio.ContactoEmpresa;
import app.client.dominio.DateDesdeHasta;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.FallaReportada;
import app.client.dominio.FallaTecnica;
import app.client.dominio.HistoricoOrden;
import app.client.dominio.Marca;
import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.Modelo;
import app.client.dominio.ModeloActivoGenerico;
import app.client.dominio.ModeloSurtidor;
import app.client.dominio.Orden;
import app.client.dominio.Organizacion;
import app.client.dominio.Persona;
import app.client.dominio.Producto;
import app.client.dominio.Reparacion;
import app.client.dominio.Repuesto;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Sello;
import app.client.dominio.Solucion;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
import app.client.dominio.Tarea;
import app.client.dominio.Tecnico;
import app.client.dominio.TipoActivoGenerico;
import app.client.dominio.TipoFallaReportada;
import app.client.dominio.TipoFallaTecnica;
import app.client.dominio.TipoRepuesto;
import app.client.dominio.TipoTarea;
import app.client.dominio.TipoTrabajo;
import app.client.dominio.data.ActivoData;
import app.client.dominio.data.DatoConsultaHistoricoOrdenes;
import app.client.dominio.data.DatoConsultaHistoricoOrdenesTecnico;
import app.client.dominio.data.DatoListadoActivos;
import app.client.dominio.data.DatoListadoReparaciones;
import app.client.dominio.data.DatoOrdenesActivasEmpresa;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.dominio.data.EstacionDataList;
import app.client.dominio.data.ItemChequeoData;
import app.client.dominio.data.OrdenData;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PendienteDataUI;
import app.client.dominio.data.RepuestoDatoGrafica;
import app.client.dominio.data.TecnicoData;
import app.client.dominio.data.TipoActivoGenericoData;
import app.client.dominio.data.VisitaDataList;
import app.client.utilidades.utilObjects.filter.orden.ListaOrdenesData;
import app.server.control.ControlActivo;
import app.server.control.ControlActivosGenericos;
import app.server.control.ControlEmpresa;
import app.server.control.ControlExcel;
import app.server.control.ControlExcelCommon;
import app.server.control.ControlExcelPendiente;
import app.server.control.ControlFalla;
import app.server.control.ControlListado;
import app.server.control.ControlOrden;
import app.server.control.ControlPDFOrden;
import app.server.control.ControlPDFPermisoDeTrabajo;
import app.server.control.ControlPendiente;
import app.server.control.ControlPersona;
import app.server.control.ControlPreventivo;
import app.server.control.ControlReparacion;
import app.server.control.ControlRepuesto;
import app.server.control.ControlTipoFalla;
import app.server.control.ControlTipoRepuesto;
import app.server.control.ControlTipoTarea;
import app.server.control.visita.ControlVisita;
import app.server.control.visita.reporte.web.ControlVisitaReporteWebDucsaYBilpa;
import app.server.control.visita.reporte.web.ControlVisitaReporteWebOperador;
import app.server.download.FileStreamInfo;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService 
{
	public String greetServer(String input) {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	public Persona validarUsuario(String usuario, String password) {
		//InsertCorregidosYPendientes.migrarPendientes();
		//InsertCorregidosYPendientes.migrarCorregidos();

		return ControlPersona.getInstancia().validarUsuario(usuario, password);
	}


	public boolean validarNuevaFallaT(String descripcion) {
		return ControlFalla.getInstancia().validarNuevaFallaT(descripcion);
	}

	public ArrayList<FallaReportada> obtenerTodosLasFallasR() {

		return ControlFalla.getInstancia().obtenerTodasLasFallasReportadas();
	}

	public ArrayList<FallaTecnica> obtenerTodosLasFallasT() {

		return ControlFalla.getInstancia().obtenerTodasLasFallasTecnicas();
	}

	public ArrayList<Repuesto> obtenerTodosLosRepuestos() {

		return ControlRepuesto.getInstancia().obtenerTodosLosRepuestos();
	}


	public boolean eliminarFallaT(FallaTecnica falla) {
		return ControlFalla.getInstancia().eliminarFallaTecnica(falla);
	}


	public boolean eliminarFallaR(FallaReportada falla) {
		return ControlFalla.getInstancia().eliminarFallaReportada(falla);
	}


	public ArrayList<Estacion> obtenerEmpresasConOrdenesActivas() {
		return ControlEmpresa.getInstancia().obtenerEmpresasConOrdenesActivas();
	}


	public ArrayList<String> obtenerPrioridades() {
		return ControlOrden.getInstancia().obtenerPrioridades();
	}

	public ArrayList<String> obtenerTiposDeActivos(int idEstacion, boolean incluirDummy) {
		return ControlEmpresa.getInstancia().obtenerTiposDeActivos(idEstacion, incluirDummy);
	}


	public ArrayList<Orden> obtenerTodasLasOrdenesActivas() {
		return ControlOrden.getInstancia().obtenerTodasLasOrdenesActivas();
	}


	public Estacion buscarEstacion(int id) {
		return ControlEmpresa.getInstancia().buscarEstacion(id);
	}


	public Orden buscarOrden(int numero) {
		Orden orden = ControlOrden.getInstancia().buscarOrden(numero);
		return orden;
	}


	public boolean agregarRepuesto(Repuesto repuesto) {
		return ControlRepuesto.getInstancia().agregarRepuesto(repuesto);
	}

	public Repuesto buscarRepuesto(int id){
		return ControlRepuesto.getInstancia().buscarRepuesto(id);
	}


	public boolean agregarFallaTecnica(FallaTecnica fallaT) {
		return ControlFalla.getInstancia().agregarFallaTecnica(fallaT);
	}


	public boolean agregarFallaReportada(FallaReportada fallaR) {
		return ControlFalla.getInstancia().agregarFallaReportada(fallaR);
	}


	public FallaTecnica buscarFallaT(int id) {
		return ControlFalla.getInstancia().buscarFallaTecnica(id);
	}


	public boolean actualizarFallaT(FallaTecnica fallaT) {
		return ControlFalla.getInstancia().modificarFallaTecnica(fallaT);
	}

	public boolean modificarRepuestoBase(Repuesto repuesto){
		return ControlRepuesto.getInstancia().modificarRepuesto(repuesto);
	}

	public boolean eliminarRepuesto(Repuesto repuesto){
		return ControlRepuesto.getInstancia().eliminarRepuesto(repuesto);
	}

	public boolean modificarFallaTecnica(FallaTecnica falla){
		return ControlFalla.getInstancia().modificarFallaTecnica(falla);
	}

	public boolean modificarFallaReportada(FallaReportada falla){
		return ControlFalla.getInstancia().modificarFallaReportada(falla);
	}

	public FallaReportada buscarFallaReportada(int id){
		return ControlFalla.getInstancia().buscarFallaReportada(id);
	}


	public ArrayList<Estacion> obtenerEmpresas() {
		return ControlEmpresa.getInstancia().obtenerEmpresas();
	}

	public List<Activo> obtenerActivosDeEstacion(int idEstacion) 
	{
		List<Activo> lista = ControlEmpresa.getInstancia().obtenerActivos(idEstacion);
		return lista;
	}

	public ArrayList<Activo> obtenerActivosPorTipo(Estacion empresa, int tipoActivo) 
	{
		return ControlEmpresa.getInstancia().obtenerActivosPorTipo(empresa, tipoActivo);
	}

	public List<ActivoGenerico> obtenerActivosGenericos(Estacion empresa, int idTipoActivoGenerico) {
		List<ActivoGenerico> activosGenericos = new ArrayList<ActivoGenerico>();
		List<Activo> activos = ControlEmpresa.getInstancia().obtenerActivosPorTipo(empresa, 6);

		for (Activo activo : activos) {
			ActivoGenerico activoGenerico = (ActivoGenerico)activo;
			if (activoGenerico.getTipoActivoGenerico().getId() == idTipoActivoGenerico){
				activosGenericos.add(activoGenerico);
			}
		}
		return activosGenericos;
	}

	public ArrayList<ActivoData> obtenerDataActivosPorTipo(Estacion empresa, int tipoActivo) 
	{
		return ControlEmpresa.getInstancia().obtenerDataActivosPorTipo(empresa, tipoActivo);
	}

	public ArrayList<DatoListadoActivos> obtenerDataActivosPorTipo(Estacion e, Date desde, Date hasta, Tecnico tecnico, int tipoActivo) 
	{
		return ControlEmpresa.getInstancia().obtenerDataActivosPorTipo(e, desde, hasta, tecnico, tipoActivo);
	}

	public boolean agregarTarea(Tarea tarea) {
		return ControlFalla.getInstancia().agregarTarea(tarea);
	}


	public Tarea buscarTarea(int id) {
		return ControlFalla.getInstancia().buscarTarea(id);
	}


	public boolean modificarTareaBase(Tarea tarea) {
		return ControlFalla.getInstancia().modificarTarea(tarea);
	}


	public ArrayList<Tarea> obtenerTodasLasTareas() {
		return ControlFalla.getInstancia().obtenerTodasLasTareas();
	}

	public boolean eliminarTarea(Tarea tarea) {
		return ControlFalla.getInstancia().eliminarTarea(tarea);
	}

	public boolean validarNombreDeUsuarioDisponible(String nombreUsuario){
		return ControlPersona.getInstancia().validarNombreDeUsuarioDisponible(nombreUsuario);
	}

	public boolean agregarUsuario(Persona persona){
		return ControlPersona.getInstancia().agregarUsuario(persona);
	}


	public Activo buscarActivo(int id) {
		return ControlActivo.getInstancia().buscarActivo(id);
	}

	public ArrayList<Persona> obtenerTodosLosUsuarios(){
		return ControlPersona.getInstancia().obtenerTodosLosUsuarios();
	}

	public Persona buscarUsuario(int id){
		return ControlPersona.getInstancia().buscarUsuario(id);	
	}


	public Orden obtenerNuevaOrdenSinGuardarEnBase(Estacion empresa, Persona persona, Persona sesion) {
		return ControlOrden.getInstancia().obtenerNuevaOrdenSinGuardarEnBase(empresa, persona, sesion);
	}

	public Boolean actualizarOrden(Orden nuevaOrden, String estadoOrden, Persona sesion) {
		return ControlOrden.getInstancia().actualizarOrden(nuevaOrden, estadoOrden, sesion);
	}

	protected void doUnexpectedFailure(Throwable e) {
		e.printStackTrace();
		super.doUnexpectedFailure(e);
	}

	public ArrayList<Activo> obtenerTodosLosActivos() {
		return ControlActivo.getInstancia().obtenerTodosLosActivos();
	}

	public ArrayList<Activo> obtenerTodosLosSurtidores() {
		return ControlActivo.getInstancia().obtenerTodosLosSurtidores();
	}

	public boolean eliminarOrden(Orden ordenAEliminar, Persona sesion) {
		return ControlOrden.getInstancia().eliminarOrden(ordenAEliminar, sesion);
	}

	public ArrayList<Tecnico> listaTecnicos() {
		return ControlPersona.getInstancia().listaTecnicos();
	}

	public ArrayList<Persona> cargarTecnicosYEnc() {
		return ControlPersona.getInstancia().cargarTecnicosYEnc();
	}

	public boolean guardarOrdenAnulada(Orden orden, Persona sesion){
		return ControlOrden.getInstancia().guardarOrdenAnulada(orden, sesion);
	}

	public ArrayList<Orden> obtenerOrdenesCerradas() {
		return ControlOrden.getInstancia().obtenerOrdenesCerradas();
	}

	public ArrayList<DatoConsultaHistoricoOrdenesTecnico> obtenerHistoricoOrdenesTecnicos() {
		return ControlOrden.getInstancia().obtenerHistoricoOrdenesTecnicos();
	}

	public ArrayList<DatoConsultaHistoricoOrdenesTecnico> obtenerHistoricoOrdenesTecnicosDelimitado(Date inicio, Date fin) {
		return ControlOrden.getInstancia().obtenerHistoricoOrdenesTecnicosDelimitado(inicio, fin);
	}

	public boolean actualizarOrdenRebotada(Orden nuevaOrden, Tecnico tecnico, Persona sesion) {
		return ControlOrden.getInstancia().actualizarOrdenRebotada(nuevaOrden, tecnico, sesion);
	}

	public ArrayList<RepuestoDatoGrafica> listaDelos10MasUsados() {
		return ControlRepuesto.getInstancia().los10MasUsados();
	}

	public boolean validarEliminarPersona(Persona persona){
		return ControlPersona.getInstancia().validarEliminarPersona(persona);
	}

	public boolean cerrarOrden(Orden orden, Persona sesion){
		return ControlOrden.getInstancia().cerrarOrden(orden, sesion);
	}

	public ArrayList<DatoOrdenesActivasEmpresa> ordenesActivasDeUnaEmpresa(Estacion empresa){
		return ControlOrden.getInstancia().ordenesActivasDeUnaEmpresa(empresa);
	}

	public ArrayList<DatoConsultaHistoricoOrdenes> historicoOrdenesFinalizadasEmpresa(Estacion empresa) {
		return ControlOrden.getInstancia().historicoOrdenesFinalizadasEmpresa(empresa);
	}

	public boolean modificarPersona(Persona persona, boolean claveCambiada){
		return ControlPersona.getInstancia().modificarPersona(persona, claveCambiada);
	}

	public boolean eliminarPersona(Persona persona) {
		return ControlPersona.getInstancia().eliminarPersona(persona);
	}

	public int validarNuevoRepuesto(String descripcion, String nroSerie) {
		return ControlRepuesto.getInstancia().validarNuevoRepuesto(descripcion, nroSerie);
	}

	public ArrayList<ContactoEmpresa> obtenerEmpleadosPorEstacion(Estacion estacion) {
		return ControlPersona.getInstancia().obtenerEmpleadosPorEstacion(estacion);
	}

	public ArrayList<ContactoEmpresa> obtenerEmpleadosSinEmpresa() {
		return ControlPersona.getInstancia().obtenerEmpleadosSinEmpresa();
	}

	public boolean 	actualizarEstacion(Estacion estacion){
		return ControlEmpresa.getInstancia().actualizarEstacion(estacion);
	}

	public ArrayList<Sello> obtenerSellos() {
		return ControlEmpresa.getInstancia().obtenerSellos();
	}

	public boolean 	validarNuevaEstacion(Estacion estacion){
		return ControlEmpresa.getInstancia().validarNuevaEstacion(estacion);
	}

	public boolean 	agregarEstacion(Estacion estacion){
		return ControlEmpresa.getInstancia().agregarEstacion(estacion);
	}

	public boolean 	eliminarEstacion(Estacion estacion){
		return ControlEmpresa.getInstancia().eliminarEstacion(estacion);
	}

	public boolean 	esEstacionSinOrdenes(Estacion estacion){
		return ControlOrden.getInstancia().esEstacionSinOrdenes(estacion);
	}

	public ArrayList<Marca> obtenerTodasLasMArcas(){
		return ControlActivo.getInstancia().obtenerTodasLasMArcas();
	}

	public boolean validarNombreDeMarcaDisponible(String nombreMarca){
		return ControlActivo.getInstancia().validarNombreDeMarcaDisponible(nombreMarca);
	}

	public boolean agregarMarca(Marca marca){
		return ControlActivo.getInstancia().agregarMarca(marca);
	}

	public boolean agregarModelo(Modelo modelo){
		return ControlActivo.getInstancia().agregarModelo(modelo);
	}

	public Marca buscarMarca(int id){
		return ControlActivo.getInstancia().buscarMarca(id);
	}

	public Marca buscarMarca(String nombre){
		return ControlActivo.getInstancia().buscarMarca(nombre);
	}

	public boolean modificarMarca(int idMarca, String nuevoNombreMarca){
		return ControlActivo.getInstancia().modificarMarca(idMarca, nuevoNombreMarca);
	}

	public boolean validarModeloDisponible(String nombre, int idMarca, int idModeloExistente){
		return ControlActivo.getInstancia().validarModeloDisponible(nombre, idMarca, idModeloExistente);
	}

	public ArrayList<ModeloSurtidor> obtenerTodosLosModelosSurtidores(){
		return ControlActivo.getInstancia().obtenerTodosLosModelosSurtidores();
	}

	public boolean guardarHistorico(HistoricoOrden historicoOrden) {
		return ControlOrden.getInstancia().guardarHistorico(historicoOrden);
	}

	public boolean guardarSurtidor(Surtidor s){
		return ControlActivo.getInstancia().guardarSurtidor(s);
	}

	public boolean validarSurtidorExiste(Surtidor s){
		return ControlActivo.getInstancia().validarSurtidorExiste(s);
	}

	public ArrayList<TipoTrabajo> obtenerTiposDeTrabajo() {
		return ControlOrden.getInstancia().obtenerTiposDeTrabajo();
	}

	public ArrayList<Activo> obtenerTodosLosActivosTipoPorEstacion(int tipo, int idEstacion) {
		ArrayList<Activo> lista = ControlActivo.getInstancia().obtenerTodosLosActivosTipoPorEstacion(tipo, idEstacion);
		return lista;
	}

	public List<Producto> obtenerTiposCombustibles(){
		return ControlActivo.getInstancia().obtenerTiposCombustibles();
	}

	public boolean guardarTanque(Tanque tanque){
		return ControlActivo.getInstancia().guardarTanque(tanque);
	}

	public boolean guardarCanio(Canio canio){
		return ControlActivo.getInstancia().guardarCanio(canio);
	}

	public boolean validarNumeroDUCSA(String numeroDucsa) {
		return ControlOrden.getInstancia().validarNumeroDUCSA(numeroDucsa);
	}

	public boolean guardarBomba(BombaSumergible bomba) {
		return ControlActivo.getInstancia().guardarBomba(bomba);
	}

	public boolean actualizarSurtidor(Surtidor s){
		return ControlActivo.getInstancia().actualizarSurtidor(s);
	}

	public boolean modificarModelo(ModeloSurtidor modeloSurtidor) {
		return ControlActivo.getInstancia().modificarModelo(modeloSurtidor);
	}

	public boolean modificarSurtidor(Surtidor s) {
		return ControlActivo.getInstancia().modificarActivo(s);
	}

	public boolean modificarTanque(Tanque tanque) {
		return ControlActivo.getInstancia().modificarActivo(tanque);
	}

	public boolean modificarCanio(Canio canio) {
		return ControlActivo.getInstancia().modificarActivo(canio);
	}

	public boolean modificarBomba(BombaSumergible bomba) {
		return ControlActivo.getInstancia().modificarActivo(bomba);

	}

	public boolean guardarOrden(Orden orden, String estado, Persona sesion) {
		return ControlOrden.getInstancia().guardarOrden(orden, estado, sesion);
	}

	public boolean ordenesPorSelloYFechas(Sello sello, Date inicio, Date fin) {
		return ControlListado.getInstancia().validarSelloYFechas(sello, inicio, fin);
	}

	public String crearExcelOrdenesSello(Sello selloSeleccionado, Date inicio, Date fin) {
		File aux;
		ControlExcel c = new ControlExcel();

		aux =  c.crearExcel(selloSeleccionado, inicio, fin);

		SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
		String fInicio = sdf.format(inicio);
		String fFin = sdf.format(fin);

		String nombre = "cierre_petrobras_" + fInicio + "-" + fFin + ".xls";

		FileStreamInfo fileStream = new FileStreamInfo("application/xls",aux, nombre);
		getThreadLocalRequest().getSession().setAttribute(FileStreamInfo.SESSION_KEY, fileStream);		
		return aux.getName();
	}

	public String crearExcelPendientes(Date inicio, Date fin, List<Organizacion> organizaciones){
		File aux;
		ControlExcelCommon c = new ControlExcelPendiente();

		aux =  c.crearExcel(inicio, fin, organizaciones);

		SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
		String fInicio = sdf.format(inicio);
		String fFin = sdf.format(fin);

		String nombre = "informe_pendientes_" + fInicio + "-" + fFin + ".xls";

		FileStreamInfo fileStream = new FileStreamInfo("application/xls",aux, nombre);
		getThreadLocalRequest().getSession().setAttribute(FileStreamInfo.SESSION_KEY, fileStream);		
		return aux.getName();
	}

	public ArrayList<Orden> obtenerUltimasOrdenesInactivas(int cantidadOrdenes, Persona usuario) {
		return ControlOrden.getInstancia().obtenerUltimasOrdenesInactivas(cantidadOrdenes, usuario);
	}

	public CantidadOrdenes obtenerCantidadOrdenes(Persona sesion) 
	{
		CantidadOrdenes cant = ControlOrden.getInstancia().obtenerCantidadOrdenes(sesion);
		if ( cant != null)
		{
			return cant;			
		}
		else
		{
			CantidadOrdenes cantidad = new CantidadOrdenes();
			cantidad.setCantidad(5);
			return cant;
		}
	}


	public ArrayList<OrdenData> obtenerTodasLasOrdenesInactivas() {
		return ControlOrden.getInstancia().ordenesDataInactivas();
	}

	public ArrayList<Orden> buscarOrdenesPendientes(Estacion empresa) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean actualizarUsuariosSinAsignar(List<ContactoEmpresa> listaEmpleadosSinAsignar) {
		return ControlPersona.getInstancia().actualizarUsuariosSinAsignarParche(listaEmpleadosSinAsignar);
	}

	public ArrayList<OrdenData> obtenerTodasLasOrdenesActivasData() {
		return ControlOrden.getInstancia().ordenesDataActivas();
	}

	public ArrayList<OrdenData> obtenerTodasLasOrdenesActivasDataExcel(Sello s, Date inicio, Date fin) {
		return ControlOrden.getInstancia().obtenerTodasLasOrdenesActivasDataExcel(s,inicio,fin);
	}

	public List<PendienteData> obtenerTodosLosPendientesVisibles(Date inicio, Date fin, List<Organizacion> organizaciones){
		return ControlOrden.getInstancia().obtenerTodosLosPendientesVisibles(inicio, fin, organizaciones);
	}

	public ArrayList<EstacionDataList> obtenerEmpresasDataList() {
		return ControlEmpresa.getInstancia().obtenerEmpresasDataList();
	}

	public boolean agregarTipoTarea(TipoTarea tipoTarea) {
		return ControlTipoTarea.getInstancia().agregarTipoTarea(tipoTarea);
	}

	public TipoTarea buscarTipoTarea(int id) {
		return ControlTipoTarea.getInstancia().buscarTipoTarea(id);
	}

	public boolean modificarTipoTareaBase(TipoTarea tipoTarea) {
		return ControlTipoTarea.getInstancia().modificarTipoTarea(tipoTarea);
	}

	//TIPO TAREA
	public ArrayList<TipoTarea> obtenerTodasLosTiposTareasActivos() {
		return ControlTipoTarea.getInstancia().obtenerTodasLosTiposTareasActivos();
	}

	public boolean validarTipoTareaExiste(String descripcion) {
		return ControlTipoTarea.getInstancia().validarTipoTareaExiste(descripcion);
	}

	public boolean eliminarTipoTarea(TipoTarea result) {
		return ControlTipoTarea.getInstancia().eliminarTipoTarea(result);
	}

	public boolean validarTareaExiste(String descripcion) {
		return ControlFalla.getInstancia().validarTareaExiste(descripcion);
	}

	public boolean validarTareaExiste(Tarea tarea) {
		return ControlFalla.getInstancia().validarTareaExiste(tarea);
	}

	//TIPO REPUESTO
	public ArrayList<TipoRepuesto> obtenerTodasLosTiposRepuestosActivos() {
		return ControlTipoRepuesto.getInstancia().obtenerTodasLosTiposRepuestosActivos();
	}

	public boolean agregarTipoRepuesto(TipoRepuesto tipoRepuesto) {
		return ControlTipoRepuesto.getInstancia().agregarTipoRepuesto(tipoRepuesto);
	}

	public TipoRepuesto buscarTipoRepuesto(int id) {
		return ControlTipoRepuesto.getInstancia().buscarTipoRepuesto(id);
	}

	public boolean modificarTipoRepuestoBase(TipoRepuesto tipoRepuesto) {
		return ControlTipoRepuesto.getInstancia().modificarTipoRepuesto(tipoRepuesto);
	}

	public boolean validarTipoRepuestoExiste(String descripcion) {
		return ControlTipoRepuesto.getInstancia().validarTipoRepuestoExiste(descripcion);
	}

	public boolean eliminarTipoRepuesto(TipoRepuesto tipoRepuesto) {
		return ControlTipoRepuesto.getInstancia().eliminarTipoRepuesto(tipoRepuesto);
	}

	public ArrayList<Comentario> obtenerComentarios(Solucion solucion)
	{
		return ControlOrden.getInstancia().obtenerComentarios(solucion);
	}

	public Date obtenerFechaServidor() {
		return ControlOrden.getInstancia().obtenerFechaServidor();
	}

	public String obtenerHoraServidor() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	}

	public Date obtenerHoraServidorDate() {
		Date actual = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.format(actual);
		return actual;
	}

	public ArrayList<DatoOrdenesActivasEmpresa> ordenesPorFecha(EstacionDataList estacion, Date desde, Date hasta)
	{
		return ControlOrden.getInstancia().ordenesPorFecha(estacion, desde, hasta);
	}

	public ArrayList<Reparacion> obtenerTodosLasReparaciones(Orden orden) 
	{
		return ControlReparacion.getInstancia().obtenerTodosLasReparaciones(orden);
	}

	public ArrayList<Reparacion> obtenerTodosLasReparacionesDeUnActivo(Activo a) 
	{
		return ControlActivo.getInstancia().obtenerTodosLasReparacionesDeUnActivo(a);
	}

	public ArrayList<RepuestoLinea> obtenerRepuestosLinea(Activo activo)
	{
		return ControlActivo.getInstancia().obtenerRepuestosLinea(activo);
	}

	public ArrayList<DatoListadoReparaciones> obtenerDatoListadoActivosPorTipo(DatoOrdenesActivasEmpresa orden) 
	{
		return ControlActivo.getInstancia().obtenerDatoListadoActivosPorTipo(orden);
	}

	public DateDesdeHasta formatearFechas(Date desde, Date hasta) 
	{
		SimpleDateFormat formater = new SimpleDateFormat("dd MMM yyyy HH:mm");
		try
		{
			String dateDesde = formater.format(desde);
			String dateHasta = formater.format(hasta);
			DateDesdeHasta ddh = new DateDesdeHasta(dateDesde, dateHasta);
			return ddh;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	public String crearPDF(int numero, Boolean pdfBilpa) 
	{
		Orden orden = ControlOrden.getInstancia().buscarOrden(numero);

		//File aux1  =  ControlPDFOrden.getInstancia().crearPDF(orden, pdfBilpa);//parche para arreglar bug de cantidad de paginas.
		File aux  =  ControlPDFOrden.getInstancia().crearPDF(orden, pdfBilpa);

		String nombre = "Orden_Nro_" + numero + ".pdf";
		FileStreamInfo fileStream = new FileStreamInfo("application/pdf",aux, nombre);
		getThreadLocalRequest().getSession().setAttribute(FileStreamInfo.SESSION_KEY, fileStream);

		return aux.getName();
	}

	public String crearPDFPermiso(Orden orden) 
	{
		File aux =  ControlPDFPermisoDeTrabajo.getInstancia().crearPDFPermisoTrabajo(orden);

		String nombre = "Permiso_Trabajo_Orden_Nro_" + orden.getNumero() + ".pdf";
		FileStreamInfo fileStream = new FileStreamInfo("application/pdf",aux, nombre);
		getThreadLocalRequest().getSession().setAttribute(FileStreamInfo.SESSION_KEY, fileStream);

		return aux.getName();
	}

	public String crearPDFVisitas(int idVisita, Organizacion organizacion) {

		File aux;
		if (organizacion.equals(Organizacion.Operador)){
			aux =  ControlVisitaReporteWebOperador.getInstancia().crearPDFPreventivosVisitas(idVisita, organizacion);
		} else {
			aux =  ControlVisitaReporteWebDucsaYBilpa.getInstancia().crearPDFPreventivosVisitas(idVisita, organizacion);
		}

		String nombre = "Bilpa reporte mantenimiento preventivo " + idVisita + ".pdf";
		FileStreamInfo fileStream = new FileStreamInfo("application/pdf",aux, nombre);
		getThreadLocalRequest().getSession().setAttribute(FileStreamInfo.SESSION_KEY, fileStream);

		return aux.getName();
	}

	public ArrayList<Tecnico> obtenerTodosLosTecnicos() {
		return ControlPersona.getInstancia().obtenerTodosLosTecnicos();
	}

	public List<TecnicoData> obtenerTodosLosDataTecnico(){
		return ControlPersona.getInstancia().obtenerTodosLosTecnicosData();
	}

	public ArrayList<TipoFallaReportada> obtenerTiposFallasR() 
	{
		return ControlTipoFalla.getInstancia().obtenerTiposFallasActivasR();
	}

	public ArrayList<TipoFallaTecnica> obtenerTiposFallasT() 
	{
		return ControlTipoFalla.getInstancia().obtenerTiposFallasActivasT();
	}

	public TipoFallaReportada buscarTipoFallaR(int id)
	{
		return ControlTipoFalla.getInstancia().buscarTipoFallaR(id);
	}

	public TipoFallaTecnica buscarTipoFallaT(int id) 
	{
		return ControlTipoFalla.getInstancia().buscarTipoFallaT(id);
	}

	public boolean eliminarTipoFallaR(TipoFallaReportada tipoFallaR) 
	{
		return ControlTipoFalla.getInstancia().eliminarTipoFallaR(tipoFallaR);
	}

	public boolean eliminarTipoFallaT(TipoFallaTecnica tipoFallaT) 
	{
		return ControlTipoFalla.getInstancia().eliminarTipoFallaT(tipoFallaT);
	}

	public boolean modificarTipoFallaT(TipoFallaTecnica falla) 
	{
		return ControlTipoFalla.getInstancia().modificarTipoFallaT(falla);
	}

	public boolean modificarTipoFallaR(TipoFallaReportada falla) 
	{
		return ControlTipoFalla.getInstancia().modificarTipoFallaR(falla);
	}

	public boolean agregarTipoFallaR(TipoFallaReportada fallaR) 
	{
		return ControlTipoFalla.getInstancia().agregarTipoFallaR(fallaR);
	}

	public boolean agregarTipoFallaT(TipoFallaTecnica fallaT) 
	{
		return ControlTipoFalla.getInstancia().agregarTipoFallaT(fallaT);
	}

	public boolean removerRepuestosDeOrden(ArrayList<RepuestoLinea> repuetosLineaARemover) {
		return ControlOrden.getInstancia().removerRepuestosDeOrden(repuetosLineaARemover);
	}

	public ArrayList<DestinoDelCargoData> obtenerDestinosDelCargo(){
		return ControlOrden.getInstancia().obtenerDestinosDelCargo();
	}

	public ArrayList<OrdenData> obtenerOrdenes(OrdenData ordenFilter) {
		return ControlOrden.getInstancia().obtenerOrdenes(ordenFilter);
	}

	public ListaOrdenesData obtenerListaOrdenesData() {
		return ControlOrden.getInstancia().obtenerListaOrdenesData();
	}

	public List<VisitaDataList> obtenerListaVisitasDataWeb(int sello){
		return ControlVisita.getInstancia().obtenerListaVisitasDataWeb(sello);
	}


	public List<VisitaDataList> obtenerListaVisitasDataConFiltro(VisitaDataList visitaData, int sello){
		return ControlVisita.getInstancia().obtenerListaVisitasDataPorFiltroWeb(visitaData, sello);
	}

	public List<VisitaDataList> obtenerListaVistasDataEstacionWeb(String nombreEmpresa){
		return ControlVisita.getInstancia().obtenerListaVistasDataEstacionWeb(nombreEmpresa);
	}

	public boolean eliminarVisita(VisitaDataList dataVisita){
		return ControlVisita.getInstancia().eliminarVisita(dataVisita);
	}

	public boolean nuevaVisita(VisitaDataList dataVisita){
		return ControlVisita.getInstancia().nuevaVisita(dataVisita);
	}

	public boolean modificarVisita(VisitaDataList dataVisita) {
		return ControlVisita.getInstancia().modificarVisitaWeb(dataVisita);
	}

	public List<Estacion> obtenerEstaciones(int idSello) {
		return ControlEmpresa.getInstancia().obtenerEstaciones(idSello);
	}

	public List<TipoActivoGenericoData> obtenerTiposActivoGenerico(){
		return ControlActivosGenericos.getInstancia().obtenerTiposActivoGenerico();
	}

	public boolean agregarTipoActivoGenerico(TipoActivoGenerico param) {
		return ControlActivosGenericos.getInstancia().agregarTipoActivoGenerico(param);
	}

	public boolean modificarTipoActivoGenerico(TipoActivoGenericoData param) {
		return ControlActivosGenericos.getInstancia().modificarTipoActivoGenerico(param);
	}

	public boolean eliminarTipoActivoGenerico(TipoActivoGenericoData param) {
		return ControlActivosGenericos.getInstancia().eliminarTipoActivoGenerico(param);
	}

	public List<MarcaActivoGenerico> obtenerMarcasActivoGenerico(TipoActivoGenericoData param){
		return ControlActivosGenericos.getInstancia().obtenerMarcasActivoGenerico(param);
	}

	public boolean agregarMarcaActivoGenerico(MarcaActivoGenerico param) {
		return ControlActivosGenericos.getInstancia().agregarMarcaActivoGenerico(param);
	}

	public boolean modificarMarcaActivoGenerico(MarcaActivoGenerico param) {
		return ControlActivosGenericos.getInstancia().modificarMarcaActivoGenerico(param);
	}

	public boolean eliminarMarcaActivoGenerico(MarcaActivoGenerico param) {
		return ControlActivosGenericos.getInstancia().eliminarMarcaActivoGenerico(param);
	}

	public List<ModeloActivoGenerico> obtenerModelosActivoGenerico(MarcaActivoGenerico param){
		return ControlActivosGenericos.getInstancia().obtenerModelosActivoGenerico(param);
	}

	public boolean agregarModeloActivoGenerico(ModeloActivoGenerico object) {
		return ControlActivosGenericos.getInstancia().agregarModeloActivoGenerico(object);
	}

	public boolean modificarModeloActivoGenerico(ModeloActivoGenerico object) {
		return ControlActivosGenericos.getInstancia().modificarModeloActivoGenerico(object);
	}

	public boolean eliminarModeloActivoGenerico(ModeloActivoGenerico param) {
		return ControlActivosGenericos.getInstancia().eliminarModeloActivoGenerico(param);
	}

	public List<ItemChequeoData> obtenerItemsActivoGenerico(TipoActivoGenericoData tipo){
		return ControlActivosGenericos.getInstancia().obtenerItemsActivoGenerico(tipo);
	}

	public boolean agregarItemChequeo(ItemChequeoData param){
		return ControlActivosGenericos.getInstancia().agregarItemChequeo(param);
	}

	public boolean modificarItemChequeo(ItemChequeoData param){
		return ControlActivosGenericos.getInstancia().modificarItemChequeo(param);
	}

	public boolean eliminarItemChequeo(ItemChequeoData param){
		return ControlActivosGenericos.getInstancia().eliminarItemChequeo(param);
	}

	public boolean guardarActivoGenerico(ActivoGenerico activo) {
		return ControlActivosGenericos.getInstancia().guardarActivoGenerico(activo);
	}

	public boolean modificarActivoGenerico(ActivoGenerico param){
		return ControlActivosGenericos.getInstancia().modificarActivoGenerico(param);
	}

	public boolean eliminarActivoGenerico(ActivoGenerico param){
		return ControlActivosGenericos.getInstancia().eliminarActivoGenerico(param);
	}

	@Override
	public PendienteDataUI obtenerPendientes(int idSello, EstadoPendiente estadoPendiente) {
		return ControlPendiente.getInstancia().obtenerPendientes(idSello, estadoPendiente);
	}

	@Override
	public PendienteDataUI obtenerFiltrosPendientes(int idSello) {
		// return ControlPendiente.getInstancia().obtenerFiltrosPendientes(idSello);
		return null;
	}

	public String descargarFotoPendiente(PendienteData pd) 
	{
		String nombre = "pendiente_" + pd.getId() + ".png";
		
		File aux = null;

		// URL url;
		InputStream in = null;
		try {
			// url = new URL(pd.getUrlFoto());
			// URLConnection conn = url.openConnection();
			File file = new File(pd.getPathFoto());
			// in = conn.getInputStream();
			in = new FileInputStream(file);
			aux = File.createTempFile(nombre, ".png");
			FileOutputStream out = new FileOutputStream(aux);
			IOUtils.copy(in, out);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileStreamInfo fileStream = new FileStreamInfo("image/png", aux, nombre);
		getThreadLocalRequest().getSession().setAttribute(FileStreamInfo.SESSION_KEY, fileStream);
		// return aux.getName();
		return nombre;
	}
	
	public boolean guardarPendiente(PendienteData pd, String fotoStr) throws Exception {
		return ControlPendiente.getInstancia().guardarPendiente(pd, fotoStr);
	}
	
	public boolean descartarPendiente(List<PendienteData> pendientes, String motivoDescarte, int idPersona) {
		return ControlPendiente.getInstancia().descartarPendiente(pendientes, motivoDescarte, idPersona);
	}

	@Override
	public boolean asignarCorrectivoAPendientes(int idSesion, int numeroOrden, List<PendienteData> pendientes) {
		return ControlPendiente.getInstancia().asignarCorrectivoAPendientes(idSesion, numeroOrden, pendientes);
	}

	@Override
	public boolean eliminarPendiente(int id) {
		return ControlPendiente.getInstancia().eliminarPendientes(id);
	}

	@Override
	public PendienteDataUI obtenerPendientesDeEstacion(int idEstacion) {
		return ControlPendiente.getInstancia().obtenerPendientesDeEstacion(idEstacion);
	}

	@Override
	public String reporteVisitas(Sello selloSeleccionado, Date inicio, Date fin, Organizacion operador) {
		File aux = ControlPreventivo.getInstancia().reportePreventivos(selloSeleccionado, inicio, fin, operador);
		
		String nombre = "preventivos_" + new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date()) +".zip";
		FileStreamInfo fileStream = new FileStreamInfo("application/zip",aux, nombre);
		getThreadLocalRequest().getSession().setAttribute(FileStreamInfo.SESSION_KEY, fileStream);

		if (aux == null) return "";
		return aux.getName();
	}

	@Override
	public List<FallaReportada> obtenerFallasRPorTipo(int idTipo) {
		return ControlFalla.getInstancia().obtenerFallasRPorTipo(idTipo);
	}
}
