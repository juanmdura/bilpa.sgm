package app.server.control.visita.reporte.mobile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import app.client.dominio.ActivoGenerico;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Chequeo;
import app.client.dominio.ChequeoBomba;
import app.client.dominio.ChequeoGenerico;
import app.client.dominio.ChequeoPico;
import app.client.dominio.ChequeoProducto;
import app.client.dominio.ChequeoSurtidor;
import app.client.dominio.ChequeoTanque;
import app.client.dominio.Corregido;
import app.client.dominio.ItemChequeado;
import app.client.dominio.Pendiente;
import app.client.dominio.Preventivo;
import app.client.dominio.RepuestoLineaCorregido;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
import app.client.dominio.TipoChequeo;
import app.client.dominio.TipoDescarga;
import app.client.dominio.Visita;
import app.client.dominio.data.DetalleReporteData;
import app.client.dominio.data.SeccionReporteData;
import app.server.UtilDecimal;
import app.server.control.visita.ControlVisita;
import app.server.control.visita.reporte.UtilVisitaReporteOperador;
import app.server.persistencia.ActivoGenericoDao;
import app.server.persistencia.BombaSumergibleDao;
import app.server.persistencia.ChequeoBombaDao;
import app.server.persistencia.ChequeoGenericoDao;
import app.server.persistencia.ChequeoSurtidorDao;
import app.server.persistencia.ChequeoTanqueDao;
import app.server.persistencia.SurtidorDao;
import app.server.persistencia.TanqueDao;
import app.server.persistencia.TipoDescargaDao;

public class UtilVisitaReporteMobileOperador extends UtilVisitaReporteOperador {

	/**
	 * 
	 * @param visita
	 * @return
	 * 
	 *  Se genera esta parte del reporte:
	 *  
	 *  Cliente	                      -    Lan-Cam
	 *  Fecha de visita	              -    04.12.2014
	 *  Agente de servicio	          -    Camilo Burgos
	 *  Preventivo correspondiente a  -    Diciembre 2014  
	 *  
	 */
	public static SeccionReporteData getPrimerSeccion(Visita visita) {

		SeccionReporteData seccionHeader = new SeccionReporteData();
		seccionHeader.setNombre("header");

		DetalleReporteData detalleCliente = new DetalleReporteData();
		detalleCliente.setLabel("Cliente");
		detalleCliente.setValor(visita.getEstacion().getNombre());
		detalleCliente.setFormato(DetalleReporteData.COMPLETA);
		detalleCliente.setEstilo(DetalleReporteData.BOLD);
		seccionHeader.getDetalles().add(detalleCliente);

		DetalleReporteData detalleFechaVisita = new DetalleReporteData();
		detalleFechaVisita.setLabel("Fecha de visita");
		if(visita.getFechaInicio() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			detalleFechaVisita.setValor(sdf.format(visita.getFechaInicio()));
		} else {
			detalleFechaVisita.setValor("-");
		}
		detalleFechaVisita.setFormato(DetalleReporteData.COMPLETA);
		seccionHeader.getDetalles().add(detalleFechaVisita);

		DetalleReporteData detalleAgente = new DetalleReporteData();
		detalleAgente.setLabel("Agente de servicio");
		detalleAgente.setValor(visita.getTecnico().getNombre() + " " + visita.getTecnico().getApellido());
		detalleAgente.setFormato(DetalleReporteData.COMPLETA);
		seccionHeader.getDetalles().add(detalleAgente);

		DetalleReporteData detalleFechaCorresponde = new DetalleReporteData();
		detalleFechaCorresponde.setLabel("Preventivo corresponde a");
		if(visita.getFechaProximaVisita() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(visita.getFechaProximaVisita());
			String fechaPlanificada = app.server.UtilFechas.getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
			detalleFechaCorresponde.setValor(fechaPlanificada);
		} else {
			detalleFechaCorresponde.setValor("-");
		}
		detalleFechaCorresponde.setFormato(DetalleReporteData.COMPLETA);
		seccionHeader.getDetalles().add(detalleFechaCorresponde);

		return seccionHeader;
	}


