package app.server.control;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import app.client.dominio.Activo;
import app.client.dominio.Comentario;
import app.client.dominio.Contador;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Orden;
import app.client.dominio.Pico;
import app.client.dominio.Reparacion;
import app.client.dominio.ReparacionSurtidor;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Solucion;
import app.client.dominio.Surtidor;
import app.client.dominio.data.PendienteData;
import app.client.utilidades.UtilOrden;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;


public class ControlPDFOrden extends ControlPDF{

	private boolean esComentarioDeReparacion = false;
	private static ControlPDFOrden instancia = null;

	public static ControlPDFOrden getInstancia() {
		if(instancia == null){
			instancia = new ControlPDFOrden();
		}
		return instancia;
	}

	public File crearPDF(Orden _orden, Boolean pdfBilpa)
	{		
		try 
		{
			getPdf(_orden, pdfBilpa);
			return  file2;			
		}
		catch (DocumentException e) 
		{
			e.printStackTrace();
			return null;
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			document1.close();
			document2.close();
		}	
	}

	private void getPdf(Orden _orden, boolean pdfBilpa) throws IOException,
	FileNotFoundException, DocumentException, BadElementException {
		orden = _orden;
		this.pdfBilpa = pdfBilpa;

		file1 = File.createTempFile("bilpa_", ".pdf");
		document1 = new Document(PageSize.A4, 50, 50, 50, 20);
		out1 =  new FileOutputStream(file1);
		writer1 = PdfWriter.getInstance(document1, out1);			
		crearPDF(document1, new FileInputStream(file1), out1);			

		leerCantidadPaginas(file1);

		file2 = File.createTempFile("bilpa_", ".pdf");
		document2 = new Document(PageSize.A4, 50, 50, 50, 20);
		out2 =  new FileOutputStream(file2);
		writer2 = PdfWriter.getInstance(document2, out2);
		crearPDF(document2, new FileInputStream(file2), out2);

		mergePDFs();
	}

	private void crearPDF(Document document, InputStream streamOfPDFFile, FileOutputStream out) throws DocumentException, IOException, BadElementException 
	{
		piePagina(document);

		document.open();
		crearTableCabezal(document);

		crearTotalizadores(document);

		crearSoluciones(document);

		if (pdfBilpa){// Nicre
			crearPendientes(document, EstadoPendiente.REPARADO);
			crearPendientes(document, EstadoPendiente.INICIADO);
		}

		if(document2 != null && cantidadPaginas >= 2 && orden.getComentariosImprimibles().size()>0)
		{
			// document.newPage();	
			cantidadPaginas+=1;
		}
		crearComentariosDeOrden(document);

		crearFirma(document);

		document.close();
	}

	protected void crearFirma(Document document1) throws MalformedURLException, IOException, DocumentException {
		if (orden.getFirma() != null && orden.getFirma().getPath() != ""){
			document1.newPage();
			try{
				Image firma = Image.getInstance(orden.getFirma().getPath());
				firma.scaleToFit(100, 100);
				firma.setAlignment(Element.ALIGN_CENTER);
				document1.add(new Phrase("Comentario de operador ", FontFactory.getFont("Arial", 10, Font.BOLD)));

				String comentario = (orden.getFirma() != null && orden.getFirma().getComentario() != null && !orden.getFirma().getComentario().equals("")) ? orden.getFirma().getComentario() : "El operador no ingresó comentarios.";
				document1.add(new Phrase(comentario, FontFactory.getFont("Arial", 10)));
				document1.add(Chunk.NEWLINE);
				document1.add(Chunk.NEWLINE);

				document1.add(new Phrase("Firma de operador", FontFactory.getFont("Arial", 10, Font.BOLD)));
				document1.add(firma);
			} catch (Exception e){
				System.out.println("No se encontró la firma " + orden.getFirma().getPath());
			}
		}
	}

