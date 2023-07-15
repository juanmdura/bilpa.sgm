package app.server.servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.exception.ConstraintViolationException;

import app.client.dominio.Activo;
import app.client.dominio.ChequeoBomba;
import app.client.dominio.ChequeoGenerico;
import app.client.dominio.ChequeoPico;
import app.client.dominio.ChequeoProducto;
import app.client.dominio.ChequeoSurtidor;
import app.client.dominio.ChequeoTanque;
import app.client.dominio.ComentarioChequeo;
import app.client.dominio.Corregido;
import app.client.dominio.CorregidoSurtidor;
import app.client.dominio.DestinoDelCargo;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.EstadoVisita;
import app.client.dominio.FallaTecnica;
import app.client.dominio.ItemChequeado;
import app.client.dominio.Organizacion;
import app.client.dominio.Persona;
import app.client.dominio.Pico;
import app.client.dominio.Preventivo;
import app.client.dominio.Producto;
import app.client.dominio.Repuesto;
import app.client.dominio.RepuestoLineaCorregido;
import app.client.dominio.Surtidor;
import app.client.dominio.Tarea;
import app.client.dominio.Tecnico;
import app.client.dominio.TipoChequeo;
import app.client.dominio.TipoFallaTecnica;
import app.client.dominio.TipoRepuesto;
import app.client.dominio.TipoTarea;
import app.client.dominio.ValorSiNoNa;
import app.client.dominio.Visita;
import app.client.dominio.data.ActivoData;
import app.client.dominio.data.ChequeoBombaPreventivoData;
import app.client.dominio.data.ChequeoGenericoPreventivoData;
import app.client.dominio.data.ChequeoSurtidorPicoData;
import app.client.dominio.data.ChequeoSurtidorPreventivoData;
import app.client.dominio.data.ChequeoSurtidorProductoData;
import app.client.dominio.data.ChequeoTanquePreventivoData;
import app.client.dominio.data.ComentarioData;
import app.client.dominio.data.CorregidoData;
import app.client.dominio.data.DestinoDelCargoData;
import app.client.dominio.data.EmailEmpresaData;
import app.client.dominio.data.ItemChequeoData;
import app.client.dominio.data.OrganizacionData;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PendienteDataList;
import app.client.dominio.data.PicoData;
import app.client.dominio.data.PreventivoData;
import app.client.dominio.data.ProductoData;
import app.client.dominio.data.ReporteData;
import app.client.dominio.data.TecnicoData;
import app.client.dominio.data.TipoDescargaData;
import app.client.dominio.data.UsuarioData;
import app.client.dominio.data.VisitaData;
import app.client.dominio.data.VisitaDataList;
import app.client.dominio.json.ChequeoBombaJson;
import app.client.dominio.json.ChequeoGenericoJson;
import app.client.dominio.json.ChequeoPicoJson;
import app.client.dominio.json.ChequeoSurtidorJson;
import app.client.dominio.json.ChequeoSurtidorProductoJson;
import app.client.dominio.json.ChequeoTanqueJson;
import app.client.dominio.json.ComentarioChequeoJson;
import app.client.dominio.json.CorregidoJson;
import app.client.dominio.json.FinalizarVisitaJson;
import app.client.dominio.json.ItemChequeadoJson;
import app.client.dominio.json.PendienteJson;
import app.client.dominio.json.RepuestoLineaCorregidoJson;
import app.client.dominio.json.VisitaJson;
import app.server.control.ControlActivo;
import app.server.control.ControlChequeo;
import app.server.control.ControlComentarioChequeo;
import app.server.control.ControlCorregido;
import app.server.control.ControlEmpresa;
import app.server.control.ControlFalla;
import app.server.control.ControlOperaciones;
import app.server.control.ControlOrden;
import app.server.control.ControlPendiente;
import app.server.control.ControlPersona;
import app.server.control.ControlPico;
import app.server.control.ControlPreventivo;
import app.server.control.ControlRepuesto;
import app.server.control.ControlTipoFalla;
import app.server.control.ControlTipoRepuesto;
import app.server.control.ControlTipoTarea;
import app.server.control.visita.ControlVisita;
import app.server.exception.ValorChequeoInvalidoException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class OperacionesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Gson gson;

	public OperacionesServlet () {
		gson = new GsonBuilder().create();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		String operacion = req.getParameter("operacion");
		//String operacion = "finalizarVisita";
		// String operacion = "guardarCorregido";
		// String operacion = "guardarPendiente";
		
		if(operacion != null) {

			PrintWriter out = resp.getWriter();

			if (operacion.equals("obtenerVisitasAsignadas")) {
				obtenerVisitasAsignadas(req,out);

			} else if(operacion.equals("obtenerVisita")) {
				obtenerVisita(req,out);

			} else if(operacion.equals("modificarVisita")) {
				modificarVisita(req,out);

			} else if(operacion.equals("login")) {
				login(req,out);

			} else if(operacion.equals("obtenerTecnico")) {
				obtenerTecnico(req,out);

			} else if(operacion.equals("obtenerDestinosDelCargo")) {
				obtenerDestinosDelCargo(req,out);

			} else if(operacion.equals("obtenerTipoFallasTecnicas")) {
				obtenerTipoFallasTecnicas(req,out);

			} else if(operacion.equals("obtenerTipoTareas")) {
				obtenerTipoTareas(req,out);

			} else if(operacion.equals("obtenerTipoRepuestos")) {
				obtenerTipoRepuestos(req,out);

			} else if(operacion.equals("obtenerTareas")) {
				obtenerTareas(req,out);

			} else if(operacion.equals("obtenerFallasTecnicas")) {
				obtenerFallasTecnicas(req,out);

			} else if(operacion.equals("obtenerRepuestos")) {
				obtenerRepuestos(req,out);

			} else if(operacion.equals("obtenerTiposDeDescarga")) {
				obtenerTiposDeDescarga(req,out);

			} else if(operacion.equals("guardarChequeoBomba")) {
				guardarChequeoBomba(req,out);

			} else if(operacion.equals("guardarChequeoGenerico")) {
				guardarChequeoGenerico(req,out);

			} else if(operacion.equals("guardarChequeoTanque")) {
				guardarChequeoTanque(req,out);

			} else if(operacion.equals("guardarChequeoSurtidor")) {
				guardarChequeoSurtidor(req,out);

			} else if(operacion.equals("guardarChequeoSurtidorProducto")) {
				guardarChequeoSurtidorProducto(req,out);

			} else if(operacion.equals("guardarChequeoSurtidorPico")) {
				guardarChequeoSurtidorPico(req,out);

			} else if(operacion.equals("obtenerChequeoTanque")) {
				obtenerChequeoTanque(req,out);

			} else if(operacion.equals("obtenerChequeoBomba")) {
				obtenerChequeoBomba(req,out);

			} else if(operacion.equals("obtenerChequeoGenerico")) {
				obtenerChequeoGenerico(req,out);

			} else if(operacion.equals("obtenerChequeoSurtidor")) {
				obtenerChequeoSurtidor(req,out);

			} else if(operacion.equals("obtenerChequeoSurtidorProducto")) {
				obtenerChequeoSurtidorProducto(req,out);

			} else if(operacion.equals("obtenerChequeoSurtidorPico")) {
				obtenerChequeoSurtidorPico(req,out);

			} else if(operacion.equals("obtenerPicos")) {
				obtenerPicos(req,out);

			} else if(operacion.equals("asociarQrAActivo")) {
				asociarQrAActivo(req,out);

			} else if(operacion.equals("asociarQrAPico")) {
				asociarQrAPico(req,out);

			} else if(operacion.equals("guardarCorregido")) {
				guardarCorregido(req,out);

			} else if(operacion.equals("obtenerCorregido")) {
				obtenerCorregido(req,out);

			} else if(operacion.equals("obtenerCorregidos")) {
				obtenerCorregidos(req,out);

			} else if(operacion.equals("eliminarCorregido")) {
				eliminarCorregido(req,out);

			} else if(operacion.equals("obtenerIdActivoPorQR")) {
				obtenerIdActivoPorQR(req,out);

			} else if(operacion.equals("obtenerIdPicoPorQR")) {
				obtenerIdPicoPorQR(req,out);

			} else if(operacion.equals("obtenerPendiente")) {
				obtenerPendiente(req,out);

			} else if(operacion.equals("obtenerPendientes")) {
				obtenerPendientes(req,out);

			} else if(operacion.equals("obtenerPendientesPorChequeo")) {
				obtenerPendientesPorChequeo(req,out);

			} else if(operacion.equals("obtenerCorregidosPorChequeo")) {
				obtenerCorregidosPorChequeo(req,out);

			}else if(operacion.equals("guardarPendiente")) {
				guardarPendiente(req,out);

			} else if(operacion.equals("eliminarPendiente")) {
				eliminarPendiente(req,out);
				
			} else if(operacion.equals("obtenerProductos")) {
				obtenerProductos(req,out);

			} else if(operacion.equals("reporteVisitaPreventivaEstaciones")) {
				reporteVisitaPreventivaEstaciones(req,out);

			} else if(operacion.equals("obtenerReporteVisitaPreventiva")) {
				obtenerReporteVisitaPreventiva(req,out);

			} else if(operacion.equals("obtenerCorreosEstacion")) {
				obtenerCorreosEstacion(req,out);

			} else if(operacion.equals("finalizarVisita")) {
				finalizarVisita(req,out);

			} else if(operacion.equals("obtenerOrganizaciones")) {
				obtenerOrganizaciones(req,out);

			} else if(operacion.equals("modificarComentarioChequeo")) {
				modificarComentarioChequeo(req,out);

			} else if(operacion.equals("eliminarComentarioChequeo")) {
				eliminarComentarioChequeo(req,out);

			} else if(operacion.equals("agregarComentarioChequeo")) {
				agregarComentarioChequeo(req,out);
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		String operacion = req.getParameter("operacion");

		if(operacion != null){

			PrintWriter out = resp.getWriter();

			if (operacion.equals("obtenerVisitasAsignadas")) {
				obtenerVisitasAsignadas(req,out);

			} else if(operacion.equals("obtenerVisita")) {
				obtenerVisita(req,out);

			} else if(operacion.equals("modificarVisita")) {
				Response r = getResponse("El servicio modificarVisita se debe consumir por el metodo POST", "101", null);
				enviarRespuesta(out, r);

			} else if(operacion.equals("login")) {
				login(req,out);

			} else if(operacion.equals("obtenerTecnico")) {
				obtenerTecnico(req,out);

			} else if(operacion.equals("obtenerDestinosDelCargo")) {
				obtenerDestinosDelCargo(req,out);

			} else if(operacion.equals("obtenerTipoFallasTecnicas")) {
				obtenerTipoFallasTecnicas(req,out);

			} else if(operacion.equals("obtenerTipoTareas")) {
				obtenerTipoTareas(req,out);

			} else if(operacion.equals("obtenerTipoRepuestos")) {
				obtenerTipoRepuestos(req,out);

			} else if(operacion.equals("obtenerTareas")) {
				obtenerTareas(req,out);

			} else if(operacion.equals("obtenerFallasTecnicas")) {
				obtenerFallasTecnicas(req,out);

			} else if(operacion.equals("obtenerRepuestos")) {
				obtenerRepuestos(req,out);

			} else if(operacion.equals("obtenerTiposDeDescarga")) {
				obtenerTiposDeDescarga(req,out);

			} else if(operacion.equals("guardarChequeoBomba")) {
				guardarChequeoBomba(req,out);

			} else if(operacion.equals("guardarChequeoTanque")) {
				guardarChequeoTanque(req,out);

			} else if(operacion.equals("guardarChequeoSurtidor")) {
				guardarChequeoSurtidor(req,out);

			} else if(operacion.equals("guardarChequeoSurtidorProducto")) {
				guardarChequeoSurtidorProducto(req,out);

			} else if(operacion.equals("guardarChequeoSurtidorPico")) {
				Response r = getResponse("El servicio guardarChequeoSurtidorPico se debe consumir por el metodo POST", "101", null);
				enviarRespuesta(out, r);

			} else if(operacion.equals("obtenerChequeoTanque")) {
				obtenerChequeoTanque(req,out);

			} else if(operacion.equals("obtenerChequeoGenerico")) {
				obtenerChequeoGenerico(req,out);

			} else if(operacion.equals("obtenerChequeoBomba")) {
				obtenerChequeoBomba(req,out);

			} else if(operacion.equals("obtenerChequeoSurtidor")) {
				obtenerChequeoSurtidor(req,out);

			} else if(operacion.equals("obtenerChequeoSurtidorProducto")) {
				obtenerChequeoSurtidorProducto(req,out);

			} else if(operacion.equals("obtenerChequeoSurtidorPico")) {
				obtenerChequeoSurtidorPico(req,out);

			} else if(operacion.equals("obtenerPicos")) {
				obtenerPicos(req,out);

			} else if(operacion.equals("asociarQrAActivo")) {
				asociarQrAActivo(req,out);

			} else if(operacion.equals("asociarQrAPico")) {
				asociarQrAPico(req,out);

			} else if(operacion.equals("guardarCorregido")) {
				Response r = getResponse("El servicio guardarCorregido se debe consumir por el metodo POST", "101", null);
				enviarRespuesta(out, r);

			} else if(operacion.equals("obtenerCorregido")) {
				obtenerCorregido(req,out);

			} else if(operacion.equals("obtenerCorregidos")) {
				obtenerCorregidos(req,out);

			} else if(operacion.equals("eliminarCorregido")) {
				eliminarCorregido(req,out);

			} else if(operacion.equals("obtenerIdActivoPorQR")) {
				obtenerIdActivoPorQR(req,out);

			} else if(operacion.equals("obtenerIdPicoPorQR")) {
				obtenerIdPicoPorQR(req,out);

			} else if(operacion.equals("obtenerPendiente")) {
				obtenerPendiente(req,out);

			} else if(operacion.equals("obtenerPendientes")) {
				obtenerPendientes(req,out);

			} else if(operacion.equals("guardarPendiente")) {
				Response r = getResponse("El servicio guardarPendiente se debe consumir por el metodo POST", "101", null);
				enviarRespuesta(out, r);

			} else if(operacion.equals("eliminarPendiente")) {
				eliminarPendiente(req,out);

			} else if(operacion.equals("descartarPendiente")) {
				descartarPendiente(req,out);

			} else if(operacion.equals("obtenerProductos")) {
				obtenerProductos(req,out);

			} else if(operacion.equals("reporteVisitaPreventivaEstaciones")) {
				reporteVisitaPreventivaEstaciones(req,out);

			} else if(operacion.equals("obtenerReporteVisitaPreventiva")) {
				obtenerReporteVisitaPreventiva(req,out);

			} else if(operacion.equals("obtenerCorreosEstacion")) {
				obtenerCorreosEstacion(req,out);

			} else if(operacion.equals("finalizarVisita")) {
				finalizarVisita(req,out);

			} else if(operacion.equals("obtenerOrganizaciones")) {
				obtenerOrganizaciones(req,out);

			} else if(operacion.equals("obtenerItemsChequeo")) {
				obtenerItemsChequeo(req,out);

			} else if(operacion.equals("obtenerPendientesPorChequeo")) {
				obtenerPendientesPorChequeo(req,out);

			} else if(operacion.equals("obtenerCorregidosPorChequeo")) {
				obtenerCorregidosPorChequeo(req,out);

			} else if(operacion.equals("obtenerComentariosChequeo")) {
				obtenerComentariosChequeo(req,out);
			}
		}
	}


	private void login(HttpServletRequest req, PrintWriter out){

		Response r = new Response();
		try{
			String user = req.getParameter("user");
			String pass = req.getParameter("pass");

			UsuarioData usuario = ControlOperaciones.getInstancia().login(user, pass);


			if(usuario != null){

				if(usuario.getRol() == 3){
					r = getResponse("", "0", usuario);
				}else{
					r = getResponse("El usuario no tiene permisos para acceder a la aplicación", "101", null);
				}	
			}else{
				r = getResponse("Usuario y/o password incorrectos", "102", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerVisitasAsignadas(HttpServletRequest req, PrintWriter out) {
		String id = req.getParameter("id");

		Response r = new Response();

		try {
			int idTecnico = Integer.parseInt(id);	
			Tecnico tecnico = ControlPersona.getInstancia().obtenerTecnico(idTecnico);

			if(tecnico == null){
				r = getResponse("No existe tecnico con el identificador: " + idTecnico, "103", null);
				return;
			}

			ArrayList<VisitaDataList> visitas = new ArrayList<VisitaDataList>();

			visitas = ControlOperaciones.getInstancia().obtenerVisitasAsignadas(idTecnico);

			r = getResponse("", "0", visitas);

		} catch (NumberFormatException e) {
			r = getResponse("El identificador del tecnico debe ser numérico", "104", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerVisita(HttpServletRequest req, PrintWriter out) {
		String id = req.getParameter("id");

		Response r = new Response();

		try {
			int idVisita = Integer.parseInt(id);	

			Visita visita = ControlVisita.getInstancia().obtenerVisita(idVisita);
			if(visita == null){
				r = getResponse("No existe visita con identificador " + idVisita, "105", null);
				return;
			}

			VisitaData visitaData = new VisitaData();
			visitaData = ControlOperaciones.getInstancia().obtenerVisitaWS(idVisita);
			
			/*for (ActivoData activoData : visitaData.getListaActivos()) {// parche hasta que la app no cambie a los tipos genericos = 6
				if (activoData.getTipo().equals("6")){
					activoData.setTipo("5");
				}
			}*/
			r = getResponse("", "0", visitaData);

		} catch (NumberFormatException e) {
			r = getResponse("El identificador de la visita debe ser numérico", "106", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void modificarVisita(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();

		try {
			GsonBuilder gSon = new GsonBuilder();
			gSon.setDateFormat(("yyyy/MM/dd HH:mm:ss"));
			VisitaJson visitaJson = gSon.create().fromJson(req.getReader(), VisitaJson.class);
			String idVisitaStr = visitaJson.getIdVisita();

			String comentarioFirma = visitaJson.getComentarioFirma();
			String firmaStr = visitaJson.getFirma();

			String estado = visitaJson.getEstado();
			EstadoVisita estadoVisita = null;

			int idVisita = (idVisitaStr != null && !idVisitaStr.isEmpty()) ? Integer.valueOf(idVisitaStr) : 0;

			Visita visita = ControlVisita.getInstancia().obtenerVisita(idVisita);
			if(visita == null){
				r = getResponse("No existe visita con identificador " + idVisita, "107", null);
				return;
			}

			if(estado != null){
				estadoVisita = EstadoVisita.getEstadoVisita(estado);
				if( estadoVisita == null){
					r = getResponse("El estado " + estado + " no es valido. [" + EstadoVisita.INICIADA + ", " + EstadoVisita.PENDIENTE + ", " + EstadoVisita.REALIZADA + "]", "128", null);
					return;	
				}
			}

			byte[] firma = (firmaStr != null && firmaStr.length() > 0) ? new Base64().decode(firmaStr.getBytes()) : null;

			ControlOperaciones.getInstancia().modificarVisita(idVisita, visitaJson.getFechaInicio(), visitaJson.getFechaFin(), firma, comentarioFirma, estadoVisita);
			r = getResponse("", "0", true);

		} catch (NumberFormatException e) {
			r = getResponse("El identificador de la visita debe ser numérico", "108", null);

		} catch (ParseException e) {
			r = getResponse("El formato de las fechas de la visita debe ser yyyy/MM/D", "109", null);

		} catch (IOException e) {
			r = getResponse("Ocurrió un error guardando la firma", "110", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ocurrió un error guardando la visita", "111", null);

		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerTecnico(HttpServletRequest req, PrintWriter out){
		Response r = new Response();
		String id = req.getParameter("id");
		try {

			int idTecnico = Integer.parseInt(id);
			TecnicoData tecnico = ControlOperaciones.getInstancia().obtenerTecnico(idTecnico);

			if(tecnico != null){
				r = getResponse("", "0", tecnico);
			}else{
				r = getResponse("No existe el tecnico con identificador " + id, "112", null);
			}

		} catch (NumberFormatException e) {
			r = getResponse("El identificador del tecnico debe ser numérico", "113", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerDestinosDelCargo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			List<DestinoDelCargoData> destinos = ControlOperaciones.getInstancia().obtenerDestinosDelCargo();

			if(destinos != null && !destinos.isEmpty()){
				r = getResponse("", "0", destinos);
			}else{
				r = getResponse("No hay destinos del cargo", "114", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerTipoFallasTecnicas(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			List<TipoFallaTecnica> fallas = ControlOperaciones.getInstancia().obtenerTipoFallasTecnicas();

			if(fallas != null && !fallas.isEmpty()){
				r = getResponse("", "0", fallas);
			}else{
				r = getResponse("No hay tipo fallas técnicas", "115", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerTipoTareas(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			List<TipoTarea> fallas = ControlOperaciones.getInstancia().obtenerTipoTareas();

			if(fallas != null && !fallas.isEmpty()){
				r = getResponse("", "0", fallas);
			}else{
				r = getResponse("No hay tipo tareas", "116", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerTipoRepuestos(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			List<TipoRepuesto> fallas = ControlOperaciones.getInstancia().obtenerTipoRepuestos();

			if(fallas != null && !fallas.isEmpty()){
				r = getResponse("", "0", fallas);
			}else{
				r = getResponse("No hay tipo repuestos", "117", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerFallasTecnicas(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		List<FallaTecnica> fallas = new ArrayList<FallaTecnica>();
		try{
			String tipoStr = req.getParameter("tipo");
			if(tipoStr != null && !tipoStr.isEmpty())
			{
				try {
					int tipoInt = Integer.parseInt(tipoStr);
					TipoFallaTecnica tipo = ControlTipoFalla.getInstancia().obtenerTipoFallaT(tipoInt);
					if(tipo != null){
						fallas = ControlOperaciones.getInstancia().obtenerFallasTecnicas(tipo);
					}else{
						r = getResponse("No existe tipo falla tecnica con identificador " + tipoInt, "118", null);
						return;
					}
				} catch (NumberFormatException e) {
					r = getResponse("El identificador del tipo de falla debe ser numérico", "119", null);
					return;
				}
			}else{
				fallas = ControlOperaciones.getInstancia().obtenerFallasTecnicas();
				for (FallaTecnica fallaTecnica : fallas) {//parche, en realidad se deberia cambiar la app para que utilice el subTipo
					fallaTecnica.setTipo(fallaTecnica.getSubTipo());
				}
			}

			if(!fallas.isEmpty()){
				r = getResponse("", "0", fallas);
			}else{
				r = getResponse("No hay fallas técnicas", "120", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerTareas(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		List<Tarea> fallas = new ArrayList<Tarea>();
		try{
			String tipoStr = req.getParameter("tipo");
			if(tipoStr != null && !tipoStr.isEmpty())
			{
				try {
					int tipoInt = Integer.parseInt(tipoStr);
					TipoTarea tipo = ControlTipoTarea.getInstancia().obtenerTipoTarea(tipoInt);
					if(tipo != null){
						fallas = ControlOperaciones.getInstancia().obtenerTareas(tipo);
					}else{
						r = getResponse("No existe tipo tarea con identificador " + tipoInt, "121", null);
						return;
					}
				} catch (NumberFormatException e) {
					r = getResponse("El identificador del tipo de tarea debe ser numérico", "122", null);
					return;
				}
			}else{
				fallas = ControlOperaciones.getInstancia().obtenerTareas();
			}

			if(!fallas.isEmpty()){
				r = getResponse("", "0", fallas);
			}else{
				r = getResponse("No hay tareas", "123", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerTiposDeDescarga (HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			List<TipoDescargaData> tiposDescarga = ControlOperaciones.getInstancia().obtenerTiposDescarga();
			r = getResponse("", "0", tiposDescarga);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerRepuestos(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		List<Repuesto> repuestos = new ArrayList<Repuesto>();
		try{
			String tipoStr = req.getParameter("tipo");
			if(tipoStr != null && !tipoStr.isEmpty())
			{
				try {
					int tipoInt = Integer.parseInt(tipoStr);
					TipoRepuesto tipo = ControlTipoRepuesto.getInstancia().obtenerTipoRepuesto(tipoInt);
					if(tipo != null){
						repuestos = ControlOperaciones.getInstancia().obtenerRepuestos(tipo);
					}else{
						r = getResponse("No existe tipo repuesto con identificador " + tipoInt, "124", null);
						return;
					}
				} catch (NumberFormatException e) {
					r = getResponse("El identificador del tipo de repuesto debe ser numérico", "125", null);
					return;
				}
			}else{
				repuestos = ControlOperaciones.getInstancia().obtenerRepuestos();
			}

			if(!repuestos.isEmpty()){
				r = getResponse("", "0", repuestos);
			}else{
				r = getResponse("No hay repuestos", "126", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void guardarChequeoGenerico(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		PreventivoData preventivo;
		try{
			ChequeoGenericoJson chequeoJson = new Gson().fromJson(req.getReader(), ChequeoGenericoJson.class);
			preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(chequeoJson.getIdPreventivo());

			if(preventivo == null){
				r = getResponse("No existe preventivo con identificador " + chequeoJson.getIdPreventivo(), "127", null);
				return;
			}
			if(preventivo.getIdTipoActivo() != 6){
				r = getResponse("El preventivo " + chequeoJson.getIdPreventivo() + ", pertenece a un tipo de activo distinto de genérico.", "127", null);
				return;
			}

			ChequeoGenerico cb = new ChequeoGenerico();
			Set<ItemChequeado> itemsChequeados = new HashSet<ItemChequeado>();
			for(ItemChequeadoJson icj : chequeoJson.getItemsChequeados()) {
				setItemChequeo(itemsChequeados, icj);
			}
			cb.setUltimaModificacion(new Date());
			cb.setItemsChequeados(itemsChequeados);

			if(ControlOperaciones.getInstancia().actualizarPreventivo(chequeoJson.getIdPreventivo(), cb)) {
				r = getResponse("", "0", true);
			} else {
				r = getResponse("Error al guardar chequeo genérico", "128", null);
			}

		} catch (ValorChequeoInvalidoException e) {
			r = getResponse("Valor de chequeo invalido, debe ser B, C, P o N/A", "100", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally{
			enviarRespuesta(out, r);
		}
	}

	private void guardarChequeoBomba(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		PreventivoData preventivo;
		try{
			ChequeoBombaJson chequeoBombaJson = new Gson().fromJson(req.getReader(), ChequeoBombaJson.class);
			preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(chequeoBombaJson.getIdPreventivo());

			if(preventivo == null){
				r = getResponse("No existe preventivo con identificador " + chequeoBombaJson.getIdPreventivo(), "127", null);
				return;
			}
			if(preventivo.getIdTipoActivo() != 4){
				r = getResponse("El preventivo " + chequeoBombaJson.getIdPreventivo() + ", pertenece a un tipo de activo distinto de bomba.", "127", null);
				return;
			}

			ChequeoBomba cb = new ChequeoBomba();
			Set<ItemChequeado> itemsChequeados = new HashSet<ItemChequeado>();
			for(ItemChequeadoJson icj : chequeoBombaJson.getItemsChequeados()) {
				setItemChequeo(itemsChequeados, icj);
			}
			cb.setUltimaModificacion(new Date());
			cb.setItemsChequeados(itemsChequeados);

			if(ControlOperaciones.getInstancia().actualizarPreventivo(chequeoBombaJson.getIdPreventivo(), cb)) {
				r = getResponse("", "0", true);
			} else {
				r = getResponse("Error al guardar chequeo bomba", "128", null);
			}

		} catch (ValorChequeoInvalidoException e) {
			r = getResponse("Valor de chequeo invalido, debe ser B, C, P o N/A", "100", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally{
			enviarRespuesta(out, r);
		}
	}

	private void setItemChequeo(Set<ItemChequeado> itemsChequeados, ItemChequeadoJson icj) {
		ItemChequeado itemChequeado = new ItemChequeado();
		itemChequeado.setItemChequeo(ControlChequeo.getInstancia().getItemChequeo(icj.getNombreItemChequeo()));
		itemChequeado.setValor(icj.getValor());
		itemChequeado.setPendiente(icj.isPendiente());
		itemsChequeados.add(itemChequeado);
	}

	private void guardarChequeoTanque(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		PreventivoData preventivo;
		try{
			ChequeoTanqueJson chequeoTanqueJson = new Gson().fromJson(req.getReader(), ChequeoTanqueJson.class);
			preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(chequeoTanqueJson.getIdPreventivo());

			if(preventivo == null){
				r = getResponse("No existe preventivo con identificador " + chequeoTanqueJson.getIdPreventivo(), "130", null);
				return;
			}
			if(preventivo.getIdTipoActivo() != 2){
				r = getResponse("El preventivo " + chequeoTanqueJson.getIdPreventivo() + ", pertenece a un tipo de activo distinto de tanque.", "127", null);
				return;
			}

			ChequeoTanque ct = new ChequeoTanque();
			Set<ItemChequeado> itemsChequeados = new HashSet<ItemChequeado>();
			for(ItemChequeadoJson icj : chequeoTanqueJson.getItemsChequeados()) {
				setItemChequeo(itemsChequeados, icj);
			}
			ct.setUltimaModificacion(new Date());
			ct.setItemsChequeados(itemsChequeados);
			ct.setTipoDeDescarga(chequeoTanqueJson.getTipoDeDescarga());
			ct.setMedidaDelAgua(chequeoTanqueJson.getMedidaDelAgua());

			if(ControlOperaciones.getInstancia().actualizarPreventivo(chequeoTanqueJson.getIdPreventivo(), ct)) {
				r = getResponse("", "0", true);
			} else {
				r = getResponse("Error al guardar chequeo tanque", "132", null);
			}

		} catch (ValorChequeoInvalidoException e) {
			r = getResponse("Valor de chequeo " + e.getMessage() + " invalido, debe ser B, C, P o N/A", "100", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void guardarChequeoSurtidor(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		PreventivoData preventivo;
		try{
			ChequeoSurtidorJson chequeoSurtidorJson = new Gson().fromJson(req.getReader(), ChequeoSurtidorJson.class);
			preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(chequeoSurtidorJson.getIdPreventivo());

			if(preventivo == null){
				r = getResponse("No existe preventivo con identificador " + chequeoSurtidorJson.getIdPreventivo(), "127", null);
				return;
			}
			if(preventivo.getIdTipoActivo() != 1){
				r = getResponse("El preventivo " + chequeoSurtidorJson.getIdPreventivo() + ", pertenece a un tipo de activo distinto de surtidor.", "127", null);
				return;
			}

			ChequeoSurtidor cs = new ChequeoSurtidor();
			Set<ItemChequeado> itemsChequeados = new HashSet<ItemChequeado>();
			for(ItemChequeadoJson icj : chequeoSurtidorJson.getItemsChequeados()) {
				setItemChequeo(itemsChequeados, icj);
			}
			cs.setUltimaModificacion(new Date());
			cs.setItemsChequeados(itemsChequeados);

			if(ControlOperaciones.getInstancia().actualizarPreventivo(chequeoSurtidorJson.getIdPreventivo(), cs)) {
				r = getResponse("", "0", true);
			} else {
				r = getResponse("Error al guardar chequeo surtidor", "184", null);
			}

		} catch (ValorChequeoInvalidoException e) {
			r = getResponse("Valor de chequeo invalido, debe ser B, C, P o N/A", "100", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally{
			enviarRespuesta(out, r);
		}

	}

	private void guardarChequeoSurtidorProducto(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		PreventivoData preventivo;
		try{
			ChequeoSurtidorProductoJson chequeoSurtidorProductoJson = new Gson().fromJson(req.getReader(), ChequeoSurtidorProductoJson.class);
			preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(chequeoSurtidorProductoJson.getIdPreventivo());

			if(preventivo == null){
				r = getResponse("No existe preventivo con identificador " + chequeoSurtidorProductoJson.getIdPreventivo(), "127", null);
				return;
			}
			if(preventivo.getIdTipoActivo() != 1){
				r = getResponse("El preventivo " + chequeoSurtidorProductoJson.getIdPreventivo() + ", pertenece a un tipo de activo distinto de surtidor.", "127", null);
				return;
			}

			Activo activo = ControlActivo.getInstancia().buscarActivo(preventivo.getIdActivo());
			if (activo.getClass().equals(Surtidor.class)) {
				Surtidor surtidor = (Surtidor)activo;

				boolean productoValido = false;
				for (Pico pico : surtidor.getPicos()){
					if (pico.getProducto().getId() == chequeoSurtidorProductoJson.getIdProducto()){
						productoValido = true;
					}
				}

				if (!productoValido){
					r = getResponse("El surtidor con id " + surtidor.getId() + ", no tiene un pico con el producto con id " + chequeoSurtidorProductoJson.getIdProducto(), "184", null);
					return ;
				}

			} else {
				r = getResponse("El id preventivo no pertenece a un surtidor", "184", null);
				return ;
			}

			ChequeoProducto chequeoProducto = new ChequeoProducto();
			Set<ItemChequeado> itemsChequeados = new HashSet<ItemChequeado>();
			for(ItemChequeadoJson icj : chequeoSurtidorProductoJson.getItemsChequeados()) {
				setItemChequeo(itemsChequeados, icj);
			}
			chequeoProducto.setUltimaModificacion(new Date());
			chequeoProducto.setItemsChequeados(itemsChequeados);
			chequeoProducto.setPreventivo(new Preventivo(chequeoSurtidorProductoJson.getIdPreventivo()));
			chequeoProducto.setProducto(new Producto(chequeoSurtidorProductoJson.getIdProducto()));

			if(ControlOperaciones.getInstancia().actualizarPreventivo(chequeoSurtidorProductoJson.getIdPreventivo(), chequeoProducto)) {
				r = getResponse("", "0", true);
			} else {
				r = getResponse("Error al guardar el producto del chequeo surtidor del preventivo " + chequeoSurtidorProductoJson.getIdPreventivo(), "185", null);
			}

		} catch (ValorChequeoInvalidoException vcie){
			r = getResponse("Valor de chequeo invalido, debe ser B, C, P o N/A", "100", null);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally{
			enviarRespuesta(out, r);
		}

	}

	private void guardarChequeoSurtidorPico(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try {
			ChequeoPicoJson chequeoPicoJson = new Gson().fromJson(req.getReader(), ChequeoPicoJson.class);
			PreventivoData preventivo = null;
			try {
				preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(chequeoPicoJson.getIdPreventivo());

				if(preventivo == null){
					r = getResponse("No existe preventivo con identificador " + chequeoPicoJson.getIdPreventivo(), "127", null);
					return;
				}

				if(preventivo.getIdTipoActivo() != 1){
					r = getResponse("El preventivo " + chequeoPicoJson.getIdPreventivo() + ", pertenece a un tipo de activo distinto de surtidor.", "127", null);
					return;
				}

				Activo activo = ControlActivo.getInstancia().buscarActivo(preventivo.getIdActivo());
				if (activo.getClass().equals(Surtidor.class)) {
					Surtidor surtidor = (Surtidor)activo;

					boolean idPicoValido = false;
					for (Pico pico : surtidor.getPicos()){
						if (pico.getId() == chequeoPicoJson.getIdPico()){
							idPicoValido = true;
						}
					}

					if (!idPicoValido){
						r = getResponse("El surtidor con id " + surtidor.getId() + ", no tiene un pico con el id " + chequeoPicoJson.getIdPico(), "184", null);
						return ;
					}

				} else {
					r = getResponse("El id preventivo no pertenece a un surtidor", "184", null);
					return ;
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del preventivo debe ser numérico", "128", null);
				return;
			}

			if (chequeoPicoJson.getPrecinto().getRemplazado().equals(ValorSiNoNa.SI)){
				if (chequeoPicoJson.getCalibre4() == null || chequeoPicoJson.getCalibre5() == null || chequeoPicoJson.getCalibre6() == null ){
					r = getResponse("Si el precinto es reemplazado, debe ingresar los campos 4, 5, 6. ", "131", null);
					return;
				}
			}

			ChequeoPico chequeoPico = new ChequeoPico();
			chequeoPico.setFromChequeoPicoJson(chequeoPicoJson);

			Set<ItemChequeado> itemsChequeados = new HashSet<ItemChequeado>();
			for(ItemChequeadoJson icj : chequeoPicoJson.getItemsChequeados()) {
				setItemChequeo(itemsChequeados, icj);
			}
			chequeoPico.setUltimaModificacion(new Date());
			chequeoPico.setItemsChequeados(itemsChequeados);			

			if(ControlOperaciones.getInstancia().actualizarPreventivo(chequeoPicoJson.getIdPreventivo(), chequeoPico)) {
				r = getResponse("", "0", true);
			} else {
				r = getResponse("Error al guardar el pico del chequeo surtidor del preventivo " + chequeoPicoJson.getIdPreventivo(), "185", null);
			}

		} catch (ValorChequeoInvalidoException e) {
			r = getResponse("Valor de chequeo invalido, debe ser B, C, P o N/A", "100", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);

		} finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerChequeoGenerico(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		String idVisitaStr = req.getParameter("idVisita");
		String idActivoStr = req.getParameter("idActivo");
		String codigoQRStr = req.getParameter("codigoQR");

		int idVisita;
		int idActivo;

		Visita visita;
		Activo activo;
		try{

			try {
				idVisita = Integer.valueOf(idVisitaStr);
				visita = ControlVisita.getInstancia().obtenerVisita(idVisita);

				if(visita == null){
					r = getResponse("No existe visita con identificador " + idVisita, "133", null);
					return;
				}
			} catch (NumberFormatException e) {
				r = getResponse("El identificador de la visita debe ser numérico", "134", null);
				return;
			}

			boolean vieneIdActivo = false;

			if(idActivoStr != null && !idActivoStr.isEmpty()) {
				vieneIdActivo = true;

			} else if(codigoQRStr != null && !codigoQRStr.isEmpty()) {
				vieneIdActivo = false;
			} else {
				r = getResponse("Deben enviar idActivo o codigoQR.", "183", null);
				return;
			}

			if(vieneIdActivo) {

				try {
					idActivo = Integer.valueOf(idActivoStr);
					activo = ControlActivo.getInstancia().buscarActivo(idActivo);

					if(activo == null){
						r = getResponse("No existe activo con identificador " + idActivo, "135", null);
						return;
					}

					if(activo.getTipo() != 6){
						r = getResponse("El id activo " + activo.getId() + ", pertenece a un tipo de activo distinto de genérico.", "127", null);
						return;
					}

				} catch (NumberFormatException e) {
					r = getResponse("El identificador del activo debe ser numérico", "136", null);
					return;
				}

			} else {

				activo = ControlActivo.getInstancia().buscarActivo(codigoQRStr);
				if(activo == null){
					r = getResponse("No existe activo con codigo qr " + codigoQRStr, "135", null);
					return;
				}

				if(activo.getTipo() != 4){
					r = getResponse("El id activo " + activo.getId() + ", pertenece a un tipo de activo distinto de genérico.", "127", null);
					return;
				}
			}
			ChequeoGenericoPreventivoData cd = (ChequeoGenericoPreventivoData) ControlOperaciones.getInstancia().
					obtenerChequeoPreventivo(visita, activo, new ChequeoGenericoPreventivoData(), new ChequeoGenerico());
			if(cd != null) {
				r = getResponse("", "0", cd);
			} else {
				r = getResponse("No fue posible obtener el chequeo para la visita con id " + idVisita + " y codigo QR " + codigoQRStr, "137", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}

	private void obtenerChequeoTanque(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		String idVisitaStr = req.getParameter("idVisita");
		String idActivoStr = req.getParameter("idActivo");
		String codigoQRStr = req.getParameter("codigoQR");

		int idVisita;
		int idActivo;

		Visita visita;
		Activo activo;
		try{

			try {
				idVisita = Integer.valueOf(idVisitaStr);
				visita = ControlVisita.getInstancia().obtenerVisita(idVisita);

				if(visita == null){
					r = getResponse("No existe visita con identificador " + idVisita, "133", null);
					return;
				}
			} catch (NumberFormatException e) {
				r = getResponse("El identificador de la visita debe ser numérico", "134", null);
				return;
			}

			boolean vieneIdActivo = false;

			if(idActivoStr != null && !idActivoStr.isEmpty()) {
				vieneIdActivo = true;

			} else if(codigoQRStr != null && !codigoQRStr.isEmpty()) {
				vieneIdActivo = false;
			} else {
				r = getResponse("Deben enviar idActivo o codigoQR.", "183", null);
				return;
			}

			if(vieneIdActivo) {
				try {
					idActivo = Integer.valueOf(idActivoStr);
					activo = ControlActivo.getInstancia().buscarActivo(idActivo);

					if(activo == null){
						r = getResponse("No existe activo con identificador " + idActivo, "135", null);
						return;
					}

					if(activo.getTipo() != 2){
						r = getResponse("El id activo " + idActivo + ", pertenece a un tipo de activo distinto de tanque.", "127", null);
						return;
					}

				} catch (NumberFormatException e) {
					r = getResponse("El identificador del activo debe ser numérico", "136", null);
					return;
				}

			} else {
				activo = ControlActivo.getInstancia().buscarActivo(codigoQRStr);
				if(activo == null){
					r = getResponse("No existe activo con codigo qr " + codigoQRStr, "135", null);
					return;
				}

				if(activo.getTipo() != 2){
					r = getResponse("El id activo " + activo.getId() + ", pertenece a un tipo de activo distinto de tanque.", "127", null);
					return;
				}

			}
			ChequeoTanquePreventivoData cd = (ChequeoTanquePreventivoData)ControlOperaciones.getInstancia().
					obtenerChequeoPreventivo(visita, activo, new ChequeoTanquePreventivoData(), new ChequeoTanque());
			if(cd != null) {
				r = getResponse("", "0", cd);
			} else {
				r = getResponse("No fue posible obtener el chequeo para la visita con id " + idVisita + " y codigo QR " + codigoQRStr, "137", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerChequeoBomba(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		String idVisitaStr = req.getParameter("idVisita");
		String idActivoStr = req.getParameter("idActivo");
		String codigoQRStr = req.getParameter("codigoQR");

		int idVisita;
		int idActivo;

		Visita visita;
		Activo activo;
		try{

			try {
				idVisita = Integer.valueOf(idVisitaStr);
				visita = ControlVisita.getInstancia().obtenerVisita(idVisita);

				if(visita == null){
					r = getResponse("No existe visita con identificador " + idVisita, "133", null);
					return;
				}
			} catch (NumberFormatException e) {
				r = getResponse("El identificador de la visita debe ser numérico", "134", null);
				return;
			}

			boolean vieneIdActivo = false;

			if(idActivoStr != null && !idActivoStr.isEmpty()) {
				vieneIdActivo = true;

			} else if(codigoQRStr != null && !codigoQRStr.isEmpty()) {
				vieneIdActivo = false;
			} else {
				r = getResponse("Deben enviar idActivo o codigoQR.", "183", null);
				return;
			}

			if(vieneIdActivo) {

				try {
					idActivo = Integer.valueOf(idActivoStr);
					activo = ControlActivo.getInstancia().buscarActivo(idActivo);

					if(activo == null){
						r = getResponse("No existe activo con identificador " + idActivo, "135", null);
						return;
					}

					if(activo.getTipo() != 4){
						r = getResponse("El id activo " + activo.getId() + ", pertenece a un tipo de activo distinto de bomba.", "127", null);
						return;
					}

				} catch (NumberFormatException e) {
					r = getResponse("El identificador del activo debe ser numérico", "136", null);
					return;
				}

			} else {

				activo = ControlActivo.getInstancia().buscarActivo(codigoQRStr);
				if(activo == null){
					r = getResponse("No existe activo con codigo qr " + codigoQRStr, "135", null);
					return;
				}

				if(activo.getTipo() != 4){
					r = getResponse("El id activo " + activo.getId() + ", pertenece a un tipo de activo distinto de bomba.", "127", null);
					return;
				}
			}
			ChequeoBombaPreventivoData cd = (ChequeoBombaPreventivoData) ControlOperaciones.getInstancia().
					obtenerChequeoPreventivo(visita, activo, new ChequeoBombaPreventivoData(), new ChequeoBomba());
			if(cd != null) {
				r = getResponse("", "0", cd);
			} else {
				r = getResponse("No fue posible obtener el chequeo para la visita con id " + idVisita + " y codigo QR " + codigoQRStr, "137", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}

	private void obtenerChequeoSurtidor(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		String idVisitaStr = req.getParameter("idVisita");
		String idActivoStr = req.getParameter("idActivo");
		String codigoQRStr = req.getParameter("codigoQR");

		int idVisita;
		int idActivo;

		Visita visita;
		Activo activo;
		try{

			try {
				idVisita = Integer.valueOf(idVisitaStr);
				visita = ControlVisita.getInstancia().obtenerVisita(idVisita);

				if(visita == null){
					r = getResponse("No existe visita con identificador " + idVisita, "133", null);
					return;
				}
			} catch (NumberFormatException e) {
				r = getResponse("El identificador de la visita debe ser numérico", "134", null);
				return;
			}

			boolean vieneIdActivo = false;

			if(idActivoStr != null && !idActivoStr.isEmpty()) {
				vieneIdActivo = true;

			} else if(codigoQRStr != null && !codigoQRStr.isEmpty()) {
				vieneIdActivo = false;
			} else {
				r = getResponse("Deben enviar idActivo o codigoQR.", "183", null);
				return;
			}

			if(vieneIdActivo) {

				try {
					idActivo = Integer.valueOf(idActivoStr);
					activo = ControlActivo.getInstancia().buscarActivo(idActivo);

					if(activo == null){
						r = getResponse("No existe activo con identificador " + idActivo, "135", null);
						return;
					}

					if(activo.getTipo() != 1){
						r = getResponse("El id activo " + activo.getId() + ", pertenece a un tipo de activo distinto de surtidor.", "127", null);
						return;
					}

				} catch (NumberFormatException e) {
					r = getResponse("El identificador del activo debe ser numérico", "136", null);
					return;
				}

				ChequeoSurtidorPreventivoData cspd = (ChequeoSurtidorPreventivoData) ControlOperaciones.getInstancia().
						obtenerChequeoPreventivo(visita, activo, new ChequeoSurtidorPreventivoData(), new ChequeoSurtidor());
				if(cspd != null) {
					r = getResponse("", "0", cspd);
				} else {
					r = getResponse("No fue posible obtener el chequeo para la visita con id " + idVisita + " y activo con id " + idActivo, "137", null);
				}

			} else {

				activo = ControlActivo.getInstancia().buscarActivo(codigoQRStr);
				if(activo == null){
					r = getResponse("No existe activo con codigo qr " + codigoQRStr, "135", null);
					return;
				}

				if(activo.getTipo() != 1){
					r = getResponse("El id activo " + activo.getId() + ", pertenece a un tipo de activo distinto de surtidor.", "127", null);
					return;
				}

				ChequeoSurtidorPreventivoData cspd = (ChequeoSurtidorPreventivoData) ControlOperaciones.getInstancia().
						obtenerChequeoPreventivo(visita, activo, new ChequeoSurtidorPreventivoData(), new ChequeoSurtidor());
				if(cspd != null) {
					r = getResponse("", "0", cspd);
				} else {
					r = getResponse("No fue posible obtener el chequeo para la visita con id " + idVisita + " y codigo QR " + codigoQRStr, "137", null);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}

	private void obtenerChequeoSurtidorProducto(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		String idProductoStr = req.getParameter("idProducto");
		String idPreventivoStr = req.getParameter("idPreventivo");
		try{
			int idProducto;
			int idPreventivo;

			try {
				idProducto = Integer.valueOf(idProductoStr);

				if (idProducto <= 1){
					r = getResponse("El id producto debe ser mayor que 1", "131", null);
					return;
				}

			} catch (NumberFormatException e) {
				r = getResponse("El id producto debe ser numérico", "131", null);
				return;
			}

			PreventivoData preventivo;
			try {
				idPreventivo = Integer.valueOf(idPreventivoStr);
				preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(idPreventivo);

				if(preventivo == null){
					r = getResponse("No existe preventivo con identificador " + idPreventivo, "153", null);
					return;
				}

				Activo activo = ControlActivo.getInstancia().buscarActivo(preventivo.getIdActivo());
				if (activo.getClass().equals(Surtidor.class)) {
					Surtidor surtidor = (Surtidor)activo;

					boolean productoValido = false;
					for (Pico pico : surtidor.getPicos()){
						if (pico.getProducto().getId() == idProducto){
							productoValido = true;
						}
					}

					if (!productoValido){
						r = getResponse("El surtidor con id " + surtidor.getId() + ", no tiene un pico con el producto con id " + idProducto, "184", null);
						return ;
					}

				} else {
					r = getResponse("El id preventivo no pertenece a un surtidor", "184", null);
					return ;
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del preventivo debe ser numérico", "154", null);
				return;
			}

			ChequeoSurtidorProductoData producto = ControlChequeo.getInstancia().getChequeoProducto(idPreventivo, idProducto);
			r = getResponse("", "0", producto);

		} catch (NumberFormatException e) {
			r = getResponse("El identificador del producto debe ser numérico", "128", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}		

	}

	private void obtenerChequeoSurtidorPico(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		String idPicoStr = req.getParameter("idPico");
		String idPreventivoStr = req.getParameter("idPreventivo");
		try{
			int idPico;
			int idPreventivo;

			try {
				idPico = Integer.valueOf(idPicoStr);

				if (idPico <= 0){
					r = getResponse("El id pico debe ser mayor que 0", "131", null);
					return;
				}

			} catch (NumberFormatException e) {
				r = getResponse("El id pico debe ser numérico", "131", null);
				return;
			}

			PreventivoData preventivo;
			try {
				idPreventivo = Integer.valueOf(idPreventivoStr);
				preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(idPreventivo);

				if(preventivo == null){
					r = getResponse("No existe preventivo con identificador " + idPreventivo, "153", null);
					return;
				}

				Activo activo = ControlActivo.getInstancia().buscarActivo(preventivo.getIdActivo());
				if (activo.getClass().equals(Surtidor.class)) {
					Surtidor surtidor = (Surtidor)activo;

					boolean idPicoValido = false;
					for (Pico pico : surtidor.getPicos()){
						if (pico.getId() == idPico){
							idPicoValido = true;
						}
					}

					if (!idPicoValido){
						r = getResponse("El surtidor con id " + surtidor.getId() + ", no tiene un pico con el id " + idPico, "184", null);
						return ;
					}

				} else {
					r = getResponse("El id preventivo no pertenece a un surtidor", "184", null);
					return ;
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del preventivo debe ser numérico", "154", null);
				return;
			}

			ChequeoSurtidorPicoData chequeoPico = ControlChequeo.getInstancia().getChequeoPico(idPreventivo, idPico);
			r = getResponse("", "0", chequeoPico);

		} catch (NumberFormatException e) {
			r = getResponse("El identificador del producto debe ser numérico", "128", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}

	}

	private void obtenerPicos(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			String idSurtidorStr = req.getParameter("idSurtidor");
			Activo activo;
			int idActivo;

			try {
				idActivo = Integer.valueOf(idSurtidorStr);
				activo = ControlActivo.getInstancia().buscarActivo(idActivo);

				if(activo == null){
					r = getResponse("No existe activo con identificador " + idActivo, "138", null);
					return;
				}

				if(activo.getTipo() != 1 ){
					r = getResponse("El activo debe ser de tipo surtidor", "139", null);
					return;
				}
			} catch (NumberFormatException e) {
				r = getResponse("El identificador del activo debe ser numérico", "140", null);
				return;
			}

			int idSurtidor = idActivo;
			List<PicoData> picosData = ControlOperaciones.getInstancia().obtenerPicos(Integer.valueOf(idSurtidor));
			if(picosData != null) {
				r = getResponse("", "0", picosData);
			} else {
				r = getResponse("No fue posible obtener los picos para el surtidor con id " + idSurtidor, "141", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void asociarQrAActivo(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			String idActivoStr = req.getParameter("idActivo");
			String codigoQrStr = req.getParameter("codigoQr");

			Activo activo;
			int idActivo;
			int codigoQr;

			try {
				idActivo = Integer.valueOf(idActivoStr);
				activo = ControlActivo.getInstancia().buscarActivo(idActivo);

				if(activo == null){
					r = getResponse("No existe activo con identificador " + idActivo, "142", null);
					return;
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del activo debe ser numérico", "143", null);
				return;
			}

			try {
				codigoQr = Integer.valueOf(codigoQrStr);

			} catch (NumberFormatException e) {
				r = getResponse("El codigo QR debe ser numérico", "144", null);
				return;
			}

			Pico pico = ControlPico.getInstancia().buscarPico(codigoQrStr);

			if(pico != null){
				r = getResponse("El codigo QR ya está asociado a un pico", "144", null);
				return;
			}

			try {

				boolean response = ControlOperaciones.getInstancia().asociarQrAActivo(Integer.valueOf(codigoQr), Integer.valueOf(idActivo));
				if(response) {
					r = getResponse("", "0", response);
				} else {
					r = getResponse("No fue posible asociar el código QR al activo " + idActivo, "145", null);
				}

			} catch(ConstraintViolationException cve) {
				r = getResponse("El código QR " + codigoQr +  " ya está asociado a otro activo.", "191", null);
			}

		} catch (Exception e) {
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void asociarQrAPico(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idPicoStr = req.getParameter("idPico");
			String codigoQrStr = req.getParameter("codigoQr");

			int idPico;
			int codigoQr;
			Pico pico;

			try {
				idPico = Integer.valueOf(idPicoStr);
			} catch (NumberFormatException e) {
				r = getResponse("El id de pico debe ser numérico", "146", null);
				return;
			}

			try {
				codigoQr = Integer.valueOf(codigoQrStr);
			} catch (NumberFormatException e) {
				r = getResponse("El codigo QR debe ser numérico", "147", null);
				return;
			}

			pico = ControlPico.getInstancia().getPico(idPico);
			if (pico == null){
				r = getResponse("No existe pico con id " + idPico, "147", null);
				return;
			}

			try {
				boolean response = ControlOperaciones.getInstancia().asociarQrAPico(Integer.valueOf(codigoQr), idPico);

				if(response) {
					r = getResponse("", "0", response);
				} else {
					r = getResponse("No fue posible asociar el código QR al id pico " + idPico, "152", null);
				}

			} catch(ConstraintViolationException cve) {
				r = getResponse("El código QR " + codigoQr +  " ya está asociado a otro pico.", "192", null);
			}

		} catch (Exception e) {
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerCorregidos(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			String idPreventivoStr = req.getParameter("idPreventivo");
			int idPreventivo;
			PreventivoData preventivo;
			try {
				idPreventivo = Integer.valueOf(idPreventivoStr);
				preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(idPreventivo);

				if(preventivo == null){
					r = getResponse("No existe preventivo con identificador " + idPreventivo, "153", null);
					return;
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del preventivo debe ser numérico", "154", null);
				return;
			}

			List<CorregidoData> corregidosData = ControlOperaciones.getInstancia().obtenerCorregidos(Integer.valueOf(idPreventivo));
			r = getResponse("", "0", corregidosData);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void obtenerCorregido(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{
			String idCorregidoStr = req.getParameter("idCorregido");
			int idCorregido;
			CorregidoData corregido;
			try {
				idCorregido = Integer.valueOf(idCorregidoStr);
				corregido = ControlOperaciones.getInstancia().obtenerCorregido(idCorregido);

				if(corregido == null){
					r = getResponse("No existe corregido con identificador " + idCorregido, "155", null);
					return;
				}
				r = getResponse("", "0", corregido);
			} catch (NumberFormatException e) {
				r = getResponse("El identificador del corregido debe ser numérico", "156", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void guardarCorregido(HttpServletRequest req, PrintWriter out) throws JsonSyntaxException, JsonIOException, IOException {

		Response r = new Response();
		ComentarioData comentario = null;
		Set<RepuestoLineaCorregido> repuestos = new HashSet<RepuestoLineaCorregido>();
		try {

			CorregidoJson corregidoJson = new Gson().fromJson(req.getReader(), CorregidoJson.class);

			if(corregidoJson.getIdItemChequeado() <= 0) {
				r = getResponse("Debe ingresar un id item chequeado", "158", null);
				return;
			} 

			if(corregidoJson.getComentario() != null && corregidoJson.getComentario().getTexto() != null && corregidoJson.getComentario().getTexto().isEmpty()) {
				r = getResponse("El texto del comentario no puede ser vacio", "157", null);
				return;
			} 

			if(corregidoJson.getComentario() != null && corregidoJson.getComentario().getTexto() != null && corregidoJson.getComentario().getTexto().length() > 1000) {
				r = getResponse("El texto del comentario no tener mas de 255 caracteres", "158", null);
				return;
			} 

			if(corregidoJson.getComentario() != null ) {
				comentario = new ComentarioData(corregidoJson.getComentario().getTexto(), corregidoJson.getComentario().isVisible());
			}

			if(corregidoJson.getRepuestosLineaCorregidos() != null) {

				for(RepuestoLineaCorregidoJson repuesto : corregidoJson.getRepuestosLineaCorregidos()) {

					if (repuesto.getIdRepuesto() <= 0){
						r = getResponse("El id del repuesto del corregido debe ser mayor a 0", "160", null);
						return;
					}

					Repuesto repuestoData = ControlRepuesto.getInstancia().buscarRepuesto(repuesto.getIdRepuesto());
					if (repuestoData == null){
						r = getResponse("No existe repuesto con id " + repuesto.getIdRepuesto(), "162", null);
						return;
					}

					repuestos.add(new RepuestoLineaCorregido(0, repuestoData, repuesto.isNuevo(), repuesto.getCantidad()));
				}
			}


			PreventivoData preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(corregidoJson.getIdPreventivo());
			if(preventivo == null){
				r = getResponse("No existe preventivo con id " + corregidoJson.getIdPreventivo(), "176", null);
				return;
			}

			Corregido corregido; 
			int tipoActivo = preventivo.getIdTipoActivo();

			if(corregidoJson.getIdCorregido() != 0) {

				corregido = ControlCorregido.getInstancia().obtenerCorregido(corregidoJson.getIdCorregido());
				if(corregido == null){
					r = getResponse("No existe corregido con id " + corregidoJson.getIdCorregido(), "177", null);
					return;
				}

				boolean corregidoPerteneceAPreventivo = ControlPreventivo.getInstancia().validarCorregido(corregidoJson.getIdPreventivo(), corregidoJson.getIdCorregido());
				if (!corregidoPerteneceAPreventivo){
					r = getResponse("El corregido con id " + corregidoJson.getIdCorregido() + " no pertenece al preventivo con id " + corregidoJson.getIdPreventivo(), "178", null);
					return;
				}

			} else {
				corregido = Corregido.getNewInstance(tipoActivo);
			}

			corregido.setPreventivo(new Preventivo(corregidoJson.getIdPreventivo()));

			if(comentario != null){
				corregido.setComentario(comentario.getTexto());
				corregido.setComentarioVisible(comentario.isVisible());
			}

			FallaTecnica falla = ControlFalla.getInstancia().buscarFallaTecnica(corregidoJson.getIdFalla());
			if(falla == null){
				r = getResponse("No existe falla con id " + corregidoJson.getIdFalla(), "179", null);
				return;
			}

			Tarea tarea = ControlFalla.getInstancia().buscarTarea(corregidoJson.getIdTarea());
			if(tarea == null){
				r = getResponse("No existe tarea con id " + corregidoJson.getIdTarea(), "180", null);
				return;
			}

			corregido.setTarea(tarea);
			corregido.setFalla(falla);

			DestinoDelCargo destinoDelCargo = ControlOrden.getInstancia().buscarDestinoDelCargo(corregidoJson.getIdDestinoDelCargo());
			if(destinoDelCargo == null){
				r = getResponse("No existe destino del cargo con id " + corregidoJson.getIdDestinoDelCargo(), "181", null);
				return;
			}

			corregido.setDestinoDelCargo(destinoDelCargo);

			if(tipoActivo == 1 && corregidoJson.getIdPico() > 0){
				Surtidor surtidor = (Surtidor)ControlActivo.getInstancia().buscarActivo(preventivo.getIdActivo());

				Pico pico = surtidor.buscarPico(corregidoJson.getIdPico());
				if (pico != null){
					((CorregidoSurtidor)corregido).setPico(pico);
				} else {
					r = getResponse("El pico con id " + corregidoJson.getIdPico() + " no pertenece al surtidor con id " + surtidor.getId(), "181", null);
					return;	
				}
			}

			corregido.setListaDeRepuestos(repuestos);

			PendienteData pendiente = null;
			if (corregidoJson.getIdPendiente() > 0){
				pendiente = ControlPendiente.getInstancia().obtenerPendiente(corregidoJson.getIdPendiente());
				if (pendiente == null){
					r = getResponse("No existe pendiente con id " + corregidoJson.getIdPendiente(), "131", null);
					return;
				}
				
				if (pendiente.getIdActivo() != preventivo.getIdActivo()){
					r = getResponse("El activo del pendiente (" + pendiente.getIdActivo() + ") debe coincidir con el activo del preventivo (" + preventivo.getIdActivo() + ")", "131", null);
					return;
				}
			}
			
			ControlOperaciones.getInstancia().guardarCorregido(corregido, pendiente, corregidoJson.getIdItemChequeado(), corregidoJson.getFotoBytes(), corregidoJson.getFoto2Bytes());

			r = getResponse("", "0", true);

		} catch (IOException e) {
			r = getResponse("Error guarando la foto del corregido", "181", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);

		} finally{
			enviarRespuesta(out, r);
		}
	}

	private void eliminarCorregido(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idCorregidoStr = req.getParameter("idCorregido");

			int idCorregido;
			CorregidoData corregido;
			try {
				idCorregido = Integer.valueOf(idCorregidoStr);
				corregido = ControlOperaciones.getInstancia().obtenerCorregido(idCorregido);

				if(corregido == null){
					r = getResponse("No existe corregido con identificador " + idCorregido, "155", null);
					return;
				}

				boolean eliminado = ControlOperaciones.getInstancia().eliminarCorregido(Integer.valueOf(idCorregidoStr));

				if(eliminado){
					r = getResponse("", "0", true);
				} else {
					r = getResponse("Error al eliminar el corregido con id " + idCorregidoStr, "182", null);
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del corregido debe ser numérico", "156", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}

	private void obtenerIdActivoPorQR(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try{

			String codigoQR = req.getParameter("codigoQR");
			String idEstacionStr = req.getParameter("idEstacion");

			int idEstacion;

			try {

				idEstacion = Integer.valueOf(idEstacionStr);

				Activo activo = ControlActivo.getInstancia().buscarActivo(codigoQR);

				if(activo == null){
					r = getResponse("", "0", -1);

				} else {

					int idActivoEstacion = activo.getEmpresa().getId();

					if(idActivoEstacion == idEstacion){
						r = getResponse("", "0", activo.getId());
					}else{
						r = getResponse("Error, el QR esta asociado a un activo de otra estacion ( " + activo.getEmpresa().getNombre() + " ) ", "180", null);
						return;
					}
				}		

			} catch (NumberFormatException e) {
				r = getResponse("El identificador de la estacion debe ser numérico", "181", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}

	private void obtenerIdPicoPorQR(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{

			String codigoQRStr = req.getParameter("codigoQR");
			String idEstacionStr = req.getParameter("idEstacion");

			int idEstacion;

			try {
				Integer.valueOf(codigoQRStr);

			} catch (NumberFormatException e) {
				r = getResponse("El codigo QR debe ser numérico", "156", null);
				return;
			}
			try {

				idEstacion = Integer.valueOf(idEstacionStr);

				Activo activo = ControlActivo.getInstancia().buscarActivo(codigoQRStr);

				if (activo != null){
					r = getResponse("Error, el QR no está asociado a un pico. ", "180", null);
					return;
				}

				Pico pico = ControlPico.getInstancia().buscarPico(codigoQRStr);

				if(pico == null){
					r = getResponse("", "0", -1);
				} else {
					int idActivoEstacion = pico.getSurtidor().getEmpresa().getId();

					if(idActivoEstacion == idEstacion){
						r = getResponse("", "0", pico.getId());
					}else{
						r = getResponse("Error, el QR esta asociado a un pico de otra estacion ( " + pico.getSurtidor().getEmpresa().getNombre() + " ) ", "180", null);
						return;
					}
				}		

			} catch (NumberFormatException e) {
				r = getResponse("El identificador de la estacion debe ser numérico", "181", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}


	private void obtenerPendiente(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idPendienteStr = req.getParameter("idPendiente");
			int idPendiente;
			PendienteData pendiente;
			try {
				idPendiente = Integer.valueOf(idPendienteStr);
				pendiente = ControlOperaciones.getInstancia().obtenerPendiente(idPendiente);

				if(pendiente != null){
					r = getResponse("", "0", pendiente);
				} else {
					r = getResponse("No existe pendiente con identificador " + idPendiente, "195", null);
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del pendiente debe ser numérico", "196", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}

	}

	private void obtenerPendientes(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idPreventivoStr = req.getParameter("idPreventivo");
			int idPreventivo;
			List<PendienteDataList> pendientes = new ArrayList<PendienteDataList>();
			try {
				idPreventivo = Integer.valueOf(idPreventivoStr);
				PreventivoData preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(idPreventivo);

				if(preventivo == null){
					r = getResponse("No existe preventivo con identificador " + idPreventivo, "127", null);
					return;
				}
				
				// pendientes = ControlOperaciones.getInstancia().obtenerPendientes(idPreventivo);
				pendientes = ControlOperaciones.getInstancia().obtenerPendientesDeActivo(preventivo.getIdActivo());

				if(pendientes != null){
					r = getResponse("", "0", pendientes);
				} else {
					r = getResponse("", "0", new ArrayList<PendienteDataList>());
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del preventivo debe ser numérico", "198", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}

	}

	private void obtenerPendientesPorChequeo(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idItemChequeadoStr = req.getParameter("idItemChequeado");
			int idItemChequeado;
			List<PendienteDataList> pendientes = new ArrayList<PendienteDataList>();
			try {
				idItemChequeado = Integer.valueOf(idItemChequeadoStr);
				pendientes = ControlOperaciones.getInstancia().obtenerPendientesPorChequeo(idItemChequeado);

				r = getResponse("", "0", pendientes);

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del idItemChequeado debe ser numérico", "198", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerCorregidosPorChequeo(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idItemChequeadoStr = req.getParameter("idItemChequeado");
			int idItemChequeado;
			try {
				idItemChequeado = Integer.valueOf(idItemChequeadoStr);

				List<CorregidoData> corregidosData = ControlOperaciones.getInstancia().obtenerCorregidosPorChequeo(idItemChequeado);
				r = getResponse("", "0", corregidosData);

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del idItemChequeado debe ser numérico", "198", null);
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
			PendienteJson pendienteJson = new Gson().fromJson(req.getReader(), PendienteJson.class);
			String idStr = pendienteJson.getId();
			String fotoStr = pendienteJson.getFotoBytes();
			String comentario = pendienteJson.getComentario(); 
			String comentarioVisible = pendienteJson.getComentarioVisible();
			String idPreventivoStr = pendienteJson.getIdPreventivo();
			String destinatario = pendienteJson.getDestinatario();
			int idItemChequeado = pendienteJson.getIdItemChequeado();

			int idPreventivo;
			PreventivoData preventivo;

			if (idItemChequeado <= 0){
				r = getResponse("Debe indicar un item chequeado para este pendiente", "127", null);
				return;
			}
			try{
				idPreventivo = Integer.valueOf(idPreventivoStr);
				preventivo = ControlPreventivo.getInstancia().obtenerPreventivo(idPreventivo);

				if(preventivo == null){
					r = getResponse("No existe preventivo con identificador " + idPreventivo, "127", null);
					return;
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del preventivo debe ser numérico", "128", null);
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
			pd.setIdPreventivo(idPreventivo);
			pd.setPlazo(pendienteJson.getPlazo());
			pd.setIdItemChequeado(pendienteJson.getIdItemChequeado());
			pd.setDestinatario(destinatario);
			pd.setEstado(EstadoPendiente.INICIADO);

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

	private void eliminarPendiente(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idPendienteStr = req.getParameter("idPendiente");

			int idPendiente;
			PendienteData pendiente;
			try {
				idPendiente = Integer.valueOf(idPendienteStr);
				pendiente = ControlOperaciones.getInstancia().obtenerPendiente(idPendiente);

				if(pendiente == null){
					r = getResponse("No existe el pendiente con identificador " + idPendiente, "204", null);
					return;
				}

				boolean eliminado = ControlOperaciones.getInstancia().eliminarPendiente(idPendiente);

				if(eliminado){
					r = getResponse("", "0", true);
				} else {
					r = getResponse("Error al eliminar el pendiente con id " + idPendienteStr, "206", null);
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del pendiente debe ser numérico", "205", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}

	}

	private void descartarPendiente(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idPendienteStr = req.getParameter("idPendiente");
			String idDescartadorStr = req.getParameter("idDescartador");
			String motivoDescarte = req.getParameter("motivoDescarte");

			int idPendiente;
			int idDescartador;
			
			PendienteData pendiente;
			Persona descartador;
			try {
				idPendiente = Integer.valueOf(idPendienteStr);
				pendiente = ControlOperaciones.getInstancia().obtenerPendiente(idPendiente);

				if(pendiente == null){
					r = getResponse("No existe el pendiente con identificador " + idPendiente, "204", null);
					return;
				}
				
				if(pendiente.getEstado().equals(EstadoPendiente.DESCARTADO)){
					r = getResponse("No se puede descartar el pendiente, ya está descartado.", "204", null);
					return;
				}
				
				if(pendiente.getEstado().equals(EstadoPendiente.REPARADO)){
					r = getResponse("No se puede descartar un pendiente que ya fué reparado.", "204", null);
					return;
				}
				idDescartador = Integer.valueOf(idDescartadorStr);
				descartador = ControlPersona.getInstancia().buscarUsuario(idDescartador);

				if(descartador == null){
					r = getResponse("No existe usuario con id " + idDescartador, "204", null);
					return;
				}

				if (motivoDescarte == null || motivoDescarte.trim().equals("")){
					r = getResponse("Debe ingresar un motivo de descarte.", "204", null);
					return;
				}
				
				List<PendienteData> pendientes = new ArrayList<PendienteData>();
				pendientes.add(pendiente);
				boolean descartado = ControlPendiente.getInstancia().descartarPendiente(pendientes, motivoDescarte, idDescartador);

				if(descartado){
					r = getResponse("", "0", true);
				} else {
					r = getResponse("Error al descartar el pendiente con id " + idPendienteStr, "206", null);
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del pendiente y del descartador debe ser numérico", "205", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}

	}

	private void obtenerProductos(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try {
			String idSurtidorStr = req.getParameter("idSurtidor");
			int idSurtidor;

			try {
				idSurtidor = Integer.valueOf(idSurtidorStr);
				List<ProductoData> productos = ControlOperaciones.getInstancia().obtenerProductos(idSurtidor);

				if(productos != null && !productos.isEmpty()){
					r = getResponse("", "0", productos);
				} else {
					r = getResponse("", "0", new ArrayList<ProductoData>());
				}

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del surtidor debe ser numérico", "208", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}

	}

	private void obtenerReporteVisitaPreventiva(HttpServletRequest req, PrintWriter out){
		Response r = new Response();
		try {
			String idVisitaStr = req.getParameter("idVisita");
			int idVisita;

			try {
				idVisita = Integer.valueOf(idVisitaStr);

				Visita visita = ControlVisita.getInstancia().obtenerVisita(idVisita);
				if(visita != null){
					byte[] reporteVisita = ControlOperaciones.getInstancia().obtenerReporteVisitaPreventiva(idVisita);

					if(reporteVisita != null){
						//TODO r = getResponse("", "0", reporteVisita);

						//String jsonResponse = gson.toJson(r);
						out.print(reporteVisita);

					} else {
						r = getResponse("No existe visita con identificador " + idVisita, "105", null);
					} 
				} else {
					r = getResponse("No existe visita con identificador " + idVisita, "105", null);
				} 


			} catch (NumberFormatException e) {
				r = getResponse("El identificador de la visita debe ser numérico", "208", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			//TODO enviarRespuesta(out, r);
		}
	}

	private void reporteVisitaPreventivaEstaciones(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try {
			String idVisitaStr = req.getParameter("idVisita");
			int idVisita;

			try {
				idVisita = Integer.valueOf(idVisitaStr);

				Visita visita = ControlVisita.getInstancia().obtenerVisita(idVisita);
				if(visita != null){
					ReporteData reporteVisita = ControlOperaciones.getInstancia().reporteVisitaPreventivaEstaciones(idVisita);

					if(reporteVisita != null){
						r = getResponse("", "0", reporteVisita);
					} else {
						r = getResponse("No existe visita con identificador " + idVisita, "105", null);
					} 
				} else {
					r = getResponse("No existe visita con identificador " + idVisita, "105", null);
				} 


			} catch (NumberFormatException e) {
				r = getResponse("El identificador de la visita debe ser numérico", "208", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void obtenerCorreosEstacion(HttpServletRequest req, PrintWriter out) {
		String id = req.getParameter("id");

		Response r = new Response();

		try {

			int idEstacion = Integer.parseInt(id);	

			List<EmailEmpresaData> emails = ControlEmpresa.getInstancia().obtenerEmails(idEstacion);

			if(emails != null && !emails.isEmpty()){
				r = getResponse("", "0", emails);
			} else {
				r = getResponse("", "0", new ArrayList<EmailEmpresaData>());
			}


		} catch (NumberFormatException e) {
			r = getResponse("El identificador de la visita debe ser numérico", "106", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}
	}

	private void finalizarVisita(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try {
			FinalizarVisitaJson finVisitaJson = new Gson().fromJson(req.getReader(), FinalizarVisitaJson.class);

			int idVisita = finVisitaJson.getIdVisita();	
			VisitaData visita = ControlVisita.getInstancia().obtenerVisitaDataWS(idVisita);

			if (visita == null){
				r = getResponse("No existe visita con id " + idVisita, "208", null);
				return;
			}

			if (finVisitaJson.getMails().size() > 0){
				ControlVisita.getInstancia().finalizarVisita(visita.getId(), 
						visita.getNombreEstacion(), 
						visita.getIdEstacion(), 
						visita.getFechaProximaVisita(), 
						finVisitaJson.getMails());
				r = getResponse("", "0", "OK");
			} else {
				r = getResponse("Debe ingresar al menos un destinatario", "209", null);
				return;
			}
		} catch (NumberFormatException e) {
			r = getResponse("El identificador de la visita debe ser numérico", "106", null);
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}

	private void obtenerOrganizaciones(HttpServletRequest req, PrintWriter out) {
		Response r = new Response();
		try {
			List<OrganizacionData> organizacionesData = ControlPreventivo.getInstancia().getOrganizacionesData();

			if(organizacionesData != null & !organizacionesData.isEmpty()) {
				r = getResponse("", "0", organizacionesData);
			} else {
				r = getResponse("No hay organizaciones registradas en el sistema.", "211", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}

	private void obtenerItemsChequeo(HttpServletRequest req, PrintWriter out) {
		String tipoChequeo = req.getParameter("tipoChequeo");
		Response r = new Response();

		try {

			TipoChequeo tipoChequeoEnum = TipoChequeo.valueOf(tipoChequeo);

			List<ItemChequeoData> items = ControlChequeo.getInstancia().obtenerItemsChequeoData(tipoChequeoEnum);

			if(items != null && !items.isEmpty()){
				r = getResponse("", "0", items);
			} else {
				r = getResponse("", "0", new ArrayList<EmailEmpresaData>());
			}

		} catch (java.lang.IllegalArgumentException e){
			r = getResponse("El tipoChequeo no es valido", "106", null);

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		}finally{
			enviarRespuesta(out, r);
		}

	}	

	private void obtenerComentariosChequeo(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			String idItemChequedoStr = req.getParameter("idItemChequedo");
			int idItemChequedo;
			try {
				idItemChequedo = Integer.valueOf(idItemChequedoStr);

				List<ComentarioChequeo> items = ControlComentarioChequeo.getInstancia().obtenerComentariosChequeo(new ItemChequeado(idItemChequedo));
				r = getResponse("", "0", items);

			} catch (NumberFormatException e) {
				r = getResponse("El identificador del idItemChequeado debe ser numérico", "198", null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void eliminarComentarioChequeo(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			ComentarioChequeoJson chequeoJson = new Gson().fromJson(req.getReader(), ComentarioChequeoJson.class);

			if (chequeoJson.getId() == 0){
				r = getResponse("Debe indicar el id del comentario", "198", null);
				return;
			}

			ComentarioChequeo cc = ControlComentarioChequeo.getInstancia().obtenerComentarioChequeo(chequeoJson.getId());
			if (cc == null){
				r = getResponse("No existe comentario con ese id", "198", null);
				return;
			}

			boolean result = ControlComentarioChequeo.getInstancia().eliminarComentarioChequeo(chequeoJson.getId());
			if (result){
				r = getResponse("", "0", result);
			} else {
				r = getResponse("Error al intentar eliminar el comentario chequeo", "198", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void agregarComentarioChequeo(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			ComentarioChequeoJson chequeoJson = new Gson().fromJson(req.getReader(), ComentarioChequeoJson.class);
			if (chequeoJson.getIdItemChequeado() == 0){
				r = getResponse("Debe indicar el idItemChequedo", "198", null);
				return;
			}

			ItemChequeado itemChequedo = ControlChequeo.getInstancia().obtenerItemChequeado(chequeoJson.getIdItemChequeado());
			if (itemChequedo == null){
				r = getResponse("No existe un itemChequeado con ese id", "198", null);
				return;
			}

			if (chequeoJson.getIdUsuario() == 0){
				r = getResponse("Debe indicar el idUsuario", "198", null);
				return;
			}

			Persona persona = ControlPersona.getInstancia().buscarUsuario(chequeoJson.getIdUsuario());
			if (persona == null){
				r = getResponse("No existe un usuario con ese id", "198", null);
				return;
			}

			if (chequeoJson.getTexto().equals("")){
				r = getResponse("Debe ingresar un texto", "198", null);
				return;
			}

			boolean result = ControlComentarioChequeo.getInstancia().agregarComentarioChequeo(chequeoJson);

			if (result){
				r = getResponse("", "0", result);
			} else {
				r = getResponse("Error al intentar eliminar el comentario chequeo", "198", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private void modificarComentarioChequeo(HttpServletRequest req, PrintWriter out) {

		Response r = new Response();
		try{
			ComentarioChequeoJson chequeoJson = new Gson().fromJson(req.getReader(), ComentarioChequeoJson.class);
			if (chequeoJson.getId() == 0){
				r = getResponse("Debe indicar el id del comentario", "198", null);
				return;
			}

			ComentarioChequeo cc = ControlComentarioChequeo.getInstancia().obtenerComentarioChequeo(chequeoJson.getId());
			if (cc == null){
				r = getResponse("No existe comentario con ese id", "198", null);
				return;
			}

			if (chequeoJson.getIdUsuario() == 0){
				r = getResponse("Debe indicar el idUsuario", "198", null);
				return;
			}

			Persona persona = ControlPersona.getInstancia().buscarUsuario(chequeoJson.getIdUsuario());
			if (persona == null){
				r = getResponse("No existe un usuario con ese id", "198", null);
				return;
			}

			if (chequeoJson.getTexto().equals("")){
				r = getResponse("Debe ingresar un texto", "198", null);
				return;
			}

			boolean result = ControlComentarioChequeo.getInstancia().modificarComentarioChequeo(chequeoJson);

			if (result){
				r = getResponse("", "0", result);
			} else {
				r = getResponse("Error al intentar modificar el comentario chequeo", "198", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			r = getResponse("Ha ocurrido un error inesperado", "100", null);
		} finally {
			enviarRespuesta(out, r);
		}
	}

	private Response getResponse(String error, String status, Object o){
		Response r = new Response();

		r.setError(error);
		r.setStatus(status);;
		r.setDatos(o);

		return r;
	}

	private void enviarRespuesta(PrintWriter out, Response r){

		String jsonResponse = gson.toJson(r);

		out.print(jsonResponse);
	}

}