	/**
	 * 
	 * @param preventivo
	 * @return 
	 * 
	 *  Se genera esta parte del reporte:
	 * 
	 * 	Se despacharon las siguientes cantidades de combustible
	 *
	 *	Surtidor Wayne 3G2201P ! | Año 2012 | Serie W3G108297
	 *	Manguera        -   	Litros despachados
	 *	Manguera 37	    - 		154 
	 *	Manguera 36		-		101
	 * 
	 */
	public static SeccionReporteData getSegundaSeccion(Preventivo preventivo, int contadorSegSeccion) {

		if(preventivo.getActivo().getTipo() == 1) {
			if(preventivo.getChequeo() != null) {
				ChequeoSurtidorDao csd = new ChequeoSurtidorDao();
				ChequeoSurtidor chequeoSurtidor = csd.get(preventivo.getChequeo().getId());

				SeccionReporteData seccionDespacho = new SeccionReporteData();
				seccionDespacho.setNombre("despacho");

				if (!chequeoSurtidor.getListaDeChequeosPicos().isEmpty()){
					if(contadorSegSeccion == 1) {
						seccionDespacho.setTitulo(SECCION_DESPACHO_TITULO);
					}

					SurtidorDao sd = new SurtidorDao();
					Surtidor s = sd.get(preventivo.getActivo().getId());

					DetalleReporteData detalleSurtidor = new DetalleReporteData();
					detalleSurtidor.setLabel(s.getModelo().getNombre() + " | " + s.getNumeroSerie());
					detalleSurtidor.setFormato(DetalleReporteData.SIMPLE);

					DetalleReporteData detalleMangueraLitros = new DetalleReporteData();
					detalleMangueraLitros.setLabel("Manguera");
					detalleMangueraLitros.setValor("Litros despachados");
					detalleMangueraLitros.setFormato(DetalleReporteData.COMPLETA);

					seccionDespacho.getDetalles().add(detalleSurtidor);
					seccionDespacho.getDetalles().add(detalleMangueraLitros);

					boolean tieneTotalizadores = false;
					List<ChequeoPico> chequeosPico = new ArrayList<ChequeoPico>();
					chequeosPico.addAll(chequeoSurtidor.getListaDeChequeosPicos());
					Collections.sort(chequeosPico);

					for (int i =0; i < chequeosPico.size(); i++){
						ChequeoPico cp = chequeosPico.get(i);

						if (cp.tieneTotalizador()) {
							tieneTotalizadores = true;

							DetalleReporteData detalleManguera = new DetalleReporteData();
							detalleManguera.setLabel("Manguera número " + cp.getPico().getNumeroEnLaEstacion());

							double valor = cp.getTotalizadorElectronicoFinal() - cp.getTotalizadorElectronicoInicial();
							String pattern = "#.##";
							DecimalFormat decimalFormat = new DecimalFormat(pattern);
							String valorFormatted = decimalFormat.format(valor);

							detalleManguera.setValor(valorFormatted);
							detalleManguera.setFormato(DetalleReporteData.COMPLETA);
							seccionDespacho.getDetalles().add(detalleManguera);
						}
					}
					if (tieneTotalizadores){
						return seccionDespacho;
					} 
				}
			}
		}
		return null;

	}

