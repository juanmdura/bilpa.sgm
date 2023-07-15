package app.server.control;

import java.util.ArrayList;
import java.util.List;

import app.client.dominio.TipoDescarga;
import app.client.dominio.data.TipoDescargaData;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.TipoDescargaDao;

public class ControlDescarga {
	
	private static ControlDescarga instancia = null;

	public static ControlDescarga getInstancia() {
		if(instancia == null){
			instancia = new ControlDescarga();
		}
		return instancia;
	}

	private ControlDescarga() {
		super();
	}
	

	public List<TipoDescargaData> obtenerTiposDescarga() {
		
		List<TipoDescargaData> tiposDescargaData = new ArrayList<TipoDescargaData>();
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			TipoDescargaDao tiposDescargaDao = new TipoDescargaDao();
			List<TipoDescarga> tiposDescarga = tiposDescargaDao.list();
			tx.commit();
			
			for(TipoDescarga td : tiposDescarga) {
				tiposDescargaData.add(td.getTipoDescargaData());
			}
			
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			
		}finally{
			tx.close();
		}
		return tiposDescargaData;
		
	}

	public TipoDescarga obtenerTipoDescarga(int tipoDeDescarga) {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			TipoDescargaDao tiposDescargaDao = new TipoDescargaDao();
			TipoDescarga tipoDescarga = tiposDescargaDao.get(tipoDeDescarga);
			return tipoDescarga;
			
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			
		}finally{
			tx.close();
		}
		return null;
	}
}