	void leerCantidadPaginas(File file) 
	{
		try 
		{
			InputStream pdf = new FileInputStream(file);

			PdfReader pdfReader = new PdfReader(pdf);
			cantidadPaginas = pdfReader.getNumberOfPages();

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void piePagina(Document document) 
	{
		Paragraph paragFooter = new Paragraph();

		String via = pdfBilpa ? "Bilpa" : "Operador";
		paragFooter.add(new Phrase(" Bilpa S.A. | Valladolid 3525 | Montevideo | +598 2211 20 20 | Rut 21126170018 | service.estaciones@bilpa.com.uy                                                                 Vía "+via,  FontFactory.getFont("Arial", 6, Font.BOLD)));
		HeaderFooter footer = new HeaderFooter(paragFooter, false);
		footer.setBorder(0);

		footer.setBorder(0);
		footer.setAlignment(Element.ALIGN_LEFT);	
		document.setFooter(footer);
	}

	private void crearSoluciones(Document document) throws DocumentException, MalformedURLException, IOException 
	{
		if (orden.tieneSoluciones())
		{
			crearEspacioEnBlanco(1, document);
			Paragraph pgpTituloSol = new Paragraph("Reparaciones", FontFactory.getFont("Arial", sizeTitulo, Font.BOLD));
			pgpTituloSol.setAlignment(Element.ALIGN_LEFT);

			document.add(pgpTituloSol);
			int i = 0;

			for (Reparacion r : orden.getReparaciones()) 
			{
				if (r.tieneSoluciones()) 
				{

					i++;
					PdfPTable tRep = null;
					PdfPTable tComentario = null;
					if(r.tieneSolucionesConComentariosImprimibles() || pdfBilpa){
						tComentario = crearCabezalTableComentario();
					}
					Paragraph pgphSoluciones = new Paragraph(i + ". " + UtilOrden.tiposDeActivos(r.getActivo()) + ": " + r.getActivo().toString(), FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159)));
					PdfPTable t = crearTablaSol(r.getTipo(), r);
					int cont=0;
					for (Solucion solucion : r.getSoluciones())
					{			
						cont++;
						PdfPCell c1;
						if (r.getActivo().getTipo() == 1 && r.tienePicosReparados())
						{
							ReparacionSurtidor repSurtidor = (ReparacionSurtidor) r;
							Contador contador = repSurtidor.buscarContador(solucion);

							PdfPCell cPico;
							PdfPCell cI;
							PdfPCell cF;

							if (contador != null && contador.getPico() != null)//CONTADORES
							{
								cPico = new PdfPCell(new Phrase(contador.getPico().toString(), FontFactory.getFont("Arial", 6)));
								//PICO

								cI = new PdfPCell(new Phrase(contador.getInicio() +"", FontFactory.getFont("Arial", 6)));
								//INICIO

								cF = new PdfPCell(new Phrase(contador.getFin() +"", FontFactory.getFont("Arial", 6)));
								//FIN


							}
							else
							{
								cPico = new PdfPCell(new Phrase("-", FontFactory.getFont("Arial", 6)));
								//PICO

								cI = new PdfPCell(new Phrase("-", FontFactory.getFont("Arial", 6)));
								//INICIO

								cF = new PdfPCell(new Phrase("-", FontFactory.getFont("Arial", 6)));
								//FIN
							}

							PdfPCell cFD = new PdfPCell(new Phrase(solucion.getFallaTecnica().getDescripcion(), FontFactory.getFont("Arial", 6)));

							PdfPCell cTR = new PdfPCell(new Phrase(solucion.getTarea().getDescripcion(), FontFactory.getFont("Arial", 6 )));

							PdfPCell cTel;

							t.addCell(cPico);
							t.addCell(cI);
							t.addCell(cF);
							t.addCell(cFD);
							t.addCell(cTR);
							if (r.tieneAlgunaSolucionTelefonica())
							{
								if (solucion.isTelefonica())
								{
									cTel = new PdfPCell(new Phrase("Si", FontFactory.getFont("Arial", 6)));
									t.addCell(cTel);
								}
								else
								{
									cTel = new PdfPCell(new Phrase("No", FontFactory.getFont("Arial", 6)));
									t.addCell(cTel);
								}							
							}
						}
						else
						{
							c1 = new PdfPCell(new Phrase(solucion.getFallaTecnica().getDescripcion(), FontFactory.getFont("Arial", 6)));
							t.addCell(c1);
							c1 = new PdfPCell(new Phrase(solucion.getTarea().getDescripcion(), FontFactory.getFont("Arial", 6)));
							t.addCell(c1);
							if (r.tieneAlgunaSolucionTelefonica())
							{
								if (solucion.isTelefonica())
								{
									c1 = new PdfPCell(new Phrase("Si", FontFactory.getFont("Arial", 6)));
									t.addCell(c1);
								}else{
									c1 = new PdfPCell(new Phrase("No", FontFactory.getFont("Arial", 6)));
									t.addCell(c1);
								}

							}
						}

						if (pdfBilpa){
							if (solucion.getFoto() != null){
								PdfPCell cellFoto = new PdfPCell(new Phrase(" Antes de la reparación",  FontFactory.getFont("Arial", 6, Font.BOLD)));
								Image foto = Image.getInstance(solucion.getFoto().getPath());
								foto.scaleAbsolute(100, 100);

								PdfPCell cellFotoData = new PdfPCell(foto);
								t.addCell(cellFoto);
								t.addCell(cellFotoData);
							}

							if (solucion.getFoto2() != null){
								PdfPCell cellFoto2 = new PdfPCell(new Phrase(" Después de la reparación",  FontFactory.getFont("Arial", 6, Font.BOLD)));
								Image foto2 = Image.getInstance(solucion.getFoto2().getPath());
								foto2.scaleAbsolute(100, 100);
								PdfPCell cellFoto2Data = new PdfPCell(foto2);
								t.addCell(cellFoto2);
								t.addCell(cellFoto2Data);
							}
						}

						Set<RepuestoLinea> rls = orden.getRepuestosLinea(r.getActivo());
						if (rls.size() > 0 && cont == 1)
						{
							float[] colsWidth = {25f, 3f}; // Code 1
							tRep = new PdfPTable(colsWidth);

							tRep.setWidthPercentage(100); 

							tRep.setHorizontalAlignment(Element.ALIGN_LEFT);
							PdfPCell cell1 = new PdfPCell(new Phrase (" Repuestos Utilizados ", FontFactory.getFont("Arial", 6, Font.BOLD)));
							PdfPCell cell2 = new PdfPCell(new Phrase (" Cantidad ", FontFactory.getFont("Arial", 6, Font.BOLD)));
							// cell1.setBackgroundColor(new Color (155, 155, 155));
							// cell2.setBackgroundColor(new Color (155, 155, 155));
							cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

							tRep.addCell(cell1);
							tRep.addCell(cell2);
							tRep.setSpacingBefore(5);

							for (RepuestoLinea rl : rls)
							{
								tRep.addCell(new Phrase ( rl.getRepuesto().getDescripcion(), FontFactory.getFont("Arial", 6)));
								tRep.addCell(new Phrase ( rl.getCantidad()+"", FontFactory.getFont("Arial", 6)));

							}
						}
						if (tComentario!=null) {
							crearComentariosDeReparacion(solucion, tComentario);
						}
					}	

					pgphSoluciones.add(t);
					document.add(pgphSoluciones);
					if (tRep!=null)
					{
						document.add(tRep);
					}

					if (tComentario!=null)
					{
						document.add(tComentario);
					}

					if(i < orden.getReparaciones().size())
					{
						// document.add(linea);
						crearEspacioEnBlanco(1, document);
					}
				}
			}
		}
	}