	/**
	 * 
	 * @param seccionMantenimiento
	 * @param preventivo
	 * 
	 *  Agrega a la sección la siguiente parte del reporte: 
	 * 
	 *	Surtidor Wayne 3G2201P ! | Año 2012 | Serie W3G108297
	 *	Valores por manguera 
	 *	Manguera 36		-		Caudal 10000 litros / minuto
	 *							Se instaló el precinto 150020
	 *	Manguera 37		-		Caudal 10020 litros / minuto
	 *							Se instaló el precinto 150021
	 *
	 */
	public static void addValoresPorManguera(SeccionReporteData seccionMantenimiento, Preventivo preventivo) {
		if(preventivo.getActivo().getTipo() == 1 && preventivo.getChequeo() != null) {

			ChequeoSurtidorDao csd = new ChequeoSurtidorDao();
			ChequeoSurtidor chequeoSurtidor = csd.get(preventivo.getChequeo().getId());
			boolean imprimirTituloValoresPorManguera = true;

			List<ChequeoPico> chequeosPico = new ArrayList<ChequeoPico>();
			chequeosPico.addAll(chequeoSurtidor.getListaDeChequeosPicos());
			Collections.sort(chequeosPico);
			for(ChequeoPico cp : chequeosPico) {
				if (cp.tieneValoresPorManguera()){

					if (imprimirTituloValoresPorManguera){
						imprimirTituloValoresPorManguera = false;
						DetalleReporteData detalleValoresManguera = new DetalleReporteData();
						detalleValoresManguera.setLabel("Valores por manguera");
						detalleValoresManguera.setFormato(DetalleReporteData.SIMPLE);
						seccionMantenimiento.getDetalles().add(detalleValoresManguera);
					}

					DetalleReporteData detalleManguera = new DetalleReporteData();
					detalleManguera.setLabel("Manguera número " + cp.getPico().getNumeroEnLaEstacion());

					if(cp.tieneCaudal()) {
						if(detalleManguera.getValor() == null) {
							detalleManguera.setValor("Caudal " + UtilDecimal.getLitrosPorMinuto(cp.getCaudal().getTiempo(), cp.getCaudal().getVolumen()) + " litros / minuto");
							detalleManguera.setFormato(DetalleReporteData.COMPLETA);
							seccionMantenimiento.getDetalles().add(detalleManguera);
						} else {
							detalleManguera = new DetalleReporteData();
							detalleManguera.setValor("Caudal " + UtilDecimal.getLitrosPorMinuto(cp.getCaudal().getTiempo(), cp.getCaudal().getVolumen()) + " litros / minuto");
							detalleManguera.setFormato(DetalleReporteData.VALOR);
							seccionMantenimiento.getDetalles().add(detalleManguera);
						}
					}

					addPrecintoYCalibres(seccionMantenimiento, cp);

					addRow(seccionMantenimiento, DetalleReporteData.SIMPLE);
				}
			}
		}
	}

	private static void addPrecintoYCalibres(SeccionReporteData seccionMantenimiento, ChequeoPico cp) {
		if (cp.tienePrecintos()){
			DetalleReporteData detalle = new DetalleReporteData();
			detalle.setFormato(DetalleReporteData.SIMPLE);
			detalle.setLabel("Calibres y precintos");
			seccionMantenimiento.getDetalles().add(detalle);

			DetalleReporteData detalle1 = new DetalleReporteData();
			detalle1.setFormato(DetalleReporteData.COMPLETA);
			detalle1.setLabel("Calibre 1");
			detalle1.setValor(cp.getCalibre().getCalibre1()+"");
			seccionMantenimiento.getDetalles().add(detalle1);

			DetalleReporteData detalle2 = new DetalleReporteData();
			detalle2.setFormato(DetalleReporteData.COMPLETA);
			detalle2.setLabel("Calibre 2");
			detalle2.setValor(cp.getCalibre().getCalibre2()+"");
			seccionMantenimiento.getDetalles().add(detalle2);

			DetalleReporteData detalle3 = new DetalleReporteData();
			detalle3.setFormato(DetalleReporteData.COMPLETA);
			detalle3.setLabel("Calibre 3");
			detalle3.setValor(cp.getCalibre().getCalibre3()+"");
			seccionMantenimiento.getDetalles().add(detalle3);


			if(cp.getPrecinto() != null && cp.getPrecinto().cambioPrecinto()) {
				DetalleReporteData detallePr = new DetalleReporteData();
				detallePr.setFormato(DetalleReporteData.COMPLETA);
				detallePr.setLabel("Se instaló el precinto número  ");
				detallePr.setValor(cp.getPrecinto().getNumero());
				seccionMantenimiento.getDetalles().add(detallePr);

			} else if (cp.getPrecinto() != null && cp.getPrecinto().getNumero() != null && !cp.getPrecinto().getNumero().isEmpty()){
				DetalleReporteData detallePr = new DetalleReporteData();
				detallePr.setFormato(DetalleReporteData.COMPLETA);
				detallePr.setLabel("Se mantuvo el precinto número");
				detallePr.setValor(cp.getPrecinto().getNumero());
				seccionMantenimiento.getDetalles().add(detallePr);
			}

			if (cp.tienePrecintosReemplazados()){
				DetalleReporteData detalle4 = new DetalleReporteData();
				detalle4.setFormato(DetalleReporteData.COMPLETA);
				detalle4.setLabel("Calibre 4");
				detalle4.setValor(cp.getCalibre().getCalibre4()+"");
				seccionMantenimiento.getDetalles().add(detalle4);

				DetalleReporteData detalle5 = new DetalleReporteData();
				detalle5.setFormato(DetalleReporteData.COMPLETA);
				detalle5.setLabel("Calibre 5");
				detalle5.setValor(cp.getCalibre().getCalibre5()+"");
				seccionMantenimiento.getDetalles().add(detalle5);

				DetalleReporteData detalle6 = new DetalleReporteData();
				detalle6.setFormato(DetalleReporteData.COMPLETA);
				detalle6.setLabel("Calibre 6");
				detalle6.setValor(cp.getCalibre().getCalibre6()+"");
				seccionMantenimiento.getDetalles().add(detalle6);

			}
		}
	}

