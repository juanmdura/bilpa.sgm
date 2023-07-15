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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

import app.client.dominio.ChequeoPico;
import app.client.dominio.ChequeoSurtidor;
import app.client.dominio.Organizacion;
import app.client.dominio.Preventivo;
import app.client.dominio.Surtidor;
import app.server.persistencia.ActivoDao;
import app.server.persistencia.ChequeoSurtidorDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.SurtidorDao;
import app.server.persistencia.VisitaDao;
import app.server.propiedades.PropiedadUrlLogo;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ControlVisitaReporteWebOperador extends ControlVisitaReporteWeb {

	private PropiedadUrlLogo propiedadLogo = new PropiedadUrlLogo();

	private static ControlVisitaReporteWebOperador instancia = null;

	public static ControlVisitaReporteWebOperador getInstancia() {
		if (instancia == null) {
			instancia = new ControlVisitaReporteWebOperador();
		}
		return instancia;
	}

	public static void setInstancia(ControlVisitaReporteWebOperador instancia) {
		ControlVisitaReporteWebOperador.instancia = instancia;
	}

	private ControlVisitaReporteWebOperador() {

	}
	
	public byte[] crearPDFPreventivosVisitasBytes(int idVisita, Organizacion organizacion) {

		try {
			File pdf = crearPDFPreventivosVisitas(idVisita, organizacion);
			return IOUtils.toByteArray(new FileInputStream(pdf));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
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
			writer = PdfWriter.getInstance(document, out);
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

			document.open();
			
			ArrayList<Preventivo> preventivos = new ArrayList<Preventivo>(visita.getListaDePreventivos());
			Collections.sort(preventivos);

			this.preventivos = preventivos;
			
			crearLogo(organizacion);
			crearCabezal();

			crearEspacioEnBlanco(1);
			
			crearTotalizadores();
			
			document.add(new Phrase(UtilVisitaReporteWebOperador.SECCION_MANTENIMIENTO_TITULO, FontFactory.getFont("Arial", 10, Font.BOLD)));
			boolean esElPrimero = true;
			for(int i=0; i < preventivos.size() ; i++) {
				Preventivo p = preventivos.get(i);
				if (p.tieneAlgunaData(organizacion, "R")) 
				{
					if (!esElPrimero){// si es el primer preventivo, NO agrego una pagina.
						document.add(Chunk.NEXTPAGE);
					}
					
					esElPrimero = false;
					float[] colsWidthFotoYTablas = {1f, 1f};
					PdfPTable t = new PdfPTable(colsWidthFotoYTablas);
					t.setWidthPercentage(100);
					
					PdfPCell cell = new PdfPCell(new Phrase(activoDao.get(p.getActivo().getId()).toStringLargo(), FontFactory.getFont("Arial", 10, Font.BOLD, new Color(0, 84, 159))));
					cell.setColspan(2);
					t.addCell(cell);
					
					UtilVisitaReporteWebOperador.addValoresPorManguera(t, p);
					UtilVisitaReporteWebOperador.addChequeo(t, p);
					UtilVisitaReporteWebOperador.addReparaciones(t, p);
					UtilVisitaReporteWebOperador.addPendientes(t, p);

					document.add(t);
				}
			}

			crearFirma();

		} catch (DocumentException e) {
			e.printStackTrace();
		} 
	}
	
	private void crearCabezal() throws DocumentException {
		PdfPCell cellCliente = new PdfPCell(new Phrase("Cliente ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		PdfPCell cellClienteVal = new PdfPCell(new Phrase(visita.getEstacion().getNombre(), FontFactory.getFont("Arial", 10, Font.BOLD)));
		
		PdfPCell cellFecha = new PdfPCell(new Phrase("Fecha de visita", FontFactory.getFont("Arial", 10)));
		PdfPCell cellFechaVal;
		if(visita.getFechaInicio() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			cellFechaVal = new PdfPCell(new Phrase(sdf.format(visita.getFechaInicio()), FontFactory.getFont("Arial", 10)));
		} else {
			cellFechaVal = new PdfPCell(new Phrase("-", FontFactory.getFont("Arial", 10)));
		}
		
		PdfPCell cellAgente = new PdfPCell(new Phrase("Agente de servicio", FontFactory.getFont("Arial", 10)));
		PdfPCell cellAgenteVal = new PdfPCell(new Phrase(visita.getTecnico().getNombre() + " " + visita.getTecnico().getApellido(), FontFactory.getFont("Arial", 10)));
		
		PdfPCell cellPeriodo = new PdfPCell(new Phrase("Preventivo corresponde a", FontFactory.getFont("Arial", 10)));
		PdfPCell cellPeriodoVal;
		if(visita.getFechaProximaVisita() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(visita.getFechaProximaVisita());
			String fechaPlanificada = app.server.UtilFechas.getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
			cellPeriodoVal = new PdfPCell(new Phrase(fechaPlanificada, FontFactory.getFont("Arial", 10)));
		} else {
			cellPeriodoVal = new PdfPCell(new Phrase("-", FontFactory.getFont("Arial", 10)));
		}
		
		float[] colsWidthFotoYTablas = {1f, 1f};
		PdfPTable t = new PdfPTable(2);
		t.setWidths(colsWidthFotoYTablas);
		t.setWidthPercentage(100);
		
		t.addCell(cellCliente);
		t.addCell(cellClienteVal);
		
		t.addCell(cellFecha);
		t.addCell(cellFechaVal);
		
		t.addCell(cellAgente);
		t.addCell(cellAgenteVal);
		
		t.addCell(cellPeriodo);
		t.addCell(cellPeriodoVal);
		
		crearEspacioEnBlanco(1);
		document.add(t);
		
	}
	
	private void crearTotalizadores() throws DocumentException {
		boolean agregarTitulo = true;
		for(Preventivo preventivo : preventivos) {
			if(preventivo.getActivo().getTipo() == 1) {
				if(preventivo.getChequeo() != null) {
					ChequeoSurtidorDao csd = new ChequeoSurtidorDao();
					ChequeoSurtidor chequeoSurtidor = csd.get(preventivo.getChequeo().getId());
					
					if (!chequeoSurtidor.getListaDeChequeosPicos().isEmpty()){
						SurtidorDao sd = new SurtidorDao();
						Surtidor s = sd.get(preventivo.getActivo().getId());

						float[] colsWidthFotoYTablas = {1f, 1f};
						PdfPTable t = new PdfPTable(colsWidthFotoYTablas);
						t.setWidthPercentage(100);
						PdfPCell cell = new PdfPCell(new Phrase(s.toStringLargo(), FontFactory.getFont("Arial", 10, Font.BOLD, new Color(0, 84, 159))));
						cell.setColspan(2);
						t.addCell(cell);
						
						t.addCell(new PdfPCell(new Phrase ("Manguera", FontFactory.getFont("Arial", 10, Font.BOLD))));
						t.addCell(new PdfPCell(new Phrase ("Litros despachados", FontFactory.getFont("Arial", 10, Font.BOLD))));
						
						boolean tieneTotalizadores = false;
						List<ChequeoPico> chequeosPico = new ArrayList<ChequeoPico>();
						chequeosPico.addAll(chequeoSurtidor.getListaDeChequeosPicos());
						Collections.sort(chequeosPico);
						
						for (int i =0; i < chequeosPico.size(); i++){
							ChequeoPico cp = chequeosPico.get(i);

							if (cp.tieneTotalizador()) {
								tieneTotalizadores = true;

								t.addCell(new PdfPCell(new Phrase ("Manguera número " + cp.getPico().getNumeroEnLaEstacion(), FontFactory.getFont("Arial", 10))));

								double valor = cp.getTotalizadorElectronicoFinal() - cp.getTotalizadorElectronicoInicial();
								String pattern = "#.##";
								DecimalFormat decimalFormat = new DecimalFormat(pattern);
								String valorFormatted = decimalFormat.format(valor);

								t.addCell(new PdfPCell(new Phrase (valorFormatted, FontFactory.getFont("Arial", 10))));
							}
						}
						if (tieneTotalizadores){
							if (agregarTitulo){
								document.add(new Phrase("\n \n" + UtilVisitaReporteWebOperador.SECCION_DESPACHO_TITULO, FontFactory.getFont("Arial", 10, Font.BOLD)));
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
