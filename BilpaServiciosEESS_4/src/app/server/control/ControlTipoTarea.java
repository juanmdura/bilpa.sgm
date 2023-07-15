package app.server.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.client.dominio.Tarea;
import app.client.dominio.TipoTarea;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.TareaDao;
import app.server.persistencia.TipoTareaDao;

public class ControlTipoTarea {

	private static ControlTipoTarea instancia = null;

	public static ControlTipoTarea getInstancia() {
		if(instancia == null){
			instancia = new ControlTipoTarea();
		}
		return instancia;
	}

	public static void setInstancia(ControlTipoTarea instancia) {
		ControlTipoTarea.instancia = instancia;
	}

	private ControlTipoTarea (){}

	public boolean agregarTipoTarea (TipoTarea tarea) {

		if(validarTipoTareaExiste(tarea.getDescripcion())){
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();

				TipoTareaDao dao = new TipoTareaDao();

				dao.save(tarea);

				tx.commit();

				return true;

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
            	tx.close();
            }
		}

		return false;
	}

	public TipoTarea buscarTipoTarea(int id){
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			TipoTareaDao dao = new TipoTareaDao();
			TipoTarea tareaRetorno = new TipoTarea();

			tareaRetorno = dao.get(id);
			return tareaRetorno;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		return null;
	}

	public boolean modificarTipoTarea(TipoTarea tarea){
		if(validarTipoTareaExiste(tarea.getDescripcion())){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				TipoTareaDao dao = new TipoTareaDao();
				dao.update(tarea);
				tx.commit();
				return true;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
            	tx.close();
            }
		}
		return false;
	}

	//Retorna FALSE si ya existe!
	public boolean validarTipoTareaExiste(String descripcion){
		ArrayList<TipoTarea> tiposTareas = new ArrayList<TipoTarea>();
		tiposTareas = obtenerTodasLosTiposTareas();

		for (int i = 0; i < tiposTareas.size(); i++) {
			TipoTarea tarea = (TipoTarea) tiposTareas.get(i);

			if(tarea.getDescripcion().trim().equals(descripcion.trim()))
			{
				return false;
			}
		}
		return true;
	}

	public ArrayList<TipoTarea> obtenerTodasLosTiposTareasActivos(){
		ArrayList<TipoTarea> todasLasTareas = new ArrayList<TipoTarea>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			TipoTareaDao dao = new TipoTareaDao();

			List<TipoTarea> lista = dao.obtenerTareasActivas();

			for (TipoTarea r : lista) {
				todasLasTareas.add(r.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		Collections.sort(todasLasTareas);
		return todasLasTareas;
	}
	
	public ArrayList<TipoTarea> obtenerTodasLosTiposTareas(){
		ArrayList<TipoTarea> todasLasTareas = new ArrayList<TipoTarea>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			TipoTareaDao dao = new TipoTareaDao();

			List<TipoTarea> lista = dao.list();

			for (TipoTarea r : lista) {
				todasLasTareas.add(r.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		Collections.sort(todasLasTareas);
		return todasLasTareas;
	}

	public boolean eliminarTipoTarea(TipoTarea tipo) {
		if (setearInactivasTareasDeEsteTipo(tipo))
		{
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				TipoTareaDao dao = new TipoTareaDao();
				tipo.setInactiva(true);
				dao.merge(tipo);
				tx.commit();
				return true;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
            	tx.close();
            }
		}
		return false;	
	}

	private boolean setearInactivasTareasDeEsteTipo(TipoTarea tipo) 
	{
		TipoTarea tipoTareaSinClasificar = buscarTipoTarea(1);
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			TareaDao dao = new TareaDao();
			ArrayList<Tarea> tareas = (ArrayList<Tarea>) dao.obtenerTareas(tipo);
			for(Tarea t : tareas)
			{
				t.setTipoTarea(tipoTareaSinClasificar.copiar());
				dao.merge(t);
			}
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

	public TipoTarea obtenerTipoTarea(int tipoInt) {
		DaoTransaction tx = new DaoTransaction();
		TipoTarea tipoTarea = null;
		try {
			tx.begin();

			TipoTareaDao dao = new TipoTareaDao();
			tipoTarea = dao.get(tipoInt);

		} catch (Exception e) {
//			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return tipoTarea;
	}
}
