package app.server.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.client.dominio.Orden;
import app.server.UtilFechas;
import app.server.propiedades.PropiedadUrlLogo;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;


public abstract class ControlPDF {

	protected int sizeTitulo = 7;
	
	protected Orden orden;
	protected boolean pdfBilpa;

	protected Document document1;
	protected Document document2;
	protected File file1;
	protected File file2;
	protected FileOutputStream out1;
	protected FileOutputStream out2;
	protected PdfWriter writer1;
	protected PdfWriter writer2;

	protected PropiedadUrlLogo propiedadLogo = new PropiedadUrlLogo();	
	protected Paragraph pie;
	// protected Phrase linea = new Phrase("__________________________________________________________________________");
	protected Phrase linea = new Phrase("");
	
	protected int cantidadPaginas;
	
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

	protected void crearTableCabezal(Document document) throws DocumentException, MalformedURLException, IOException 
	{

		float[] colsWidth = {3f, 3.5f, 1.5f, 3f, 3.5f}; // Code 1
		PdfPTable t = new PdfPTable(colsWidth);

		t.setHorizontalAlignment(Element.ALIGN_RIGHT);
		t.setWidthPercentage(58);

		PdfPCell cell1 = new PdfPCell(new Phrase(" Cliente",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell2 = new PdfPCell(new Phrase(" Nro Cliente",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell3 = new PdfPCell(new Phrase(" Dirección",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell4 = new PdfPCell(new Phrase(" Localidad",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell5 = new PdfPCell(new Phrase(" Petrolera",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell6 = new PdfPCell(new Phrase(" Tipo Trabajo",  FontFactory.getFont("Arial", 6, Font.BOLD)));

		PdfPCell cell1Data = new PdfPCell(new Phrase( orden.getEmpresa().getNombre() + "",  FontFactory.getFont("Arial", 6)));
		PdfPCell cell2Data = new PdfPCell(new Phrase( orden.getEmpresa().getNumeroSerie() +"",  FontFactory.getFont("Arial", 6)));
		PdfPCell cell3Data = new PdfPCell(new Phrase( orden.getEmpresa().getDireccion(),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell4Data = new PdfPCell(new Phrase( orden.getEmpresa().getLocalidad(),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell5Data = new PdfPCell(new Phrase( orden.getEmpresa().getSello() + "",  FontFactory.getFont("Arial", 6)));
		PdfPCell cell6Data = new PdfPCell(new Phrase( orden.getTipoTrabajo()+"",  FontFactory.getFont("Arial", 6)));

		PdfPCell cell7 = new PdfPCell(new Phrase(" Correctivo Nro",  FontFactory.getFont("Arial", 6, Font.BOLD)));

		PdfPCell cell8 = new PdfPCell(new Phrase(" Fecha Reclamo",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell9 = new PdfPCell(new Phrase(" Hora Reclamo",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell10 = new PdfPCell(new Phrase(" Fecha de visita",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell11 = new PdfPCell(new Phrase(" Hora de inicio",  FontFactory.getFont("Arial", 6, Font.BOLD)));
		PdfPCell cell12 = new PdfPCell(new Phrase(" Hora de fin",  FontFactory.getFont("Arial", 6, Font.BOLD)));

		String textoNroOrden = orden.getNumero()+"";
		if (orden.getNumeroParteDucsa() != null && !orden.getNumeroParteDucsa().equalsIgnoreCase(""))
		{
			textoNroOrden+=" - Ducsa: " + orden.getNumeroParteDucsa();
		}
		PdfPCell cell7Data = new PdfPCell(new Phrase( textoNroOrden,  FontFactory.getFont("Arial", 6)));
		PdfPCell cell8Data = new PdfPCell(new Phrase( UtilFechas.getDia(orden.getFechaInicio()),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell9Data = new PdfPCell(new Phrase( UtilFechas.getHora(orden.getFechaInicio()),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell10Data = new PdfPCell(new Phrase( UtilFechas.getDia( orden.getInicioService()),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell11Data = new PdfPCell(new Phrase( UtilFechas.getHora( orden.getInicioService()),  FontFactory.getFont("Arial", 6)));
		PdfPCell cell12Data = new PdfPCell(new Phrase( UtilFechas.getHora( orden.getFinService())+"",  FontFactory.getFont("Arial", 6)));

		PdfPCell cellsp1 = new PdfPCell();
		cellsp1.setBorderWidth(0);
		PdfPCell cellsp2 = new PdfPCell();
		cellsp2.setBorderWidth(0);
		PdfPCell cellsp3 = new PdfPCell();
		cellsp3.setBorderWidth(0);
		PdfPCell cellsp4 = new PdfPCell();
		cellsp4.setBorderWidth(0);
		PdfPCell cellsp5 = new PdfPCell();
		cellsp5.setBorderWidth(0);
		PdfPCell cellsp6 = new PdfPCell();
		cellsp6.setBorderWidth(0);

		t.addCell(cell1);
		t.addCell(cell1Data);
		t.addCell(cellsp1);
		t.addCell(cell7);
		t.addCell(cell7Data);

		t.addCell(cell2);
		t.addCell(cell2Data);
		t.addCell(cellsp2);
		t.addCell(cell8);
		t.addCell(cell8Data);

		t.addCell(cell3);
		t.addCell(cell3Data);	
		t.addCell(cellsp3);
		t.addCell(cell9);
		t.addCell(cell9Data);

		t.addCell(cell4);
		t.addCell(cell4Data);
		t.addCell(cellsp4);
		t.addCell(cell10);
		t.addCell(cell10Data);

		t.addCell(cell5);
		t.addCell(cell5Data);
		t.addCell(cellsp5);
		t.addCell(cell11);
		t.addCell(cell11Data);

		t.addCell(cell6);
		t.addCell(cell6Data);
		t.addCell(cellsp6);
		t.addCell(cell12);
		t.addCell(cell12Data);

		Image foto = Image.getInstance(propiedadLogo.getURLPropertie());
		foto.scaleToFit(100, 100);
		foto.setAlignment(Element.ALIGN_LEFT);

		Paragraph paragFotoYTablas = new Paragraph();
		float[] colsWidthFotoYTablas = {1f, 3f}; // Code 1
		PdfPTable tablaFotoYTablas = new PdfPTable(colsWidthFotoYTablas);
		tablaFotoYTablas.setWidthPercentage(100);

		PdfPCell cellFoto = new PdfPCell(foto);
		cellFoto.setBorderWidth(0);

		PdfPCell cellTabla = new PdfPCell(t);
		cellTabla.setBorderWidth(0);

		tablaFotoYTablas.addCell(cellFoto);		
		tablaFotoYTablas.addCell(cellTabla);
		paragFotoYTablas.add(tablaFotoYTablas);

		HeaderFooter header = new HeaderFooter(paragFotoYTablas, false);
		header.setBorder(0);
		header.setAlignment(Element.ALIGN_RIGHT);	

		document.setHeader(header);		
		document.add(paragFotoYTablas);
	}

	protected void setearTabla(PdfPTable t) 
	{
		t.setHorizontalAlignment(Element.ALIGN_LEFT);
		t.setWidthPercentage(100);
	}

	protected void crearEspacioEnBlanco(int cant, Document document) throws DocumentException 
	{
		for (int i = 0; i < cant; i++) 
		{
			Paragraph espacio = new Paragraph("  ");
			document.add(espacio);		
		}
	}

	void mergePDFs()
	{
		try 
		{
			List<InputStream> pdfs = new ArrayList<InputStream>();
			pdfs.add(new FileInputStream(file1));
			pdfs.add(new FileInputStream(file2));

			//TODO Juan esta fruta que es??
			OutputStream output = new FileOutputStream("merge.pdf");

			concatPDFs(pdfs, output, true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) 
	{
		Document document = new Document();
		try 
		{
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) 
			{
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}

			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) 
			{
				PdfReader pdfReader = iteratorPDFReader.next();

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
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}

			outputStream.flush();
			document.close();
			outputStream.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if (document.isOpen())
				document.close();
			try 
			{
				if (outputStream != null)
				{	
					outputStream.close();
				}
			} 
			catch (IOException ioe) 
			{
				ioe.printStackTrace();
			}
		}
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
			// data

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
}
