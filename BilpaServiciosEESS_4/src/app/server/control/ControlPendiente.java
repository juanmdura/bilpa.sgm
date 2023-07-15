package app.server.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import app.client.dominio.Activo;
import app.client.dominio.Estacion;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Foto;
import app.client.dominio.ItemChequeado;
import app.client.dominio.Orden;
import app.client.dominio.Pendiente;
import app.client.dominio.Preventivo;
import app.client.dominio.Reparacion;
import app.client.dominio.Sello;
import app.client.dominio.data.PendienteData;
import app.client.dominio.data.PendienteDataList;
import app.client.dominio.data.PendienteDataUI;
import app.server.persistencia.ActivoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.EstacionDao;
import app.server.persistencia.ItemChequeadoDao;
import app.server.persistencia.OrdenDao;
import app.server.persistencia.PendienteDao;
import app.server.persistencia.PersonaDao;
import app.server.persistencia.PreventivoDao;
import app.server.persistencia.ReparacionDao;
import app.server.propiedades.PropiedadEntorno;
import app.server.propiedades.PropiedadUrlFotoPendiente;

public class ControlPendiente {

	private static ControlPendiente instancia = null;

	public static ControlPendiente getInstancia() {
		if(instancia == null){
			instancia = new ControlPendiente();
		}
		return instancia;
	}

	private ControlPendiente() {
		super();
	}


