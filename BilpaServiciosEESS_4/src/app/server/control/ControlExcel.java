package app.server.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import app.client.dominio.Orden;
import app.client.dominio.Organizacion;
import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Sello;
import app.client.dominio.Solucion;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.OrdenDao;
import app.server.persistencia.RepuestoLineaDao;

public class ControlExcel extends ControlExcelCommon{
	
	public ControlExcel(){}


	private void crearExcel(Workbook workbook, Sello selloSeleccionado, Date inicio, Date fin) throws IOException {

		Sheet libro = workbook.createSheet("Cierre Petrobras");

		crearCabezal(workbook,libro);

		crearDatosOrdenes(workbook,libro,selloSeleccionado,inicio,fin);

		datosIniciales(workbook,libro,inicio,fin);

		autoSizeColumnas(libro);	
	}

	

	private void crearDatosOrdenes(Workbook workbook, Sheet libro, Sello selloSeleccionado, Date inicio, Date fin) {
		DaoTransaction tx = new DaoTransaction();
		List<Orden> ordenes = new ArrayList<Orden>();

		try {
			tx.begin();
			ordenes = new OrdenDao().ordenesFinalizadasCierrePetroleras(selloSeleccionado, inicio, fin);

			int cantidadDeFilas = 0;
			for (Orden orden : ordenes) {
				if(orden.getEmpresa().getSello().getId() == selloSeleccionado.getId()){

					cantidadDeFilas++;
					CellStyle estiloCeldaComun = estiloCeldaComun(workbook, cantidadDeFilas);

					SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");

					String estacion = orden.getEmpresa().getNombre();			//0
					String fechaReclamo = sdf.format(orden.getFechaInicio());	//1
					String fechaVisita = orden.getInicioService() != null ? sdf.format(orden.getInicioService()) : "";	//2
					String numeroOrden = String.valueOf(orden.getNumero());		//3
					String activo;												//4
					String fallaReportada = null;								//5
					String fallaTecnica = null;									//6
					String tarea = null;										//7
					String repuesto;											//8
					double precio;												//9
					double cantidad;											//10
					double total; 												//11
					String destinoDelCargo = "Estación";						//12
					String comentario = NA;										//13

					for (Reparacion reparacion : orden.getReparaciones()) {
						activo = reparacion.getActivo().toString();
						//Set<RepuestoLinea> repuestosDeReparacion = orden.getRepuestosLinea(reparacion.getActivo());
						List<RepuestoLinea> repuestosDeReparacionList = new RepuestoLineaDao().obtenerRepuestosLinea(orden, reparacion.getActivo());
						
						Set<RepuestoLinea> repuestosDeReparacion = new TreeSet<RepuestoLinea>();
						Set<RepuestoLinea> repuestosDeReparacionCopiados = new TreeSet<RepuestoLinea>();
						
						for(RepuestoLinea rl : repuestosDeReparacionList){
							repuestosDeReparacionCopiados.add(rl.copiar());
						}
						repuestosDeReparacion.addAll(repuestosDeReparacionList);
						
						if(reparacion.getFallaReportada() != null){
							fallaReportada = reparacion.getFallaReportada().getDescripcion();	
						}

						for (Solucion solucion : reparacion.getSoluciones()) {
							if(solucion.getFallaTecnica() != null){
								fallaTecnica = solucion.getFallaTecnica().toString();
							}

							if(solucion.getTarea() != null){
								tarea = solucion.getTarea().toString();
							}
							
							if(solucion.getDestinoDelCargo() != null){
								destinoDelCargo = solucion.getDestinoDelCargo().getNombre();
							}
							
							if(solucion.getComentario() != null){
								comentario = solucion.getComentario().getTexto();
							}
							
							Set<RepuestoLinea> repuestosDeSolucion = solucion.obtenerRepuestosLinea(repuestosDeReparacionCopiados);
							if(repuestosDeSolucion.size() > 0){
								for(RepuestoLinea r : repuestosDeSolucion){

									int iteradorAux = libro.getLastRowNum()+1;
									Row filaAux = libro.createRow(iteradorAux);

									crearCelda(filaAux, 0, estacion, estiloCeldaComun);				
									crearCelda(filaAux, 1, fechaReclamo, estiloCeldaComun);
									crearCelda(filaAux, 2, fechaVisita, estiloCeldaComun);
									crearCelda(filaAux, 3, numeroOrden, estiloCeldaComun);
									crearCelda(filaAux, 4, activo, estiloCeldaComun);
									crearCelda(filaAux, 5, fallaReportada, estiloCeldaComun);
									crearCelda(filaAux, 6, fallaTecnica, estiloCeldaComun);
									crearCelda(filaAux, 7, tarea, estiloCeldaComun);

									repuesto = r.getRepuesto().toString();
									precio = r.getRepuesto().getPrecio();
									cantidad = r.getCantidad();
									total = precio * cantidad; 

									crearCelda(filaAux, 8, repuesto, estiloCeldaComun);
									crearCelda(filaAux, 9, String.valueOf(precio), estiloCeldaComun);
									crearCelda(filaAux, 10, String.valueOf(cantidad), estiloCeldaComun);
									crearCelda(filaAux, 11, String.valueOf(total), estiloCeldaComun);	
									crearCelda(filaAux, 12, destinoDelCargo, estiloCeldaComun);
									crearCelda(filaAux, 13, comentario, estiloCeldaComun);
								}

							}else{
								int iteradorAux = libro.getLastRowNum()+1;
								Row filaAux = libro.createRow(iteradorAux);

								crearCelda(filaAux, 0, estacion, estiloCeldaComun);				
								crearCelda(filaAux, 1, fechaReclamo, estiloCeldaComun);
								crearCelda(filaAux, 2, fechaVisita, estiloCeldaComun);
								crearCelda(filaAux, 3, numeroOrden, estiloCeldaComun);
								crearCelda(filaAux, 4, activo, estiloCeldaComun);
								crearCelda(filaAux, 5, fallaReportada, estiloCeldaComun);
								crearCelda(filaAux, 6, fallaTecnica, estiloCeldaComun);
								crearCelda(filaAux, 7, tarea, estiloCeldaComun);
								crearCelda(filaAux, 8, NA, estiloCeldaComun);
								crearCelda(filaAux, 9, NA, estiloCeldaComun);
								crearCelda(filaAux, 10, NA, estiloCeldaComun);	
								crearCelda(filaAux, 11, NA, estiloCeldaComun);	
								crearCelda(filaAux, 12, destinoDelCargo, estiloCeldaComun);
								crearCelda(filaAux, 13, comentario, estiloCeldaComun);
							}
						}	

						Set<RepuestoLinea> repuestosDeReparacionSinSolucion = orden.obtenerRepuestosLineasSinSolucion(reparacion);
						for(RepuestoLinea r : repuestosDeReparacionSinSolucion){

							int iteradorAux = libro.getLastRowNum()+1;
							Row filaAux = libro.createRow(iteradorAux);

							crearCelda(filaAux, 0, estacion, estiloCeldaComun);				
							crearCelda(filaAux, 1, fechaReclamo, estiloCeldaComun);
							crearCelda(filaAux, 2, fechaVisita, estiloCeldaComun);
							crearCelda(filaAux, 3, numeroOrden, estiloCeldaComun);
							crearCelda(filaAux, 4, activo, estiloCeldaComun);
							crearCelda(filaAux, 5, fallaReportada, estiloCeldaComun);
							crearCelda(filaAux, 6, NA, estiloCeldaComun);
							crearCelda(filaAux, 7, NA, estiloCeldaComun);

							repuesto = r.getRepuesto().toString();
							precio = r.getRepuesto().getPrecio();
							cantidad = r.getCantidad();
							total = precio * cantidad; 

							crearCelda(filaAux, 8, repuesto, estiloCeldaComun);
							crearCelda(filaAux, 9, String.valueOf(precio), estiloCeldaComun);
							crearCelda(filaAux, 10, String.valueOf(cantidad), estiloCeldaComun);
							crearCelda(filaAux, 11, String.valueOf(total), estiloCeldaComun);	
							crearCelda(filaAux, 12, destinoDelCargo, estiloCeldaComun);
							crearCelda(filaAux, 13, comentario, estiloCeldaComun);
						}
					}
				}
			}
			totalfilas = cantidadDeFilas;
			System.out.println("cantidad de ordenes = " + cantidadDeFilas);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}
	}

