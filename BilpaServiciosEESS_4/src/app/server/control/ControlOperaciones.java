package app.server.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import app.client.dominio.Activo;
import app.client.dominio.Chequeo;
import app.client.dominio.Corregido;
import app.client.dominio.EstadoVisita;
import app.client.dominio.FallaTecnica;
import app.client.dominio.Organizacion;
import app.client.dominio.Persona;
import app.client.dominio.Repuesto;
import app.client.dominio.Tarea;
import app.client.dominio.Tecnico;
import app.client.dominio.TipoFallaTecnica;
import app.client.dominio.TipoRepuesto;
import app.client.dominio.TipoTarea;
import app.client.dominio.Visita;
import app.client.dominio.data.ChequeoPreventivoData;
import app.client.dominio.data.CorregidoData;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PendienteDataList;
import app.client.dominio.data.PicoData;
import app.client.dominio.data.ProductoData;
import app.client.dominio.data.ReporteData;
import app.client.dominio.data.TecnicoData;
import app.client.dominio.data.TipoDescargaData;
import app.client.dominio.data.UsuarioData;
import app.client.dominio.data.VisitaData;
import app.client.dominio.data.VisitaDataList;
import app.server.control.visita.ControlVisita;
import app.server.control.visita.reporte.mobile.ControlVisitaReporteMobileOperador;
import app.server.control.visita.reporte.web.ControlVisitaReporteWebDucsaYBilpa;
import app.server.exception.ValorChequeoInvalidoException;

public class ControlOperaciones implements IOperaciones{

	private static ControlOperaciones instancia = null;

	public static ControlOperaciones getInstancia() {
		if(instancia == null){
			instancia = new ControlOperaciones();
		}
		return instancia;
	}


	private ControlOperaciones (){

	}

	public UsuarioData login(String user, String pass){
		Persona persona = ControlPersona.getInstancia().validarUsuario(user, pass);

		if(persona != null){
			UsuarioData usuario = persona.getUsuarioData(persona);
			return usuario;
		}
		return null;
	}

	public ArrayList<VisitaDataList> obtenerVisitasAsignadas(int id) {
		return ControlVisita.getInstancia().obtenerVisitasAsignadas(id);
	}

	public VisitaData obtenerVisitaWS(int idVisita) throws Exception {
		return ControlVisita.getInstancia().obtenerVisitaDataWS(idVisita);
	}

	public TecnicoData obtenerTecnico(int idTecnico) {
		Tecnico tecnico = ControlPersona.getInstancia().obtenerTecnico(idTecnico);
		if(tecnico != null){
			return tecnico.getTecnicoData();
		}
		return null;
	}

	public List<DestinoDelCargoData> obtenerDestinosDelCargo() {
		return ControlOrden.getInstancia().obtenerDestinosDelCargo();
	}

	public List<TipoFallaTecnica> obtenerTipoFallasTecnicas() {
		return ControlTipoFalla.getInstancia().obtenerTiposFallasActivasT();
	}

	public List<TipoTarea> obtenerTipoTareas() {
		return ControlTipoTarea.getInstancia().obtenerTodasLosTiposTareasActivos();
	}

	public List<TipoRepuesto> obtenerTipoRepuestos() {
		return ControlTipoRepuesto.getInstancia().obtenerTodasLosTiposRepuestosActivos();
	}

	public List<FallaTecnica> obtenerFallasTecnicas() {
		return ControlFalla.getInstancia().obtenerTodasLasFallasTecnicas();
	}

	public List<FallaTecnica> obtenerFallasTecnicas(TipoFallaTecnica tipo) {
		return ControlFalla.getInstancia().obtenerFallasTPorTipo(tipo);
	}

	public List<Tarea> obtenerTareas() {
		return ControlFalla.getInstancia().obtenerTodasLasTareas();
	}

	public List<Tarea> obtenerTareas(TipoTarea tipo) {
		return ControlFalla.getInstancia().obtenerTareas(tipo);
	}

	public List<Repuesto> obtenerRepuestos() {
		return ControlRepuesto.getInstancia().obtenerTodosLosRepuestos();
	}

	public List<Repuesto> obtenerRepuestos(TipoRepuesto tipo) {
		return ControlRepuesto.getInstancia().obtenerRepuestos(tipo);
	}

	public ChequeoPreventivoData obtenerChequeoPreventivo(Visita visita, Activo activo, ChequeoPreventivoData chequeoPreventivoData, Chequeo chequeo) throws Exception {
		return ControlChequeo.getInstancia().obtenerChequeoPreventivo(visita, activo, chequeoPreventivoData, chequeo);
	}
	
