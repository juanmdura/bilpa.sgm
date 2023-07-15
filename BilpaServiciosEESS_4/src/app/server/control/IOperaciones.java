package app.server.control;

import java.util.Date;
import java.util.List;

import app.client.dominio.Corregido;
import app.client.dominio.EstadoVisita;
import app.client.dominio.FallaTecnica;
import app.client.dominio.Repuesto;
import app.client.dominio.Tarea;
import app.client.dominio.TipoFallaTecnica;
import app.client.dominio.TipoRepuesto;
import app.client.dominio.TipoTarea;
import app.client.dominio.data.CorregidoData;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PicoData;
import app.client.dominio.data.TecnicoData;
import app.client.dominio.data.UsuarioData;
import app.client.dominio.data.VisitaData;
import app.client.dominio.data.VisitaDataList;

public interface IOperaciones {

	UsuarioData login(String user, String pass);

	List<VisitaDataList> obtenerVisitasAsignadas(int idTecnico);
	VisitaData obtenerVisitaWS(int idVisita) throws Exception;
	void modificarVisita(int idVisita, Date fechaInicio, Date fechaFin, byte[] firma, String comentarioFirma, EstadoVisita estadoVisita) throws Exception;

	TecnicoData obtenerTecnico(int idTecnico);
	List<DestinoDelCargoData> obtenerDestinosDelCargo();

	List<TipoTarea> obtenerTipoTareas();
	List<TipoRepuesto> obtenerTipoRepuestos();
	List<TipoFallaTecnica> obtenerTipoFallasTecnicas();

	List<FallaTecnica> obtenerFallasTecnicas();
	List<Tarea> obtenerTareas();
	List<Repuesto> obtenerRepuestos();

	List<FallaTecnica> obtenerFallasTecnicas(TipoFallaTecnica tipo);
	List<Tarea> obtenerTareas(TipoTarea tipo);
	List<Repuesto> obtenerRepuestos(TipoRepuesto tipo);

	List<PicoData> obtenerPicos(int idSurtidor);
	
	boolean asociarQrAActivo(int codigoQr, int idActivo) throws Exception;
	
	boolean asociarQrAPico(int codigoQr, int idPico) throws Exception;

	List<CorregidoData> obtenerCorregidos(int idPreventivo) throws Exception;
	CorregidoData obtenerCorregido(int idCorregido) throws Exception;

	void guardarCorregido(Corregido corregido, PendienteData pendiente, int idItemChequeado, String foto, String foto2) throws Exception;


	// *** Pendientes ***
	/*
 	. obtenerVisitasAsignadas
		. Calcular: Ultima visita y dias sin vista

	. obtenerPendientes(int idPreventivo)

	. obtenerPendiente(int idPendiente)

	. guardarPendiente(int idPreventivo, int idPendiente, String texto, boolean visible, Date plazo, byte[] foto)

	. obtenerChequeoSurtidor(int idVisita, int idActivo)
		. Datos generales
		. Lista de productos
		. Lista de mangueras

	. guardarChequeoSurtidorGeneral(int idChequeoSurtidor)

	. guardarChequeoSurtidorProducto(int idProducto, ChequeoProducto chequeoProducto)

	. guardarChequeoSurtidorPico(int idPico, ChequeoPico chequeoPico) 

	 */
	// *** Pendientes ***

}