	/**
	 * 
	 * @param seccionMantenimiento
	 * @param preventivo
	 * @param contadorCorreccion 
	 * 
	 *  Agrega a la sección la siguiente parte del reporte:
	 * 
	 *	Elementos que necesitaron corrección
	 *	A nivel de Surtidor					-		Visual y limpieza
	 *												Prueba display/ iluminación
	 *	A nivel de Producto Super 95 SP		-		Correas/ Poleas
	 *	A nivel de Producto Gasoil Especial	-		Motor unidad bombeo
	 *	A nivel de Manguera 36				-		Identificación de producto
	 *	A nivel de Manguera 37				-		Sistema bloqueo
	 *												Plan sellado
	 *
	 */
	public static void addChequeo(SeccionReporteData seccionMantenimiento, Preventivo preventivo) {
		/*if (preventivo.getChequeo() == null || !preventivo.getChequeo().tieneChequeo("R")){
			return;
		}*/

		String estado = "R";
		boolean seAgregoChequeo = false;
		// Tipo de activo = 1 para los chequeos de surtidores
		if(preventivo.getActivo().getTipo() == 1 &&  preventivo.getChequeo().tieneChequeo(estado)) {
			addTitulo(seccionMantenimiento);
			ChequeoSurtidorDao csd = new ChequeoSurtidorDao();
			ChequeoSurtidor chequeoSurtidor = csd.get(preventivo.getChequeo().getId());

			addChequeoSurtidor(seccionMantenimiento, chequeoSurtidor, preventivo);
			addChequeoProducto(seccionMantenimiento, chequeoSurtidor);
			addChequeoPico(seccionMantenimiento, chequeoSurtidor);
			seAgregoChequeo = true;
		}

		// Tipo de activo = 2 para los chequeos de tanque
		if(preventivo.getActivo().getTipo() == 2 && ( preventivo.getChequeo().tieneChequeo(estado) 
				|| preventivo.getChequeo().tieneOtraData())) {
			addChequeoTanque(seccionMantenimiento, preventivo);
			seAgregoChequeo = true;
		}

		// Tipo de activo = 4 para los chequeos de bomba
		if(preventivo.getActivo().getTipo() == 4 && ( preventivo.getChequeo().tieneChequeo(estado) 
				|| preventivo.getChequeo().tieneOtraData())) {
			addChequeoBomba(seccionMantenimiento, preventivo);
			seAgregoChequeo = true;
		}

		// Tipo de activo = 6 para los chequeos genericos
		if(preventivo.getActivo().getTipo() == 6 && ( preventivo.getChequeo().tieneChequeo(estado) 
				|| preventivo.getChequeo().tieneOtraData())) {
			addChequeoGenerico(seccionMantenimiento, preventivo);
			seAgregoChequeo = true;
		}

		if (seAgregoChequeo){
			addRow(seccionMantenimiento, DetalleReporteData.SIMPLE);
		}
	}


