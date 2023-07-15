package app.server.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.client.dominio.ComentarioChequeo;
import app.client.dominio.ItemChequeado;
import app.client.dominio.json.ComentarioChequeoJson;
import app.server.persistencia.ComentarioChequeoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.ItemChequeadoDao;
import app.server.persistencia.PersonaDao;

public class ControlComentarioChequeo {
	private static ControlComentarioChequeo instancia = null;

	public static ControlComentarioChequeo getInstancia() {
		if(instancia == null){
			instancia = new ControlComentarioChequeo();
		}
		return instancia;
	}

	private ControlComentarioChequeo() {
		super();
	}

	public ComentarioChequeo obtenerComentarioChequeo(int id) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			return new ComentarioChequeoDao().get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}
	
	public List<ComentarioChequeo> obtenerComentariosChequeo(ItemChequeado itemChequeado) {
		List<ComentarioChequeo> list = new ArrayList<ComentarioChequeo>();
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			List<ComentarioChequeo> listTmp = new ComentarioChequeoDao().getByItemChequeado(itemChequeado);
			
			for(ComentarioChequeo cc : listTmp){
				list.add(cc.copiarMinimo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return list;
	}

	public boolean agregarComentarioChequeo(ComentarioChequeoJson param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();

			ComentarioChequeoDao dao = new ComentarioChequeoDao();
			ComentarioChequeo item = new ComentarioChequeo();
			item.setTexto(param.getTexto());
			item.setItemChequeado(new ItemChequeadoDao().get(param.getIdItemChequeado()));
			item.setFecha(new Date());
			item.setUsuario(new PersonaDao().get(param.getIdUsuario()));
			item.setImprimible(param.isImprimible());
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

	public boolean modificarComentarioChequeo(ComentarioChequeoJson param) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ComentarioChequeoDao dao = new ComentarioChequeoDao();
			ComentarioChequeo item = dao.get(param.getId());
			item.setTexto(param.getTexto());
			item.setFecha(new Date());
			item.setImprimible(param.isImprimible());
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

	public boolean eliminarComentarioChequeo(int id) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ComentarioChequeoDao dao = new ComentarioChequeoDao();
			ComentarioChequeo item = dao.get(id);
			item.setActivo(false);
			item.setFecha(new Date());
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
}