	private PdfPTable crearTablaSol(int tipo, Reparacion r) throws BadElementException {
		PdfPTable t;

		if (tipo == 1 && r.tienePicosReparados()) {
			if(r.tieneAlgunaSolucionTelefonica()){
				float[] colsWidth = {6f, 3f, 3f, 6f, 6f, 1f}; // Code 1
				t = new PdfPTable(colsWidth);				
				setearTabla(t);

				Phrase phrasePico = new Phrase("Pico", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseInicio = new Phrase("Inicio", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseFin = new Phrase("Fin", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseFallaDetectada = new Phrase("Falla Detectada", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseTareaRealizada = new Phrase("Tarea Realizada", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseTelef = new Phrase("Tel", FontFactory.getFont("Arial", 6, Font.BOLD));

				PdfPCell cPico = new PdfPCell(phrasePico);
				// cPico.setBackgroundColor(new Color (155, 155, 155));
				cPico.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cI = new PdfPCell(phraseInicio);
				// cI.setBackgroundColor(new Color (155, 155, 155));
				cI.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cF = new PdfPCell(phraseFin);
				// cF.setBackgroundColor(new Color (155, 155, 155));
				cF.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cFD = new PdfPCell(phraseFallaDetectada);
				// cFD.setBackgroundColor(new Color (155, 155, 155));
				cFD.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cTR = new PdfPCell(phraseTareaRealizada);
				// cTR.setBackgroundColor(new Color (155, 155, 155));
				cTR.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cTel = new PdfPCell(phraseTelef);
				// cTel.setBackgroundColor(new Color (155, 155, 155));
				cTel.setHorizontalAlignment(Element.ALIGN_CENTER);

				t.addCell(cPico);
				t.addCell(cI);
				t.addCell(cF);

				t.addCell(cFD);
				t.addCell(cTR);
				t.addCell(cTel);

			} else {

				float[] colsWidth = {6f, 3f, 3f, 7f, 7f}; // Code 1
				t = new PdfPTable(colsWidth);				
				setearTabla(t);
				setearTabla(t);

				Phrase phrasePico = new Phrase("Pico", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseInicio = new Phrase("Inicio", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseFin = new Phrase("Fin", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseFallaDetectada = new Phrase("Falla Detectada", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseTareaRealizada = new Phrase("Tarea Realizada", FontFactory.getFont("Arial", 6, Font.BOLD));

				PdfPCell cPico = new PdfPCell(phrasePico);
				// cPico.setBackgroundColor(new Color (155, 155, 155));
				cPico.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cI = new PdfPCell(phraseInicio);
				// cI.setBackgroundColor(new Color (155, 155, 155));
				cI.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cF = new PdfPCell(phraseFin);
				// cF.setBackgroundColor(new Color (155, 155, 155));
				cF.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cFD = new PdfPCell(phraseFallaDetectada);
				// cFD.setBackgroundColor(new Color (155, 155, 155));
				cFD.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cTR = new PdfPCell(phraseTareaRealizada);
				// cTR.setBackgroundColor(new Color (155, 155, 155));
				cTR.setHorizontalAlignment(Element.ALIGN_CENTER);

				t.addCell(cPico);
				t.addCell(cI);
				t.addCell(cF);
				t.addCell(cFD);
				t.addCell(cTR);
			}

		} else { 

			if(r.tieneAlgunaSolucionTelefonica()){
				float[] colsWidth = {12f, 12f, 1f}; // Code 1
				t = new PdfPTable(colsWidth);				
				setearTabla(t);

				Phrase phraseFallaDetectada = new Phrase("Falla Detectada", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseTareaRealizada = new Phrase("Tarea Realizada", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseTelef = new Phrase("Tel", FontFactory.getFont("Arial", 6, Font.BOLD));

				PdfPCell cell = new PdfPCell(phraseFallaDetectada);
				// cell.setBackgroundColor(new Color (155, 155, 155));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cell2 = new PdfPCell(phraseTareaRealizada);
				// cell2.setBackgroundColor(new Color (155, 155, 155));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cell3 = new PdfPCell(phraseTelef);
				// cell3.setBackgroundColor(new Color (155, 155, 155));
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

				t.addCell(cell);
				t.addCell(cell2);
				t.addCell(cell3);
			} else {
				float[] colsWidth = {13f, 13f}; // Code 1
				t = new PdfPTable(colsWidth);				
				setearTabla(t);

				Phrase phraseFallaDetectada = new Phrase("Falla Detectada", FontFactory.getFont("Arial", 6, Font.BOLD));
				Phrase phraseTareaRealizada = new Phrase("Tarea Realizada", FontFactory.getFont("Arial", 6, Font.BOLD));

				PdfPCell cell = new PdfPCell(phraseFallaDetectada);
				// cell.setBackgroundColor(new Color (155, 155, 155));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell cell2 = new PdfPCell(phraseTareaRealizada);
				// cell2.setBackgroundColor(new Color (155, 155, 155));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

				t.addCell(cell);
				t.addCell(cell2);
			}
		}
		return t;
	}

	void crearPiePagina(Document document, InputStream streamOfPDFFile, OutputStream outputStream, boolean paginate) 
	{
		try 
		{
			int totalPages = 0;

			PdfReader pdfReader = new PdfReader(streamOfPDFFile);
			totalPages += pdfReader.getNumberOfPages();

			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;

			// Create a new page in the target for each source page.
			while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) 
			{
				document.newPage();
				pageOfCurrentReaderPDF++;
				currentPageNumber++;
				page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
				cb.addTemplate(page, 0, 0);

				// Code for pagination.
				if (paginate) 
				{
					cb.beginText();
					cb.setFontAndSize(bf, 9);
					String textoPie = "" + currentPageNumber + " of " + totalPages;
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, textoPie, 520, 5, 0);
					cb.endText();
				}
			}
			pageOfCurrentReaderPDF = 0;

			outputStream.flush();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}

	private void crearComentariosDeReparacion(Solucion s, PdfPTable tComentario) throws DocumentException {
		esComentarioDeReparacion = s.getComentario() != null && s.getComentario().isImprimible() && s.getComentario().getTexto() != null && s.getComentario().getTexto().length() > 0;

		if (esComentarioDeReparacion) {
			crearCeldasTableComentario(tComentario, s.getComentario());
		}
	}

	private void crearComentariosDeOrden(Document document) throws DocumentException {
		if (orden.getComentariosImprimibles().size() > 0 && !esComentarioDeReparacion)
		{
			crearEspacioEnBlanco(1, document);
			Paragraph pgphReparaciones = new Paragraph("Comentarios de la Orden", FontFactory.getFont("Arial", 6, Font.BOLD));
			pgphReparaciones.setAlignment(Element.ALIGN_LEFT);

			PdfPTable tComentario = crearCabezalTableComentario();

			for (Comentario comentario : orden.getComentariosImprimibles())
			{
				crearCeldasTableComentario(tComentario, comentario);
			}

			pgphReparaciones.add(tComentario);
			document.add(pgphReparaciones);		
		}
	}

	private void crearCeldasTableComentario(PdfPTable tComentario, Comentario r) {
		PdfPCell clCant = new PdfPCell(new Phrase(r.getTexto() +"", FontFactory.getFont("Arial", 6)));
		tComentario.addCell(clCant);

		PdfPCell cA = new PdfPCell(new Phrase(r.getUsuario() != null ? r.getUsuario().toString() : "", FontFactory.getFont("Arial", 6)));
		tComentario.addCell(cA);

		PdfPCell c1 = new PdfPCell(new Phrase(r.getFecha() != null ? r.getFecha()+"" : "", FontFactory.getFont("Arial", 6)));
		tComentario.addCell(c1);
	}

	private PdfPTable crearCabezalTableComentario() {
		float[] colsWidth = {14.5f, 6f, 6f}; // Code 1
		PdfPTable tComentario = new PdfPTable(colsWidth);
		setearTabla(tComentario);
		if(esComentarioDeReparacion){
			tComentario.setSpacingBefore(5);
		}

		Phrase phraseComentario = new Phrase("Comentario", FontFactory.getFont("Arial", 6, Font.BOLD));
		Phrase phraseActivo = new Phrase("Usuario", FontFactory.getFont("Arial", 6, Font.BOLD));
		Phrase phraseRepuesto = new Phrase("Fecha", FontFactory.getFont("Arial", 6, Font.BOLD));

		PdfPCell cell1 = new PdfPCell(phraseComentario);
		// cell1.setBackgroundColor(new Color (155, 155, 155));
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell cell2 = new PdfPCell(phraseActivo);
		// cell2.setBackgroundColor(new Color (155, 155, 155));
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell cell3 = new PdfPCell(phraseRepuesto);
		// cell3.setBackgroundColor(new Color (155, 155, 155));
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

		tComentario.addCell(cell1);
		tComentario.addCell(cell2);
		tComentario.addCell(cell3);
		return tComentario;
	}

	private void crearTotalizadores(Document document) throws DocumentException {
		float[] colsWidthFotoYTablas = {1f, 1f};
		PdfPTable t = new PdfPTable(colsWidthFotoYTablas);

		boolean hayData = false;
		for (Reparacion r : orden.getReparaciones()) 
		{
			if (r.getActivo().getTipo() == 1) 
			{
				Surtidor surtidor = (Surtidor) ControlActivo.getInstancia().buscarActivo(r.getActivo().getId());
				ReparacionSurtidor reparacionSurtidor = ControlReparacion.getInstancia().obtenerReparacionSurtidor(r.getId());

				if (reparacionSurtidor.getContadores().size() > 0 || reparacionSurtidor.tienePrecintosReemplazados()){
					if (hayData){
						agregarCeldaEnBlanco(t);
					}
					hayData = true;
					t.setWidthPercentage(100);
					PdfPCell cell = new PdfPCell(new Phrase(surtidor.toStringLargo(), FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
					cell.setColspan(2);
					t.addCell(cell);

					for (Solucion solucion : reparacionSurtidor.getSoluciones()){
						Pico pico = solucion.getPico(reparacionSurtidor.getContadores());

						if (pico != null){
							PdfPCell cellPico = new PdfPCell(new Phrase ("Manguera número " + pico.getNumeroEnLaEstacion(), FontFactory.getFont("Arial", 6, Font.BOLD)));
							cellPico.setColspan(2);
							t.addCell(cellPico);

							addContador(t, reparacionSurtidor, solucion);
							addPrecintoYCalibres(t, solucion);
						}
					}
				}
			}
		}

		if (hayData){
			crearEspacioEnBlanco(1, document);
			document.add(new Phrase ("Valores por manguera", FontFactory.getFont("Arial", sizeTitulo, Font.BOLD)));
			document.add(t);
			document.add(Chunk.NEWLINE);
		}
	}

	private void agregarCeldaEnBlanco(PdfPTable t) {
		PdfPCell cell = new PdfPCell((new Phrase ("")));
		cell.setBorder(0);
		cell.setColspan(2);
		t.addCell(cell);
	}

	private void addContador(PdfPTable t, ReparacionSurtidor reparacionSurtidor, Solucion solucion) {
		Contador contador = solucion.getContadores(reparacionSurtidor.getContadores());
		if (contador != null){
			t.addCell(new PdfPCell(new Phrase ("Litros despachados", FontFactory.getFont("Arial", 6, Font.BOLD))));
			t.addCell(new PdfPCell(new Phrase (contador.getFin()- contador.getInicio()+"", FontFactory.getFont("Arial", 6))));
		}
	}

	private static void addPrecintoYCalibres(PdfPTable t, Solucion solucion) {
		if (solucion.tienePrecintos()){
			PdfPCell cell = new PdfPCell(new Phrase("Calibres y precintos", FontFactory.getFont("Arial", 6, Font.BOLD)));
			cell.setColspan(2);
			t.addCell(cell);

			t.addCell(new PdfPCell(new Phrase("Calibre 1", FontFactory.getFont("Arial", 6))));
			t.addCell(new PdfPCell(new Phrase (solucion.getCalibre().getCalibre1()+"", FontFactory.getFont("Arial", 6))));

			t.addCell(new PdfPCell(new Phrase ("Calibre 2", FontFactory.getFont("Arial", 6))));
			t.addCell(new PdfPCell(new Phrase (solucion.getCalibre().getCalibre2()+"", FontFactory.getFont("Arial", 6))));

			t.addCell(new PdfPCell(new Phrase ("Calibre 3", FontFactory.getFont("Arial", 6))));
			t.addCell(new PdfPCell(new Phrase (solucion.getCalibre().getCalibre3()+"", FontFactory.getFont("Arial", 6))));

			if(solucion.getPrecinto() != null && solucion.getPrecinto().cambioPrecinto()) {
				t.addCell(new PdfPCell(new Phrase ("Se instaló el precinto número  ", FontFactory.getFont("Arial", 6))));
				t.addCell(new PdfPCell(new Phrase (solucion.getPrecinto().getNumero(), FontFactory.getFont("Arial", 6))));

			} else if (solucion.getPrecinto() != null && solucion.getPrecinto().getNumero() != null && !solucion.getPrecinto().getNumero().isEmpty()){
				t.addCell(new PdfPCell(new Phrase ("Se mantuvo el precinto número", FontFactory.getFont("Arial", 6))));
				t.addCell(new PdfPCell(new Phrase (solucion.getPrecinto().getNumero(), FontFactory.getFont("Arial", 6))));
			}

			if (solucion.tienePrecintosReemplazados()){
				t.addCell(new PdfPCell(new Phrase ("Calibre 4", FontFactory.getFont("Arial", 6))));
				t.addCell(new PdfPCell(new Phrase (solucion.getCalibre().getCalibre4()+"", FontFactory.getFont("Arial", 6))));

				t.addCell(new PdfPCell(new Phrase ("Calibre 5", FontFactory.getFont("Arial", 6))));
				t.addCell(new PdfPCell(new Phrase (solucion.getCalibre().getCalibre5()+"", FontFactory.getFont("Arial", 6))));

				t.addCell(new PdfPCell(new Phrase ("Calibre 6", FontFactory.getFont("Arial", 6))));
				t.addCell(new PdfPCell(new Phrase (solucion.getCalibre().getCalibre6()+"", FontFactory.getFont("Arial", 6))));

			}
		}
	}

	private void crearPendientes(Document document, EstadoPendiente estadoPendiente) throws DocumentException {
		float[] colsWidthFotoYTablas = {1f, 1f};
		PdfPTable t = new PdfPTable(colsWidthFotoYTablas);
		t.setWidthPercentage(100);

		boolean hay = false;
		for (Activo activo : orden.getEmpresa().getListaDeActivos()) 
		{
			List<PendienteData> pendientes = ControlPendiente.getInstancia().obtenerPendientesDeActivo(activo.getId(), estadoPendiente);

			boolean tieneVisibles = false;
			boolean agregarTitulo = true;

			for (PendienteData p : pendientes) {
				if (p.isComentarioVisible()){
					tieneVisibles = true;
				}
			}

			if (tieneVisibles || pdfBilpa){
				for (PendienteData p : pendientes) {
					if (agregarTitulo){
						agregarTitulo = false;
						if (hay){
							PdfPCell cellEnBlanco = new PdfPCell();
							cellEnBlanco.setColspan(2);
							cellEnBlanco.setBorder(0);
							t.addCell(cellEnBlanco);
						}

						PdfPCell cell = new PdfPCell(new Phrase (activo.toStringLargo(), FontFactory.getFont("Arial", 6, Font.BOLD, new Color(0, 84, 159))));
						cell.setColspan(2);
						t.addCell(cell);
					}

					String pendienteStr = p.getComentario();
					if(p.getPlazo() != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						pendienteStr+=", antes de: " + sdf.format(p.getPlazo());
					}
					PdfPCell cell5 = new PdfPCell(new Phrase(pendienteStr, FontFactory.getFont("Arial", 6)));
					cell5.setColspan(2);
					t.addCell(cell5);

					if (pdfBilpa){
						try{

							if (p.getPathFoto()!= null){
								PdfPCell cellFoto = new PdfPCell(new Phrase(" Foto del pendiente",  FontFactory.getFont("Arial", 6, Font.BOLD)));
								Image foto = Image.getInstance(p.getPathFoto());
								//foto.scaleAbsolute(100, 100);
								foto.scalePercent(20, 20);

								PdfPCell cellFotoData = new PdfPCell(foto);
								t.addCell(cellFoto);
								t.addCell(cellFotoData);
							}
						} catch (Exception e){
							System.out.println("No se encontro la foto: " + p.getPathFoto());
						}
					}
					hay = true;
				}
			}
		}

		if (hay){
			crearEspacioEnBlanco(1, document);

			String titulo = "Pendientes a realizar";
			if (estadoPendiente.equals(EstadoPendiente.REPARADO)){
				titulo = "Pendientes que fueron reparados";
			}
			document.add(new Phrase (titulo, FontFactory.getFont("Arial", sizeTitulo, Font.BOLD)));
			document.add(t);
			document.add(Chunk.NEWLINE);
		}
	}
}