	private void datosIniciales(Workbook workbook, Sheet libro, Date inicio, Date fin) {
		CellStyle estiloCeldaCabezal = estiloCabezal(workbook);

		Row fila = libro.createRow(1);
		fila.setHeight((short)400);

		SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
		String fInicio = sdf.format(inicio);
		String fFin = sdf.format(fin);

		crearCelda(fila,0,"Fechas contempladas: ",estiloCeldaCabezal);
		crearCelda(fila,1,fInicio + " al " + fFin,estiloCeldaCabezal);

		Row fila2 = libro.createRow(2);
		fila2.setHeight((short)400);

		crearCelda(fila2,0,"Total de ordenes: ",estiloCeldaCabezal);
		crearCelda(fila2,1,totalfilas+"",estiloCeldaCabezal);
	}

	private void crearCabezal(Workbook workbook, Sheet libro) {
		Row fila = libro.createRow(4);

		fila.setHeight((short)400);

		crearCeldasCabezal(workbook, fila);
	}

	private void crearCeldasCabezal(Workbook workbook, Row fila) {
		CellStyle estiloCeldaCabezal = estiloCabezal(workbook); 

		crearCelda(fila,totalColumnas++,"Estación",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Fecha reclamo",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Fecha visita",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Nro orden",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Activo",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Falla reportada",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Falla técnica",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Tarea",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Repuesto",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Precio",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Cantidad",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Total",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Destino del cargo",estiloCeldaCabezal);
		crearCelda(fila,totalColumnas++,"Comentario",estiloCeldaCabezal);
	}

	private void crearCelda(Row fila, int posicion, String valor, CellStyle estilo){
		Cell celda = fila.createCell(posicion);
		celda.setCellValue(valor);
		celda.setCellStyle(estilo);
	}

	@Override
	public File crearExcel(Sello selloSeleccionado, Date inicio, Date fin) {
		FileOutputStream out  = null;
		File salida = null;

		try {

			salida = File.createTempFile("cierre_petrobras_", ".xls");

			out = new FileOutputStream(salida);

			Workbook workbook = new HSSFWorkbook();
			crearExcel(workbook,selloSeleccionado,inicio,fin);
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


	@Override
	public File crearExcel(Date inicio, Date fin, List<Organizacion> organizaciones) {
		return null;
	}

}
