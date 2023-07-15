package app.server.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import app.client.dominio.Sello;
import app.client.dominio.data.DatoConsultaOrdenesPorSelloYFechas;

public class ControlExcelOrdenesSello 
{
	private Date inicio;
	private Date fin;
	private Sello sello;
	
	private File file;
	private WritableWorkbook workbook;
	
	List<DatoConsultaOrdenesPorSelloYFechas> ordenesDato = new ArrayList<DatoConsultaOrdenesPorSelloYFechas>();

	private static ControlExcelOrdenesSello instancia = null;

	public static ControlExcelOrdenesSello getInstancia() {
		if(instancia == null){
			instancia = new ControlExcelOrdenesSello();
		}
		return instancia;
	}

	public File crearExcelOrdenesSello(Sello _sello, Date _inicio, Date _fin)
	{
		try {
			inicio = _inicio;
			fin = _fin;			
			sello = _sello;
			ordenesDato = ControlListado.getInstancia().ordenesPorSelloYFechas(sello, _inicio, _fin);
			
			crearArchivo();
			crearHojas();
			cerrarArchivo();
			
		} catch (IOException e) {
			System.out.print("Error al crear el Archivo Excel \n");
			e.printStackTrace();
			return null;

		} catch (RowsExceededException e) {
			System.out.print("Error al agregar contenido a las celdas del Archivo Excel \n");
			e.printStackTrace();
			return null;

		} catch (WriteException e) {
			System.out.print("Error al escribir el Archivo Excel \n");
			e.printStackTrace();
			return null;
		}
		
		return file;
	}

	private void cerrarArchivo() throws IOException, WriteException {
		workbook.write(); 
		workbook.close();
	}

	private void crearArchivo() throws IOException
	{
		file = File.createTempFile("bilpa_", ".xls");
		workbook = Workbook.createWorkbook(file);
	}

	private void crearHojas() throws RowsExceededException, WriteException 
	{
		//En realidad seria una hoja por E.S.
		for (int i = 0 ; i < ordenesDato.size() ; i ++)
		{
			WritableSheet sheet = crearHoja(ordenesDato.get(i), i);
			crearDatos(sheet, ordenesDato.get(i), i);
		}		
	}

	private void crearDatos(WritableSheet sheet, DatoConsultaOrdenesPorSelloYFechas ordenDato, int i) throws WriteException
	{
		WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10); 
		WritableCellFormat format = new WritableCellFormat (arial10font); 
		
		Label label = new Label(1, 4, "Estación de Servicio: " + ordenDato.getEmpresa().getNombre() , format);
		sheet.addCell(label);		
	}

	private WritableSheet crearHoja(DatoConsultaOrdenesPorSelloYFechas ordenDato, int numeroHoja) throws RowsExceededException, WriteException {
		WritableSheet sheet = workbook.createSheet("Correctivo Nro: " + ordenDato.getNumero(), numeroHoja);
		
		WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 14); 
		arial10font.setBoldStyle(WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat (arial10font); 
 
		Label label = new Label(0, 2, "Listado de órdenes de " + sello.getNombre() +  " desde " + inicio + " hasta " + fin, format);
		sheet.addCell(label);
		return sheet;
	}
}
