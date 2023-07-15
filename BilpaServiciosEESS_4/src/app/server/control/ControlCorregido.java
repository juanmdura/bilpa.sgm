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

import org.apache.commons.codec.binary.Base64;

import app.client.dominio.Corregido;
import app.client.dominio.EstadoPendiente;
import app.client.dominio.Foto;
import app.client.dominio.ItemChequeado;
import app.client.dominio.Pendiente;
import app.client.dominio.Solucion;
import app.client.dominio.data.CorregidoData;
import app.client.dominio.data.PendienteData;
import app.client.dominio.json.SolucionJson;
import app.server.persistencia.CorregidoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.ItemChequeadoDao;
import app.server.persistencia.OrdenDao;
import app.server.persistencia.PendienteDao;
import app.server.propiedades.PropiedadEntorno;
import app.server.propiedades.PropiedadUrlFoto;

public class ControlCorregido {

	private static ControlCorregido instancia = null;

	public static ControlCorregido getInstancia() {
		if(instancia == null){
			instancia = new ControlCorregido();
		}
		return instancia;
	}


	private ControlCorregido (){

	}


	public void guardarCorregido(Corregido corregidoParam, PendienteData pendiente, int idItemChequeado, String fotoStr, String foto2Str) throws Exception {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();

			CorregidoDao corregidoDao = new CorregidoDao();

			Corregido corregido;
			if(corregidoParam.getId() > 0){//update
				corregido = corregidoDao.get(corregidoParam.getId());
				corregido.merge(corregidoParam);
				corregidoDao.merge(corregido);

			} else {//insert
				corregido = Corregido.getNewInstance(corregidoParam.getTipo());
				corregido.merge(corregidoParam);
				corregidoDao.save(corregido);

			}

			ItemChequeadoDao daoIC = new ItemChequeadoDao();
			ItemChequeado ic = daoIC.get(idItemChequeado);
			boolean existe = false;
			for(Corregido c : ic.getListaDeCorregidos()){
				if (c.getId() == corregido.getId()){
					c = corregido;
					existe = true;
				}
			}
			if (!existe){
				ic.getListaDeCorregidos().add(corregido);
			}
			ic.setValor("R");
			daoIC.save(ic);


			byte[] fotoBytes = (fotoStr != null && fotoStr.length() > 0) ? new Base64().decode(fotoStr.getBytes()) : null;
			byte[] foto2Bytes = (foto2Str != null && foto2Str.length() > 0) ? new Base64().decode(foto2Str.getBytes()) : null;

			if (fotoBytes != null) {
				String path = agregarImagen(fotoBytes, corregido.getId());
				setFoto(corregido, path);
				corregidoDao.merge(corregido);
			}

			if (foto2Bytes != null) {
				String path2 = agregarImagen2(foto2Bytes, corregido.getId());
				setFoto2(corregido, path2);
				corregidoDao.merge(corregido);
			}
			updatePendiente(pendiente, corregido);
			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}

	}
	
	private void updatePendiente(PendienteData pendienteData, Corregido corregido) {
		if (pendienteData != null){
			PendienteDao pendienteoDao = new PendienteDao();
			Pendiente pendiente = pendienteoDao.get(pendienteData.getId());

			if(pendiente != null) {
				pendiente.setEstado(EstadoPendiente.REPARADO);
				pendiente.setCorregido(corregido);
				pendiente.setFechaReparado(new Date());
				pendienteoDao.save(pendiente);
			}
		}
	}


	private void setFoto(Corregido corregido, String path) {

		if (!path.isEmpty()) {
			if (corregido.getFoto() != null) {
				corregido.getFoto().setPath(path);
				String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
				if(new PropiedadEntorno().getEntorno().equals("produccion")){
					corregido.getFoto().setUrl("http://179.27.66.44:6912/produccion/fotos/" + nombreArchivo);	
				}else{
					corregido.getFoto().setUrl("http://179.27.66.44:6912/testing/fotos/" + nombreArchivo);
				}
			} else {
				Foto foto = new Foto();
				foto.setPath(path);
				String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
				if(new PropiedadEntorno().getEntorno().equals("produccion")){
					foto.setUrl("http://179.27.66.44:6912/produccion/fotos/" + nombreArchivo);	
				}else{
					foto.setUrl("http://179.27.66.44:6912/testing/fotos/" + nombreArchivo);
				}
				corregido.setFoto(foto);
			}
			if(new PropiedadEntorno().getEntorno().equals("produccion")){
				String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
				corregido.getFoto().setUrl("http://179.27.66.44:6912/produccion/fotos/" + nombreArchivo);	
			}else{
				String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
				corregido.getFoto().setUrl("http://179.27.66.44:6912/testing/fotos/" + nombreArchivo);
			}
		}

	}

	private void setFoto2(Corregido corregido, String path) {

		if (!path.isEmpty()) {
			if (corregido.getFoto2() != null) {
				corregido.getFoto2().setPath(path);
				String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
				if(new PropiedadEntorno().getEntorno().equals("produccion")){
					corregido.getFoto2().setUrl("http://179.27.66.44:6912/produccion/fotos/" + nombreArchivo);	
				}else{
					corregido.getFoto2().setUrl("http://179.27.66.44:6912/testing/fotos/" + nombreArchivo);
				}
			} else {
				Foto foto = new Foto();
				foto.setPath(path);
				String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
				if(new PropiedadEntorno().getEntorno().equals("produccion")){
					foto.setUrl("http://179.27.66.44:6912/produccion/fotos/" + nombreArchivo);	
				}else{
					foto.setUrl("http://179.27.66.44:6912/testing/fotos/" + nombreArchivo);
				}
				corregido.setFoto2(foto);
			}
			if(new PropiedadEntorno().getEntorno().equals("produccion")){
				String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
				corregido.getFoto2().setUrl("http://179.27.66.44:6912/produccion/fotos/" + nombreArchivo);	
			}else{
				String nombreArchivo = ArchivoUtil.getNombreArchivo(path); 
				corregido.getFoto2().setUrl("http://179.27.66.44:6912/testing/fotos/" + nombreArchivo);
			}
		}

	}

	private String agregarImagen(byte[] bytes, int idCorregido) throws IOException {

		if (bytes != null) {
			DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			String path = new PropiedadUrlFoto().getURLPropertie(false);

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String pathRelative = idCorregido + "_" + format.format(new Date()) + ".png";
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

	private String agregarImagen2(byte[] bytes, int idCorregido) throws IOException {

		if (bytes != null) {
			DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			String path = new PropiedadUrlFoto().getURLPropertie(false);

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String pathRelative = idCorregido + "_2_" + format.format(new Date()) + ".png";
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

	public CorregidoData obtenerCorregidoData(int idCorregido) throws Exception {
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			CorregidoDao dao = new CorregidoDao();
			Corregido corregido = dao.get(idCorregido);
			if (corregido != null){
				CorregidoData data = corregido.getData();
				
				List<ItemChequeado> ics = new ItemChequeadoDao().getByCorregido(corregido);
				if (ics != null && ics.size() > 0){
					data.setIdItemChequeado(ics.get(0).getId());
					data.setTextoItemChequado(ics.get(0).getItemChequeo().getTexto());
				}
				return data;
			}
			return null;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}
	}

	public Corregido obtenerCorregido(int idCorregido) throws Exception {
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			CorregidoDao dao = new CorregidoDao();
			return dao.get(idCorregido);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}
	}


	public List<CorregidoData> obtenerCorregidos(int idPreventivo) throws Exception {
		DaoTransaction tx = new DaoTransaction();
		List<CorregidoData> corregidosData = new ArrayList<CorregidoData>();

		try {
			tx.begin();

			CorregidoDao dao = new CorregidoDao();
			List<Corregido> corregidos = dao.getCorregidos(idPreventivo);
			for (Corregido corregido : corregidos){
				CorregidoData data = corregido.getData();
				corregidosData.add(data);
				
				List<ItemChequeado> ics = new ItemChequeadoDao().getByCorregido(corregido);
				if (ics != null && ics.size() > 0){
					data.setIdItemChequeado(ics.get(0).getId());
					data.setTextoItemChequado(ics.get(0).getItemChequeo().getTexto());
				}
			}
			return corregidosData;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}
	}


	public boolean eliminarCorregido(int idCorregido) throws Exception {

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			CorregidoDao dao = new CorregidoDao();
			dao.delete(idCorregido);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return false;

	}

	public List<CorregidoData> obtenerCorregidosPorChequeo(int idItemChequeado) {
		DaoTransaction tx = new DaoTransaction();
		List<CorregidoData> corregidosData = new ArrayList<CorregidoData>();

		try {
			tx.begin();

			ItemChequeadoDao dao = new ItemChequeadoDao();
			ItemChequeado ic = dao.get(idItemChequeado);

			if(ic != null) {
				if(ic.getListaDeCorregidos() != null && !ic.getListaDeCorregidos().isEmpty()){
					for (Corregido corregido : ic.getListaDeCorregidos()){
						CorregidoData cd = corregido.getData();
						cd.setIdItemChequeado(idItemChequeado);
						cd.setTextoItemChequado(ic.getItemChequeo().getTexto());
						corregidosData.add(cd);
					}
				}
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return corregidosData;
	}
}