	private static void addTitulo(SeccionReporteData seccionMantenimiento) {
		DetalleReporteData detalleheader = new DetalleReporteData();
		detalleheader.setLabel("Elementos que necesitaron corrección");
		detalleheader.setFormato(DetalleReporteData.SIMPLE);
		seccionMantenimiento.getDetalles().add(detalleheader);
	}


	private static void addChequeoBomba(SeccionReporteData seccionMantenimiento, Preventivo preventivo) {
		ChequeoBombaDao chequeoDao = new ChequeoBombaDao();
		ChequeoBomba cb = chequeoDao.get(preventivo.getChequeo().getId());
		if (cb.tieneChequeo("R")){

			BombaSumergibleDao bd = new BombaSumergibleDao();
			BombaSumergible b = bd.get(preventivo.getActivo().getId());

			DetalleReporteData detalleheader = new DetalleReporteData();
			/*detalleheader.setLabel("Bomba " + b.toString());
			detalleheader.setFormato(DetalleReporteData.SIMPLE);
			seccionMantenimiento.getDetalles().add(detalleheader);*/

			detalleheader = new DetalleReporteData();
			detalleheader.setLabel("Elementos que necesitaron corrección");
			detalleheader.setFormato(DetalleReporteData.SIMPLE);
			seccionMantenimiento.getDetalles().add(detalleheader);

			DetalleReporteData detalle = new DetalleReporteData();
			detalle.setLabel("A nivel de Bomba");
			seccionMantenimiento.getDetalles().add(detalle);

			addItemsChequeados(seccionMantenimiento, cb, detalle);
		}
	}

	private static void addChequeoGenerico(SeccionReporteData seccionMantenimiento, Preventivo preventivo) {
		ChequeoGenericoDao chequeoDao = new ChequeoGenericoDao();
		ChequeoGenerico cb = chequeoDao.get(preventivo.getChequeo().getId());
		if (cb.tieneChequeo("R")){

			ActivoGenericoDao bd = new ActivoGenericoDao();
			ActivoGenerico b = bd.get(preventivo.getActivo().getId());

			DetalleReporteData detalleheader = new DetalleReporteData();
			/*detalleheader.setLabel("Bomba " + b.toString());
			detalleheader.setFormato(DetalleReporteData.SIMPLE);
			seccionMantenimiento.getDetalles().add(detalleheader);*/

			detalleheader = new DetalleReporteData();
			detalleheader.setLabel("Elementos que necesitaron corrección");
			detalleheader.setFormato(DetalleReporteData.SIMPLE);
			seccionMantenimiento.getDetalles().add(detalleheader);

			DetalleReporteData detalle = new DetalleReporteData();
			detalle.setLabel("A nivel de " + b.getTipoActivoGenerico().getNombre());
			seccionMantenimiento.getDetalles().add(detalle);

			addItemsChequeados(seccionMantenimiento, cb, detalle);
		}
	}

	private static void addItemsChequeados(SeccionReporteData seccionMantenimiento, Chequeo chequeo, DetalleReporteData detalle) {
		Set<ItemChequeado> itemsChequeados = chequeo.getItemsChequeados();
		if (itemsChequeados != null){
			boolean primerItem = true;
			for (ItemChequeado icd : itemsChequeados){
				if (icd.getValor() != null && icd.getValor().equalsIgnoreCase("R")){
					String texto = icd.getItemChequeo().getTexto();
					texto = ControlVisita.agregarComentarios(icd, texto, false);
					if(primerItem) {
						
						detalle.setValor(texto);
						detalle.setFormato(DetalleReporteData.COMPLETA);
						primerItem = false;
					} else {
						detalle = new DetalleReporteData();
						detalle.setValor(texto);
						detalle.setFormato(DetalleReporteData.VALOR);
						seccionMantenimiento.getDetalles().add(detalle);
					}
				}
			}
		}
	}


