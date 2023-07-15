package app.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")

public interface GreetingService extends RemoteService {
	
	String greetServer(String name);
	
	public Persona validarUsuario(String usuario, String password);
	
	public boolean validarNuevaFallaT(String descripcion);
	
	public int validarNuevoRepuesto(String descripcion, String nroSerie);
	
	public ArrayList<FallaReportada> obtenerTodosLasFallasR();
	
	public ArrayList<FallaTecnica> obtenerTodosLasFallasT();
	
	public ArrayList<Repuesto> obtenerTodosLosRepuestos();
	
	public boolean eliminarFallaT(FallaTecnica falla);
	
	boolean eliminarFallaR(FallaReportada falla);
	
	public ArrayList<Estacion> obtenerEmpresasConOrdenesActivas();
	
	public ArrayList<String> obtenerPrioridades();
	
	ArrayList<String> obtenerTiposDeActivos(int idEstacion, boolean incluirDummy);

	public ArrayList<Orden> obtenerTodasLasOrdenesActivas();

	public Estacion buscarEstacion(int id);

	public Orden buscarOrden(int numero);
	
	public boolean agregarRepuesto(Repuesto repuesto);
	
	public Repuesto buscarRepuesto(int id);

	public boolean agregarFallaTecnica(FallaTecnica fallaT);

	boolean agregarFallaReportada(FallaReportada fallaR);

	public FallaTecnica buscarFallaT(int id);

	boolean actualizarFallaT(FallaTecnica fallaT);
	
	public boolean modificarRepuestoBase(Repuesto repuesto);
	
	public boolean eliminarRepuesto(Repuesto repuesto);
	
	public boolean modificarFallaTecnica(FallaTecnica falla);

	public boolean modificarFallaReportada(FallaReportada falla);
	
	public FallaReportada buscarFallaReportada(int id);

	public ArrayList<Estacion> obtenerEmpresas();

	public ArrayList<Activo> obtenerActivosPorTipo(Estacion empresa, int tipoActivo);

	public ArrayList<Tarea> obtenerTodasLasTareas();

	public Tarea buscarTarea(int id);

	boolean validarTareaExiste(Tarea tarea);
	
	boolean validarTareaExiste(String descripcion);

	boolean agregarTarea(Tarea tarea);

	boolean modificarTareaBase(Tarea tarea);

	boolean eliminarTarea(Tarea result);
	
	public boolean validarNombreDeUsuarioDisponible(String nombreUsuario);
	
	public boolean agregarUsuario(Persona persona);

	public Activo buscarActivo(int id);
	
	public ArrayList<Persona> obtenerTodosLosUsuarios();

	public ArrayList<Orden> buscarOrdenesPendientes(Estacion empresa);

	public ArrayList<DatoConsultaHistoricoOrdenes> historicoOrdenesFinalizadasEmpresa(Estacion empresa);
	
	public Persona buscarUsuario(int id);

	public Orden obtenerNuevaOrdenSinGuardarEnBase(Estacion empresa, Persona persona, Persona sesion);

	public Boolean actualizarOrden(Orden nuevaOrden, String estadoOrden, Persona sesion);

	public ArrayList<Activo> obtenerTodosLosActivos();

	public ArrayList<Reparacion> obtenerTodosLasReparaciones(Orden orden);
	
	public boolean eliminarOrden(Orden ordenAEliminar, Persona sesion);

	public ArrayList<Tecnico> listaTecnicos();
	
	public ArrayList<Persona> cargarTecnicosYEnc();
	
	public boolean guardarOrdenAnulada(Orden orden, Persona sesion);
	
	public ArrayList<Orden> obtenerOrdenesCerradas();
	
	public ArrayList<DatoConsultaHistoricoOrdenesTecnico> obtenerHistoricoOrdenesTecnicos();

	public ArrayList<DatoConsultaHistoricoOrdenesTecnico> obtenerHistoricoOrdenesTecnicosDelimitado(Date inicio, Date fin);
	
	public boolean actualizarOrdenRebotada(Orden nuevaOrden, Tecnico tecnico, Persona sesion);
	
	public ArrayList<RepuestoDatoGrafica> listaDelos10MasUsados();
	
	public boolean validarEliminarPersona(Persona persona);
	
	public boolean cerrarOrden(Orden orden, Persona sesion);
	
	public ArrayList<DatoOrdenesActivasEmpresa> ordenesActivasDeUnaEmpresa(Estacion empresa);
	
	public boolean modificarPersona(Persona persona, boolean claveCambiada);
	
	public boolean eliminarPersona(Persona persona);

	String crearPDF(int numero, Boolean pdfBilpa);
	
	public String crearPDFPermiso(Orden orden);

	public ArrayList<ContactoEmpresa> obtenerEmpleadosSinEmpresa();

