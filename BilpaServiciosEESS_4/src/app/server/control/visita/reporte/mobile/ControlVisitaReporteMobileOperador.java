package app.server.control.visita.reporte.mobile;

import java.util.ArrayList;
import java.util.Collections;

import app.client.dominio.Organizacion;
import app.client.dominio.Preventivo;
import app.client.dominio.Visita;
import app.client.dominio.data.DetalleReporteData;
import app.client.dominio.data.ReporteData;
import app.client.dominio.data.SeccionReporteData;
import app.server.persistencia.ActivoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.VisitaDao;

public class ControlVisitaReporteMobileOperador {
	
	private static ControlVisitaReporteMobileOperador instancia = null;

	public static ControlVisitaReporteMobileOperador getInstancia() {
		if (instancia == null) {
			instancia = new ControlVisitaReporteMobileOperador();
		}
		return instancia;
	}

	public static void setInstancia(ControlVisitaReporteMobileOperador instancia) {
		ControlVisitaReporteMobileOperador.instancia = instancia;
	}

	private ControlVisitaReporteMobileOperador() {

	}

	/**
	 * 
	 * @param idVisita
	 * @return
	 *  Se genera el siguiente reporte:
	 *  
	 * 				Reporte de visita preventiva 
	 * 				 
	 *	Cliente	                      -    Lan-Cam
	 *	Fecha de visita	              -    04.12.2014
	 *	Agente de servicio	          -    Camilo Burgos
	 *	Preventivo correspondiente a  -    Diciembre 2014
	 *	
	 *
	 *	Se despacharon las siguientes cantidades de combustible
	 *	
	 *	Surtidor Wayne 3G2201P ! | Año 2012 | Serie W3G108297
	 *	Manguera        -   	Litros despachados
	 *	Manguera 37	    - 		154 
	 *	Manguera 36		-		101 
	 *
	 *	
	 *	Se hizo mantenimiento preventivo a los siguientes activos
	 *
	 *	Surtidor Wayne 3G2201P ! | Año 2012 | Serie W3G108297
	 *	Valores por manguera 
	 *	Manguera 36		-		Caudal 10000 litros / minuto
	 *							Se instaló el precinto 150020
	 *	Manguera 37		-		Caudal 10020 litros / minuto
	 *							Se instaló el precinto 150021
	 *	Elementos que necesitaron corrección
	 *	A nivel de Surtidor		-		Visual y limpieza
	 *									Prueba display/ iluminación
	 *	A nivel de Producto Super 95 SP		-		Correas/ Poleas
	 *	A nivel de Producto Gasoil Especial	-		Motor unidad bombeo
	 *	A nivel de Manguera 36				-		Identificación de producto
	 *	A nivel de Manguera 37				-		Sistema bloqueo
	 *												Plan sellado
	 *	Elementos que quedaron pendientes
	 *	Comentario										-		Plazo
	 *	Reemplazar display, funciona incorrectamente	-		01.2015
	 *	Reparaciones 
	 *	Reparación 1
	 *	Falla			-		CONEXIÓN DOUBLE BUMP CON PERDIDA
	 *	Tarea			-		APERTURA DE VALVULA DE CHOQUE
	 *	Repuestos		-		O-RING DOBLE BUMP 62-100315 - NAC10078
	 *							ACRILICO DISPLAY GILBARCO - GEN001
	 *	Manguera		-		Nro. 37 | Super 95 SP
	 *	Comentario		-		Válvula de choque desgastada  
	 * 
	 */
	public ReporteData reporteVisitaPreventivaEstaciones(int idVisita) {

		DaoTransaction tx = new DaoTransaction();
		ReporteData reporte = new ReporteData();
		try {
			tx.begin();
			VisitaDao dao = new VisitaDao();
			ActivoDao activoDao = new ActivoDao();

			Visita visita = dao.get(idVisita);

			reporte.setNombre(UtilVisitaReporteMobileOperador.VISITA_PREVENTIVA_ESTACIONES);
			reporte.setTitulo(UtilVisitaReporteMobileOperador.VISITA_PREVENTIVA_ESTACIONES_TITULO);

			reporte.getSecciones().add(UtilVisitaReporteMobileOperador.getPrimerSeccion(visita));

			if(visita.getListaDePreventivos() != null && !visita.getListaDePreventivos().isEmpty()) {
				ArrayList<Preventivo> preventivos = new ArrayList<Preventivo>(visita.getListaDePreventivos());
				Collections.sort(preventivos);

				int contadorSegSeccion = 1;
				for(Preventivo p : preventivos) {
					SeccionReporteData seccion2 = UtilVisitaReporteMobileOperador.getSegundaSeccion(p, contadorSegSeccion);
					if (seccion2 != null){
						reporte.getSecciones().add(seccion2);
						contadorSegSeccion++;
					}
				}

				SeccionReporteData seccion3 = new SeccionReporteData();
				seccion3.setNombre("mantenimiento");
				seccion3.setTitulo(UtilVisitaReporteMobileOperador.SECCION_MANTENIMIENTO_TITULO);

				for(int i = 0; i < preventivos.size(); i++) {
					Preventivo p = preventivos.get(i);
					if (p.tieneAlgunaData(Organizacion.Operador, "R")){

						DetalleReporteData detalleSurtidor = new DetalleReporteData();
						detalleSurtidor.setLabel(activoDao.get(p.getActivo().getId()).toStringLargo());
						detalleSurtidor.setFormato(DetalleReporteData.SIMPLE);
						detalleSurtidor.setColor(DetalleReporteData.BLUE);
						seccion3.getDetalles().add(detalleSurtidor);

						UtilVisitaReporteMobileOperador.addValoresPorManguera(seccion3, p);
						UtilVisitaReporteMobileOperador.addChequeo(seccion3, p);
						UtilVisitaReporteMobileOperador.addReparaciones(seccion3, p);
						UtilVisitaReporteMobileOperador.addPendientes(seccion3, p);

						if (i != preventivos.size()-1){// si no es el ultimo preventivo, agrego un separador.
							UtilVisitaReporteMobileOperador.addRow(seccion3, DetalleReporteData.VACIA);//todo
						}
					}
				}
				reporte.getSecciones().add(seccion3);
			}

			return reporte;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return null;
	}

	/*private boolean tieneAlgunaData(Preventivo p) {
		if (p.getChequeo() != null){
			for(ItemChequeado ic : p.getChequeo().getItemsChequeados()) {
				if(p.getActivo().getId() > 0   
						(p.getChequeo() != null && p.getChequeo().tieneChequeo("R")) || 
						ic.getListaDeCorregidos().size() > 0 || 
						p.tienePendientesVisibles(p.getChequeo()))) {
					return true;
				}
			}
		}
		return false;
	}*/
}
