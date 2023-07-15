package app.server.control.visita.reporte.web;

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
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;

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

public class UtilVisitaReporteWebOperador extends UtilVisitaReporteOperador {

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

						/*if (cp.tieneTotalizador()) {
							tieneTotalizadores = true;

							DetalleReporteData detalleManguera = new DetalleReporteData();
							detalleManguera.setLabel("Manguera n�mero " + cp.getPico().getNumeroEnLaEstacion());

							double valor = cp.getTotalizadorElectronicoFinal() - cp.getTotalizadorElectronicoInicial();
							String pattern = "#.##";
							DecimalFormat decimalFormat = new DecimalFormat(pattern);
							String valorFormatted = decimalFormat.format(valor);

							detalleManguera.setValor(valorFormatted);
							detalleManguera.setFormato(DetalleReporteData.COMPLETA);
							seccionDespacho.getDetalles().add(detalleManguera);
						}*/
					}
					if (tieneTotalizadores){
						return seccionDespacho;
					} 
				}
			}
		}
		return null;

	}

	public static void addValoresPorManguera(PdfPTable t, Preventivo preventivo) {
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
						PdfPCell cell = new PdfPCell(new Phrase ("Valores por manguera", FontFactory.getFont("Arial", 10, Font.BOLD)));
						cell.setColspan(2);
						t.addCell(cell);
					}

					PdfPCell cellm = new PdfPCell(new Phrase ("Manguera número " + cp.getPico().getNumeroEnLaEstacion(), FontFactory.getFont("Arial", 10, Font.BOLD)));
					cellm.setColspan(2);
					t.addCell(cellm);

					if(cp.tieneCaudal()) {
						t.addCell(new PdfPCell(new Phrase ("Caudal ", FontFactory.getFont("Arial", 10, Font.BOLD))));
						t.addCell(new PdfPCell(new Phrase (UtilDecimal.getLitrosPorMinuto(cp.getCaudal().getTiempo(), cp.getCaudal().getVolumen()) + " litros / minuto", FontFactory.getFont("Arial", 10))));
					}

					if(preventivo.getActivo().getTipo() == 1 ) {
						addPrecintoYCalibres(t, cp);
					}

					PdfPCell cell = new PdfPCell(new Phrase (" ", FontFactory.getFont("Arial", 10, Font.BOLD)));
					cell.setColspan(2);
					t.addCell(cell);
				}
			}
		}
	}

	public static void addChequeo(PdfPTable t, Preventivo preventivo) {
		/*if (preventivo.getChequeo() == null || !preventivo.getChequeo().tieneChequeo("R")){
			return;
		}*/

		if(preventivo.getActivo().getTipo() == 2 ) {
			ChequeoTanqueDao ctd = new ChequeoTanqueDao();
			ChequeoTanque ct = ctd.get(preventivo.getChequeo().getId());

			crearChequeoGeneralTanque(t, ct);

			PdfPCell cell3 = new PdfPCell(new Phrase (" ", FontFactory.getFont("Arial", 10, Font.BOLD)));
			cell3.setColspan(2);
			t.addCell(cell3);
		}

		String estado = "R";
		// Tipo de activo = 1 para los chequeos de surtidores
		if(preventivo.getActivo().getTipo() == 1 ) {
			ChequeoSurtidor chequeoSurtidor = new ChequeoSurtidorDao().get(preventivo.getChequeo().getId());

			if (chequeoSurtidor.tieneChequeoSurtidor(estado) || 
					chequeoSurtidor.tieneChequeoProducto(estado) || 
					chequeoSurtidor.tieneChequeoPico(estado)){

				addTituloChequeo(t);
				addChequeoSurtidor(t, chequeoSurtidor, preventivo);
				addChequeoProducto(t, chequeoSurtidor);
				addChequeoPico(t, chequeoSurtidor);
			}
		}

		// Tipo de activo = 2 para los chequeos de tanque
		if(preventivo.getActivo().getTipo() == 2 && ( preventivo.getChequeo().tieneChequeo(estado) 
				|| preventivo.getChequeo().tieneOtraData())) {
			addChequeoTanque(t, preventivo);
		}

		// Tipo de activo = 4 para los chequeos de bomba
		if(preventivo.getActivo().getTipo() == 4 && ( preventivo.getChequeo().tieneChequeo(estado) 
				|| preventivo.getChequeo().tieneOtraData())) {
			addChequeoBomba(t, preventivo);
		}

		// Tipo de activo = 6 para los chequeos genericos
		if(preventivo.getActivo().getTipo() == 6 && ( preventivo.getChequeo().tieneChequeo(estado) 
				|| preventivo.getChequeo().tieneOtraData())) {
			addChequeoGenerico(t, preventivo);
		}

		if (preventivo.tienePendientesVisibles(preventivo.getChequeo())){
			PdfPCell cell3 = new PdfPCell(new Phrase (" ", FontFactory.getFont("Arial", 10, Font.BOLD)));
			cell3.setColspan(2);
			t.addCell(cell3);
		}
	}


	private static void addTituloChequeo(PdfPTable t) {
		PdfPCell cell = new PdfPCell(new Phrase("Elementos que necesitaron corrección", FontFactory.getFont("Arial", 10, Font.BOLD)));
		cell.setColspan(2);
		t.addCell(cell);
	}

	private static void addPrecintoYCalibres(PdfPTable t, ChequeoPico cp) {
		if (cp.tienePrecintos()){
			PdfPCell cell = new PdfPCell(new Phrase("Calibres y precintos", FontFactory.getFont("Arial", 10, Font.BOLD)));
			cell.setColspan(2);
			t.addCell(cell);

			t.addCell(new PdfPCell(new Phrase("Calibre 1", FontFactory.getFont("Arial", 10))));
			t.addCell(new PdfPCell(new Phrase (cp.getCalibre().getCalibre1()+"", FontFactory.getFont("Arial", 10))));

			t.addCell(new PdfPCell(new Phrase ("Calibre 2", FontFactory.getFont("Arial", 10))));
			t.addCell(new PdfPCell(new Phrase (cp.getCalibre().getCalibre2()+"", FontFactory.getFont("Arial", 10))));

			t.addCell(new PdfPCell(new Phrase ("Calibre 3", FontFactory.getFont("Arial", 10))));
			t.addCell(new PdfPCell(new Phrase (cp.getCalibre().getCalibre3()+"", FontFactory.getFont("Arial", 10))));

			if(cp.getPrecinto() != null && cp.getPrecinto().cambioPrecinto()) {
				t.addCell(new PdfPCell(new Phrase ("Se instaló el precinto número  ", FontFactory.getFont("Arial", 10))));
				t.addCell(new PdfPCell(new Phrase (cp.getPrecinto().getNumero(), FontFactory.getFont("Arial", 10))));

			} else if (cp.getPrecinto() != null && cp.getPrecinto().getNumero() != null && !cp.getPrecinto().getNumero().isEmpty()){
				t.addCell(new PdfPCell(new Phrase ("Se mantuvo el precinto número", FontFactory.getFont("Arial", 10))));
				t.addCell(new PdfPCell(new Phrase (cp.getPrecinto().getNumero(), FontFactory.getFont("Arial", 10))));
			}

			if (cp.tienePrecintosReemplazados()){
				t.addCell(new PdfPCell(new Phrase ("Calibre 4", FontFactory.getFont("Arial", 10))));
				t.addCell(new PdfPCell(new Phrase (cp.getCalibre().getCalibre4()+"", FontFactory.getFont("Arial", 10))));

				t.addCell(new PdfPCell(new Phrase ("Calibre 5", FontFactory.getFont("Arial", 10))));
				t.addCell(new PdfPCell(new Phrase (cp.getCalibre().getCalibre5()+"", FontFactory.getFont("Arial", 10))));

				t.addCell(new PdfPCell(new Phrase ("Calibre 6", FontFactory.getFont("Arial", 10))));
				t.addCell(new PdfPCell(new Phrase (cp.getCalibre().getCalibre6()+"", FontFactory.getFont("Arial", 10))));

			}
		}
	}

	private static void addChequeoBomba(PdfPTable t, Preventivo preventivo) {
		ChequeoBombaDao ctd = new ChequeoBombaDao();
		ChequeoBomba cb = ctd.get(preventivo.getChequeo().getId());

		BombaSumergibleDao bd = new BombaSumergibleDao();

		String estado = "R";

		if (!cb.tieneChequeo(estado)){
			return;
		}
		addTituloChequeo(t);
		addItemsChequeados(t, cb, estado);
	}
	
	private static void addChequeoGenerico(PdfPTable t, Preventivo preventivo) {
		ChequeoGenericoDao ctd = new ChequeoGenericoDao();
		ChequeoGenerico cb = ctd.get(preventivo.getChequeo().getId());

		ActivoGenericoDao bd = new ActivoGenericoDao();

		String estado = "R";

		if (!cb.tieneChequeo(estado)){
			return;
		}
		addTituloChequeo(t);
		addItemsChequeados(t, cb, estado);
	}

	private static void addChequeoTanque(PdfPTable t, Preventivo preventivo) {

		ChequeoTanqueDao ctd = new ChequeoTanqueDao();
		ChequeoTanque ct = ctd.get(preventivo.getChequeo().getId());

		TanqueDao td = new TanqueDao();

		String estado = "R";

		if (!ct.tieneChequeo(estado)){
			return;
		}
		addTituloChequeo(t);
		addItemsChequeados(t, ct, estado);
	}

	private static void crearChequeoGeneralTanque(PdfPTable t, ChequeoTanque ct) {

		TipoDescargaDao tiposDescargaDao = new TipoDescargaDao();
		TipoDescarga tipoDescarga = tiposDescargaDao.get(ct.getTipoDeDescarga());

		if (tipoDescarga != null){
			PdfPCell cell1 = new PdfPCell(new Phrase(" Tipo descarga ", FontFactory.getFont("Arial", 10, Font.BOLD)));
			t.addCell(cell1);

			PdfPCell cell2 = new PdfPCell(new Phrase(tipoDescarga.getNombre(), FontFactory.getFont("Arial", 10)));
			t.addCell(cell2);
		}

		if (ct.getMedidaDelAgua() > 0){
			PdfPCell cell1 = new PdfPCell(new Phrase(" Medida del agua ", FontFactory.getFont("Arial", 10, Font.BOLD)));
			t.addCell(cell1);

			PdfPCell cell2 = new PdfPCell(new Phrase(ct.getMedidaDelAgua() + "", FontFactory.getFont("Arial", 10)));
			t.addCell(cell2);
		}
	}


	private static void addChequeoPico(PdfPTable t, ChequeoSurtidor chequeoSurtidor) {
		String estado = "R";
		if (!chequeoSurtidor.tieneChequeoPico(estado)){
			return;
		}

		for(ChequeoPico cp : chequeoSurtidor.getListaDeChequeosPicos()) {
			if (!cp.tieneChequeo(estado)){
				continue;
			}

			PdfPCell cell1 = new PdfPCell(new Phrase(" En la manguera " + ((cp.getPico().getNumeroEnLaEstacion() > 0) ? cp.getPico().getNumeroEnLaEstacion() : " - Sin nro."),  FontFactory.getFont("Arial", 10)));
			t.addCell(cell1);

			addItemsChequeados(t, cp, estado);

		}
	}


	private static void addChequeoProducto(PdfPTable t, ChequeoSurtidor chequeoSurtidor) {
		String estado = "R";

		if (!chequeoSurtidor.tieneChequeoProducto(estado)){
			return;
		}

		for(ChequeoProducto cp : chequeoSurtidor.getListaDeProductos()) {
			if (!cp.tieneChequeo(estado)){
				continue;
			}

			PdfPCell cell1 = new PdfPCell(new Phrase(" En el producto " + cp.getProducto().getNombre(),  FontFactory.getFont("Arial", 10)));
			t.addCell(cell1);

			addItemsChequeados(t, cp, estado);
		}
	}


	private static void addChequeoSurtidor(PdfPTable t, ChequeoSurtidor chequeoSurtidor, Preventivo preventivo) {
		String estado = "R";
		if (!chequeoSurtidor.tieneChequeoSurtidor(estado)){
			return;
		}


		PdfPCell cell1 = new PdfPCell(new Phrase(" En el surtidor",  FontFactory.getFont("Arial", 10)));
		t.addCell(cell1);

		addItemsChequeados(t, chequeoSurtidor, estado);
	}


	private static void addItemsChequeados(PdfPTable t, Chequeo chequeo, String estado) {
		Set<ItemChequeado> itemsChequeados = chequeo.getItemsChequeados();
		if (itemsChequeados != null){
			boolean primerItem = true;
			for (ItemChequeado icd : itemsChequeados){
				if (icd.getValor() != null && icd.getValor().equalsIgnoreCase(estado)){
					String texto = icd.getItemChequeo().getTexto();
					texto = ControlVisita.agregarComentarios(icd, texto, false);
					primerItem = addChequeoCell(t, texto, primerItem);
				}
			}
		}
	}

	private static boolean addChequeoCell(PdfPTable t, String texto, Boolean elPrimero) {
		PdfPCell cell1Data = new PdfPCell(new Phrase(texto,  FontFactory.getFont("Arial", 10)));
		if (!elPrimero){
			t.addCell(cellConBorde());
		}
		t.addCell(cell1Data);
		return Boolean.FALSE;
	}

	public static void addPendientes(PdfPTable t, Preventivo preventivo) {
		Chequeo chequeo = preventivo.getChequeo();
		addPendientes(t, chequeo, "");

		if (preventivo.getActivo().getTipo() == 1){
			ChequeoSurtidor cs = new ChequeoSurtidorDao().get(chequeo.getId());
			for (Chequeo chequeoPico : cs.getListaDeChequeosPicos()){
				addPendientes(t, chequeoPico, "Nro: " + ((ChequeoPico)chequeoPico).getPico().getNumeroEnLaEstacion());	
			}

			for (Chequeo chequeoProducto : cs.getListaDeProductos()){
				addPendientes(t, chequeoProducto, ((ChequeoProducto)chequeoProducto).getProducto().getNombre());
			}
		}
	}


	private static boolean addPendientes(PdfPTable t, Chequeo chequeo, String texto) {
		boolean agregarTitulo = true;
		for(ItemChequeado ic : chequeo.getItemsChequeados()) {

			for(Pendiente p : ic.getListaDePendientes()) {

				if (p.isComentarioVisible()){
					if (agregarTitulo){

						agregarTitulo = false;
						String subTitulo = ic.getItemChequeo().getTextoTipoChequeo();
						if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Generico)){
							subTitulo = ic.getItemChequeo().getTipoActivoGenerico().getNombre();
						}
						String titulo = "Pendientes de " + ic.getItemChequeo().getTexto() + " en " + subTitulo;

						PdfPCell cell = new PdfPCell(new Phrase (titulo + " " + texto, FontFactory.getFont("Arial", 10, Font.BOLD)));
						cell.setColspan(2);
						t.addCell(cell);
					}

					String pendienteStr = p.getComentario();
					if(p.getPlazo() != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						pendienteStr.concat(", antes de: " + sdf.format(p.getPlazo()));
					}
					PdfPCell cell5 = new PdfPCell(new Phrase(pendienteStr, FontFactory.getFont("Arial", 10)));
					cell5.setColspan(2);
					t.addCell(cell5);
				}
			}
		}
		return agregarTitulo;
	}


	/**
	 * 
	 * @param t
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
	public static void addReparaciones(PdfPTable t, Preventivo preventivo) {
		Chequeo chequeo = preventivo.getChequeo();
		addReparaciones(t, chequeo, "");

		if (preventivo.getActivo().getTipo() == 1){
			ChequeoSurtidor cs = new ChequeoSurtidorDao().get(chequeo.getId());
			for (Chequeo chequeoPico : cs.getListaDeChequeosPicos()){
				addReparaciones(t, chequeoPico, "Nro: " + ((ChequeoPico)chequeoPico).getPico().getNumeroEnLaEstacion());	
			}

			for (Chequeo chequeoProducto : cs.getListaDeProductos()){
				addReparaciones(t, chequeoProducto, ((ChequeoProducto)chequeoProducto).getProducto().getNombre());
			}
		}

		if (preventivo.tienePendientesVisibles(preventivo.getChequeo())){
			PdfPCell cell5 = new PdfPCell(new Phrase (" ", FontFactory.getFont("Arial", 10)));
			cell5.setColspan(2);
			t.addCell(cell5);
		}
	}


	private static int addReparaciones(PdfPTable t, Chequeo chequeo, String texto) {
		int numeroReparacion = 0;
		for(ItemChequeado ic : chequeo.getItemsChequeados()) {

			for(Corregido c : ic.getListaDeCorregidos()) {
				numeroReparacion ++;
				if (numeroReparacion == 1){

					String subTitulo = ic.getItemChequeo().getTextoTipoChequeo();
					if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Generico)){
						subTitulo = ic.getItemChequeo().getTipoActivoGenerico().getNombre();
					}
					String titulo = "Reparaciones de " + ic.getItemChequeo().getTexto() + " en " + subTitulo;

					/*try{
						PdfPRow row = (PdfPRow) t.getRows().get(t.getRows().size()-1);
						if (row.getCells()[1].getPhrase().getContent().equals("")){
							PdfPCell cell0 = new PdfPCell(new Phrase( "" ));
							t.addCell(cell0);
						}
					} catch (Exception e){
						//no importa
					}
					PdfPCell[] cells = new PdfPCell[2];
					
					t.getRows().add(new PdfPRow(cells));*/
					PdfPCell cell = new PdfPCell(new Phrase( titulo	+ " " + texto, FontFactory.getFont("Arial", 10, Font.BOLD)));
					cell.setColspan(2);
					t.addCell(cell);
				}

				PdfPCell cell2 = new PdfPCell(new Phrase ("Reparación " + numeroReparacion, FontFactory.getFont("Arial", 10)));
				cell2.setColspan(2);
				t.addCell(cell2);

				if(c.getFalla() != null) {
					PdfPCell cell3 = new PdfPCell(new Phrase ("Falla", FontFactory.getFont("Arial", 10)));
					t.addCell(cell3);

					PdfPCell cell4 = new PdfPCell(new Phrase (c.getFalla().getDescripcion(), FontFactory.getFont("Arial", 10)));
					t.addCell(cell4);
				}

				if(c.getTarea() != null) {
					PdfPCell cell3 = new PdfPCell(new Phrase ("Tarea", FontFactory.getFont("Arial", 10)));
					t.addCell(cell3);

					PdfPCell cell4 = new PdfPCell(new Phrase (c.getTarea().getDescripcion(), FontFactory.getFont("Arial", 10)));
					t.addCell(cell4);

				}

				if(c.getListaDeRepuestos() != null && !c.getListaDeRepuestos().isEmpty()) {
					PdfPCell cell3 = new PdfPCell(new Phrase ("Repuestos", FontFactory.getFont("Arial", 10)));
					t.addCell(cell3);

					boolean tieneRepuestos = false;
					for(RepuestoLineaCorregido rlc : c.getListaDeRepuestos()) {
						if (tieneRepuestos){ // NO es el primer repuesto
							PdfPCell cell = new PdfPCell(new Phrase (" ", FontFactory.getFont("Arial", 10)));
							t.addCell(cell);
						}
						tieneRepuestos = true;
						PdfPCell cell5 = new PdfPCell(new Phrase (rlc.getRepuesto().getDescripcion(), FontFactory.getFont("Arial", 10)));
						t.addCell(cell5);
					}

					if (!tieneRepuestos){
						PdfPCell cell5 = new PdfPCell(new Phrase (" Sin repuestos ", FontFactory.getFont("Arial", 10)));
						t.addCell(cell5);
					}
				}

				if(c.getComentario() != null && c.isComentarioVisible()) {
					PdfPCell cell5 = new PdfPCell(new Phrase ("Comentario", FontFactory.getFont("Arial", 10)));
					t.addCell(cell5);

					PdfPCell cell6 = new PdfPCell(new Phrase (c.getComentario(), FontFactory.getFont("Arial", 10)));
					t.addCell(cell6);
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
}