	public void modificarVisita(int idVisita, Date fechaInicio, Date fechaFin, byte[] firma, String comentarioFirma, EstadoVisita estadoVisita) throws Exception {
		ControlVisita.getInstancia().modificarVisitaWS(idVisita, fechaInicio, fechaFin, firma, comentarioFirma, estadoVisita);
	}

	public List<PicoData> obtenerPicos(int idSurtidor) {
		return ControlPico.getInstancia().obtenerPicos(idSurtidor);
	}

	public boolean asociarQrAActivo(int codigoQr, int idActivo) throws ConstraintViolationException, Exception {
		return ControlActivo.getInstancia().asociarQrAActivo(idActivo, codigoQr);
	}

	public boolean asociarQrAPico(int codigoQr, int idPico) throws ConstraintViolationException, Exception {
		return ControlPico.getInstancia().asociarQrAPico(codigoQr, idPico);
	}

	public void guardarCorregido(Corregido corregido, PendienteData pendiente, int idItemChequeado, String foto, String foto2) throws Exception {
		ControlCorregido.getInstancia().guardarCorregido(corregido, pendiente, idItemChequeado, foto, foto2);		
	}


	public List<CorregidoData> obtenerCorregidos(int idPreventivo) throws Exception {
		return ControlCorregido.getInstancia().obtenerCorregidos(idPreventivo);
	}


	public CorregidoData obtenerCorregido(int idCorregido) throws Exception {
		return ControlCorregido.getInstancia().obtenerCorregidoData(idCorregido);
	}


	public boolean eliminarCorregido(int idCorregido) throws Exception {
		return ControlCorregido.getInstancia().eliminarCorregido(idCorregido);
	}
	
	public void validarValorChequeo(String valor) throws ValorChequeoInvalidoException{
		if (valor != null && !valor.isEmpty() && !valor.equalsIgnoreCase("B") && !valor.equalsIgnoreCase("R") && !valor.equalsIgnoreCase("N/A") && !valor.equalsIgnoreCase("P")){
			ValorChequeoInvalidoException ex = new ValorChequeoInvalidoException(valor);
			
			throw ex;
		}
	}

	public PendienteData obtenerPendiente(int idPendiente) {
		return ControlPendiente.getInstancia().obtenerPendiente(idPendiente);
	}

	public List<PendienteDataList> obtenerPendientes(int idPreventivo) {
		return ControlPendiente.getInstancia().obtenerPendientesPreventivos(idPreventivo);
	}
	
	public boolean guardarPendiente(PendienteData pd, String fotoStr) throws Exception {
		return ControlPendiente.getInstancia().guardarPendiente(pd, fotoStr);
	}

	public boolean eliminarPendiente(int idPendiente) {
		return ControlPendiente.getInstancia().eliminarPendientes(idPendiente);
	}

	public List<TipoDescargaData> obtenerTiposDescarga() {
		return ControlDescarga.getInstancia().obtenerTiposDescarga();
	}
	
	public List<ProductoData> obtenerProductos(int idSurtidor) {
		return ControlPico.getInstancia().obtenerProductos(idSurtidor);
	}

	public ReporteData reporteVisitaPreventivaEstaciones(int idVisita) throws Exception {
		return ControlVisitaReporteMobileOperador.getInstancia().reporteVisitaPreventivaEstaciones(idVisita);
	}


	public byte[] obtenerReporteVisitaPreventiva(int idVisita) {
		return ControlVisitaReporteWebDucsaYBilpa.getInstancia().crearPDFPreventivosVisitasBytes(idVisita, Organizacion.Operador);
	}


	public List<PendienteDataList> obtenerPendientesPorChequeo(int idItemChequeado) {
		return ControlPendiente.getInstancia().obtenerPendientesPorChequeo(idItemChequeado);
	}


	public List<CorregidoData> obtenerCorregidosPorChequeo(int idItemChequeado) {
		return ControlCorregido.getInstancia().obtenerCorregidosPorChequeo(idItemChequeado);
	}


	public boolean actualizarPreventivo(int idPreventivo, Chequeo chequeo) throws Exception {
		return ControlChequeo.getInstancia().actualizarPreventivo(idPreventivo, chequeo);
	}


	public List<PendienteDataList> obtenerPendientesDeActivo(int idActivo) {
		return ControlPendiente.getInstancia().obtenerPendientesListDeActivo(idActivo, null);
	}
}