	private static void addChequeoTanque(SeccionReporteData seccionMantenimiento, Preventivo preventivo) {
		ChequeoTanqueDao ctd = new ChequeoTanqueDao();
		ChequeoTanque ct = ctd.get(preventivo.getChequeo().getId());

		TanqueDao td = new TanqueDao();
		Tanque t = td.get(preventivo.getActivo().getId());

		DetalleReporteData detalleheader = new DetalleReporteData();
		/*detalleheader.setLabel("Tanque " + t.toString());
		detalleheader.setFormato(DetalleReporteData.SIMPLE);
		seccionMantenimiento.getDetalles().add(detalleheader);*/

		TipoDescargaDao tiposDescargaDao = new TipoDescargaDao();
		TipoDescarga tipoDescarga = tiposDescargaDao.get(ct.getTipoDeDescarga());

		if (tipoDescarga != null && tipoDescarga.getId() > 1 ){
			DetalleReporteData detalle = new DetalleReporteData();
			detalle.setFormato(DetalleReporteData.COMPLETA);
			detalle.setLabel("Tipo Descarga");
			detalle.setValor(tipoDescarga.getNombre());
			seccionMantenimiento.getDetalles().add(detalle);
		}

		if (ct.getMedidaDelAgua() > 0){
			DetalleReporteData detalle = new DetalleReporteData();
			detalle.setFormato(DetalleReporteData.COMPLETA);
			detalle.setLabel(" Medida del agua");
			detalle.setValor(ct.getMedidaDelAgua() + "");
			seccionMantenimiento.getDetalles().add(detalle);
		}

		if (ct.tieneChequeo("R")){

			detalleheader = new DetalleReporteData();
			detalleheader.setLabel("Elementos que necesitaron corrección");
			detalleheader.setFormato(DetalleReporteData.SIMPLE);
			seccionMantenimiento.getDetalles().add(detalleheader);

			DetalleReporteData detalle = new DetalleReporteData();
			detalle.setLabel("A nivel de Tanque");
			seccionMantenimiento.getDetalles().add(detalle);

			addItemsChequeados(seccionMantenimiento, ct, detalle);
		}
	}


	private static void addChequeoPico(SeccionReporteData seccionMantenimiento, ChequeoSurtidor chequeoSurtidor) {
		if (!chequeoSurtidor.tieneChequeoPico("R")){
			return;
		}

		DetalleReporteData detalle;
		for(ChequeoPico cp : chequeoSurtidor.getListaDeChequeosPicos()) {
			if (!cp.tieneChequeo("R")){
				continue;
			}
			detalle = new DetalleReporteData();
			detalle.setLabel("A nivel de Manguera número " + cp.getPico().getNumeroEnLaEstacion());
			seccionMantenimiento.getDetalles().add(detalle);

			addItemsChequeados(seccionMantenimiento, cp, detalle);
		}
	}


	private static void addChequeoProducto(SeccionReporteData seccionMantenimiento, ChequeoSurtidor chequeoSurtidor) {
		if (!chequeoSurtidor.tieneChequeoProducto("R")){
			return;
		}

		DetalleReporteData detalle;
		for(ChequeoProducto cp : chequeoSurtidor.getListaDeProductos()) {
			if (!cp.tieneChequeo("R")){
				continue;
			}

			detalle = new DetalleReporteData();
			detalle.setLabel("A nivel de " + cp.getProducto().getNombre());
			seccionMantenimiento.getDetalles().add(detalle);

			addItemsChequeados(seccionMantenimiento, cp, detalle);
		}
	}


	private static void addChequeoSurtidor(SeccionReporteData seccionMantenimiento, ChequeoSurtidor cs, Preventivo preventivo) {
		if (!cs.tieneChequeoSurtidor("R")){
			return;
		}

		DetalleReporteData detalle = new DetalleReporteData();
		detalle.setLabel("A nivel de Surtidor");
		seccionMantenimiento.getDetalles().add(detalle);

		addItemsChequeados(seccionMantenimiento, cs, detalle);

	}


