package app.server.control;

import app.client.dominio.Calibre;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.CalibreDao;

public class ControlCalibre {

	private static ControlCalibre instancia = null;

	public static ControlCalibre getInstancia() {
		if(instancia == null){
			instancia = new ControlCalibre();
		}
		return instancia;
	}

	private ControlCalibre() {
		super();
	}
	
	public Calibre obtenerCalibre(int idCalibre) throws Exception {
		Calibre calibre = null;
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			calibre = new CalibreDao().get(idCalibre);
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}
		return calibre;
	}
}
