package app.server.control.correctivo.reporte;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import app.client.dominio.Activo;
import app.client.dominio.Contador;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Orden;
import app.client.dominio.Pico;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.Surtidor;
import app.client.dominio.data.DetalleReporteData;
import app.client.dominio.data.PendienteDataList;
import app.client.dominio.data.ReporteData;
import app.client.dominio.data.SeccionReporteData;
import app.server.UtilFechas;
import app.server.control.ControlActivo;
import app.server.control.ControlOrden;
import app.server.control.ControlPendiente;
import app.server.control.ControlReparacion;
import app.server.propiedades.PropiedadUrlLogo;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

public class ControlCorrectivoReporteMobileOperador {

	private static ControlCorrectivoReporteMobileOperador instancia = null;

	public static ControlCorrectivoReporteMobileOperador getInstancia() {
		if (instancia == null) {
			instancia = new ControlCorrectivoReporteMobileOperador();
		}
		return instancia;
	}

	public static void setInstancia(ControlCorrectivoReporteMobileOperador instancia) {
		ControlCorrectivoReporteMobileOperador.instancia = instancia;
	}

	private ControlCorrectivoReporteMobileOperador() {

	}

	private ReporteData document;
	private Orden orden;

	protected PropiedadUrlLogo propiedadLogo = new PropiedadUrlLogo();	
	protected Paragraph pie;
	protected Phrase linea = new Phrase("__________________________________________________________________________");

	protected int cantidadPaginas;


