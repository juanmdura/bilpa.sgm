package app.server.control.visita.reporte;

import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;

public class UtilVisitaReporteOperador {
	public final static String VISITA_PREVENTIVA_ESTACIONES = "Reporte de mantenimiento preventivo";
	public final static String VISITA_PREVENTIVA_ESTACIONES_TITULO = "Reporte de mantenimiento preventivo";
	public final static String SECCION_DESPACHO_TITULO = "Se despacharon las siguientes cantidades de combustible";
	public final static String SECCION_MANTENIMIENTO_TITULO = "Se hizo mantenimiento preventivo a los siguientes activos";
	
	protected static PdfPCell cellConBorde() {
		PdfPCell cell = new PdfPCell();
		cell.addElement(new Phrase(" "));
		return cell;
	}
	
	protected static PdfPCell cellEnBlanco() {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		return cell;
	}
}
