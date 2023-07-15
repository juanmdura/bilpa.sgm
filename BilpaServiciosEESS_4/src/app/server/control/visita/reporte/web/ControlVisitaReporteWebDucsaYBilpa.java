package app.server.control.visita.reporte.web;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.client.dominio.ActivoGenerico;
import app.client.dominio.Chequeo;
import app.client.dominio.ChequeoPico;
import app.client.dominio.ChequeoProducto;
import app.client.dominio.ChequeoSurtidor;
import app.client.dominio.ChequeoTanque;
import app.client.dominio.Corregido;
import app.client.dominio.ItemChequeado;
import app.client.dominio.Organizacion;
import app.client.dominio.Pendiente;
import app.client.dominio.Preventivo;
import app.client.dominio.RepuestoLineaCorregido;
import app.client.dominio.Surtidor;
import app.client.dominio.TipoChequeo;
import app.client.dominio.TipoDescarga;
import app.server.control.visita.ControlVisita;
import app.server.persistencia.ActivoDao;
import app.server.persistencia.ChequeoSurtidorDao;
import app.server.persistencia.ChequeoTanqueDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.SurtidorDao;
import app.server.persistencia.TipoActivoGenericoDao;
import app.server.persistencia.TipoDescargaDao;
import app.server.persistencia.VisitaDao;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ControlVisitaReporteWebDucsaYBilpa extends ControlVisitaReporteWeb {

	private static final String BUEN_ESTADO = "B";
	private static final String CORREGIDOS = "R";
	private static final String PENDIENTES = "P";

	private static ControlVisitaReporteWebDucsaYBilpa instancia = null;

	public static ControlVisitaReporteWebDucsaYBilpa getInstancia() {
		if (instancia == null) {
			instancia = new ControlVisitaReporteWebDucsaYBilpa();
		}
		return instancia;
	}

	public static void setInstancia(ControlVisitaReporteWebDucsaYBilpa instancia) {
		ControlVisitaReporteWebDucsaYBilpa.instancia = instancia;
	}

	private ControlVisitaReporteWebDucsaYBilpa() {

	}

	public byte[] crearPDFPreventivosVisitasBytes(int idVisita, Organizacion organizacion) {

		DaoTransaction tx = new DaoTransaction();
		File file;
		try {
			tx.begin();
			VisitaDao dao = new VisitaDao();

			this.visita = dao.get(idVisita);
			this.document = new Document(PageSize.A4, 50, 50, 50, 20);
			this.organizacion = organizacion;

			file = File.createTempFile("bilpa_mantenimiento_preventivo", ".pdf");
			FileOutputStream out =  new FileOutputStream(file);
			writer = PdfWriter.getInstance(document, out);
			FileInputStream fis = new FileInputStream(file);

			crearPDFPreventivosVisitas(organizacion, fis, out);
			byte[] b = new byte[(int) file.length()];
			fis.read(b);
			return b;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			document.close();
			tx.close();
		}
	}

	public File crearPDFPreventivosVisitas(int idVisita, Organizacion organizacion) {

		DaoTransaction tx = new DaoTransaction();
		File file;
		try {
			tx.begin();
			VisitaDao dao = new VisitaDao();

			this.visita = dao.get(idVisita);
			this.organizacion = organizacion;
			this.document = new Document(PageSize.A4, 50, 50, 50, 20);

			file = File.createTempFile("bilpa_mantenimiento_preventivo", ".pdf");
			FileOutputStream out =  new FileOutputStream(file);
			PdfWriter.getInstance(document, out);
			crearPDFPreventivosVisitas(organizacion, new FileInputStream(file), out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			document.close();
			tx.close();
		}
		return  file;
	}

	private void crearPDFPreventivosVisitas(Organizacion organizacion, FileInputStream fileInputStream, OutputStream out) throws MalformedURLException, IOException {

		try {
			ActivoDao activoDao = new ActivoDao();
			crearLogo(organizacion);

			crearEspacioEnBlanco(1);
			crearTablaDatosBilpaYCliente();

			crearTotalizadores();
			
			for(Preventivo p : visita.getListaDePreventivos()) {
				if (p.tieneAlgunaData(organizacion, ""))
				{
					//document.newPage();
					String nombre = activoDao.get(p.getActivo().getId()).toStringLargo();
					document.add(new Phrase(" " + nombre, FontFactory.getFont("Arial", 10, Font.BOLD)));
					crearEspacioEnBlanco(1);

					crearChequeos(activoDao, p);
				}
			}
			//crearFirma();

		} catch (DocumentException e) {
			e.printStackTrace();
		} 
	}

	private void crearReparaciones(ItemChequeado ic) throws DocumentException, MalformedURLException, IOException {
		float[] colsWidth = {2f, 3.5f}; // Code 1
		PdfPTable t = new PdfPTable(colsWidth);

		t.setHorizontalAlignment(Element.ALIGN_LEFT);
		t.setWidthPercentage(100);

		int numeroReparacion = 0;
		for(Corregido c : ic.getListaDeCorregidos()) {
			numeroReparacion ++;
			if (numeroReparacion == 1){
				crearEspacioEnBlanco(1);

				String subTitulo = ic.getItemChequeo().getTextoTipoChequeo();
				if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Generico)){
					subTitulo = ic.getItemChequeo().getTipoActivoGenerico().getNombre();
				}
				String titulo = "Reparaciones de " + ic.getItemChequeo().getTexto() + " en " + subTitulo;
				agregarTitulo(titulo);
				
				String subTitulo2 = "";
				subTitulo2 = ControlVisita.agregarComentarios(ic, subTitulo2, organizacion.equals(Organizacion.Bilpa));
				if (!subTitulo2.equals("")){
					agregarSubTitulo(subTitulo2);
				}
			}

			PdfPCell cell1 = new PdfPCell(new Phrase(" Reparación " + numeroReparacion,  FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
			t.addCell(cell1);
			t.addCell(cellEnBlanco());

			if(c.getFalla() != null) {
				PdfPCell cell2 = new PdfPCell(new Phrase(" Falla ",  FontFactory.getFont("Arial", 6, Font.BOLD)));
				t.addCell(cell2);

				PdfPCell cell1Data = new PdfPCell(new Phrase(" " + c.getFalla().getDescripcion(),  FontFactory.getFont("Arial", 6)));
				t.addCell(cell1Data);

			}

			if(c.getTarea() != null) {
				PdfPCell cell2 = new PdfPCell(new Phrase(" Tarea ",  FontFactory.getFont("Arial", 6, Font.BOLD)));
				t.addCell(cell2);

				PdfPCell cell1Data = new PdfPCell(new Phrase(" " + c.getTarea().getDescripcion(),  FontFactory.getFont("Arial", 6)));
				t.addCell(cell1Data);

			}

			if(c.getListaDeRepuestos() != null && !c.getListaDeRepuestos().isEmpty()) {
				PdfPCell cell2 = new PdfPCell(new Phrase(" Repuestos ",  FontFactory.getFont("Arial", 6, Font.BOLD)));
				t.addCell(cell2);
				t.addCell("");

				boolean valorSeteado = false;
				for(RepuestoLineaCorregido rlc : c.getListaDeRepuestos()) {

					if(valorSeteado) {
						PdfPCell cell1Data = new PdfPCell(new Phrase(" " + rlc.getRepuesto().getDescripcion(),  FontFactory.getFont("Arial", 6)));
						t.addCell("");
						t.addCell(cell1Data);
					} else {
						PdfPCell cell1Data = new PdfPCell(new Phrase(" " + rlc.getRepuesto().getDescripcion(),  FontFactory.getFont("Arial", 6)));
						t.addCell("");
						t.addCell(cell1Data);
						valorSeteado = true;
					}
				}

			}
			if(c.getComentario() != null && (c.isComentarioVisible() || organizacion.equals(Organizacion.Bilpa))) {
				PdfPCell cell2 = new PdfPCell(new Phrase(" Comentario",  FontFactory.getFont("Arial", 6, Font.BOLD)));
				t.addCell(cell2);

				PdfPCell cell1Data = new PdfPCell(new Phrase(" " + c.getComentario(),  FontFactory.getFont("Arial", 6)));
				t.addCell(cell1Data);

			}

			if (c.getFoto() != null){
				PdfPCell cellFoto = new PdfPCell(new Phrase(" Antes de la reparación",  FontFactory.getFont("Arial", 6, Font.BOLD)));
				try{
					Image foto = Image.getInstance(c.getFoto().getPath());
					foto.scaleAbsolute(100, 100);
					
					PdfPCell cellFotoData = new PdfPCell(foto);
					t.addCell(cellFoto);
					t.addCell(cellFotoData);
					
				} catch (Exception e){
					System.out.println("No se encontro la foto: " + c.getFoto().getPath());
				}
			}

			if (c.getFoto2() != null){
				PdfPCell cellFoto2 = new PdfPCell(new Phrase(" Después de la reparación",  FontFactory.getFont("Arial", 6, Font.BOLD)));
				try{
					Image foto2 = Image.getInstance(c.getFoto2().getPath());
					foto2.scaleAbsolute(100, 100);
					PdfPCell cellFoto2Data = new PdfPCell(foto2);
					t.addCell(cellFoto2);
					t.addCell(cellFoto2Data);
					
				} catch (Exception e){
					System.out.println("No se encontro la foto: " + c.getFoto2().getPath());
				}
				
			}

		}
		document.add(t);
	}

	private void agregarTitulo(String titulo) throws DocumentException {
		Paragraph paragraph = new Paragraph();
		paragraph.setAlignment(Element.ALIGN_LEFT);
		PdfPTable t2 = new PdfPTable(1);
		t2.setWidthPercentage(100);
		PdfPCell cell = new PdfPCell(new PdfPCell(new Phrase(titulo + "\n", FontFactory.getFont("Arial", 8, Font.BOLD))));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		t2.addCell(cell);
		paragraph.add(t2);

		document.add(t2);
	}
	
	private void agregarSubTitulo(String titulo) throws DocumentException {
		Paragraph paragraph = new Paragraph();
		paragraph.setAlignment(Element.ALIGN_LEFT);
		PdfPTable t2 = new PdfPTable(1);
		t2.setWidthPercentage(100);
		PdfPCell cell = new PdfPCell(new PdfPCell(new Phrase(titulo + "\n", FontFactory.getFont("Arial", 8, Font.BOLD,  new Color(96, 96, 96)))));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		t2.addCell(cell);
		paragraph.add(t2);

		document.add(t2);
	}

	private void crearPendientes(ItemChequeado ic) throws DocumentException, MalformedURLException, IOException {

		float[] colsWidth = {2f, 3.5f}; // Code 1
		PdfPTable t = new PdfPTable(colsWidth);

		t.setHorizontalAlignment(Element.ALIGN_LEFT);
		t.setWidthPercentage(100);

		boolean agregarTitulo = true;
		int contPendientes = 0;

		for(Pendiente pendiente : ic.getListaDePendientes()) {
			if ((pendiente.isComentarioVisible() && organizacion.equals(Organizacion.Operador)) || organizacion.equals(Organizacion.Bilpa)){
				contPendientes++;
				if (agregarTitulo) {

					agregarTitulo = false;
					crearEspacioEnBlanco(1);

					String subTitulo = ic.getItemChequeo().getTextoTipoChequeo();
					if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Generico)){
						subTitulo = ic.getItemChequeo().getTipoActivoGenerico().getNombre();
					}
					String titulo = "Pendientes de " + ic.getItemChequeo().getTexto() + " en " + subTitulo;
					
					agregarTitulo(titulo);
					
					String subTitulo2 = "";
					subTitulo2 = ControlVisita.agregarComentarios(ic, subTitulo2, organizacion.equals(Organizacion.Bilpa));
					
					if (!subTitulo2.equals("")){
						agregarSubTitulo(subTitulo2);
					}

				} else {
				}

				PdfPCell cell1 = new PdfPCell(new Phrase(" Pendiente " + contPendientes,  FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
				t.addCell(cell1);
				t.addCell(cellEnBlanco());

				PdfPCell cell2 = new PdfPCell(new Phrase(" Comentario ",  FontFactory.getFont("Arial", 6, Font.BOLD)));
				PdfPCell cell2Data = new PdfPCell(new Phrase(pendiente.getComentario(),  FontFactory.getFont("Arial", 6)));
				t.addCell(cell2);
				t.addCell(cell2Data);

				PdfPCell cell3 = new PdfPCell(new Phrase(" Plazo",  FontFactory.getFont("Arial", 6, Font.BOLD)));
				t.addCell(cell3);
				if(pendiente.getPlazo() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					PdfPCell cell3Data = new PdfPCell(new Phrase(" " + sdf.format(pendiente.getPlazo()),  FontFactory.getFont("Arial", 6)));
					t.addCell(cell3Data);
				} else {
					t.addCell(" - ");
				}

				try{
					
					if (pendiente.getFoto() != null){
						PdfPCell cellFoto = new PdfPCell(new Phrase(" Foto del pendiente",  FontFactory.getFont("Arial", 6, Font.BOLD)));
						Image foto = Image.getInstance(pendiente.getFoto().getPath());
						//foto.scaleAbsolute(100, 100);
						foto.scalePercent(20, 20);
						
						PdfPCell cellFotoData = new PdfPCell(foto);
						t.addCell(cellFoto);
						t.addCell(cellFotoData);
					}
				} catch (Exception e){
					System.out.println("No se encontro la foto: " + pendiente.getFoto().getPath());
				}
			}
		}
		document.add(t);
	}

	private void crearChequeos(ActivoDao activoDao, Preventivo p) throws DocumentException, MalformedURLException, IOException {
		Map<String, List<ItemChequeado>> itemsChequeadosClasificados = new HashMap<String, List<ItemChequeado>>();
		if (p.getChequeo() != null ) {
			cargarItemsChequeados(p.getChequeo(), itemsChequeadosClasificados);

			if (p.getActivo().getTipo() == 1){
				ChequeoSurtidor cs = new ChequeoSurtidorDao().get(p.getChequeo().getId());
				for (Chequeo chequeoPico : cs.getListaDeChequeosPicos()){
					cargarItemsChequeados(chequeoPico, itemsChequeadosClasificados);	
				}

				for (Chequeo chequeoProducto : cs.getListaDeProductos()){
					cargarItemsChequeados(chequeoProducto, itemsChequeadosClasificados);
				}
			}
			crearTablas(itemsChequeadosClasificados, p);
		}
	}

	private void cargarItemsChequeados(Chequeo chequeo, Map<String, List<ItemChequeado>> itemsChequeadosClasificados) {
		for(ItemChequeado ic : chequeo.getItemsChequeados()) {
			if (ic.getValor() != null && (ic.getValor().equals(BUEN_ESTADO) || ic.getValor().equals(CORREGIDOS))){
				if(itemsChequeadosClasificados.containsKey(ic.getValor())) {
					itemsChequeadosClasificados.get(ic.getValor()).add(ic);
				} else {
					itemsChequeadosClasificados.put(ic.getValor(), new ArrayList<ItemChequeado>());
					itemsChequeadosClasificados.get(ic.getValor()).add(ic);
				}
			} 
			if (ic.isPendiente()){//cuando esta pendiente, el valor viene vacio
				if(itemsChequeadosClasificados.containsKey("P")) {
					itemsChequeadosClasificados.get("P").add(ic);
				} else {
					itemsChequeadosClasificados.put("P", new ArrayList<ItemChequeado>());
					itemsChequeadosClasificados.get("P").add(ic);
				}
			}
		}
	}

	private void crearTablas(Map<String, List<ItemChequeado>> itemsChequeadosClasificados, Preventivo p) throws DocumentException, MalformedURLException, IOException {
		if(p.getActivo().getTipo() == 1 ) {
			if(itemsChequeadosClasificados.get(BUEN_ESTADO) != null) {
				agregarTitulo("Chequeos en buen estado");
				crearTablaChequeoSurtidorProductoPicoBuenEstado(p.getChequeo().getId(), itemsChequeadosClasificados.get(BUEN_ESTADO));
			}
		} else if(itemsChequeadosClasificados.get(BUEN_ESTADO) != null) {
			String titulo = "";
			if (p.getActivo().getTipo() == 2) {
				crearChequeoGeneralTanque(p);

				titulo = " En el tanque";

			} else if (p.getActivo().getTipo() == 4){
				titulo = " En la bomba";
			
			} else if (p.getActivo().getTipo() == 6){
				ActivoGenerico ag = (ActivoGenerico)p.getActivo();
				
				titulo = " En " + ag.getTipoActivoGenerico().getNombre();
			}

			agregarTitulo("Chequeos en buen estado");
			crearTablaChequeoBuenEstado(itemsChequeadosClasificados.get(BUEN_ESTADO), titulo);
		}

		if(itemsChequeadosClasificados.get(CORREGIDOS) != null) {
			crearTablaChequeoCorregidos(itemsChequeadosClasificados.get(CORREGIDOS));
		}
		if(itemsChequeadosClasificados.get(PENDIENTES) != null) {
			crearTablaChequeoPendientes(itemsChequeadosClasificados.get(PENDIENTES));
		}
	}

	private void crearTablaChequeoCorregidos(List<ItemChequeado> itemsChequeados) throws DocumentException, MalformedURLException, IOException {
		for(ItemChequeado ic : itemsChequeados) {
			crearReparaciones(ic);
		}
	}

	private void crearTablaChequeoSurtidorProductoPicoBuenEstado(int idChequeo, List<ItemChequeado> itemsChequeados) throws DocumentException {
		ChequeoSurtidor chequeoSurtidor = new ChequeoSurtidorDao().get(idChequeo);
		ArrayList<PdfPTable> tablasChequeoSurtidor = new ArrayList<PdfPTable>();

		crearTablaChequeoSurtidor(tablasChequeoSurtidor, itemsChequeados);
		crearTablaChequeoProducto(tablasChequeoSurtidor, chequeoSurtidor.getListaDeProductos());
		crearTablaChequeoPico(tablasChequeoSurtidor, chequeoSurtidor.getListaDeChequeosPicos());

		float[] colsWidth = {3f, 3f, 3f};
		PdfPTable table = new PdfPTable(colsWidth);
		table.setWidthPercentage(100);

		for(PdfPTable t : tablasChequeoSurtidor){
			PdfPCell cell = new PdfPCell(t);
			cell.setPadding(4);
			cell.setBorder(0);
			table.addCell(cell);
		}
		table.getDefaultCell().setBorder(0);
		table.completeRow();
		document.add(table);
	}

	private void crearTablaChequeoSurtidor(ArrayList<PdfPTable> tablasChequeoSurtidor, List<ItemChequeado> itemsChequeados) throws DocumentException {
		boolean esElPrimero = true;
		PdfPTable t = getTablaChequeoSurtidor();
		for(ItemChequeado ic : itemsChequeados) {
			if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Surtidor)){
				if (esElPrimero){
					esElPrimero = false;
					tablasChequeoSurtidor.add(t);
					PdfPCell cell1 = new PdfPCell(new Phrase(" En el surtidor",  FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
					t.addCell(cell1);
				}

				String texto = ic.getItemChequeo().getTexto();
				texto = ControlVisita.agregarComentarios(ic, texto, organizacion.equals(Organizacion.Bilpa));
				
				PdfPCell cell1Data = new PdfPCell(new Phrase(" " + texto,  FontFactory.getFont("Arial", 6)));
				t.addCell(cell1Data);
			}
		}
	}

	private void crearTablaChequeoProducto(ArrayList<PdfPTable> tablasChequeoSurtidor, Set<ChequeoProducto> productos) throws DocumentException {
		for(ChequeoProducto cp : productos) {
			boolean esElPrimero = true;
			for(ItemChequeado ic : cp.getItemsChequeados()) {
				if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Producto)){
					PdfPTable t = getTablaChequeoSurtidor();
					if (esElPrimero){
						esElPrimero = false;
						tablasChequeoSurtidor.add(t);
						t.addCell(cellEnBlanco());

						PdfPCell cell1 = new PdfPCell(new Phrase(" En el producto " + cp.getProducto().getNombre(),  FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
						t.addCell(cell1);
					}

					if(ic.getValor() != null && ic.getValor().equalsIgnoreCase(BUEN_ESTADO)) {
						String texto = ic.getItemChequeo().getTexto();
						texto = ControlVisita.agregarComentarios(ic, texto, organizacion.equals(Organizacion.Bilpa));
						
						PdfPCell cell1Data = new PdfPCell(new Phrase(" " + texto,  FontFactory.getFont("Arial", 6)));
						t.addCell(cell1Data);
					}
				}
			}
		}
	}

	private void crearTablaChequeoPico(ArrayList<PdfPTable> tablasChequeoSurtidor, Set<ChequeoPico> picos) throws DocumentException {
		for(ChequeoPico cp : picos) {
			boolean esElPrimero = true;
			PdfPTable t = getTablaChequeoSurtidor();
			for(ItemChequeado ic : cp.getItemsChequeados()) {
				if (ic.getItemChequeo().getTipoChequeo().equals(TipoChequeo.Pico)){
					if (esElPrimero){
						esElPrimero = false;
						tablasChequeoSurtidor.add(t);
						t.addCell(cellEnBlanco());

						PdfPCell cell1 = new PdfPCell(new Phrase(" En la manguera número " + (cp.getPico().getNumeroEnLaEstacion() == 0 ? " - " : cp.getPico().getNumeroEnLaEstacion()),  FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
						t.addCell(cell1);
					}
					if(ic.getValor() != null && ic.getValor().equalsIgnoreCase(BUEN_ESTADO)) {
						String texto = ic.getItemChequeo().getTexto();
						texto = ControlVisita.agregarComentarios(ic, texto, organizacion.equals(Organizacion.Bilpa));
						
						PdfPCell cell1Data = new PdfPCell(new Phrase(" " + texto,  FontFactory.getFont("Arial", 6)));
						t.addCell(cell1Data);
					}
				}
			}
			t.addCell(cellEnBlanco());
		}
	}

	private void crearTablaChequeoBuenEstado(List<ItemChequeado> itemsChequeados, String texto) throws DocumentException {
		float[] colsWidth = {2f}; // Code 1
		PdfPTable t = new PdfPTable(colsWidth);

		t.setHorizontalAlignment(Element.ALIGN_LEFT);
		t.setWidthPercentage(50);

		PdfPCell cell1 = new PdfPCell(new Phrase(texto,  FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
		t.addCell(cell1);
		t.addCell(cellEnBlanco());

		for(ItemChequeado ic : itemsChequeados) {
			String texto2 = ic.getItemChequeo().getTexto();
			texto2 = ControlVisita.agregarComentarios(ic, texto2, organizacion.equals(Organizacion.Bilpa));
			
			PdfPCell cell1Data = new PdfPCell(new Phrase(" " + texto2,  FontFactory.getFont("Arial", 6)));
			t.addCell(cell1Data);
		}
		
		document.add(t);
	}

	private void crearTablaChequeoPendientes(List<ItemChequeado> itemsChequeados) throws DocumentException, MalformedURLException, IOException {
		for(ItemChequeado ic : itemsChequeados) {
			crearPendientes(ic);
		}
	}

	//TODO INVOCAR
	private void crearChequeoGeneralTanque(Preventivo p) throws DocumentException {
		if (p.getActivo().getTipo() == 2){
			float[] colsWidth = {1f, 2f};
			PdfPTable t = new PdfPTable(colsWidth);
			t.setHorizontalAlignment(Element.ALIGN_LEFT);
			t.setWidthPercentage(50);

			ChequeoTanqueDao ctd = new ChequeoTanqueDao();
			ChequeoTanque ct = ctd.get(p.getChequeo().getId());

			TipoDescargaDao tiposDescargaDao = new TipoDescargaDao();
			TipoDescarga tipoDescarga = tiposDescargaDao.get(ct.getTipoDeDescarga());
			boolean tieneData = false;

			if (tipoDescarga != null){
				tieneData = true;
				PdfPCell cell5 = new PdfPCell(new Phrase(" Tipo descarga ", FontFactory.getFont("Arial", 6, Font.BOLD)));
				t.addCell(cell5);

				PdfPCell cell6 = new PdfPCell(new Phrase(tipoDescarga.getNombre(), FontFactory.getFont("Arial", 6)));
				t.addCell(cell6);
			}

			if (ct.getMedidaDelAgua() > 0){
				tieneData = true;
				PdfPCell cell5 = new PdfPCell(new Phrase(" Medida del agua (cm)", FontFactory.getFont("Arial", 6, Font.BOLD)));
				t.addCell(cell5);

				PdfPCell cell6 = new PdfPCell(new Phrase(ct.getMedidaDelAgua() + "", FontFactory.getFont("Arial", 6)));
				t.addCell(cell6);
			}

			if (tieneData){
				document.add(t);
				document.add(Chunk.NEWLINE);
			}
		}
	}

	private PdfPTable getTablaChequeoSurtidor() {
		float[] colsWidth = {2f};		
		PdfPTable t = new PdfPTable(colsWidth);
		t.setWidthPercentage(33);

		return t;
	}

	private void crearTablaDatosBilpaYCliente() throws DocumentException, MalformedURLException, IOException {

		PdfPTable t = new PdfPTable(5);
		float[] colsWidth = {2f, 3.5f, 0.5f, 2f, 3.5f}; // Code 1
		t.setWidths(colsWidth);

		t.setHorizontalAlignment(Element.ALIGN_LEFT);
		t.setWidthPercentage(100);

		PdfPCell cell5 = new PdfPCell(new Phrase(" Estación " + visita.getEstacion().getNombre(),  FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
		PdfPCell cell6 = new PdfPCell(new Phrase(" Dirección",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell7 = new PdfPCell(new Phrase(" Teléfono",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell8 = new PdfPCell(new Phrase(" Rut",  FontFactory.getFont("Arial", 6, Font.BOLD)));

		PdfPCell cell6Data = new PdfPCell(new Phrase(" " + visita.getEstacion().getDireccion() + " | " + visita.getEstacion().getLocalidad(),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell7Data = new PdfPCell(new Phrase(" " + visita.getEstacion().getTelefono(),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell8Data = new PdfPCell(new Phrase(" " + visita.getEstacion().getRut(),  FontFactory.getFont("Arial", 6)));


		PdfPCell cell1 = new PdfPCell(new Phrase(" Visita",  FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
		PdfPCell cell2 = new PdfPCell(new Phrase(" Agente de servicio",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell3 = new PdfPCell(new Phrase(" Fecha y hora de inicio",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell4 = new PdfPCell(new Phrase(" Fecha y hora de fin",  FontFactory.getFont("Arial", 6, Font.BOLD)));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
		PdfPCell cell2Data = new PdfPCell(new Phrase(" " + visita.getTecnico().getNombre() + " " + visita.getTecnico().getApellido(),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell3Data = new PdfPCell(new Phrase(" " + sdf.format(visita.getFechaInicio()),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell4Data;
		if(visita.getFechaFin() != null) {
			cell4Data = new PdfPCell(new Phrase(" " + sdf.format(visita.getFechaFin()),  FontFactory.getFont("Arial", 6)));
		} else {
			cell4Data = new PdfPCell(new Phrase(" ",  FontFactory.getFont("Arial", 6)));
		}

		t.addCell(cell5);
		t.addCell(cellEnBlanco());
		t.addCell(cellEnBlanco());
		t.addCell(cell1);
		t.addCell(cellEnBlanco());

		t.addCell(cell6);
		t.addCell(cell6Data);
		t.addCell(cellEnBlanco());
		t.addCell(cell2);
		t.addCell(cell2Data);	

		t.addCell(cell7);
		t.addCell(cell7Data);
		t.addCell(cellEnBlanco());
		t.addCell(cell3);
		t.addCell(cell3Data);

		t.addCell(cell8);
		t.addCell(cell8Data);
		t.addCell(cellEnBlanco());
		t.addCell(cell4);
		t.addCell(cell4Data);

		document.add(t);
	}
	
	private void crearTotalizadores() throws DocumentException {
		boolean agregarTitulo = true;
		for(Preventivo preventivo : visita.getListaDePreventivos()) {
			if(preventivo.getActivo().getTipo() == 1) {
				if(preventivo.getChequeo() != null) {
					ChequeoSurtidorDao csd = new ChequeoSurtidorDao();
					ChequeoSurtidor chequeoSurtidor = csd.get(preventivo.getChequeo().getId());
					
					if (!chequeoSurtidor.getListaDeChequeosPicos().isEmpty()){
						SurtidorDao sd = new SurtidorDao();
						Surtidor s = sd.get(preventivo.getActivo().getId());

						float[] colsWidthFotoYTablas = {1f, 1f, 1f};
						PdfPTable t = new PdfPTable(colsWidthFotoYTablas);
						t.setWidthPercentage(100);
						PdfPCell cell = new PdfPCell(new Phrase(s.toStringLargo(), FontFactory.getFont("Arial", 10, Font.BOLD, new Color(0, 84, 159))));
						cell.setColspan(3);
						t.addCell(cell);
						
						t.addCell(new PdfPCell(new Phrase ("Manguera", FontFactory.getFont("Arial", 10, Font.BOLD))));
						t.addCell(new PdfPCell(new Phrase ("Totalizador electrónico final", FontFactory.getFont("Arial", 10, Font.BOLD))));
						t.addCell(new PdfPCell(new Phrase ("Totalizador mecánico final", FontFactory.getFont("Arial", 10, Font.BOLD))));
						
						boolean tieneTotalizadores = false;
						List<ChequeoPico> chequeosPico = new ArrayList<ChequeoPico>();
						chequeosPico.addAll(chequeoSurtidor.getListaDeChequeosPicos());
						Collections.sort(chequeosPico);
						
						for (int i =0; i < chequeosPico.size(); i++){
							ChequeoPico cp = chequeosPico.get(i);

							if (cp.tieneTotalizadorMecoanicoOElectronico()) {
								tieneTotalizadores = true;
								t.addCell(new PdfPCell(new Phrase ("Manguera número " + cp.getPico().getNumeroEnLaEstacion(), FontFactory.getFont("Arial", 10))));

								if (cp.tieneTotalizadorElectronico()){
									t.addCell(new PdfPCell(new Phrase (cp.getTotalizadorElectronicoFinalFormatted(), FontFactory.getFont("Arial", 10))));
								}
								
								if (cp.tieneTotalizadorMecanico()){
									t.addCell(new PdfPCell(new Phrase (cp.getTotalizadorMecanicoFinalFormatted(), FontFactory.getFont("Arial", 10))));
								}
							}
						}
						if (tieneTotalizadores){
							if (agregarTitulo){
								document.add(new Phrase("\n \n" + "Totalizadores por manguera", FontFactory.getFont("Arial", 10, Font.BOLD)));
								agregarTitulo = false;
							}
							
							document.add(t);
							document.add(Chunk.NEWLINE);
						} 
					}
				}
			}
		}
		document.add(Chunk.NEXTPAGE);
	}
}