	public PendienteData obtenerPendiente(int idPendiente) {

		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PendienteDao pendienteoDao = new PendienteDao();
			Pendiente pendiente = pendienteoDao.get(idPendiente);

			if(pendiente != null) {
				PendienteData pd = pendiente.getPendienteData();
				pd.setFechaReparado(app.server.UtilFechas.getFormattedDate(pendiente.getFechaReparado()));
				
				return pd;
			} else {
				return null;
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;

	}

	public List<PendienteDataList> obtenerPendientesPreventivos(int idPreventivo) {
		DaoTransaction tx = new DaoTransaction(); 
		List<PendienteDataList> retorno = new ArrayList<PendienteDataList>();
		try {
			tx.begin();
			PreventivoDao preventivoDao = new PreventivoDao();
			Preventivo preventivo = preventivoDao.get(idPreventivo);

			if(preventivo.getChequeo().getItemsChequeados() != null && !preventivo.getChequeo().getItemsChequeados().isEmpty()) {
				for(ItemChequeado ic : preventivo.getChequeo().getItemsChequeados()) {
					if(ic.getListaDePendientes() != null && !ic.getListaDePendientes().isEmpty()){
						for(Pendiente pendiente : ic.getListaDePendientes()) {
							PendienteDataList pdl = pendiente.getPendienteDataList();
							retorno.add(pdl);
							
							List<ItemChequeado> ics = new ItemChequeadoDao().getByPendiente(pendiente);
							if (ics != null && ics.size() > 0){
								pdl.setIdItemChequeado(ics.get(0).getId());
								pdl.setTextoItemChequado(ics.get(0).getItemChequeo().getTexto());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return retorno;
	}

	public boolean guardarPendiente(PendienteData pd, String fotoStr) throws Exception {
		Activo activo = null;
		if (pd.getIdPreventivo() > 0){
			activo = ControlActivo.getInstancia().
					buscarActivo(ControlPreventivo.getInstancia().obtenerPreventivo(pd.getIdPreventivo()).getIdActivo());
		} else {
			activo = ControlActivo.getInstancia().
					buscarActivo(ControlActivo.getInstancia().buscarActivo(pd.getIdActivo()).getId());
		}

		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PendienteDao pendienteDao = new PendienteDao();

			Pendiente pendiente;
			boolean isUpdate = pd.getId() > 0;

			if(isUpdate){//update
				pendiente = pendienteDao.get(pd.getId());
				if (pd.esPendientePreventivo()){
					setItemChequeado(pd, pendiente);
				}
			} else {//insert
				if (pd.esPendientePreventivo()){
					pendiente = new Pendiente();
					pendiente.setPreventivo(new PreventivoDao().get(pd.getIdPreventivo()));
					setItemChequeado(pd, pendiente);
				} else {
					pendiente = new Pendiente();

					if (pd.getOrdenCreado() > 0){
						pendiente.setOrdenCreado(new OrdenDao().buscarOrden(pd.getOrdenCreado()));
					}
				}
			}

			if (pd.esPendientePreventivo()){
				ItemChequeadoDao daoItemChequeado = new ItemChequeadoDao();
				ItemChequeado itemChequeado = daoItemChequeado.get(pd.getIdItemChequeado());
				itemChequeado.setPendiente(true);
				daoItemChequeado.update(itemChequeado);
			}
			
			pendiente.merge(pd);
			pendiente.setActivo(activo);
			if (pd.getIdCreador() > 0){
				pendiente.setCreador(new PersonaDao().get(pd.getIdCreador()));
			}
			setFoto(fotoStr, pendiente, pd.esPendientePreventivo());

			if(isUpdate){//update
				pendienteDao.merge(pendiente);
			} else {
				pendienteDao.save(pendiente);
			}

			tx.commit();
			return true;

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			tx.close();
		}

	}

	private void setItemChequeado(PendienteData pd, Pendiente pendiente) {
		if (pd.getIdItemChequeado() > 0){
			ItemChequeadoDao daoIC = new ItemChequeadoDao();
			ItemChequeado ic = daoIC.get(pd.getIdItemChequeado());
			boolean existe = false;
			for(Pendiente p : ic.getListaDePendientes()){
				if (p.getId() == pendiente.getId()){
					p = pendiente;
					existe = true;
				}
			}
			if (!existe && pendiente.getClass().equals(Pendiente.class)){
				ic.getListaDePendientes().add((Pendiente)pendiente);
			}
			daoIC.save(ic);
		}
	}

	private void setFoto(String fotoStr, Pendiente pendiente, boolean esPreventivo) throws IOException {
		String carpeta = esPreventivo ? "fotos_pendientes/" : "fotos_pendientes_correctivo/";

		byte[] fotoBytes = (fotoStr != null && fotoStr.length() > 0) ? new Base64().decode(fotoStr.getBytes()) : null;
		if (fotoBytes != null) {
			String path = agregarImagen(fotoBytes, pendiente.getId(), esPreventivo);
			if (!path.isEmpty()) {

				if (pendiente.getFoto() != null) {

					pendiente.getFoto().setPath(path);
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					if(new PropiedadEntorno().getEntorno().equals("produccion")){
						pendiente.getFoto().setUrl("http://179.27.66.44:6912/produccion/" + carpeta + nombreArchivo);	
					}else{
						pendiente.getFoto().setUrl("http://179.27.66.44:6912/testing/" + carpeta + nombreArchivo);
					}
				} else {

					Foto foto = new Foto();
					foto.setPath(path);
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					if(new PropiedadEntorno().getEntorno().equals("produccion")){
						foto.setUrl("http://179.27.66.44:6912/produccion/" + carpeta + nombreArchivo);	
					}else{
						foto.setUrl("http://179.27.66.44:6912/testing/" + carpeta + nombreArchivo);
					}
					pendiente.setFoto(foto);

				}
				if(new PropiedadEntorno().getEntorno().equals("produccion")){
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					pendiente.getFoto().setUrl("http://179.27.66.44:6912/produccion/" + carpeta + nombreArchivo);	
				}else{
					String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
					pendiente.getFoto().setUrl("http://179.27.66.44:6912/testing/" + carpeta + nombreArchivo);
				}
			}
		}
	}

	private String agregarImagen(byte[] bytes, int idPendiente, boolean esPreventivo) throws IOException {

		if (bytes != null) {

			DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			String path = new PropiedadUrlFotoPendiente().getURLPropertie();

			if (!esPreventivo){
				path = path.substring(0, path.length()-1);
				path += "_correctivo/";
			}
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String pathRelative = idPendiente + "_" + format.format(new Date()) + ".png";
			String pathAbsolute = path + pathRelative;
			File foto = new File(pathAbsolute);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(foto);
				fos.write(bytes);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw e;
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
			return pathAbsolute;

		}
		return "";

	}

	public boolean eliminarPendientes(int idPendiente) {

		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PendienteDao pendienteoDao = new PendienteDao();
			pendienteoDao.delete(idPendiente);
			tx.commit();
			return true;

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return false;
	}

	public List<PendienteDataList> obtenerPendientesPorChequeo( int idItemChequeado) {
		DaoTransaction tx = new DaoTransaction(); 
		List<PendienteDataList> retorno = new ArrayList<PendienteDataList>();
		try {
			tx.begin();
			ItemChequeadoDao dao = new ItemChequeadoDao();
			ItemChequeado ic = dao.get(idItemChequeado);

			if(ic != null) {
				if(ic.getListaDePendientes() != null && !ic.getListaDePendientes().isEmpty()){
					for(Pendiente p : ic.getListaDePendientes()) {
						PendienteDataList pdl = p.getPendienteDataList();
						pdl.setIdItemChequeado(idItemChequeado);
						retorno.add(pdl);
					}
				}
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return retorno;
	}

	public List<PendienteData> obtenerPendientesDeActivo(int idActivo, EstadoPendiente estadoPendiente) {
		DaoTransaction tx = new DaoTransaction(); 
		List<PendienteData> retorno = new ArrayList<PendienteData>();
		try {
			tx.begin();
			ActivoDao daoActivo = new ActivoDao();
			Activo activo = daoActivo.get(idActivo);
			if (activo != null){
				PendienteDao daoPendiente = new PendienteDao();
				List<Pendiente> pendientes = daoPendiente.obtenerPendientesActivosDeActivo(activo, estadoPendiente);
				for (Pendiente pendiente : pendientes){
					PendienteData pd = pendiente.getPendienteData();
					pd.setFechaReparado(app.server.UtilFechas.getFormattedDate(pendiente.getFechaReparado()));
					retorno.add(pd);
				}
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return retorno;
	}

	public List<PendienteDataList> obtenerPendientesListDeActivo(int idActivo, EstadoPendiente estadoPendiente) {
		DaoTransaction tx = new DaoTransaction(); 
		List<PendienteDataList> retorno = new ArrayList<PendienteDataList>();
		try {
			tx.begin();
			ActivoDao daoActivo = new ActivoDao();
			Activo activo = daoActivo.get(idActivo);
			if (activo != null){
				PendienteDao daoPendiente = new PendienteDao();
				List<Pendiente> pendientes = daoPendiente.obtenerPendientesActivosDeActivo(activo, estadoPendiente);
				for (Pendiente pendiente : pendientes){
					PendienteDataList pdl = pendiente.getPendienteDataList();
					retorno.add(pdl);
					
					List<ItemChequeado> ics = new ItemChequeadoDao().getByPendiente(pendiente);
					if (ics != null && ics.size() > 0){
						pdl.setIdItemChequeado(ics.get(0).getId());
						pdl.setTextoItemChequado(ics.get(0).getItemChequeo().getTexto());
					}
				}
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return retorno;
	}

	public boolean descartarPendiente(List<PendienteData> pendientes, String motivoDescarte, int idPersona) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PendienteDao pendienteoDao = new PendienteDao();
			for (PendienteData pd : pendientes) {
				Pendiente pendiente = pendienteoDao.get(pd.getId());

				pendiente.setEstado(EstadoPendiente.DESCARTADO);
				pendiente.setMotivoDescarte(motivoDescarte);
				pendiente.setDescartador(new PersonaDao().get(idPersona));
				pendiente.setFechaDescarte(new Date());
				pendienteoDao.merge(pendiente);
			}

			tx.commit();
			return true;

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		return false;
	}

	public PendienteDataUI obtenerPendientes(int idSello, EstadoPendiente estadoPendiente) {
		DaoTransaction tx = new DaoTransaction(); 
		PendienteDataUI data = new PendienteDataUI();
		List<PendienteData> pendientesData = new ArrayList<PendienteData>();
		List<String> activos = new ArrayList<String>();
		List<String> estaciones = new ArrayList<String>();
		
		data.setPendientes(pendientesData);
		data.setEstaciones(estaciones);
		data.setActivos(activos);

		try {
			tx.begin();
			PendienteDao dao = new PendienteDao();
			Sello sello = new Sello(idSello);
			List<Pendiente> pendientes = dao.obtenerPendientesPorSello(sello, estadoPendiente);
			for (Pendiente pendiente : pendientes){
				PendienteData pd = pendiente.getPendienteData();
				pd.setFechaReparado(app.server.UtilFechas.getFormattedDate(pendiente.getFechaReparado()));

				pendientesData.add(pd);
				if (!estaciones.contains(pd.getEmpresa())){
					estaciones.add(pd.getEmpresa());
				}
				if (!activos.contains(pd.getActivo())){
					activos.add(pd.getActivo());
				}
			}
			
			if (estadoPendiente.equals(EstadoPendiente.INICIADO)){
				data.setCantidadIniciados(pendientes.size());
			} else {
				data.setCantidadIniciados(dao.obtenerPendientesPorSello(sello, EstadoPendiente.INICIADO).size());
			}
			
			if (estadoPendiente.equals(EstadoPendiente.REPARADO)){
				data.setCantidadReparados(pendientes.size());
			} else {
				data.setCantidadReparados(dao.obtenerPendientesPorSello(sello, EstadoPendiente.REPARADO).size());
			}
			
			if (estadoPendiente.equals(EstadoPendiente.DESCARTADO)){
				data.setCantidadDescartados(pendientes.size());
			} else {
				data.setCantidadDescartados(dao.obtenerPendientesPorSello(sello, EstadoPendiente.DESCARTADO).size());
			}
			
			if (estadoPendiente.equals(EstadoPendiente.CORRECTIVO_ASIGNADO)){
				data.setCantidadAsignados(pendientes.size());
			} else {
				data.setCantidadAsignados(dao.obtenerPendientesPorSello(sello, EstadoPendiente.CORRECTIVO_ASIGNADO).size());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return data;
	}

	public boolean asignarCorrectivoAPendientes(int idSesion, int numeroOrden, List<PendienteData> pendientes) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ReparacionDao daoReparacion = new ReparacionDao();
			OrdenDao daoOrden = new OrdenDao();
			PendienteDao daoPendiente = new PendienteDao();
			
			Orden orden;
			if (numeroOrden == 0){
				orden = ControlOrden.getInstancia().crearOrdenReparacionDePendientes(idSesion, pendientes.get(0).getIdEstacion());
				numeroOrden = daoOrden.save(orden);
			}

			for (PendienteData pd : pendientes) {
				orden = daoOrden.get(numeroOrden);
				Reparacion reparacion = ControlOrden.getInstancia().crearReparacionDePendiente(orden, pd);
				daoReparacion.save(reparacion);
				
				Pendiente pendiente = daoPendiente.get(pd.getId());
				pendiente.setEstado(EstadoPendiente.CORRECTIVO_ASIGNADO);
				pendiente.setOrdenAsignada(orden);
				daoPendiente.save(pendiente);
			}
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public PendienteDataUI obtenerPendientesDeEstacion(int idEstacion) {
		DaoTransaction tx = new DaoTransaction(); 
		PendienteDataUI retorno = new PendienteDataUI();
		List<PendienteData> pendientesData = new ArrayList<PendienteData>();
		
		List<String> activos = new ArrayList<String>();
		List<String> estaciones = new ArrayList<String>();
		
		Estacion estacion = null;
		try {
			tx.begin();
			estacion = new EstacionDao().get(idEstacion);
			Set<Activo> activosBase = estacion.getListaDeActivos();
			PendienteDao pendienteDao = new PendienteDao();
			List<Pendiente> pendientes = new ArrayList<Pendiente>();
			for (Activo activo : activosBase) {
				pendientes.addAll(pendienteDao.obtenerPendientesDeActivo(activo));
			}
			for (Pendiente pendiente : pendientes) {
				PendienteData pd = pendiente.getPendienteData();
				pendientesData.add(pd);
				if (!activos.contains(pd.getActivo())){
					activos.add(pd.getActivo());
				}
			}
			estaciones.add(estacion.getNombre());
			retorno.setEstaciones(estaciones);
			retorno.setActivos(activos);
			retorno.setPendientes(pendientesData);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return retorno;
	}
}
