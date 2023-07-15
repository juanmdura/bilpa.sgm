package app.server.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import app.client.dominio.Organizacion;
import app.client.dominio.Pendiente;
import app.client.dominio.Sello;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.PendienteDao;

public class ControlExcelPendiente extends ControlExcelCommon {

	public ControlExcelPendiente() {
	}

	public File crearExcel(Sello selloSeleccionado, Date inicio, Date fin) {
		return null;
	}

	private void crearExcel(Workbook workbook, Date inicio, Date fin,
			List<Organizacion> organizaciones) throws IOException {

		Sheet libro = workbook.createSheet("Informe pendientes");

		crearCabezal(workbook, libro);

		crearDatosOrdenes(workbook, libro, inicio, fin, organizaciones);

		autoSizeColumnas(libro);
	}

	@Override
	public File crearExcel(Date inicio, Date fin, List<Organizacion> organizaciones) {
		FileOutputStream out = null;
		File salida = null;
		try {
			salida = File.createTempFile("informe_pendientes_", ".xls");
			out = new FileOutputStream(salida);
			Workbook workbook = new HSSFWorkbook();
			crearExcel(workbook, inicio, fin, organizaciones);
			workbook.write(out);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return salida;
	}

	private void crearDatosOrdenes(Workbook workbook, Sheet libro, Date inicio,
			Date fin, List<Organizacion> organizaciones) {
		DaoTransaction tx = new DaoTransaction();
		List<Pendiente> pendientes = new ArrayList<Pendiente>();

		try {
			tx.begin();
			pendientes = new PendienteDao().pendientesVisibles(inicio, fin,
					organizaciones);

			int cantidadDeFilas = 0;

			for (Pendiente p : pendientes) {
				cantidadDeFilas++;
				CellStyle estiloCeldaComun = estiloCeldaComun(workbook,
						cantidadDeFilas);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

				int id = p.getId(); // 0
				String comentario = p.getComentario(); // 1

				String plazo = null;
				if (p.getPlazo() != null) {
					plazo = sdf.format(p.getPlazo()); // 2
				}
				String destinatario = null; // 3
				String activo = null; // 4
				String estacion = null; // 5

				if (p.getOrganizacion().toString() != null) {
					destinatario = p.getOrganizacion().toString();
				}

				if (p.getActivo() != null) {
					activo = p.getActivo().toString();
					if (p.getActivo().getEmpresa() != null) {
						estacion = p.getActivo().getEmpresa().toString();
					}
				}
				int iteradorAux = libro.getLastRowNum() + 1;
				Row filaAux = libro.createRow(iteradorAux);

				crearCelda(filaAux, 0, id + "", estiloCeldaComun);
				crearCelda(filaAux, 1, estacion, estiloCeldaComun);
				crearCelda(filaAux, 2, activo, estiloCeldaComun);
				crearCelda(filaAux, 3, comentario, estiloCeldaComun);
				crearCelda(filaAux, 4, plazo, estiloCeldaComun);
				crearCelda(filaAux, 5, destinatario, estiloCeldaComun);
			}

			totalfilas = cantidadDeFilas;
			System.out.println("cantidad de pendientes = " + cantidadDeFilas);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
	}

	private void crearCabezal(Workbook workbook, Sheet libro) {
		Row fila = libro.createRow(0);

		fila.setHeight((short) 400);

		crearCeldasCabezal(workbook, fila);
	}

	private void crearCeldasCabezal(Workbook workbook, Row fila) {
		CellStyle estiloCeldaCabezal = estiloCabezal(workbook);


		crearCelda(fila,totalColumnas++,"Número",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Estación",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Activo",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Comentario",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Plazo",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Destinatario",estiloCeldaCabezal);
	
	}

	private void crearCelda(Row fila, int posicion, String valor,
			CellStyle estilo) {
		Cell celda = fila.createCell(posicion);
		celda.setCellValue(valor);
		celda.setCellStyle(estilo);
	}
}
