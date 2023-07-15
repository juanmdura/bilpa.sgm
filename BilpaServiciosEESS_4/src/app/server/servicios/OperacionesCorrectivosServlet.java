package app.server.servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.client.dominio.Activo;
import app.client.dominio.Calibre;
import app.client.dominio.Comentario;
import app.client.dominio.DestinoDelCargo;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.FallaTecnica;
import app.client.dominio.Orden;
import app.client.dominio.Organizacion;
import app.client.dominio.Pico;
import app.client.dominio.Reparacion;
import app.client.dominio.Repuesto;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Surtidor;
import app.client.dominio.Tarea;
import app.client.dominio.Tecnico;
import app.client.dominio.TipoTrabajo;
import app.client.dominio.ValorSiNoNa;
import app.client.dominio.comparable.OrdenarActivoPorTipo;
import app.client.dominio.data.ActivoReparacionData;
import app.client.dominio.data.OrdenData;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PendienteDataList;
import app.client.dominio.data.ReporteData;
import app.client.dominio.data.SolucionData;
import app.client.dominio.json.CorrectivoJson;
import app.client.dominio.json.PendienteJson;
import app.client.dominio.json.RepuestoLineaCorregidoJson;
import app.client.dominio.json.SolucionJson;
import app.server.control.ControlActivo;
import app.server.control.ControlCalibre;
import app.server.control.ControlEmpresa;
import app.server.control.ControlFalla;
import app.server.control.ControlOperaciones;
import app.server.control.ControlOrden;
import app.server.control.ControlPDFPermisoDeTrabajo;
import app.server.control.ControlPendiente;
import app.server.control.ControlPersona;
import app.server.control.ControlReparacion;
import app.server.control.ControlRepuesto;
import app.server.control.correctivo.reporte.ControlCorrectivoReporteMobileOperador;
import app.server.exception.ReparacionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class OperacionesCorrectivosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Gson gson;

	public OperacionesCorrectivosServlet() {
		gson = new GsonBuilder().create();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		String operacion = req.getParameter("operacion");
		//operacion = "guardarSolucion";

		if (operacion != null) {
			PrintWriter out = resp.getWriter();

			if (operacion.equals("guardarPendiente")){
				guardarPendiente(req, out);

			} else if (operacion.equals("guardarSolucion")){
				guardarSolucion(req, out);

			} else if (operacion.equals("iniciarCorrectivo")) {
				iniciarCorrectivo(req, out);

			} else if (operacion.equals("modificarCorrectivo")) {
				modificarCorrectivo(req, out);

			} else if (operacion.equals("finalizarCorrectivo")) {
				finalizarCorrectivo(req, out);

			} 
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		String operacion = req.getParameter("operacion");

		if (operacion != null) {
			PrintWriter out = resp.getWriter();

			if (operacion.equals("obtenerPendientesDeActivo")){
				obtenerPendientesDeActivo(req, out);

			} else if (operacion.equals("obtenerCorrectivos")) {
				obtenerCorrectivos(req, out);

			} else if (operacion.equals("obtenerCorrectivo")) {
				obtenerCorrectivo(req, out);

			} else if (operacion.equals("obtenerTiposTrabajo")) {
				obtenerTiposTrabajo(req, out);

			} else if (operacion.equals("obtenerActivosNoReportados")) {
				obtenerActivosNoReportados(req, out);

			} else if (operacion.equals("obtenerPermisoDeTrabajo")) {
				obtenerPermisoDeTrabajo(req, out);

			} else if (operacion.equals("obtenerActivosConFallasReportadas")) {
				obtenerActivosConFallasReportadas(req, out);

			} else if (operacion.equals("obtenerSolucion")) {
				obtenerSolucion(req, out);

			} else if (operacion.equals("obtenerSoluciones")) {
				obtenerSoluciones(req, out);

			} else if (operacion.equals("eliminarSolucion")) {
				eliminarSolucion(req, out);

			} else if(operacion.equals("corregirActivoDeFallaReportada")){
				corregirActivoDeFallaReportada(req, out);

			} else if(operacion.equals("obtenerPreviewCorrectivo")) {
				obtenerPreviewCorrectivo(req,out);
			}
		}
	}

	private void obtenerPreviewCorrectivo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try {
			String numero = req.getParameter("numero");
			if (numero == null) {
				r = getResponse("Debe ingresar número de orden","201", null);
				return;
			}

			OrdenData ordenData = ControlOrden.getInstancia().buscarCorrectivo(Integer.parseInt(numero));
			if (ordenData == null) {
				r = getResponse("No existe orden con el numero: " + numero,"203", null);
				return;
			}

			ReporteData reporteData = ControlCorrectivoReporteMobileOperador.getInstancia().obtenerPreviewCorrectivo(Integer.valueOf(numero));
			r = getResponse("", "0", reporteData);
		} catch (NumberFormatException e) {
			r = getResponse("El numero de orden debe ser numérico", "104", null);

		} catch (Exception ex) {
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void corregirActivoDeFallaReportada(HttpServletRequest req, PrintWriter out){ 
		Response r = new Response();
		try {
			String numero = req.getParameter("numero");
			String idActivoReportado = req.getParameter("idActivoReportado");
			String idActivoCorregido = req.getParameter("idActivoCorregido");

			if (numero == null) {
				r = getResponse("Debe ingresar número de orden","201", null); 
				return;
			}		
			if(idActivoReportado == null){
				r = getResponse("Debe ingresar id del activo reportado","201", null); 
				return;
			}
			if(idActivoCorregido == null){
				r = getResponse("Debe ingresar id del activo corregido","201", null); 
				return;
			}

			Orden orden = ControlOrden.getInstancia().buscarOrden(Integer.parseInt(numero));
			if (orden == null) {
				r = getResponse("No existe orden con el numero: " + numero,	"203", null);
				return;
			}

			Activo activoReportado = ControlActivo.getInstancia().buscarActivo(Integer.parseInt(idActivoReportado));	
			if (activoReportado == null) {
				r = getResponse("No existe activo reportado con id: " + idActivoReportado, "203", null);
				return;
			}
			Activo activoCorregido = ControlActivo.getInstancia().buscarActivo(Integer.parseInt(idActivoCorregido));
			if (activoCorregido == null) {
				r = getResponse("No existe activo corregido con id: " + idActivoCorregido, "203", null);
				return;
			}

			if (activoReportado.getTipo() != activoCorregido.getTipo()) {
				r = getResponse("No coindicen los tipos de activos del activo reportado y del corregido", "203", null);
				return;
			}

			if (activoReportado.getEmpresa().getId() != orden.getEmpresa().getId()){
				r = getResponse("El activo reportado " + activoReportado.getId() + " no pertenece a la misma estacion que la orden: " + orden.getNumero(), "203", null);
				return;
			}

			if (activoReportado.getEmpresa().getId() != activoCorregido.getEmpresa().getId()){
				r = getResponse("El activo reportado " + activoReportado.getId() + " no pertenece a la misma estacion que el activo corregido: " + activoCorregido.getId(), "203", null);
				return;
			}

			ControlReparacion.getInstancia().actualizarActivoDeFallaReportada(orden, activoReportado, activoCorregido);		
			r = getResponse("", "0", true);
		} catch	(ReparacionException rex){
			r = getResponse(rex.getMessage(), "203", null);
		} catch (NumberFormatException e) {
			r = getResponse("El numero de orden y el id de los activos deben ser numéricos", "104", null);
		} catch (Exception ex) {
			ex.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void eliminarSolucion(HttpServletRequest req, PrintWriter out) { 
		Response r = new Response();

		try {
			String idSolucion = req.getParameter("idSolucion");
			ControlReparacion.getInstancia().eliminarSolucion(Integer.parseInt(idSolucion));
			r = getResponse("", "0", true);

		} catch (NumberFormatException e) {
			r = getResponse("El idSolucion deben ser numérico", "104", null);

		} catch (Exception ex) {
			ex.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerSolucion(HttpServletRequest req, PrintWriter out) { 
		Response r = new Response();

		try {
			String idSolucion = req.getParameter("idSolucion");
			SolucionData data = ControlReparacion.getInstancia().obtenerSolucion(Integer.parseInt(idSolucion));
			r = getResponse("", "0", data);

		} catch (NumberFormatException e) {
			r = getResponse("El idSolucion deben ser numérico", "104", null);

		} catch (Exception ex) {
			ex.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void guardarSolucion(HttpServletRequest req, PrintWriter out) { 
		Response r = new Response();
		try {

			GsonBuilder gSon = new GsonBuilder();
			SolucionJson solucionJson = gSon.create().fromJson(req.getReader(), SolucionJson.class);

			if (solucionJson.getIdTecnico() == 0){
				r = getResponse("Debe indicar el idTecnico (usuario logueado)", "157", null);
				return;
			}

			if(solucionJson.getComentario() != null && solucionJson.getComentario().getTexto() != null) {
				if (solucionJson.getComentario().getTexto().isEmpty()){
					r = getResponse("El texto del comentario no puede ser vacio", "157", null);
					return;
				}

				if(solucionJson.getComentario().getTexto().length() > 1000) {
					r = getResponse("El texto del comentario no debe tener mas de 255 caracteres", "158", null);
					return;
				} 

				if (solucionJson.getComentario().getId() > 0){
					Comentario comentario = ControlOrden.getInstancia().obtenerComentario(solucionJson.getComentario().getId());
					if (comentario == null){
						r = getResponse("No existe comentario con el id " + solucionJson.getComentario().getId(), "158", null);
						return;

					} else if (comentario.getOrden() != null && comentario.getOrden().getNumero() != solucionJson.getNumero()){
						r = getResponse("El comentario con id " + solucionJson.getComentario().getId() + " no pertenece a la orden con numero " + solucionJson.getNumero(), "158", null);
						return;
					}
				}
			} 

			if(solucionJson.getRepuestosLineaCorregidos() != null) {

				for(RepuestoLineaCorregidoJson repuesto : solucionJson.getRepuestosLineaCorregidos()) {

					if (repuesto.getIdRepuesto() <= 0){
						r = getResponse("El id del repuesto de la solucion debe ser mayor a 0", "160", null);
						return;
					}

					Repuesto repuestoData = ControlRepuesto.getInstancia().buscarRepuesto(repuesto.getIdRepuesto());
					if (repuestoData == null){
						r = getResponse("No existe repuesto con id " + repuesto.getIdRepuesto(), "162", null);
						return;
					}
				}
			}

			if (solucionJson.getNumero() == 0) {
				r = getResponse("Debe ingresar número de orden","201", null); 
				return;
			}

			Orden orden = ControlOrden.getInstancia().buscarOrden(solucionJson.getNumero());
			if (orden == null) {
				r = getResponse("No existe orden con el numero: " + solucionJson.getNumero(),	"203", null);
				return;
			}

			Reparacion reparacion = ControlReparacion.getInstancia().obtenerReparacionDeActivoEnOrden(solucionJson.getNumero(), solucionJson.getIdActivo(), solucionJson.getIdPendiente());

			if (reparacion != null){
				solucionJson.setIdReparacion(reparacion.getId());
			}
			
			SolucionData solucion; 
			if(solucionJson.getId() != 0) {
				solucion = ControlReparacion.getInstancia().obtenerSolucion(solucionJson.getId());
				if(solucion == null){
					r = getResponse("No existe solucion con id " + solucionJson.getId(), "177", null);
					return;
				}

				if (!orden.tieneSolucion(solucionJson.getId())){
					r = getResponse("La solucion con id " + solucionJson.getId() + " no pertenece a la orden " + solucionJson.getNumero(), "177", null);
					return;
				}
			}

			FallaTecnica falla = ControlFalla.getInstancia().buscarFallaTecnica(solucionJson.getIdFalla());
			if(falla == null){
				r = getResponse("No existe falla con id " + solucionJson.getIdFalla(), "179", null);
				return;
			}

			Tarea tarea = ControlFalla.getInstancia().buscarTarea(solucionJson.getIdTarea());
			if(tarea == null){
				r = getResponse("No existe tarea con id " + solucionJson.getIdTarea(), "180", null);
				return;
			}

			List<SolucionData> reparaciones = 
					ControlReparacion.getInstancia().obtenerReparacionesDeActivoEnOrden(solucionJson.getNumero(), solucionJson.getIdActivo());
			
			for (SolucionData solucionData : reparaciones) {
				if (solucionData.getTarea().getId() == solucionJson.getIdTarea() 
						&& solucionData.getFallaTecnica().getId() == solucionJson.getIdFalla() && mismoPico(solucionData, solucionJson) &&
						solucionData.getId() != solucionJson.getId()){

					r = getResponse("Ya ha agregado esta reparación a este activo.", "180", null);
					return;
				}
				
				
				if (reparacion != null && solucionJson.getIdActivo() == reparacion.getActivo().getId()){
					if (solucionJson.getRepuestosLineaCorregidos() != null)	{
						for (RepuestoLinea repuesto : solucionData.getRepuestos()) {
							for(RepuestoLineaCorregidoJson repuestoInput : solucionJson.getRepuestosLineaCorregidos()){
								if (repuesto.getRepuesto().getId() == repuestoInput.getIdRepuesto()&&
										solucionData.getId() != solucionJson.getId()){
									r = getResponse("Ya seleccionó este repuesto para ese activo.", "180", null);
									return;
								}
							}
						}						
					}
				}
			}

			DestinoDelCargo destinoDelCargo = ControlOrden.getInstancia().buscarDestinoDelCargo(solucionJson.getIdDestinoDelCargo());
			if(destinoDelCargo == null){
				r = getResponse("No existe destino del cargo con id " + solucionJson.getIdDestinoDelCargo(), "181", null);
				return;
			}

			Activo activo = ControlActivo.getInstancia().buscarActivo(solucionJson.getIdActivo());
			if (activo == null){
				r = getResponse("No existe activo con el id: " + solucionJson.getIdActivo(), "198", null);
				return;
			}

			if ((solucionJson.getCalibre() != null || 
					solucionJson.getContador() != null || 
					solucionJson.getPrecinto() != null) && solucionJson.getIdPico() == 0) {
				r = getResponse("Debe indicar el id de pico", "131", null);
				return;
			}

			if(activo.getTipo() == 1 && solucionJson.getIdPico() > 0){
				Surtidor surtidor = (Surtidor)activo;
				Pico pico = surtidor.buscarPico(solucionJson.getIdPico());
				if (pico == null){
					r = getResponse("El pico con id " + solucionJson.getIdPico() + " no pertenece al surtidor con id " + surtidor.getId(), "181", null);
					return;	
				}
			}

			if (solucionJson.getPrecinto() != null && solucionJson.getPrecinto().getRemplazado().equals(ValorSiNoNa.SI)){
				if (solucionJson.getCalibre() == null || solucionJson.getCalibre().getCalibre4() == null || solucionJson.getCalibre().getCalibre5() == null || solucionJson.getCalibre().getCalibre6() == null ){
					r = getResponse("Si el precinto es reemplazado, debe ingresar los calibres 4, 5, 6. ", "131", null);
					return;
				}
			}

			if (solucionJson.getCalibre() != null && solucionJson.getCalibre().getId() > 0){
				Calibre calibre = ControlCalibre.getInstancia().obtenerCalibre(solucionJson.getCalibre().getId());
				if (calibre.getPico().getId() != solucionJson.getIdPico()){
					r = getResponse("El pico con id " + solucionJson.getIdPico() + ", no pertenece al calibre con id " + calibre.getId(), "131", null);
					return;
				}
			}

			if (solucionJson.getRepuestosLineaCorregidos() != null){
				for (RepuestoLineaCorregidoJson rlj : solucionJson.getRepuestosLineaCorregidos()) {
					Repuesto repuesto = ControlRepuesto.getInstancia().buscarRepuesto(rlj.getIdRepuesto());
					if (repuesto == null){
						r = getResponse("No existe repuesto con id " + rlj.getIdRepuesto(), "131", null);
						return;
					}
				}
			}

			if (solucionJson.getIdPendiente() > 0){
				PendienteData pendiente = ControlPendiente.getInstancia().obtenerPendiente(solucionJson.getIdPendiente());
				if (pendiente == null){
					r = getResponse("No existe pendiente con id " + solucionJson.getIdPendiente(), "131", null);
					return;
				}
			}

			if (solucionJson.getIdTarea() > 0 && 
					solucionJson.getIdFalla() > 0 && 
					solucionJson.getIdDestinoDelCargo() > 0 && 
					solucionJson.getIdActivo() > 0 && 
					solucionJson.getNumero() > 0) {

				int idSolucion = ControlReparacion.getInstancia().guardarSolucion(solucionJson);

				r = getResponse("", "0", idSolucion);

			} else {
				if (solucionJson.getIdTarea() == 0){
					r = getResponse("Debe indicar una tarea", "198", null);
					return;

				} else if (solucionJson.getIdFalla() == 0){
					r = getResponse("Debe indicar una falla", "198", null);
					return;

				} else if (solucionJson.getIdDestinoDelCargo() == 0){
					r = getResponse("Debe indicar el destino del cargo", "198", null);
					return;

				} else if (solucionJson.getIdActivo() == 0){
					r = getResponse("Debe indicar un activo", "198", null);
					return;

				} else if (solucionJson.getNumero() == 0){
					r = getResponse("Debe indicar un correctivo", "198", null);
					return;
				}   
			}

		} catch (JsonSyntaxException jsonException){
			jsonException.printStackTrace();
			r = getResponse("Error en los parametros", "100", null);


		} catch (Exception ex) {
			ex.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private boolean mismoPico(SolucionData solucionData, SolucionJson solucionJson) {
		return (solucionData.getPico() != null && 
				solucionJson.getContador() != null && 
				solucionJson.getContador().getPico() != null && 
				solucionJson.getContador().getPico().getNumeroPico() == solucionData.getPico().getNumeroPico() && 
				solucionJson.getContador().getPico().getNumeroEnLaEstacion() == solucionData.getPico().getNumeroEnLaEstacion());			
		
	}

	private void obtenerSoluciones(HttpServletRequest req, PrintWriter out) { 
		Response r = new Response();

		try {
			String idActivo = req.getParameter("idActivo");
			String numero = req.getParameter("numero");
			if (numero == null) {
				r = getResponse("Debe ingresar número de orden","201", null); 
				return;
			}

			Orden orden = ControlOrden.getInstancia().buscarOrden(Integer.parseInt(numero));
			if (orden == null) {
				r = getResponse("No existe orden con el numero: " + numero,	"203", null);
				return;
			}

			List<SolucionData> data = ControlReparacion.getInstancia().obtenerReparacionesDeActivoEnOrden(Integer.parseInt(numero), Integer.parseInt(idActivo));

			r = getResponse("", "0", data);
		} catch (NumberFormatException e) {
			r = getResponse("El numero de la orden y el idActivo deben ser numéricos", "104", null);

		} catch (Exception ex) {
			ex.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerActivosConFallasReportadas(HttpServletRequest req, PrintWriter out) { 
		Response r = new Response();

		try {
			String numero = req.getParameter("numero");
			if (numero == null) {
				r = getResponse("Debe ingresar número de orden","201", null); 
				return;
			}

			Orden orden = ControlOrden.getInstancia().buscarOrden(Integer.parseInt(numero));
			if (orden == null) {
				r = getResponse("No existe orden con el numero: " + numero,	"203", null);
				return;
			}

			List<ActivoReparacionData> data = ControlReparacion.getInstancia().obtenerActivosConFallasReportadas(Integer.parseInt(numero));
			r = getResponse("", "0", data);

		} catch (NumberFormatException e) {
			r = getResponse("El numero de la orden debe ser numérico", "104", null);

		} catch (Exception ex) {
			ex.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerTiposTrabajo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try {
			List<TipoTrabajo> tiposTrabajo = ControlOrden.getInstancia().obtenerTiposDeTrabajo();
			r = getResponse("", "0", tiposTrabajo);
		} catch (Exception ex) {
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerActivosNoReportados(HttpServletRequest req, PrintWriter out) {  
		Response r = new Response();

		try {
			String empresaId = req.getParameter("idEstacion");
			String numero = req.getParameter("numero");
			if (numero == null) {
				r = getResponse("Debe ingresar número de orden","201", null);
				return;
			}

			Orden orden = ControlOrden.getInstancia().buscarOrden(Integer.parseInt(numero));
			if (orden == null) {
				r = getResponse("No existe orden con el numero: " + numero,	"203", null);
				return;
			}

			if (empresaId == null) {
				r = getResponse("Debe ingresar id de la estacion","201", null); 
				return;
			}

			List<Activo> activos = ControlEmpresa.getInstancia().obtenerActivosXEmpresa(new Estacion(Integer.parseInt(empresaId)), Integer.parseInt(numero));
			quitarActivosReportados(activos, Integer.parseInt(numero));
			List<Activo> activosOrdenados = ordenarListaActivosNoReportados(activos);	

			r = getResponse("", "0", activosOrdenados);
		} catch (NumberFormatException e) {
			r = getResponse("El id de estacion y el numero de orden debe ser numérico", "104", null);
		} catch (Exception ex) {
			ex.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void quitarActivosReportados(List<Activo> activos, int numero) {
		List<ActivoReparacionData> activosReportados = ControlReparacion.getInstancia().obtenerActivosConFallasReportadas(numero);
		List<Activo> activosARemover = new ArrayList<Activo>();

		for (ActivoReparacionData activoReparacionData : activosReportados) {
			for (Activo activo : activos) {
				if (activoReparacionData.getActivo().getId() == activo.getId()){
					activosARemover.add(activo);
				}
			}
		}
		activos.removeAll(activosARemover);
	}

	private List<Activo> ordenarListaActivosNoReportados(List<Activo> activos){
		List<Activo> activosConPendientes = new ArrayList<Activo>();
		List<Activo> activosSinPendientes = new ArrayList<Activo>();
		List<PendienteDataList> pendientes;

		for(Activo a : activos){
			pendientes = ControlOperaciones.getInstancia().obtenerPendientesDeActivo(a.getId());
			if(pendientes==null || pendientes.isEmpty()){
				activosSinPendientes.add(a);
			}else{
				activosConPendientes.add(a);
			}
		}

		Collections.sort(activosConPendientes, new OrdenarActivoPorTipo());		
		Collections.sort(activosSinPendientes, new OrdenarActivoPorTipo());

		List<Activo> res = new ArrayList<Activo>();
		res.addAll(activosConPendientes);
		res.addAll(activosSinPendientes);

		return res;
	}

	private void obtenerPermisoDeTrabajo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();

		try {
			String numero = req.getParameter("numero");
			if (numero == null) {
				r = getResponse("Debe ingresar número de orden","201", null);
				return;
			}

			Orden orden = ControlOrden.getInstancia().buscarOrden(Integer.parseInt(numero));
			if (orden == null) {
				r = getResponse("No existe orden con el numero: " + numero,	"203", null);
				return;
			}

			String bytes = ControlPDFPermisoDeTrabajo.getInstancia().crearPDFPermisoByte(orden);

			r = getResponse("", "0", bytes);
		} catch (NumberFormatException e) {
			r = getResponse("El numero de orden debe ser numérico", "104", null);

		} catch (Exception ex) {
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerCorrectivo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try {
			String numero = req.getParameter("numero");
			if (numero == null) {
				r = getResponse("Debe ingresar número de orden","201", null);
				return;
			}

			OrdenData ordenData = ControlOrden.getInstancia().buscarCorrectivo(Integer.parseInt(numero));
			if (ordenData == null) {
				r = getResponse("No existe orden con el numero: " + numero,"203", null);
				return;
			}

			r = getResponse("", "0", ordenData);
		} catch (NumberFormatException e) {
			r = getResponse("El numero de orden debe ser numérico", "104", null);

		} catch (Exception ex) {
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void iniciarCorrectivo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();

		try {
			GsonBuilder gSon = new GsonBuilder();
			// gSon.setDateFormat(("yyyy/MM/dd HH:mm:ss"));
			CorrectivoJson correctivoJson = gSon.create().fromJson(req.getReader(), CorrectivoJson.class);
			correctivoJson.setEstado(7);
			correctivoJson.setInicioReal(new Date());

			/*if (correctivoJson.getInicio() == null){
				r = getResponse("Para iniciar el correctivo se debe indicar la fecha de inicio",	"203", null);
				return;
			}*/
			
			int numero = correctivoJson.getNumero();
			Orden orden = ControlOrden.getInstancia().buscarOrden(numero);
			if (orden == null) {
				r = getResponse("No existe orden con el numero: " + numero,	"203", null);
				return;
			}

			ControlOrden.getInstancia().actualizarOrden(correctivoJson);

			r = getResponse("", "0", true);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void modificarCorrectivo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();

		try {
			GsonBuilder gSon = new GsonBuilder();
			gSon.setDateFormat(("yyyy/MM/dd HH:mm:ss"));
			CorrectivoJson correctivoJson = gSon.create().fromJson(req.getReader(), CorrectivoJson.class);

			int numero = correctivoJson.getNumero();

			Orden orden = ControlOrden.getInstancia().buscarOrden(numero);
			if (orden == null) {
				r = getResponse("No existe orden con el número: " + numero,	"203", null);
				return;
			}

			correctivoJson.setEstado(7);
			ControlOrden.getInstancia().actualizarOrden(correctivoJson);

			r = getResponse("", "0", true);
		} catch (NumberFormatException e) {
			r = getResponse(
					"El numero de orden, el estado y el tipo de trabajo, deben ser numéricos",
					"104", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void finalizarCorrectivo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();

		try {
			GsonBuilder gSon = new GsonBuilder();
			// gSon.setDateFormat(("yyyy/MM/dd HH:mm:ss"));
			CorrectivoJson correctivoJson = gSon.create().fromJson(req.getReader(), CorrectivoJson.class);
			correctivoJson.setEstado(5);
			correctivoJson.setFinReal(new Date());
			
			/*if (correctivoJson.getFin() == null){
				r = getResponse("Para finalizar el correctivo se debe indicar la fecha de fin",	"203", null);
				return;
			}*/

			int numero = correctivoJson.getNumero();
			Orden orden = ControlOrden.getInstancia().buscarOrden(numero);
			if (orden == null) {
				r = getResponse("No existe orden con el numero: " + numero,	"203", null);
				return;
			}

			if (correctivoJson.getMails() == null || correctivoJson.getMails().size() == 0){
				r = getResponse("Para finalizar el correctivo debe indicar al menos una cuenta de correo",	"203", null);
				return;
			}

			ControlOrden.getInstancia().actualizarOrden(correctivoJson);
			ControlOrden.getInstancia().finalizarCorrectivo(correctivoJson);

			r = getResponse("", "0", true);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerCorrectivos(HttpServletRequest req, PrintWriter out) {
		String id = req.getParameter("idTecnico");

		Response r = new Response();

		try {
			int idTecnico = Integer.parseInt(id);
			Tecnico tecnico = ControlPersona.getInstancia().obtenerTecnico(idTecnico);

			if (tecnico == null) {
				r = getResponse("No existe tecnico con el identificador: " + idTecnico, "103", null);
				return;
			}

			ArrayList<OrdenData> ordenes = ControlOrden.getInstancia().ordenesActivasTecnicoData(tecnico);
			r = getResponse("", "0", ordenes);

		} catch (NumberFormatException e) {
			r = getResponse("El identificador del tecnico debe ser numérico",
					"104", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerPendientesDeActivo(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idActivoStr = req.getParameter("idActivo");
			int idActivo;
			List<PendienteDataList> pendientes = new ArrayList<PendienteDataList>();
			try {
				idActivo = Integer.valueOf(idActivoStr);
				Activo activo = ControlActivo.getInstancia().buscarActivo(idActivo);
				if (activo == null){
					r = getResponse("No existe activo con el id: " + idActivo, "198", null);
					return;
				}
				pendientes = ControlOperaciones.getInstancia().obtenerPendientesDeActivo(idActivo);

				if(pendientes != null){
					r = getResponse("", "0", pendientes);
				} else {
					r = getResponse("", "0", new ArrayList<PendienteDataList>());
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del activo debe ser numérico", "198", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void guardarPendiente(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try {
			GsonBuilder gSon = new GsonBuilder();
			gSon.setDateFormat(("yyyy/MM/dd"));
			PendienteJson pendienteJson = gSon.create().fromJson(req.getReader(), PendienteJson.class);
			String idStr = pendienteJson.getId();
			String fotoStr = pendienteJson.getFotoBytes();
			String comentario = pendienteJson.getComentario(); 
			String comentarioVisible = pendienteJson.getComentarioVisible();
			int numero = pendienteJson.getNumero();
			int idActivo = pendienteJson.getIdActivo();

			String destinatario = pendienteJson.getDestinatario();
			//int idItemChequeado = pendienteJson.getIdItemChequeado();

			OrdenData orden;

			orden = ControlOrden.getInstancia().buscarCorrectivo(numero);

			if(orden == null){
				r = getResponse("No existe correctivo con identificador " + numero, "127", null);
				return;
			}

			boolean activoPerteneceAEmpresaDeOrden = false;
			Set<Activo> activos = ControlOrden.getInstancia().buscarOrden(numero).getEmpresa().getListaDeActivos();
			for (Activo activo : activos){
				if (activo.getId() == idActivo){
					activoPerteneceAEmpresaDeOrden = true;
				}
			}

			if(!activoPerteneceAEmpresaDeOrden){
				r = getResponse("La estacion del correctivo, no tiene un activo con id " + idActivo, "127", null);
				return;
			}

			int id = 0;
			try {
				if(idStr != null && !idStr.isEmpty()) {
					id = Integer.valueOf(idStr);

					PendienteData pendiente = ControlPendiente.getInstancia().obtenerPendiente(id);
					if (pendiente == null){
						r = getResponse("No existe pendiente con id " + id, "198", null);
						return;
					}
				}
			} catch (NumberFormatException e) {
				r = getResponse("El identificador del pendiente debe ser numérico", "198", null);
				return;
			}

			if(!comentarioVisible.equalsIgnoreCase("true") && !comentarioVisible.equalsIgnoreCase("false")) {
				r = getResponse("El campo comentario visible debe ser true o false.", "199", null);
				return;
			}

			if (comentario.length() > 256){
				r = getResponse("El campo comentario debe ser maximo de 256 caracteres.", "199", null);
				return;
			}

			if(pendienteJson.getPlazo() != null && pendienteJson.getPlazo().before(new Date())) {
				r = getResponse("El plazo debe ser mayor al dia de hoy.", "207", null);
				return;
			}

			try{
				Organizacion.valueOf(destinatario);
			}catch(Exception e){
				r = getResponse("El destinatario debe ser: " + Organizacion.Bilpa + ", " + Organizacion.Petrolera + ", o " + Organizacion.Operador , "207", null);
				return;
			}

			PendienteData pd = new PendienteData();
			pd.setId(id);
			pd.setComentario(comentario);
			pd.setComentarioVisible(Boolean.parseBoolean(comentarioVisible));
			pd.setOrdenCreado(numero);
			pd.setPlazo(pendienteJson.getPlazo());
			//pd.setIdItemChequeado(pendienteJson.getIdItemChequeado()); en correctivos no hay itemChequeado
			pd.setDestinatario(destinatario);
			pd.setEstado(EstadoPendiente.INICIADO);
			pd.setIdActivo(idActivo);

			if(ControlOperaciones.getInstancia().guardarPendiente(pd, fotoStr)) {
				r = getResponse("", "0", true);
			} else {
				r = getResponse("Error al guardar el pendiente.", "201", null);
			}

		} catch (IOException e) {
			r = getResponse("Error guardando la foto del pendiente", "202", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);

		} finally {
			enviarRespuesta(out, r);
		}
	}

	private Response getResponse(String error, String status, Object o) {
		Response r = new Response();
		r.setError(error);
		r.setStatus(status);
		r.setDatos(o);
		return r;
	}

	private void enviarRespuesta(PrintWriter out, Response r) {
		String jsonResponse = gson.toJson(r);
		out.print(jsonResponse);
	}
}
