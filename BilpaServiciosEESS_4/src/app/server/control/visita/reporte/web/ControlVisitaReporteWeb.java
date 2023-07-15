package app.server.control.visita.reporte.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import app.client.dominio.ComentarioChequeo;
import app.client.dominio.ItemChequeado;
import app.client.dominio.Organizacion;
import app.client.dominio.Preventivo;
import app.client.dominio.Visita;
import app.server.persistencia.ComentarioChequeoDao;
import app.server.propiedades.PropiedadUrlLogo;

public abstract class ControlVisitaReporteWeb {

	protected PropiedadUrlLogo propiedadLogo = new PropiedadUrlLogo();

	protected Document document;
	protected ArrayList<Preventivo> preventivos;
	protected Visita visita;
	protected Organizacion organizacion;
	protected PdfWriter writer;
	
	protected void crearLogo(Organizacion organizacion) throws DocumentException, MalformedURLException, IOException {

		Image foto = Image.getInstance(propiedadLogo.getURLPropertie());
		foto.scaleToFit(100, 100);
		foto.setAlignment(Element.ALIGN_LEFT);
		
		float[] colsWidthFotoYTablas = {1f, 2f}; // Code 1
		PdfPTable tablaFotoYTablas = new PdfPTable(2);
		tablaFotoYTablas.setWidths(colsWidthFotoYTablas);
		tablaFotoYTablas.setWidthPercentage(100);

		PdfPCell cellFoto = new PdfPCell(foto);
		cellFoto.setBorderWidth(0);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(visita.getFechaProximaVisita());
		String periodo = (app.server.UtilFechas.getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR));

		PdfPTable pTitulo = new PdfPTable(1);
		//pTitulo.setAlignment(Element.ALIGN_CENTER);
		PdfPCell cell1 = new PdfPCell(new Phrase(" Mantenimiento preventivo \n", FontFactory.getFont("Arial", 11, Font.BOLD)));
		cell1.setBorder(0);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTitulo.addCell(cell1);

		PdfPCell cell2 = new PdfPCell(new Phrase(visita.getEstacion().getNombre(), FontFactory.getFont("Arial", 11, Font.BOLD)));
		cell2.setBorder(0);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTitulo.addCell(cell2);

		PdfPCell cell3 = new PdfPCell(new Phrase(periodo, FontFactory.getFont("Arial", 11	, Font.BOLD)));
		cell3.setBorder(0);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTitulo.addCell(cell3);

		PdfPCell cellTitulo = new PdfPCell(pTitulo);
		cellTitulo.setBorderWidth(0);
		cellTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);

		tablaFotoYTablas.addCell(cellFoto);	
		tablaFotoYTablas.addCell(cellTitulo);	

		Paragraph paragFotoYTablas = new Paragraph();
		paragFotoYTablas.add(tablaFotoYTablas);

		HeaderFooter header = new HeaderFooter(paragFotoYTablas, false);
		header.setBorder(0);
		header.setAlignment(Element.ALIGN_RIGHT);	

		
		Paragraph paragFooter = new Paragraph();

		paragFooter.add(new Phrase(" Bilpa S.A. | Valladolid 3525 | Montevideo | +598 2211 20 20 | Rut 21126170018 | service.estaciones@bilpa.com.uy                                                                 Vía " + organizacion.toString(),  FontFactory.getFont("Arial", 6, Font.BOLD)));
		HeaderFooter footer = new HeaderFooter(paragFooter, false);
		footer.setBorder(0);

		document.setHeader(header);		
		document.setFooter(footer);
		document.open();
		//document.add(paragFotoYTablas);
	}

	protected void crearEspacioEnBlanco(int cant) throws DocumentException {
		for (int i = 0; i < cant; i++) {
			document.add(Chunk.NEWLINE);
		}
	}

	protected PdfPCell cellConBorde() {
		PdfPCell cell = new PdfPCell();
		return cell;
	}
	
	protected PdfPCell cellEnBlanco() {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		return cell;
	}
	
	protected void crearFirma() throws MalformedURLException, IOException, DocumentException {
		if (visita.getFirma() != null && visita.getFirma().getPath() != ""){
			document.newPage();
			Image firma = null;
			try{
				firma = Image.getInstance(visita.getFirma().getPath());
				firma.scaleToFit(100, 100);
				firma.setAlignment(Element.ALIGN_CENTER);
			} catch (Exception e){
				System.out.println(e.getMessage() + " - " + e.getStackTrace());
			}
			document.add(new Phrase("Comentario de operador ", FontFactory.getFont("Arial", 10, Font.BOLD)));
			
			String comentario = (visita.getFirma() != null && visita.getFirma().getComentario() != null && !visita.getFirma().getComentario().equals("")) ? visita.getFirma().getComentario() : "El operador no ingresó comentarios.";
			document.add(new Phrase(comentario, FontFactory.getFont("Arial", 10)));
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			
			document.add(new Phrase("Firma de operador", FontFactory.getFont("Arial", 10, Font.BOLD)));
			if (firma != null){
				document.add(firma);
			}
		}
	}
}
