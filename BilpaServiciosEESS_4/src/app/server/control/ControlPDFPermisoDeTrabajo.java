package app.server.control;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;

import app.client.dominio.Orden;
import app.client.dominio.Reparacion;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.org.apache.xml.internal.security.utils.Base64;


public class ControlPDFPermisoDeTrabajo extends ControlPDF
{

	private static ControlPDFPermisoDeTrabajo instancia = null;

	public static ControlPDFPermisoDeTrabajo getInstancia()
	{
		if(instancia == null)
		{
			instancia = new ControlPDFPermisoDeTrabajo();
		}
		return instancia;
	}

	public File crearPDFPermisoTrabajo(Orden orden)
	{
		try 
		{
			this.orden = orden;

			file2 = File.createTempFile("bilpa_", ".pdf");
			document2 = new Document(PageSize.A4, 50, 50, 50, 20);
			out2 =  new FileOutputStream(file2);
			writer2 = PdfWriter.getInstance(document2, out2);
			crearPDFPersmisoTrabajo(document2, new FileInputStream(file2), out2);

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
			document2.close();
		}	
	}

	public String crearPDFPermisoByte(Orden orden) throws IOException{
		return Base64.encode(FileUtils.readFileToByteArray(crearPDFPermisoTrabajo(orden)));
	}
	
	protected void crearPDFPersmisoTrabajo(Document document22, FileInputStream fileInputStream, FileOutputStream out22) throws MalformedURLException, IOException {

		try 
		{
			piePaginaPermiso(document22);
			document22.open();
			crearTableCabezal(document22);
			crearPermisos(document22);
			crearFallasReportadas(document22);
			ingresoComentarios(document22);
			
		} 
		catch (DocumentException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			document22.close();
		} 
	}

	protected void piePaginaPermiso(Document document22) 
	{
		//abre
		String firmaE = new String("Firma  Técnico Bilpa:     __________________________________________________             Firma Responsable EESS:    __________________________________________________");
		String firmaB = new String("Aclaración   Técnico:     __________________________________________________             Aclaración Responsable:    __________________________________________________");

		Paragraph phFirmaEs = new Paragraph(firmaE, FontFactory.getFont("Arial", 5,Font.BOLD));		
		Paragraph phAcFirmaEs = new Paragraph(firmaB, FontFactory.getFont("Arial", 5, Font.BOLD));

		pie = new Paragraph();
		pie.setFont(FontFactory.getFont("Arial", 5, Font.BOLD));
		pie.add(phFirmaEs);
		pie.add(phAcFirmaEs);

		pie.setAlignment(Element.ALIGN_CENTER);

		HeaderFooter footer = new HeaderFooter(pie, false);

		footer.setBorder(0);
		footer.setAlignment(Element.ALIGN_CENTER);	
		document22.setFooter(footer);
	}

	private void crearPermisos(Document document) throws DocumentException
	{
		crearEspacioEnBlanco(1, document);
		document.add(new Phrase("Se autoriza a realizar trabajos en la EE.SS: ",  FontFactory.getFont("Arial", 7, Font.BOLD)));
		document.add(crearTablaPermisos());
	}

	private void ingresoComentarios(Document document) throws DocumentException 
	{
		crearEspacioEnBlanco(1, document);
		document.add(new Phrase("Comentarios: ",  FontFactory.getFont("Arial", 7, Font.BOLD)));
		
		for (int i = 0; i < 12; i++) {
			String raya = new String("__________________________________________________________________________________________________________________________________________________________________________________");

			crearEspacioEnBlanco(1, document);
			Paragraph phRaya = new Paragraph(raya, FontFactory.getFont("Arial", 5,Font.BOLD));
			document.add(phRaya);	
		}
	}

	private PdfPTable crearTablaPermisos() throws DocumentException 
	{
		float[] colsWidth = {8f, 0.8f, 8f, 0.8f, 8f, 0.8f}; // Code 1
		PdfPTable t = new PdfPTable(colsWidth);
		
		setearTabla(t);
		setearTabla(t);

		PdfPCell cell1 = new PdfPCell(new Phrase(" VALLADO ZONA DE TRABAJO",  FontFactory.getFont("Arial", 6)));
		PdfPCell cell2 = new PdfPCell(new Phrase(" INHABILITAR EQUIPOS",  FontFactory.getFont("Arial", 6)));
		PdfPCell cell3 = new PdfPCell(new Phrase(" DESCONEXION ELECTRICA",  FontFactory.getFont("Arial", 6)));
		PdfPCell cell4 = new PdfPCell(new Phrase(" CORTE CIRCULACION VEHICULAR",  FontFactory.getFont("Arial", 6)));
		PdfPCell cell5 = new PdfPCell(new Phrase(" USO EQUIPO SEGURIDAD PERSONAL",  FontFactory.getFont("Arial", 6)));
		PdfPCell cell6 = new PdfPCell(new Phrase(" PROTECCION CONTRA INCENDIOS",  FontFactory.getFont("Arial", 6)));

		t.addCell(cell1);
		t.addCell(new PdfPCell());
		t.addCell(cell2);
		t.addCell(new PdfPCell());
		t.addCell(cell3);
		t.addCell(new PdfPCell());
		t.addCell(cell4);
		t.addCell(new PdfPCell());
		t.addCell(cell5);
		t.addCell(new PdfPCell());
		t.addCell(cell6);
		t.addCell(new PdfPCell());
		return t;
	}

	private void crearFallasReportadas(Document document) throws DocumentException 
	{
		if (orden.getReparaciones().size()>0)
		{			
			crearEspacioEnBlanco(1, document);
			Paragraph pgphReparaciones = new Paragraph("Fallas Reportadas por el Cliente",  FontFactory.getFont("Arial", 7, Font.BOLD));
			pgphReparaciones.setAlignment(Element.ALIGN_LEFT);

			float[] colsWidth = {6.5f, 10f, 10f}; // Code 1
			PdfPTable t = new PdfPTable(colsWidth);

			setearTabla(t);

			Phrase phraseActivo = new Phrase("Activo",  FontFactory.getFont("Arial", 6, Font.BOLD));
			Phrase phraseFalla = new Phrase("Falla Reportada", FontFactory.getFont("Arial", 6, Font.BOLD));
			Phrase phraseCom = new Phrase("Comentario", FontFactory.getFont("Arial", 6, Font.BOLD));


			PdfPCell cell = new PdfPCell(phraseActivo);
			cell.setBackgroundColor(new Color (155, 155, 155));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell2 = new PdfPCell(phraseFalla);
			cell2.setBackgroundColor(new Color (155, 155, 155));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cell3 = new PdfPCell(phraseCom);
			cell3.setBackgroundColor(new Color (155, 155, 155));
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

			t.addCell(cell);
			t.addCell(cell2);
			t.addCell(cell3);

			for (Reparacion r : orden.getReparaciones())
			{
				if (r.getFallaReportada() != null)
				{
					PdfPCell c1 = new PdfPCell(new Phrase(r.getActivo().toString(), FontFactory.getFont("Arial", 6)));
					t.addCell(c1);

					if (r.getFallaReportada() != null)
					{
						c1 = new PdfPCell(new Phrase(r.getFallaReportada().getDescripcion(), FontFactory.getFont("Arial", 6)));
						t.addCell(c1);					
					}
					c1 = new PdfPCell(new Phrase(r.getComentario(), FontFactory.getFont("Arial", 6)));
					t.addCell(c1);
				}
			}
			pgphReparaciones.add(t);
			document.add(pgphReparaciones);
		}
	}
}
