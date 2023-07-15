package app.server.control;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import app.client.dominio.ActivoGenerico;
import app.client.dominio.Chequeo;
import app.client.dominio.ChequeoPico;
import app.client.dominio.ChequeoProducto;
import app.client.dominio.ChequeoSurtidor;
import app.client.dominio.Corregido;
import app.client.dominio.ItemChequeado;
import app.client.dominio.ItemChequeo;
import app.client.dominio.Organizacion;
import app.client.dominio.Pico;
import app.client.dominio.Preventivo;
import app.client.dominio.Sello;
import app.client.dominio.TipoActivoGenerico;
import app.client.dominio.Visita;
import app.client.dominio.data.OrganizacionData;
import app.client.dominio.data.PreventivoData;
import app.client.dominio.json.ChequeoPicoJson;
import app.server.control.visita.reporte.web.ControlVisitaReporteWebDucsaYBilpa;
import app.server.persistencia.ActivoGenericoDao;
import app.server.persistencia.ChequeoSurtidorDao;
import app.server.persistencia.CorregidoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.PicoDao;
import app.server.persistencia.PreventivoDao;
import app.server.persistencia.VisitaDao;
import app.server.propiedades.PropiedadReporteVisitas;
import app.server.util.ZipUtils;


public class ControlPreventivo {

	private static ControlPreventivo instancia = null;

	public static ControlPreventivo getInstancia() {
		if(instancia == null){
			instancia = new ControlPreventivo();
		}
		return instancia;
	}

	private ControlPreventivo() {
		super();
	}

	public PreventivoData obtenerPreventivo(int idPreventivo){
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PreventivoDao preventivoDao = new PreventivoDao();
			Preventivo preventivo = preventivoDao.get(idPreventivo);

			if (preventivo != null){
				return preventivo.getPreventivoData();
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}

	public boolean actualizarPreventivo(int idPreventivo, Chequeo chequeo) throws Exception {

		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PreventivoDao preventivoDao = new PreventivoDao();
			Preventivo preventivo = preventivoDao.get(idPreventivo);

			TipoActivoGenerico tipoActivoGenerico = null;
			List<ActivoGenerico> activosGenericos = new ActivoGenericoDao().getById(preventivo.getActivo().getId());
			if(activosGenericos != null && activosGenericos.size() > 0){
				tipoActivoGenerico = activosGenericos.get(0).getTipoActivoGenerico();
			}

			if(chequeo.getItemsChequeados() == null || chequeo.getItemsChequeados().isEmpty()){
				List<ItemChequeo> items = ControlChequeo.getInstancia().obtenerItemsChequeo(chequeo.getTipo(), tipoActivoGenerico);

				chequeo.setItemsChequeados(new HashSet<ItemChequeado>());
				for(ItemChequeo itemChequeo : items) {
					ItemChequeado itemChequeado = new ItemChequeado();
					itemChequeado.setItemChequeo(itemChequeo);
					chequeo.getItemsChequeados().add(itemChequeado);
				}
			}

			if(preventivo.getChequeo() == null) {
				preventivo.setChequeo(chequeo);
			} else {
				if (chequeo.getClass().equals(ChequeoPico.class)){
					ChequeoSurtidor chequeoSurtidor = new ChequeoSurtidorDao().get(preventivo.getChequeo().getId());
					ChequeoPico chequeoPicoIn = (ChequeoPico)chequeo;
					ChequeoPico chequeoPicoDB = chequeoSurtidor.buscarChequeoPico(chequeoPicoIn.getPico().getId());
					if (chequeoPicoDB != null){
						chequeoPicoDB.merge(chequeoPicoIn);
					} else {
						chequeoSurtidor.getListaDeChequeosPicos().add(chequeoPicoIn);
					}

				} else if (chequeo.getClass().equals(ChequeoProducto.class)){
					ChequeoSurtidor chequeoSurtidor = new ChequeoSurtidorDao().get(preventivo.getChequeo().getId());
					ChequeoProducto chequeoProductoIn = (ChequeoProducto)chequeo;
					ChequeoProducto chequeoProductoDB = chequeoSurtidor.buscarChequeoProducto(chequeoProductoIn.getProducto().getId());
					if (chequeoProductoDB != null){
						chequeoProductoDB.merge(chequeoProductoIn);
					} else {
						chequeoSurtidor.getListaDeProductos().add(chequeoProductoIn);
					}

				} else {
					preventivo.getChequeo().merge(chequeo);
				}
			}

			preventivo.setUltimaModificacion(new Date());

			preventivoDao.update(preventivo);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}
	}