	public ReporteData obtenerPreviewCorrectivo(int numero) throws Exception {

		try {
			document = new ReporteData();

			orden = ControlOrden.getInstancia().buscarOrden(numero);

			document.setNombre("Reporte de mantenimiento correctivo");//????????
			document.setTitulo("Resumen de mantenimiento correctivo");

			// piePagina(document);

			crearTableCabezal(document);
			crearTableCabezal2(document);

			crearTotalizadores(document);
			
			crearSoluciones(document);

			// Nicre
			// crearPendientes(document, EstadoPendiente.REPARADO);
			// crearPendientes(document, EstadoPendiente.INICIADO);
			
			/*crearComentariosDeOrden(document);

			crearFirma();*/

			return document;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void agregarCeldaEnBlanco(List<DetalleReporteData> detalles) {
		detalles.add(new DetalleReporteData(" ", null, DetalleReporteData.VACIA, null));
	}

	protected void crearEspacioEnBlanco(int cant, List<DetalleReporteData> detalles) throws DocumentException 
	{
		for (int i = 0; i < cant; i++) 
		{
			// detalles.add(new DetalleReporteData(" ", null, DetalleReporteData.VACIA, null));
		}
	}

	protected void crearTableCabezal(ReporteData document) throws DocumentException, MalformedURLException, IOException 
	{
		List<DetalleReporteData> detalles = new ArrayList<DetalleReporteData>();
		document.getSecciones().add(new SeccionReporteData("header1", "", detalles));

		detalles.add(new DetalleReporteData(" Cliente", orden.getEmpresa().getNombre(), DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Nro Cliente", orden.getEmpresa().getNumeroSerie() +"", DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Dirección", orden.getEmpresa().getDireccion(), DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Localidad", orden.getEmpresa().getLocalidad(), DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Petrolera", orden.getEmpresa().getSello()+"", DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Tipo Trabajo", orden.getTipoTrabajo()+"", DetalleReporteData.COMPLETA, null));
	}

	protected void crearTableCabezal2(ReporteData document) throws DocumentException, MalformedURLException, IOException 
	{
		List<DetalleReporteData> detalles = new ArrayList<DetalleReporteData>();
		document.getSecciones().add(new SeccionReporteData("header2", "", detalles));

		String textoNroOrden = orden.getNumero()+"";
		if (orden.getNumeroParteDucsa() != null && !orden.getNumeroParteDucsa().equalsIgnoreCase(""))
		{
			textoNroOrden+=" - Ducsa: " + orden.getNumeroParteDucsa();
		}

		detalles.add(new DetalleReporteData(" Correctivo Nro", textoNroOrden, DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Fecha Reclamo", UtilFechas.getDia(orden.getFechaInicio()), DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Hora Reclamo", UtilFechas.getHora(orden.getFechaInicio()), DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Fecha de visita", UtilFechas.getDia( orden.getInicioService()), DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Hora de inicio", UtilFechas.getHora( orden.getInicioService()), DetalleReporteData.COMPLETA, null));
		detalles.add(new DetalleReporteData(" Hora de fin", UtilFechas.getHora( orden.getFinService()), DetalleReporteData.COMPLETA, null));
	}

	private void crearTotalizadores(ReporteData document) throws DocumentException {
		List<DetalleReporteData> detalles = new ArrayList<DetalleReporteData>();
		boolean hayData = false;

		for (Reparacion r : orden.getReparaciones()) 
		{
			if (r.getActivo().getTipo() == 1) 
			{
				Surtidor surtidor = (Surtidor) ControlActivo.getInstancia().buscarActivo(r.getActivo().getId());
				ReparacionSurtidor reparacionSurtidor = ControlReparacion.getInstancia().obtenerReparacionSurtidor(r.getId());

				if (reparacionSurtidor.getContadores().size() > 0 || reparacionSurtidor.tienePrecintosReemplazados()){
					if (hayData){
						agregarCeldaEnBlanco(detalles);
					}
					hayData = true;

					detalles.add(new DetalleReporteData(surtidor.toStringLargo(), null, DetalleReporteData.SIMPLE, DetalleReporteData.BOLD, DetalleReporteData.BLUE));

					for (Solucion solucion : reparacionSurtidor.getSoluciones()){
						Pico pico = solucion.getPico(reparacionSurtidor.getContadores());

						if (pico != null){
							detalles.add(new DetalleReporteData("Manguera número " + pico.getNumeroEnLaEstacion(), null, DetalleReporteData.SIMPLE, DetalleReporteData.BOLD));
							
							addContador(detalles, reparacionSurtidor, solucion);
							addPrecintoYCalibres(detalles, solucion);
						}
					}
				}
			}
		}

		if (hayData){
			crearEspacioEnBlanco(1, detalles);
			document.getSecciones().add(new SeccionReporteData("totalizadores", "Valores por manguera", detalles));
		}
	}

	private void addContador(List<DetalleReporteData> detalles, ReparacionSurtidor reparacionSurtidor, Solucion solucion) {
		Contador contador = solucion.getContadores(reparacionSurtidor.getContadores());
		if (contador != null){
			detalles.add(new DetalleReporteData("Litros despachados", contador.getFin()- contador.getInicio()+"", DetalleReporteData.COMPLETA, DetalleReporteData.BOLD));
		}
	}

	private static void addPrecintoYCalibres(List<DetalleReporteData> detalles, Solucion solucion) {
		if (solucion.tienePrecintos()){
			detalles.add(new DetalleReporteData("Calibres y precintos", null, DetalleReporteData.SIMPLE, DetalleReporteData.BOLD));

			detalles.add(new DetalleReporteData("Calibre 1", solucion.getCalibre().getCalibre1()+"", DetalleReporteData.COMPLETA));
			detalles.add(new DetalleReporteData("Calibre 2", solucion.getCalibre().getCalibre2()+"", DetalleReporteData.COMPLETA));
			detalles.add(new DetalleReporteData("Calibre 3", solucion.getCalibre().getCalibre3()+"", DetalleReporteData.COMPLETA));

			if(solucion.getPrecinto() != null && solucion.getPrecinto().cambioPrecinto()) {
				detalles.add(new DetalleReporteData("Se instaló el precinto número  ", solucion.getPrecinto().getNumero(), DetalleReporteData.COMPLETA));

			} else if (solucion.getPrecinto() != null && solucion.getPrecinto().getNumero() != null && !solucion.getPrecinto().getNumero().isEmpty()){
				detalles.add(new DetalleReporteData("Se mantuvo el precinto número  ", solucion.getPrecinto().getNumero(), DetalleReporteData.COMPLETA));
			}

			if (solucion.tienePrecintosReemplazados()){
				detalles.add(new DetalleReporteData("Calibre 4", solucion.getCalibre().getCalibre4()+"", DetalleReporteData.COMPLETA));
				detalles.add(new DetalleReporteData("Calibre 5", solucion.getCalibre().getCalibre5()+"", DetalleReporteData.COMPLETA));
				detalles.add(new DetalleReporteData("Calibre 6", solucion.getCalibre().getCalibre6()+"", DetalleReporteData.COMPLETA));
			}
		}
	}

	private void crearSoluciones(ReporteData document) throws DocumentException {
		List<DetalleReporteData> detalles = new ArrayList<DetalleReporteData>();
		boolean hayData = false;

		if (orden.tieneSoluciones())
		{
			int cont = 0;
			for (Reparacion r : orden.getReparaciones()) 
			{
				if (r.tieneSoluciones()) 
				{
					hayData = true;
					if (cont > 0){
						detalles.add(new DetalleReporteData(" ", null, DetalleReporteData.VACIA, null));
					}
					cont++;
					detalles.add(new DetalleReporteData(r.getActivo().toStringLargo(), null, DetalleReporteData.SIMPLE, DetalleReporteData.BOLD, DetalleReporteData.BLUE));

					int cont2 = 0;
					for (Solucion solucion : r.getSoluciones())
					{
						if (cont2 > 0){
							detalles.add(new DetalleReporteData(" ", null, DetalleReporteData.SIMPLE, null));
						}
						cont2++;
						
						detalles.add(new DetalleReporteData("Falla detectada", solucion.getFallaTecnica().getDescripcion(), DetalleReporteData.COMPLETA, DetalleReporteData.BOLD));
						detalles.add(new DetalleReporteData("Tarea realizada", solucion.getTarea().getDescripcion(), DetalleReporteData.COMPLETA, DetalleReporteData.BOLD));

						if (r.getActivo().getTipo() == 1)
						{
							ReparacionSurtidor repSurtidor = (ReparacionSurtidor) r;
							Contador contador = repSurtidor.buscarContador(solucion);
							if (contador != null)//CONTADORES
							{
								detalles.add(new DetalleReporteData("Manguera", contador.getPico().toString(), DetalleReporteData.COMPLETA, DetalleReporteData.BOLD));
								detalles.add(new DetalleReporteData("Totalizador inicial", contador.getInicio()+ "", DetalleReporteData.COMPLETA));
								detalles.add(new DetalleReporteData("Totalizador final", contador.getFin()+ "", DetalleReporteData.COMPLETA));
							}
						}
					}	

					Set<RepuestoLinea> rls = orden.getRepuestosLinea(r.getActivo());
					if (rls.size() > 0){
						detalles.add(new DetalleReporteData("Repuestos", null, DetalleReporteData.COMPLETA, DetalleReporteData.BOLD));
						detalles.add(new DetalleReporteData("Descripción", "Cantidad", DetalleReporteData.COMPLETA, DetalleReporteData.BOLD));

						for (RepuestoLinea rl : rls)
						{
							detalles.add(new DetalleReporteData(rl.getRepuesto().getDescripcion(), rl.getCantidad()+"", DetalleReporteData.COMPLETA));
						}
					}

					/*if(r.tieneSolucionesConComentariosImprimibles()){
						detalles.add(new DetalleReporteData("Comentarios", null, DetalleReporteData.COMPLETA, DetalleReporteData.BOLD));

						boolean esComentarioDeReparacion = s.getComentario() != null && s.getComentario().isImprimible() && s.getComentario().getTexto() != null && s.getComentario().getTexto().length() > 0;

						if (esComentarioDeReparacion) {
							crearCeldasTableComentario(tComentario, s.getComentario());
						}
					}*/
				}
			}
		}

		if (hayData){
			crearEspacioEnBlanco(1, detalles);
			document.getSecciones().add(new SeccionReporteData("reparaciones", "Reparaciones", detalles));
		}
	}
	
	private void crearPendientes(ReporteData document, EstadoPendiente estadoPendiente) throws DocumentException {
		List<DetalleReporteData> detalles = new ArrayList<DetalleReporteData>();
		boolean hay = false;

		int cont = 0;
		for (Activo activo : orden.getEmpresa().getListaDeActivos()) 
		{
			List<PendienteDataList> pendientes = ControlPendiente.getInstancia().obtenerPendientesListDeActivo(activo.getId(), estadoPendiente);

			boolean tieneVisibles = false;
			boolean agregarTitulo = true;

			for (PendienteDataList p : pendientes) {
				if (p.isComentarioVisible()){
					tieneVisibles = true;
				}
			}

			if (tieneVisibles){
				for (PendienteDataList p : pendientes) {
					if (agregarTitulo){
						agregarTitulo = false;
						if (hay){
							crearEspacioEnBlanco(1, detalles);
						}

						if (cont > 0){
							detalles.add(new DetalleReporteData(" ", null, DetalleReporteData.VACIA, null));
						}
						cont++;
						detalles.add(new DetalleReporteData(activo.toStringLargo(), null, DetalleReporteData.SIMPLE, DetalleReporteData.BOLD, DetalleReporteData.BLUE));
					}

					String pendienteStr = p.getComentario();
					if(p.getPlazo() != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						pendienteStr+=", antes de: " + sdf.format(p.getPlazo());
					}
					detalles.add(new DetalleReporteData(pendienteStr, null, DetalleReporteData.SIMPLE));
				}
				hay = true;
			}
		}

		if (hay){
			crearEspacioEnBlanco(1, detalles);
			String titulo = "Pendientes a realizar";
			if (estadoPendiente.equals(EstadoPendiente.REPARADO)){
				titulo = "Pendientes que fueron reparados";
			}
			document.getSecciones().add(new SeccionReporteData("pendientes", titulo, detalles));
		}
	}
}