	public ArrayList<ContactoEmpresa> obtenerEmpleadosPorEstacion(	Estacion estacionSeleccionada);

	public boolean actualizarEstacion(Estacion estacionSeleccionada);

	ArrayList<Sello> obtenerSellos();

	boolean validarNuevaEstacion(Estacion estacion);

	boolean agregarEstacion(Estacion estacion);

	boolean esEstacionSinOrdenes(Estacion estacion);

	boolean eliminarEstacion(Estacion e);
	
	public ArrayList<Marca> obtenerTodasLasMArcas();
	
	public boolean validarNombreDeMarcaDisponible(String nombreMarca);
	
	public boolean agregarMarca(Marca marca);
	
	public Marca buscarMarca(int id);
	
	public Marca buscarMarca(String nombre);

	public boolean modificarMarca(int idMarca, String nuevoNombreMarca);
	
	public boolean agregarModelo(Modelo modelo);
	
	public boolean validarModeloDisponible(String nombre, int idMarca, int idModeloExistente);
	
	boolean guardarHistorico(HistoricoOrden historicoOrden);
	
	public boolean guardarSurtidor(Surtidor s);
	
	public boolean validarSurtidorExiste(Surtidor s);

	ArrayList<TipoTrabajo> obtenerTiposDeTrabajo();
	
	public ArrayList<Activo> obtenerTodosLosActivosTipoPorEstacion(int tipo, int idEstacion);
	
	List<Producto> obtenerTiposCombustibles();
	
	public boolean guardarTanque(Tanque tanque);
	
	public boolean guardarCanio(Canio canio);
	
	public boolean guardarBomba(BombaSumergible bomba);
	
	boolean validarNumeroDUCSA(String numeroDucsa);
	
	public boolean actualizarSurtidor(Surtidor s);

	boolean modificarModelo(ModeloSurtidor modeloSurtidor);

	boolean modificarSurtidor(Surtidor s);

	boolean modificarTanque(Tanque tanque);

	boolean modificarCanio(Canio canio);

	boolean modificarBomba(BombaSumergible bomba);

	boolean guardarOrden(Orden orden, String estado, Persona sesion);

	boolean ordenesPorSelloYFechas(Sello sello, Date inicio, Date fin);

	String crearExcelOrdenesSello(Sello selloSeleccionado, Date inicio, Date fin);
	
	String crearExcelPendientes(Date inicio, Date fin, List<Organizacion> organizaciones);

	CantidadOrdenes obtenerCantidadOrdenes(Persona sesion);

	ArrayList<ModeloSurtidor> obtenerTodosLosModelosSurtidores();

	ArrayList<Activo> obtenerTodosLosSurtidores();

	ArrayList<OrdenData> obtenerTodasLasOrdenesInactivas();

	boolean actualizarUsuariosSinAsignar(List<ContactoEmpresa> listaEmpleadosSinAsignar);

	ArrayList<OrdenData> obtenerTodasLasOrdenesActivasData();
	
	public ArrayList<EstacionDataList> obtenerEmpresasDataList();

	boolean agregarTipoTarea(TipoTarea tipoTarea);

	boolean modificarTipoTareaBase(TipoTarea tipoTarea);

	ArrayList<TipoTarea> obtenerTodasLosTiposTareasActivos();

	boolean validarTipoTareaExiste(String descripcion);

	TipoTarea buscarTipoTarea(int id);

	boolean eliminarTipoTarea(TipoTarea result);

	ArrayList<TipoRepuesto> obtenerTodasLosTiposRepuestosActivos();

	boolean agregarTipoRepuesto(TipoRepuesto tipoRepuesto);

	TipoRepuesto buscarTipoRepuesto(int id);

	boolean modificarTipoRepuestoBase(TipoRepuesto tipoRepuesto);

	boolean validarTipoRepuestoExiste(String descripcion);

	boolean eliminarTipoRepuesto(TipoRepuesto result);

	ArrayList<Comentario> obtenerComentarios(Solucion solucion);

	String obtenerHoraServidor();

	Date obtenerHoraServidorDate();

	ArrayList<DatoOrdenesActivasEmpresa> ordenesPorFecha(EstacionDataList e,
			Date desde, Date hasta);

	ArrayList<RepuestoLinea> obtenerRepuestosLinea(Activo activo);

	ArrayList<DatoListadoReparaciones> obtenerDatoListadoActivosPorTipo(
			DatoOrdenesActivasEmpresa orden);

	DateDesdeHasta formatearFechas(Date desde, Date hasta);

	ArrayList<Tecnico> obtenerTodosLosTecnicos();

	ArrayList<DatoListadoActivos> obtenerDataActivosPorTipo(Estacion estacion,
			Date desde, Date hasta, Tecnico tecnico, int tipoActivo);

	ArrayList<ActivoData> obtenerDataActivosPorTipo(Estacion e, int idTipoActivo);