	/**
	 * 
	 * @param seccionMantenimiento
	 * @param preventivo
	 * 
	 *  Agrega a la sección la siguiente parte del reporte:
	 *  
	 *	Elementos que quedaron pendientes
	 *	Comentario										-		Plazo
	 *	Reemplazar display, funciona incorrectamente	-		01.2015
	 *
	 */
	public static void addPendientes(SeccionReporteData seccionMantenimiento, Preventivo preventivo) {
		Chequeo chequeo = preventivo.getChequeo();
		addPendientes(seccionMantenimiento, preventivo, chequeo, "");

		if (preventivo.getActivo().getTipo() == 1){
			ChequeoSurtidor cs = new ChequeoSurtidorDao().get(chequeo.getId());
			for (Chequeo chequeoPico : cs.getListaDeChequeosPicos()){
				addPendientes(seccionMantenimiento, preventivo, chequeoPico, "Nro: " + ((ChequeoPico)chequeoPico).getPico().getNumeroEnLaEstacion());	
			}

			for (Chequeo chequeoProducto : cs.getListaDeProductos()){
				addPendientes(seccionMantenimiento, preventivo, chequeoProducto, ((ChequeoProducto)chequeoProducto).getProducto().getNombre());
			}
		}
	}


	private static boolean addPendientes(SeccionReporteData seccionMantenimiento, Preventivo preventivo, Chequeo chequeo, String texto) {
		boolean agregarTitulo = true;
		for(ItemChequeado ic : chequeo.getItemsChequeados()) {
			for(Pendiente p : ic.getListaDePendientes()) {
				if (p.isComentarioVisible() /*&& preventivo.getId() == p.getPreventivo().getId()*/){
					if (agregarTitulo){

						agregarTitulo = false;
						DetalleReporteData detalleheader = new DetalleReporteData();

						String subTitulo = ic.getItemChequeo().getTextoTipoChequeo();
						if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Generico)){
							subTitulo = ic.getItemChequeo().getTipoActivoGenerico().getNombre();
						}
						String titulo = "Pendientes de " + ic.getItemChequeo().getTexto() + " en " + subTitulo;

						detalleheader.setLabel(titulo + " " + texto);
						detalleheader.setFormato(DetalleReporteData.SIMPLE);
						seccionMantenimiento.getDetalles().add(detalleheader);
					}

					DetalleReporteData detalle = new DetalleReporteData();
					String pendienteStr = p.getComentario();
					if(p.getPlazo() != null) {
						pendienteStr.concat(", antes de: " + new SimpleDateFormat("MM/yyyy").format(p.getPlazo()));
					}
					detalle.setLabel(pendienteStr);
					detalle.setFormato(DetalleReporteData.SIMPLE);
					seccionMantenimiento.getDetalles().add(detalle);
				}
			}

		}
		return agregarTitulo;
	}


	/**
	 * 
	 * @param seccionMantenimiento
	 * @param preventivo
	 * 
	 *  Agrega a la sección la siguiente parte del reporte:
	 * 
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
	public static void addReparaciones(SeccionReporteData seccionMantenimiento, Preventivo preventivo) {
		Chequeo chequeo = preventivo.getChequeo();
		addReparaciones(seccionMantenimiento, preventivo.getChequeo(), "");

		if (preventivo.getActivo().getTipo() == 1){
			ChequeoSurtidor cs = new ChequeoSurtidorDao().get(chequeo.getId());
			for (Chequeo chequeoPico : cs.getListaDeChequeosPicos()){
				addReparaciones(seccionMantenimiento, chequeoPico, "Nro: " + ((ChequeoPico)chequeoPico).getPico().getNumeroEnLaEstacion());	
			}

			for (Chequeo chequeoProducto : cs.getListaDeProductos()){
				addReparaciones(seccionMantenimiento, chequeoProducto, ((ChequeoProducto)chequeoProducto).getProducto().getNombre());
			}
		}

		if (preventivo.tienePendientesVisibles(preventivo.getChequeo())){
			addRow(seccionMantenimiento, DetalleReporteData.SIMPLE);
		}

	}


	private static int addReparaciones(SeccionReporteData seccionMantenimiento,	Chequeo chequeo, String texto) {
		DetalleReporteData detalle;
		int numeroReparacion = 0;

		for(ItemChequeado ic : chequeo.getItemsChequeados()) {

			for(Corregido c : ic.getListaDeCorregidos()) {
				numeroReparacion ++;
				if (numeroReparacion == 1){
					DetalleReporteData detalleheader = new DetalleReporteData();

					String subTitulo = ic.getItemChequeo().getTextoTipoChequeo();
					if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Generico)){
						subTitulo = ic.getItemChequeo().getTipoActivoGenerico().getNombre();
					}
					String titulo = "Reparaciones de " + ic.getItemChequeo().getTexto() + " en " + subTitulo;

					detalleheader.setLabel(titulo + " " + texto);
					detalleheader.setFormato(DetalleReporteData.SIMPLE);
					seccionMantenimiento.getDetalles().add(detalleheader);
				}

				detalle = new DetalleReporteData();
				detalle.setLabel("Reparación " + numeroReparacion);
				detalle.setFormato(DetalleReporteData.SIMPLE);
				seccionMantenimiento.getDetalles().add(detalle);

				if(c.getFalla() != null) {

					detalle = new DetalleReporteData();
					detalle.setLabel("Falla");
					detalle.setValor(c.getFalla().getDescripcion());
					detalle.setFormato(DetalleReporteData.COMPLETA);
					seccionMantenimiento.getDetalles().add(detalle);

				}

				if(c.getTarea() != null) {

					detalle = new DetalleReporteData();
					detalle.setLabel("Tarea");
					detalle.setValor(c.getTarea().getDescripcion());
					detalle.setFormato(DetalleReporteData.COMPLETA);
					seccionMantenimiento.getDetalles().add(detalle);

				}

				if(c.getListaDeRepuestos() != null && !c.getListaDeRepuestos().isEmpty()) {

					detalle = new DetalleReporteData();
					detalle.setLabel("Repuestos");
					seccionMantenimiento.getDetalles().add(detalle);

					boolean valorSeteado = false;
					for(RepuestoLineaCorregido rlc : c.getListaDeRepuestos()) {

						if(valorSeteado) {
							detalle = new DetalleReporteData();
							detalle.setValor(rlc.getRepuesto().getDescripcion());
							detalle.setFormato(DetalleReporteData.VALOR);
							seccionMantenimiento.getDetalles().add(detalle);
						} else {
							detalle.setValor(rlc.getRepuesto().getDescripcion());
							detalle.setFormato(DetalleReporteData.COMPLETA);
							valorSeteado = true;
						}
					}

				}

				if(c.getComentario() != null && c.isComentarioVisible()) {

					detalle = new DetalleReporteData();
					detalle.setLabel("Comentario");
					detalle.setValor(c.getComentario());
					detalle.setFormato(DetalleReporteData.COMPLETA);
					seccionMantenimiento.getDetalles().add(detalle);
				}

			}

		}
		return numeroReparacion;
	}

	public static void addRow(SeccionReporteData seccion, String formato) {
		DetalleReporteData vacio = new DetalleReporteData();
		vacio.setFormato(formato);
		seccion.getDetalles().add(vacio);
	}


	public static void crearChequeoGeneralTanque(SeccionReporteData seccionMantenimiento, Preventivo p) {
		if(p.getActivo().getTipo() == 2 && p.getChequeo() != null) {

			ChequeoTanqueDao csd = new ChequeoTanqueDao();
			ChequeoTanque ct = csd.get(p.getChequeo().getId());
			TipoDescargaDao tiposDescargaDao = new TipoDescargaDao();
			TipoDescarga tipoDescarga = tiposDescargaDao.get(ct.getTipoDeDescarga());

			if (tipoDescarga != null && tipoDescarga.getId() > 1 ){
				DetalleReporteData detalle = new DetalleReporteData();
				detalle.setFormato(DetalleReporteData.SIMPLE);
				detalle.setLabel("Tipo Descarga");
				detalle.setValor(tipoDescarga.getNombre());
				seccionMantenimiento.getDetalles().add(detalle);
			}

			if (ct.getMedidaDelAgua() > 0){
				DetalleReporteData detalle = new DetalleReporteData();
				detalle.setFormato(DetalleReporteData.COMPLETA);
				detalle.setLabel(" Medida del agua");
				detalle.setValor(ct.getMedidaDelAgua() + "");
				seccionMantenimiento.getDetalles().add(detalle);
			}
		}
	}
}