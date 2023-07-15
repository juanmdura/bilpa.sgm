package app.server.control;

import java.util.ArrayList;
import java.util.List;

import app.client.dominio.ActivoGenerico;
import app.client.dominio.ItemChequeo;
import app.client.dominio.MarcaActivoGenerico;
import app.client.dominio.ModeloActivoGenerico;
import app.client.dominio.TipoActivoGenerico;
import app.client.dominio.TipoChequeo;
import app.client.dominio.data.ItemChequeoData;
import app.client.dominio.data.TipoActivoGenericoData;
import app.server.persistencia.ActivoGenericoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.ItemChequeoDao;
import app.server.persistencia.MarcaActivoGenericoDao;
import app.server.persistencia.ModeloActivoGenericoDao;
import app.server.persistencia.TipoActivoGenericoDao;

public class ControlActivosGenericos {

	private static ControlActivosGenericos instancia = null;

	public static ControlActivosGenericos getInstancia() {
		if(instancia == null){
			instancia = new ControlActivosGenericos();
		}
		return instancia;
	}

	private ControlActivosGenericos() {
		super();
	}

	public List<TipoActivoGenericoData> obtenerTiposActivoGenerico() {
		List<TipoActivoGenericoData> returnlist = new ArrayList<TipoActivoGenericoData>();
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			List<TipoActivoGenerico> tiposActivosGenericos = new TipoActivoGenericoDao().obtenerActivos();
			for (TipoActivoGenerico tipoActivoGenerico : tiposActivosGenericos) {
				List<MarcaActivoGenerico> marcas = new MarcaActivoGenericoDao().getByTipo(tipoActivoGenerico);
				List<ModeloActivoGenerico> modelos = new ModeloActivoGenericoDao().getByTipo(tipoActivoGenerico);
				returnlist.add(new TipoActivoGenericoData(tipoActivoGenerico, marcas, modelos));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return returnlist;
	}

	public boolean agregarTipoActivoGenerico(TipoActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			TipoActivoGenericoDao dao = new TipoActivoGenericoDao();

			if (param.getNombre().toLowerCase().equals("surtidor") ||
					param.getNombre().toLowerCase().equals("tanque") ||
					param.getNombre().toLowerCase().equals("bomba sumergible") ||
					param.getNombre().toLowerCase().equals("pico")){
				return false;
			}

			List<TipoActivoGenerico> lista = dao.getByNombre(param.getNombre());
			if (lista.size() > 0){
				return false;
			}
			
			TipoActivoGenerico tipo = new TipoActivoGenerico();
			tipo.setNombre(param.getNombre());
			tipo.setActivo(true);

			dao.save(tipo);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean eliminarTipoActivoGenerico(TipoActivoGenericoData param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			TipoActivoGenericoDao dao = new TipoActivoGenericoDao();
			TipoActivoGenerico tipo = dao.get(param.getId());
			tipo.setActivo(false);
			tipo.setNombre(tipo.getNombre()+".inactivo");
			dao.update(tipo);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean modificarTipoActivoGenerico(TipoActivoGenericoData param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			TipoActivoGenericoDao dao = new TipoActivoGenericoDao();
			List<TipoActivoGenerico> lista = dao.getByNombre(param.getNombre());
			if (lista.size() > 0 && lista.get(0).getId() != param.getId()){
				return false;
			}

			TipoActivoGenerico tipo = dao.get(param.getId());
			tipo.setNombre(param.getNombre());
			dao.update(tipo);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public List<MarcaActivoGenerico> obtenerMarcasActivoGenerico(TipoActivoGenericoData tipo) {
		List<MarcaActivoGenerico> list = new ArrayList<MarcaActivoGenerico>();
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			list = new MarcaActivoGenericoDao().getByTipoData(tipo);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return list;
	}

	public boolean agregarMarcaActivoGenerico(MarcaActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();

			MarcaActivoGenericoDao dao = new MarcaActivoGenericoDao();
			List<MarcaActivoGenerico> lista = dao.getByNombre(param);
			if (lista.size() > 0){
				return false;
			}

			MarcaActivoGenerico marca = new MarcaActivoGenerico();
			marca.setNombre(param.getNombre());
			marca.setTipo(new TipoActivoGenerico(param.getTipo().getId()));
			marca.setActivo(true);

			dao.save(marca);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean modificarMarcaActivoGenerico(MarcaActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			MarcaActivoGenericoDao dao = new MarcaActivoGenericoDao();
			List<MarcaActivoGenerico> lista = dao.getByNombre(param);
			if (lista.size() > 0 && lista.get(0).getId() != param.getId()){
				return false;
			}
			MarcaActivoGenerico marca = dao.get(param.getId());
			marca.setNombre(param.getNombre());
			dao.update(marca);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean eliminarMarcaActivoGenerico(MarcaActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			MarcaActivoGenericoDao dao = new MarcaActivoGenericoDao();
			MarcaActivoGenerico marca = dao.get(param.getId());
			marca.setActivo(false);
			marca.setNombre(marca.getNombre()+".inactivo");
			dao.update(marca);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public List<ModeloActivoGenerico> obtenerModelosActivoGenerico(MarcaActivoGenerico marca) {
		List<ModeloActivoGenerico> list = new ArrayList<ModeloActivoGenerico>();
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			list = new ModeloActivoGenericoDao().getByMarca(marca);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return list;
	}
	
	public boolean agregarModeloActivoGenerico(ModeloActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ModeloActivoGenericoDao dao = new ModeloActivoGenericoDao();

			List<ModeloActivoGenerico> lista = dao.getByNombre(param.getNombre());
			if (lista.size() > 0){
				return false;
			}

			ModeloActivoGenerico modelo = new ModeloActivoGenerico();
			modelo.setNombre(param.getNombre());
			modelo.setMarca(new MarcaActivoGenerico(param.getMarca().getId()));
			modelo.setTipo(new TipoActivoGenerico(param.getTipo().getId()));
			modelo.setActivo(true);

			dao.save(modelo);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean modificarModeloActivoGenerico(ModeloActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ModeloActivoGenericoDao dao = new ModeloActivoGenericoDao();
			List<ModeloActivoGenerico> lista = dao.getByNombre(param.getNombre());
			if (lista.size() > 0 && lista.get(0).getId() != param.getId()){
				return false;
			}
			
			ModeloActivoGenerico modelo = dao.get(param.getId());
			modelo.setNombre(param.getNombre());
			dao.update(modelo);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean eliminarModeloActivoGenerico(ModeloActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ModeloActivoGenericoDao dao = new ModeloActivoGenericoDao();
			ModeloActivoGenerico modelo = dao.get(param.getId());
			modelo.setActivo(false);
			modelo.setNombre(modelo.getNombre()+".inactivo");
			dao.update(modelo);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}
	
	public List<ItemChequeoData> obtenerItemsActivoGenerico(TipoActivoGenericoData tipo) {
		List<ItemChequeoData> listRetorno = new ArrayList<ItemChequeoData>();
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			List<ItemChequeo> list = new ItemChequeoDao().getByTipoData(tipo);
			for (ItemChequeo itemChequeo : list) {
				listRetorno.add(itemChequeo.getItemChequeoData());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return listRetorno;
	}

	public boolean agregarItemChequeo(ItemChequeoData param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();

			ItemChequeoDao dao = new ItemChequeoDao();
			List<ItemChequeo> lista = dao.getByNombre(param.getNombre());
			if (lista.size() > 0){
				return false;
			}

			ItemChequeo item = new ItemChequeo();
			item.setTexto(param.getTexto());
			item.setNombre(param.getNombre());
			item.setTipoActivoGenerico(new TipoActivoGenerico(param.getTipoActivoGenerico().getId()));
			item.setTipoChequeo(TipoChequeo.Generico);
			item.setActivo(true);

			dao.save(item);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean modificarItemChequeo(ItemChequeoData param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ItemChequeoDao dao = new ItemChequeoDao();
			List<ItemChequeo> lista = dao.getByNombre(param.getNombre());
			if (lista.size() > 0 && lista.get(0).getId() != param.getId()){
				return false;
			}
			ItemChequeo item = dao.get(param.getId());
			item.setNombre(param.getNombre());
			item.setTexto(param.getTexto());
			dao.update(item);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean eliminarItemChequeo(ItemChequeoData param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ItemChequeoDao dao = new ItemChequeoDao();
			ItemChequeo item = dao.get(param.getId());
			item.setActivo(false);
			item.setNombre(item.getNombre()+".inactivo");
			dao.update(item);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}

	public boolean guardarActivoGenerico(ActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ActivoGenericoDao dao = new ActivoGenericoDao();
			List<ActivoGenerico> lista = dao.getBySerie(param.getSerie());
			if (lista.size() > 0){
				return false;
			}
			
			if (param.getMarca() != null){
				if (param.getMarca().getId() == 0){
					param.setMarca(null);
				}
			}
			if (param.getModelo()!= null) {
				if (param.getModelo().getId() == 0){
					param.setModelo(null);
				}
			}
			
			dao.save(param);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}
	
	public boolean modificarActivoGenerico(ActivoGenerico param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ActivoGenericoDao dao = new ActivoGenericoDao();

			List<ActivoGenerico> lista = dao.getBySerie(param.getSerie());
			if (lista.size() > 0 && lista.get(0).getId() != param.getId()){
				return false;
			}
			
			ActivoGenerico item = dao.get(param.getId());
			item.setSerie(param.getSerie());
			if (param.getMarca() != null){
				if (param.getMarca().getId() == 0){
					item.setMarca(null);
				} else {
					item.setMarca(new MarcaActivoGenerico(param.getMarca().getId()));
				}
			}
			if (param.getModelo()!= null) {
				if (param.getModelo().getId() == 0){
					item.setModelo(null);
				} else {
					item.setModelo(new ModeloActivoGenerico(param.getModelo().getId()));
				}
			}
			item.setFecha(param.getFecha());
			item.setDescripcion(param.getDescripcion());
			
			dao.update(item);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			return false;
		}finally{
			tx.close();
		}
		return true;
	}
	
	public boolean eliminarActivoGenerico(ActivoGenerico param) {
		return ControlActivo.getInstancia().eliminarAcivo(param);
	}

}
