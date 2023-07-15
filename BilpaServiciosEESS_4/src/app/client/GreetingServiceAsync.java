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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	void greetServer(String input, AsyncCallback<String> callback);
	
	void validarUsuario(String usuario, String password,AsyncCallback<Persona> callback);

	void validarNuevaFallaT(String descripcion,	AsyncCallback<Boolean> callback);

	void obtenerTodosLasFallasR(AsyncCallback<ArrayList<FallaReportada>> asyncCallback);

	void obtenerTodosLasFallasT(AsyncCallback<ArrayList<FallaTecnica>> asyncCallback);

	void obtenerTodosLosRepuestos(AsyncCallback<ArrayList<Repuesto>> callback);
	
	void eliminarFallaT(FallaTecnica falla, AsyncCallback<Boolean> asyncCallback);

	void eliminarFallaR(FallaReportada falla, AsyncCallback<Boolean> asyncCallback);

	void obtenerPrioridades(AsyncCallback<ArrayList<String>> asyncCallback);
	
	void obtenerTiposDeActivos(int idEstacion, boolean incluirDummy, AsyncCallback<ArrayList<String>> asyncCallback);

	void obtenerEmpresasConOrdenesActivas(AsyncCallback<ArrayList<Estacion>> asyncCallback);

	void obtenerTodasLasOrdenesActivas(AsyncCallback<ArrayList<Orden>> asyncCallback);

	void buscarEstacion(int id, AsyncCallback<Estacion> asyncCallback);

	void buscarOrden(int numero, AsyncCallback<Orden> asyncCallback);

	void agregarRepuesto(Repuesto repuesto, AsyncCallback<Boolean> callback);
	
	void buscarRepuesto(int id, AsyncCallback<Repuesto> callback);

	void agregarFallaTecnica(FallaTecnica fallaT, AsyncCallback<Boolean> asyncCallback);

	void agregarFallaReportada(FallaReportada fallaR, AsyncCallback<Boolean> asyncCallback);

	void buscarFallaT(int id, AsyncCallback<FallaTecnica> asyncCallback);

	void actualizarFallaT(FallaTecnica fallaT,AsyncCallback<Boolean> asyncCallback);

	void modificarRepuestoBase(Repuesto repuesto,AsyncCallback<Boolean> asyncCallback);
	
	void eliminarRepuesto(Repuesto repuesto,AsyncCallback<Boolean> asyncCallback);
	
	void modificarFallaTecnica(FallaTecnica falla,AsyncCallback<Boolean> asyncCallback);
	
	void modificarFallaReportada(FallaReportada falla, AsyncCallback<Boolean> asyncCallback);

	void buscarFallaReportada(int id, AsyncCallback<FallaReportada> callback);

	void obtenerEmpresas(AsyncCallback<ArrayList<Estacion>> asyncCallback);
	
	void obtenerActivosPorTipo(Estacion empresa, int tipoActivo,AsyncCallback<ArrayList<Activo>> asyncCallback);

	void obtenerTodasLasTareas(AsyncCallback<ArrayList<Tarea>> asyncCallback);

	void buscarTarea(int id, AsyncCallback<Tarea> asyncCallback);

	void validarTareaExiste(Tarea tarea,AsyncCallback<Boolean> asyncCallback);
	
	void validarTareaExiste(String descripcion,AsyncCallback<Boolean> asyncCallback);

	void agregarTarea(Tarea tarea, AsyncCallback<Boolean> asyncCallback);

	void modificarTareaBase(Tarea tarea, AsyncCallback<Boolean> asyncCallback);

	void eliminarTarea(Tarea result, AsyncCallback<Boolean> asyncCallback);

	void validarNombreDeUsuarioDisponible(String nombreUsuario, AsyncCallback<Boolean> callback);

	void agregarUsuario(Persona persona, AsyncCallback<Boolean> callback);

	void buscarActivo(int id, AsyncCallback<Activo> asyncCallback);

	void obtenerTodosLosUsuarios(AsyncCallback<ArrayList<Persona>> callback);

	void buscarOrdenesPendientes(Estacion empresa,AsyncCallback<ArrayList<Orden>> asyncCallback);

	void historicoOrdenesFinalizadasEmpresa(Estacion empresa,AsyncCallback<ArrayList<DatoConsultaHistoricoOrdenes>> asyncCallback);

	void buscarUsuario(int id, AsyncCallback<Persona> callback);

	void obtenerNuevaOrdenSinGuardarEnBase(Estacion empresa,Persona persona, Persona sesion, AsyncCallback<Orden> callback);

	void actualizarOrden(Orden nuevaOrden, String estadoOrden, Persona sesion, AsyncCallback<Boolean> asyncCallback);

	void obtenerTodosLosActivos(AsyncCallback<ArrayList<Activo>> asyncCallback);

	void obtenerTodosLosSurtidores(AsyncCallback<ArrayList<Activo>> asyncCallback);

	void obtenerTodosLasReparaciones(Orden orden,AsyncCallback<ArrayList<Reparacion>> callback);

	void eliminarOrden(Orden ordenAEliminar, Persona sesion, AsyncCallback<Boolean> callback);

	void listaTecnicos(AsyncCallback<ArrayList<Tecnico>> asyncCallback);

	void guardarOrdenAnulada(Orden orden, Persona sesion, AsyncCallback<Boolean> callback);

	void cargarTecnicosYEnc(AsyncCallback<ArrayList<Persona>> callback);

	void obtenerOrdenesCerradas(AsyncCallback<ArrayList<Orden>> callback);

	void obtenerHistoricoOrdenesTecnicos(AsyncCallback<ArrayList<DatoConsultaHistoricoOrdenesTecnico>> callback);

	void obtenerHistoricoOrdenesTecnicosDelimitado(Date inicio, Date fin,AsyncCallback<ArrayList<DatoConsultaHistoricoOrdenesTecnico>> callback);

	void actualizarOrdenRebotada(Orden nuevaOrden, Tecnico tecnico, Persona sesion,	AsyncCallback<Boolean> callback);

	void listaDelos10MasUsados(AsyncCallback<ArrayList<RepuestoDatoGrafica>> asyncCallback);

	void validarEliminarPersona(Persona persona, AsyncCallback<Boolean> callback);

	void cerrarOrden(Orden orden, Persona sesion, AsyncCallback<Boolean> callback);

	void ordenesActivasDeUnaEmpresa(Estacion empresa,AsyncCallback<ArrayList<DatoOrdenesActivasEmpresa>> callback);

	void modificarPersona(Persona persona, boolean claveCambiada,AsyncCallback<Boolean> callback);

	void eliminarPersona(Persona persona, AsyncCallback<Boolean> callback);

	void crearPDF(int numero, Boolean pdfBilpa, AsyncCallback<String> asyncCallback);

	void validarNuevoRepuesto(String descripcion, String nroSerie, AsyncCallback<Integer> callback);

	void obtenerEmpleadosSinEmpresa(AsyncCallback<ArrayList<ContactoEmpresa>> asyncCallback);

	void obtenerEmpleadosPorEstacion(Estacion estacionSeleccionada,	AsyncCallback<ArrayList<ContactoEmpresa>> asyncCallback);

	void actualizarEstacion(Estacion estacionSeleccionada,AsyncCallback<Boolean> asyncCallback);

	void obtenerSellos(AsyncCallback<ArrayList<Sello>> asyncCallback);

	void validarNuevaEstacion(Estacion estacion,AsyncCallback<Boolean> asyncCallback);

	void agregarEstacion(Estacion estacion, AsyncCallback<Boolean> asyncCallback);

	void esEstacionSinOrdenes(Estacion estacion,AsyncCallback<Boolean> asyncCallback);

	void eliminarEstacion(Estacion e, AsyncCallback<Boolean> asyncCallback);

	void obtenerTodasLasMArcas(AsyncCallback<ArrayList<Marca>> asyncCallback);
	
	void validarNombreDeMarcaDisponible(String nombreMarca, AsyncCallback<Boolean> asyncCallback);
	
	void agregarMarca(Marca marca, AsyncCallback<Boolean> asyncCallback);
	
	void buscarMarca(int id, AsyncCallback<Marca> asyncCallback);

	void buscarMarca(String nombre, AsyncCallback<Marca> asyncCallback);
	
	void modificarMarca(int idMarca, String nuevoNombreMarca, AsyncCallback<Boolean> asyncCallback);

	void agregarModelo(Modelo modelo, AsyncCallback<Boolean> callback);

	void validarModeloDisponible(String nombre, int idMarca,
			int idModeloExistente, AsyncCallback<Boolean> callback);

	void obtenerTodosLosModelosSurtidores(AsyncCallback<ArrayList<ModeloSurtidor>> callback);

	void guardarHistorico(HistoricoOrden historicoOrden, AsyncCallback<Boolean> asyncCallback);

	void guardarSurtidor(Surtidor s, AsyncCallback<Boolean> callback);

	void validarSurtidorExiste(Surtidor s, AsyncCallback<Boolean> callback);

	void obtenerTiposDeTrabajo(AsyncCallback<ArrayList<TipoTrabajo>> asyncCallback);

	void obtenerTodosLosActivosTipoPorEstacion(int tipo, int idEstacion, AsyncCallback<ArrayList<Activo>> callback);

	void obtenerTiposCombustibles(AsyncCallback<List<Producto>> asyncCallback);

	void guardarTanque(Tanque tanque, AsyncCallback<Boolean> callback);

	void guardarCanio(Canio canio, AsyncCallback<Boolean> callback);
	
	void validarNumeroDUCSA(String numeroDucsa,AsyncCallback<Boolean> asyncCallback);

	void guardarBomba(BombaSumergible bomba, AsyncCallback<Boolean> callback);

	void actualizarSurtidor(Surtidor s, AsyncCallback<Boolean> callback);

	void modificarModelo(ModeloSurtidor modeloSurtidor,	AsyncCallback<Boolean> asyncCallback);

	void modificarSurtidor(Surtidor s, AsyncCallback<Boolean> asyncCallback);

	void modificarTanque(Tanque tanque, AsyncCallback<Boolean> asyncCallback);

	void modificarCanio(Canio canio, AsyncCallback<Boolean> asyncCallback);

	void modificarBomba(BombaSumergible bomba,AsyncCallback<Boolean> asyncCallback);

	void guardarOrden(Orden orden, String estado, Persona sesion,
			AsyncCallback<Boolean> asyncCallback);

	void crearExcelOrdenesSello(Sello selloSeleccionado, Date inicio, Date fin, AsyncCallback<String> asyncCallback);
	
	void crearExcelPendientes(Date inicio, Date fin, List<Organizacion> organizaciones, AsyncCallback<String> asyncCallback);

	void ordenesPorSelloYFechas(Sello sello, Date inicio, Date fin,
			AsyncCallback<Boolean> asyncCallback);

	void obtenerCantidadOrdenes(Persona sesion,
			AsyncCallback<CantidadOrdenes> asyncCallback);

	void obtenerTodasLasOrdenesInactivas(
			AsyncCallback<ArrayList<OrdenData>> asyncCallback);

	void actualizarUsuariosSinAsignar(
			List<ContactoEmpresa> listaEmpleadosSinAsignar,
			AsyncCallback<Boolean> asyncCallback);

	void obtenerTodasLasOrdenesActivasData(
			AsyncCallback<ArrayList<OrdenData>> callback);

	void obtenerEmpresasDataList(
			AsyncCallback<ArrayList<EstacionDataList>> callback);

	void agregarTipoTarea(TipoTarea tipoTarea,
			AsyncCallback<Boolean> asyncCallback);

	void buscarTipoTarea(int id, AsyncCallback<TipoTarea> asyncCallback);

	void modificarTipoTareaBase(TipoTarea tipoTarea,AsyncCallback<Boolean> asyncCallback);

	void obtenerTodasLosTiposTareasActivos(AsyncCallback<ArrayList<TipoTarea>> asyncCallback);

	void validarTipoTareaExiste(String descripcion,AsyncCallback<Boolean> asyncCallback);

	void eliminarTipoTarea(TipoTarea result,AsyncCallback<Boolean> asyncCallback);

	void obtenerTodasLosTiposRepuestosActivos(
			AsyncCallback<ArrayList<TipoRepuesto>> asyncCallback);

	void agregarTipoRepuesto(TipoRepuesto tipoRepuesto,
			AsyncCallback<Boolean> asyncCallback);

	void buscarTipoRepuesto(int id, AsyncCallback<TipoRepuesto> asyncCallback);

	void validarTipoRepuestoExiste(String descripcion,
			AsyncCallback<Boolean> asyncCallback);

	void modificarTipoRepuestoBase(TipoRepuesto tipoRepuesto,
			AsyncCallback<Boolean> asyncCallback);

	void eliminarTipoRepuesto(TipoRepuesto result,
			AsyncCallback<Boolean> asyncCallback);

	void obtenerComentarios(
			Solucion solucion, AsyncCallback<ArrayList<Comentario>> asyncCallback);

	void obtenerHoraServidor(AsyncCallback<String> asyncCallback);

	void obtenerHoraServidorDate(AsyncCallback<Date> asyncCallback);

	void crearPDFPermiso(Orden orden, AsyncCallback<String> callback);
	
	void crearPDFVisitas(int idVisita, Organizacion organizacion, AsyncCallback<String> callback);

	void ordenesPorFecha(EstacionDataList e,
			Date desde, Date hasta, AsyncCallback<ArrayList<DatoOrdenesActivasEmpresa>> asyncCallback);

	void obtenerRepuestosLinea(Activo activo,
			AsyncCallback<ArrayList<RepuestoLinea>> asyncCallback);

	void obtenerDatoListadoActivosPorTipo(DatoOrdenesActivasEmpresa orden,
			AsyncCallback<ArrayList<DatoListadoReparaciones>> asyncCallback);

	void formatearFechas(Date desde, Date hasta, AsyncCallback<DateDesdeHasta> asyncCallback);

	void obtenerTodosLosTecnicos(AsyncCallback<ArrayList<Tecnico>> asyncCallback);
	
	void obtenerTodosLosDataTecnico(AsyncCallback<List<TecnicoData>> asyncCallback);

	void obtenerDataActivosPorTipo(Estacion estacion, Date desde, Date hasta,Tecnico tecnico, int tipoActivo,AsyncCallback<ArrayList<DatoListadoActivos>> asyncCallback);

	void obtenerDataActivosPorTipo(Estacion e, int idTipoActivo,
			AsyncCallback<ArrayList<ActivoData>> asyncCallback);

	void obtenerTiposFallasR(
			AsyncCallback<ArrayList<TipoFallaReportada>> asyncCallback);

	void obtenerTiposFallasT(
			AsyncCallback<ArrayList<TipoFallaTecnica>> asyncCallback);

	void buscarTipoFallaR(int id, AsyncCallback<TipoFallaReportada> asyncCallback);

	void eliminarTipoFallaR(TipoFallaReportada tipoFallaR, AsyncCallback<Boolean> asyncCallback);

	void eliminarTipoFallaT(TipoFallaTecnica tipoFallaT, AsyncCallback<Boolean> asyncCallback);

	void buscarTipoFallaT(int id, AsyncCallback<TipoFallaTecnica> asyncCallback);

	void modificarTipoFallaT(TipoFallaTecnica falla, AsyncCallback<Boolean> asyncCallback);

	void modificarTipoFallaR(TipoFallaReportada result, AsyncCallback<Boolean> asyncCallback);

	void agregarTipoFallaR(TipoFallaReportada fallaR, AsyncCallback<Boolean> asyncCallback);

	void agregarTipoFallaT(TipoFallaTecnica fallaT,	AsyncCallback<Boolean> asyncCallback);

	void obtenerFechaServidor(AsyncCallback<Date> asyncCallback);

	void obtenerTodasLasOrdenesActivasDataExcel(Sello s, Date inicio, Date fin,
			AsyncCallback<ArrayList<OrdenData>> callback);
	
	void obtenerTodosLosPendientesVisibles(Date inicio, Date fin, List<Organizacion> organizaciones, AsyncCallback<List<PendienteData>> callback);

	void removerRepuestosDeOrden(ArrayList<RepuestoLinea> repuetosLineaARemover,
			AsyncCallback<Boolean> asyncCallback);

	void obtenerDestinosDelCargo(AsyncCallback<ArrayList<DestinoDelCargoData>> callback);

	void obtenerOrdenes(OrdenData ordenFilter,
			AsyncCallback<ArrayList<OrdenData>> asyncCallback);

	void obtenerListaOrdenesData(AsyncCallback<ListaOrdenesData> asyncCallback);
	
	void obtenerListaVisitasDataWeb(int sello, AsyncCallback<List<VisitaDataList>> asyncCallback);
	
	void obtenerListaVisitasDataConFiltro(VisitaDataList visitaData, int sello, AsyncCallback<List<VisitaDataList>> asyncCallback);
	
	void obtenerListaVistasDataEstacionWeb(String nombreEmpresa, AsyncCallback<List<VisitaDataList>> asyncCallback);
	
	void eliminarVisita(VisitaDataList dataVisita, AsyncCallback<Boolean> asyncCallback);
	
	void nuevaVisita(VisitaDataList dataVisita, AsyncCallback<Boolean> asyncCallback);
	
	void modificarVisita(VisitaDataList dataVisita, AsyncCallback<Boolean> asyncCallback);

	void obtenerEstaciones(int idSello,
			AsyncCallback<List<Estacion>> asyncCallback);

	void obtenerTiposActivoGenerico(
			AsyncCallback<List<TipoActivoGenericoData>> callback);

	void agregarTipoActivoGenerico(TipoActivoGenerico param,
			AsyncCallback<Boolean> callback);

	void modificarTipoActivoGenerico(TipoActivoGenericoData param,
			AsyncCallback<Boolean> callback);

	void eliminarTipoActivoGenerico(TipoActivoGenericoData param,
			AsyncCallback<Boolean> callback);

	void obtenerMarcasActivoGenerico(TipoActivoGenericoData param,
			AsyncCallback<List<MarcaActivoGenerico>> callback);

	void agregarMarcaActivoGenerico(MarcaActivoGenerico param,
			AsyncCallback<Boolean> callback);

	void modificarMarcaActivoGenerico(MarcaActivoGenerico param,
			AsyncCallback<Boolean> callback);

	void eliminarMarcaActivoGenerico(MarcaActivoGenerico param,
			AsyncCallback<Boolean> callback);

	void obtenerModelosActivoGenerico(MarcaActivoGenerico param,
			AsyncCallback<List<ModeloActivoGenerico>> callback);

	void agregarModeloActivoGenerico(ModeloActivoGenerico param,
			AsyncCallback<Boolean> callback);

	void modificarModeloActivoGenerico(ModeloActivoGenerico param,
			AsyncCallback<Boolean> callback);

	void eliminarModeloActivoGenerico(ModeloActivoGenerico param,
			AsyncCallback<Boolean> callback);

	void obtenerItemsActivoGenerico(TipoActivoGenericoData tipo,
			AsyncCallback<List<ItemChequeoData>> callback);

	void agregarItemChequeo(ItemChequeoData param,
			AsyncCallback<Boolean> callback);

	void eliminarItemChequeo(ItemChequeoData param,
			AsyncCallback<Boolean> callback);

	void modificarItemChequeo(ItemChequeoData param,
			AsyncCallback<Boolean> callback);

	void guardarActivoGenerico(ActivoGenerico activo,
			AsyncCallback<Boolean> asyncCallback);

	void obtenerActivosGenericos(Estacion empresa, int tipoActivoGenerico,
			AsyncCallback<List<ActivoGenerico>> callback);

	void modificarActivoGenerico(ActivoGenerico activoGenericoSeleccionado,
			AsyncCallback<Boolean> asyncCallback);

	void eliminarActivoGenerico(ActivoGenerico object,
			AsyncCallback<Boolean> asyncCallback);

	void obtenerActivosDeEstacion(int idEstacion, AsyncCallback<List<Activo>> callback);

	void obtenerPendientes(int idSello, EstadoPendiente estadoPendiente, AsyncCallback<PendienteDataUI> asyncCallback);

	void obtenerFiltrosPendientes(int idSello, AsyncCallback<PendienteDataUI> asyncCallback);

	void descargarFotoPendiente(PendienteData pd, AsyncCallback<String> asyncCallback);

	void guardarPendiente(PendienteData pd, String fotoStr, AsyncCallback<Boolean> callback);

	void descartarPendiente(List<PendienteData> pendientes, String motivoDescarte, int idPersona, AsyncCallback<Boolean> callback);

	void asignarCorrectivoAPendientes(int id, int i, List<PendienteData> pendientes, AsyncCallback<Boolean> asyncCallback);

	void eliminarPendiente(int id, AsyncCallback<Boolean> asyncCallback);

	void obtenerPendientesDeEstacion(int numero, AsyncCallback<PendienteDataUI> asyncCallback);

	void reporteVisitas(Sello selloSeleccionado, Date inicio, Date fin, Organizacion operador,
			AsyncCallback<String> asyncCallback);

	void obtenerFallasRPorTipo(int idTipo, AsyncCallback<List<FallaReportada>> asyncCallback);

}
