package app.server.control;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import app.client.dominio.Organizacion;
import app.client.dominio.Sello;

public abstract class ControlExcelCommon {

	protected final String NA = "N/A";
	protected int totalColumnas = 0;
	protected int totalfilas = 0;

	public int getTotalColumnas() {
		return totalColumnas;
	}

	public void setTotalColumnas(int totalColumnas) {
		this.totalColumnas = totalColumnas;
	}

	public ControlExcelCommon(){}

	public abstract File crearExcel(Sello selloSeleccionado, Date inicio, Date fin);
	
	public abstract File crearExcel(Date inicio, Date fin, List<Organizacion> organizaciones);
	
	public CellStyle estiloCabezal(Workbook workbook) {

		CellStyle estiloCeldaCabezal = workbook.createCellStyle();

		estiloCeldaCabezal.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloCeldaCabezal.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloCeldaCabezal.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		estiloCeldaCabezal.setFillForegroundColor(new HSSFColor.CORNFLOWER_BLUE().getIndex()); 

		estiloCeldaCabezal.setBorderTop(HSSFCellStyle.BORDER_THIN);
		estiloCeldaCabezal.setBorderRight(HSSFCellStyle.BORDER_THIN);
		estiloCeldaCabezal.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloCeldaCabezal.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		Font fuente = fuenteCabezal(workbook);

		estiloCeldaCabezal.setFont(fuente);

		return estiloCeldaCabezal;
	}
	
	private Font fuenteCabezal(Workbook workbook) {
		Font fuente = workbook.createFont();
		fuente.setFontName("Arial");
		fuente.setColor(HSSFColor.BLACK.index);
		fuente.setBoldweight((short)1222);
		return fuente;
	}

	private Font generarFuente(Workbook workbook) {
		Font fuente = workbook.createFont();
		fuente.setFontName("Arial");
		fuente.setColor(HSSFColor.BLACK.index);
		return fuente;
	}
	
	protected CellStyle estiloCeldaComun(Workbook workbook, int fila) {

		CellStyle estiloCeldaComun = workbook.createCellStyle();
		
		estiloCeldaComun.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloCeldaComun.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloCeldaComun.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		estiloCeldaComun.setBorderTop(HSSFCellStyle.BORDER_THIN);
		estiloCeldaComun.setBorderRight(HSSFCellStyle.BORDER_THIN);
		estiloCeldaComun.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloCeldaComun.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		if(fila % 2 == 0){
			estiloCeldaComun.setFillForegroundColor(new HSSFColor.CORNFLOWER_BLUE().getIndex());
		}else{
			estiloCeldaComun.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
		}

		
		Font fuente = generarFuente(workbook);

		estiloCeldaComun.setFont(fuente);

		return estiloCeldaComun;
	}
	
	private CellStyle estiloCeldaComunNumerica(Workbook workbook, int fila) {

		CellStyle estiloCeldaComun = workbook.createCellStyle();
		
		DataFormat format = workbook.createDataFormat();
		
		estiloCeldaComun.setDataFormat(format.getFormat("#.##0,00;-#.##0,00"));
		
		estiloCeldaComun.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloCeldaComun.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		estiloCeldaComun.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		estiloCeldaComun.setBorderTop(HSSFCellStyle.BORDER_THIN);
		estiloCeldaComun.setBorderRight(HSSFCellStyle.BORDER_THIN);
		estiloCeldaComun.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estiloCeldaComun.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		if(fila % 2 == 0){
			estiloCeldaComun.setFillForegroundColor(new HSSFColor.CORNFLOWER_BLUE().getIndex());
		}else{
			estiloCeldaComun.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
		}

		

		Font fuente = generarFuente(workbook);

		estiloCeldaComun.setFont(fuente);

		return estiloCeldaComun;
	}
	
	protected void autoSizeColumnas(Sheet libro) {
		for (int i = 0; i <= totalColumnas; i++) {
			libro.autoSizeColumn(i);
		}
	}
}