	ArrayList<TipoFallaReportada> obtenerTiposFallasR();

	ArrayList<TipoFallaTecnica> obtenerTiposFallasT();

	TipoFallaReportada buscarTipoFallaR(int id);

	boolean eliminarTipoFallaR(TipoFallaReportada tipoFallaR);

	boolean eliminarTipoFallaT(TipoFallaTecnica tipoFallaT);

	TipoFallaTecnica buscarTipoFallaT(int id);

	boolean modificarTipoFallaT(TipoFallaTecnica falla);

	boolean modificarTipoFallaR(TipoFallaReportada result);

	boolean agregarTipoFallaR(TipoFallaReportada fallaR);

	boolean agregarTipoFallaT(TipoFallaTecnica fallaT);

	Date obtenerFechaServidor();
	
	public ArrayList<OrdenData> obtenerTodasLasOrdenesActivasDataExcel(Sello s, Date inicio, Date fin);
	
	List<PendienteData> obtenerTodosLosPendientesVisibles(Date inicio, Date fin, List<Organizacion> organizaciones);

	boolean removerRepuestosDeOrden(
			ArrayList<RepuestoLinea> repuetosLineaARemover); 

	public ArrayList<DestinoDelCargoData> obtenerDestinosDelCargo();

	ArrayList<OrdenData> obtenerOrdenes(OrdenData ordenFilter);

	ListaOrdenesData obtenerListaOrdenesData();
	
	List<VisitaDataList> obtenerListaVisitasDataWeb(int sello);
	
	List<VisitaDataList> obtenerListaVisitasDataConFiltro(VisitaDataList visitaData, int sello);
	
	List<VisitaDataList> obtenerListaVistasDataEstacionWeb(String nombreEmpresa);
	
	boolean eliminarVisita(VisitaDataList dataVisita);
	
	boolean nuevaVisita(VisitaDataList dataVisita);
	
	List<TecnicoData> obtenerTodosLosDataTecnico();
	
	boolean modificarVisita(VisitaDataList dataVisita);

	String crearPDFVisitas(int idVisita, Organizacion organizacion);

	List<Estacion> obtenerEstaciones(int idSello);

	List<TipoActivoGenericoData> obtenerTiposActivoGenerico();
	boolean agregarTipoActivoGenerico(TipoActivoGenerico param);
	boolean modificarTipoActivoGenerico(TipoActivoGenericoData param);
	boolean eliminarTipoActivoGenerico(TipoActivoGenericoData param);
	
	List<MarcaActivoGenerico> obtenerMarcasActivoGenerico(
			TipoActivoGenericoData param);
	boolean agregarMarcaActivoGenerico(MarcaActivoGenerico param);
	boolean modificarMarcaActivoGenerico(MarcaActivoGenerico param);
	boolean eliminarMarcaActivoGenerico(MarcaActivoGenerico param);
	
	List<ModeloActivoGenerico> obtenerModelosActivoGenerico(MarcaActivoGenerico param);
	boolean agregarModeloActivoGenerico(ModeloActivoGenerico param);
	boolean modificarModeloActivoGenerico(ModeloActivoGenerico param);
	boolean eliminarModeloActivoGenerico(ModeloActivoGenerico param);
	
	List<ItemChequeoData> obtenerItemsActivoGenerico(TipoActivoGenericoData tipo);
	 boolean agregarItemChequeo(ItemChequeoData param);
	 boolean modificarItemChequeo(ItemChequeoData param);
	 boolean eliminarItemChequeo(ItemChequeoData param);

	boolean guardarActivoGenerico(ActivoGenerico activo);
	List<ActivoGenerico> obtenerActivosGenericos(Estacion empresa, int tipoActivoGenerico);

	boolean modificarActivoGenerico(ActivoGenerico activoGenericoSeleccionado);

	boolean eliminarActivoGenerico(ActivoGenerico object);
	
	List<Activo> obtenerActivosDeEstacion(int idEstacion);

	PendienteDataUI obtenerPendientes(int idSello, EstadoPendiente estadoPendiente);

	PendienteDataUI obtenerFiltrosPendientes(int idSello);

	String descargarFotoPendiente(PendienteData pd);
	
	boolean guardarPendiente(PendienteData pd, String fotoStr) throws Exception;

	boolean descartarPendiente(List<PendienteData> pendientes, String motivoDescarte, int idPersona);

	boolean asignarCorrectivoAPendientes(int id, int i, List<PendienteData> pendientes);

	boolean eliminarPendiente(int id);

	PendienteDataUI obtenerPendientesDeEstacion(int numero);

	String reporteVisitas(Sello selloSeleccionado, Date inicio, Date fin, Organizacion operador);

	List<FallaReportada> obtenerFallasRPorTipo(int idTipo);
}