	public boolean actualizarPreventivo(int idPreventivo, ChequeoProducto producto) throws Exception {

		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PreventivoDao preventivoDao = new PreventivoDao();
			Preventivo preventivo = preventivoDao.get(idPreventivo);

			if(preventivo.getChequeo() == null) {
				return false;
			} else {
				ChequeoSurtidor cs = new ChequeoSurtidorDao().get(preventivo.getChequeo().getId());
				Set<ChequeoProducto> productos = cs.getListaDeProductos();
				if(productos.contains(producto)){
					for(ChequeoProducto p : productos){
						if(p.getId() == producto.getId()){
							p.setItemsChequeados(producto.getItemsChequeados());
							p.setProducto(producto.getProducto());
							break;
						}
					}
				} else {
					productos.add(producto);
				}
			}

			preventivo.setUltimaModificacion(new Date());
			preventivoDao.merge(preventivo);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}

	}

	public boolean actualizarPreventivo(ChequeoPicoJson chequeoPicoJson) throws Exception {

		ChequeoPico chequeoPico = new ChequeoPico();
		chequeoPico.setFromChequeoPicoJson(chequeoPicoJson);

		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PreventivoDao preventivoDao = new PreventivoDao();
			Preventivo preventivo = preventivoDao.get(chequeoPicoJson.getIdPreventivo());

			PicoDao picoDao = new PicoDao();
			Pico pico = picoDao.get(chequeoPicoJson.getIdPico());

			if(pico != null) {
				chequeoPico.setPico(pico);
			}

			if(preventivo.getChequeo() == null) {
				return false;
			} else {
				ChequeoSurtidorDao csd = new ChequeoSurtidorDao();
				ChequeoSurtidor cs = csd.get(preventivo.getChequeo().getId());
				Set<ChequeoPico> picos = cs.getListaDeChequeosPicos();
				if(picos.contains(chequeoPico)){
					for(ChequeoPico cp : picos){
						if(cp.getId() == chequeoPico.getId()){
							cp.merge(chequeoPico);
							break;
						}
					}
				} else {
					picos.add(chequeoPico);
				}
			}

			preventivo.setUltimaModificacion(new Date());
			preventivoDao.merge(preventivo);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}

	}

	public boolean validarCorregido(int idPreventivo, int idCorregido) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PreventivoDao preventivoDao = new PreventivoDao();
			Preventivo preventivo = preventivoDao.get(idPreventivo);

			CorregidoDao corregidoDao = new CorregidoDao();
			Corregido corregido = corregidoDao.get(idCorregido);

			if (preventivo != null && corregido != null && corregido.getPreventivo() != null && 
					preventivo.getId() == corregido.getPreventivo().getId()){
				return true;
			}

			/*for(ItemChequeado ic : preventivo.getChequeo().getItemsChequeados()) {
				for (Corregido corregido : ic.getListaDeCorregidos()){
					if (corregido.getId() == idCorregido){
						return true;
					}
				}
			}*/

			return false;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return false;
	}

	public List<OrganizacionData> getOrganizacionesData() {

		List<OrganizacionData> organizacionesData = new ArrayList<OrganizacionData>();
		for(Organizacion o : Organizacion.values()) {
			OrganizacionData od = new OrganizacionData();
			od.setOrganizacion(o.getOrganizacion());
			organizacionesData.add(od);
		}
		return organizacionesData;

	}

	public File reportePreventivos(Sello selloSeleccionado, Date inicio, Date fin, Organizacion operador) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			VisitaDao dao = new VisitaDao();
			List<Visita> visitas = dao.reporteVisitas(selloSeleccionado, inicio, fin);

			if (visitas != null && visitas.size() > 0){

				String path = new PropiedadReporteVisitas().getPath();

				File root = new File(path);

				if (!root.exists()) {
					root.mkdirs();
				} else {
					FileUtils.deleteDirectory(root);
					root.mkdirs();
				}

				/*File[] files = root.listFiles(new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.isDirectory();
					}
				});
				System.out.println("Folders count: " + files.length);

				Arrays.sort(files);*/

				/*int cantidadEjecuciones = files.length;
				if (cantidadEjecuciones > 3){*/
					/*for (int i = 0; i > 3 ; i--){
						try{
							File directory = files[i-1]; //files
							FileUtils.deleteDirectory(directory);
						} catch (Exception e){

						}
					}*/
				//}

				String date = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
				File folder = new File(path+"/"+date);
				folder.mkdir();
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				for (Visita visita : visitas) {
					if (map.get(visita.getEstacion().getId()) == null){
						map.put(visita.getEstacion().getId(), 1);
					} else {
						map.put(visita.getEstacion().getId(), map.get(visita.getEstacion().getId())+1);
					}
					String fechavisita = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(visita.getFechaFin());
					String nombre = visita.getEstacion().getNombre().replaceAll("\\s+","").replaceAll("[^a-zA-Z0-9]+","")+"_"+
							map.get(visita.getEstacion().getId())+ "_" + fechavisita +".pdf";

					File pdf = ControlVisitaReporteWebDucsaYBilpa.getInstancia().crearPDFPreventivosVisitas(visita.getId(), operador);
					FileUtils.copyFile(pdf, new File(folder.getAbsoluteFile()+"/"+nombre));
				}
				ZipUtils.zipFolder(folder.getAbsolutePath(), folder.getAbsoluteFile()+".zip");
				return new File (folder.getAbsoluteFile()+".zip");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}finally{
			tx.close();
		}
		return null;
	}
}
