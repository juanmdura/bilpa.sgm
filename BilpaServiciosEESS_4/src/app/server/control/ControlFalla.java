package app.server.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.client.dominio.FallaReportada;
import app.client.dominio.FallaTecnica;
import app.client.dominio.FallaTipo;
import app.client.dominio.Tarea;
import app.client.dominio.TipoFallaReportada;
import app.client.dominio.TipoFallaTecnica;
import app.client.dominio.TipoTarea;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.FallaReportadaDao;
import app.server.persistencia.FallaTecnicaDao;
import app.server.persistencia.TareaDao;


public class ControlFalla {
	private static ControlFalla instancia = null;

	public static ControlFalla getInstancia() {
		if(instancia == null){
			instancia = new ControlFalla();
		}
		return instancia;
	}

	public static void setInstancia(ControlFalla instancia) {
		ControlFalla.instancia = instancia;
	}

	private ControlFalla (){

	}

	//Despues va a haber un solo metodo para los 2 tipos de falla, y recibe la Falla.
	public boolean validarNuevaFallaT(String descripcion){
		/*ArrayList<String> todosLasFallas = obtenerTodasLasFallasT();

		for (int i=0; i<todosLasFallas.size();i++){
			if (descripcion.equalsIgnoreCase(todosLasFallas.get(i)))
				return false;
		}
		TODO*/
		return true;
	}

	public ArrayList<FallaTipo> obtenerTodasLasFallas(){
		ArrayList<FallaTipo> todasLasFallas = new ArrayList<FallaTipo>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			obtenerFallasReportadasActivas(todasLasFallas);
			obtenerFallasTecnicasActivas(todasLasFallas);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		return todasLasFallas;
	}

	private void obtenerFallasReportadasActivas(ArrayList<FallaTipo> todasLasFallasR) 
	{
		FallaReportadaDao dao = new FallaReportadaDao();
		List<FallaReportada> lista = dao.obtenerFallasReportadasActivas();
		
		for (FallaReportada t : lista)
		{
			todasLasFallasR.add(t);
		}

	}

	private void obtenerFallasTecnicasActivas(ArrayList<FallaTipo> todasLasFallasT) 
	{
		FallaTecnicaDao dao = new FallaTecnicaDao();
		List<FallaTecnica> lista = dao.obtenerFallasTecnicasActivas();

		for (FallaTecnica t : lista) 
		{
			todasLasFallasT.add(t);
		}

	}

	@SuppressWarnings("unchecked")
	public ArrayList<FallaTecnica> obtenerTodasLasFallasTecnicas(){
		ArrayList<FallaTecnica> todasLasFallas = new ArrayList<FallaTecnica>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			FallaTecnicaDao dao = new FallaTecnicaDao();
			List<FallaTecnica> lista = dao.obtenerFallasTecnicasActivas();

//			List<FallaTecnica> lista = dao.list();

			for (FallaTecnica t : lista) {
				if(t.getId()>0){
					todasLasFallas.add(t);					
				}
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			tx.close();
		}
		Collections.sort(todasLasFallas);
		return todasLasFallas;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<FallaReportada> obtenerTodasLasFallasReportadas(){
		ArrayList<FallaReportada> todasLasFallas = new ArrayList<FallaReportada>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			FallaReportadaDao dao = new FallaReportadaDao();
			List<FallaReportada> lista = dao.obtenerFallasReportadasActivas();

			//List<FallaReportada> lista = dao.list();

			for (FallaReportada t : lista) {
				if(t.getId()>0){
					todasLasFallas.add(t);					
				}
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}

		Collections.sort(todasLasFallas);
		return todasLasFallas;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Tarea>obtenerTodasLasTareas(){
		ArrayList<Tarea> todasLasTareas = new ArrayList<Tarea>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			TareaDao dao = new TareaDao();

			List<Tarea> lista = dao.obtenerTareasActivas();

			for (Tarea r : lista) {
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

	@SuppressWarnings("unchecked")
	public ArrayList<Tarea>obtenerTareas(TipoTarea tipo){
		ArrayList<Tarea> tareasPorTipo = new ArrayList<Tarea>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			TareaDao dao = new TareaDao();
			List<Tarea> lista = dao.obtenerTareas(tipo);
			for (Tarea r : lista) {
				tareasPorTipo.add(r.copiar());
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }
		Collections.sort(tareasPorTipo);
		return tareasPorTipo;
	}


	public boolean eliminarFallaTecnica(FallaTecnica falla) {

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			FallaTecnicaDao dao = new FallaTecnicaDao();
			falla.setInactiva(true);
			dao.merge(falla);
			//dao.delete(falla);
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

	public boolean eliminarFallaReportada(FallaReportada falla) {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			FallaReportadaDao dao = new FallaReportadaDao();
			falla.setInactiva(true);
			dao.merge(falla);
			//dao.delete(falla);
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

	public boolean eliminarTarea(Tarea tarea) {

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			TareaDao dao = new TareaDao();
			tarea.setInactiva(true);
			dao.merge(tarea);
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


	public boolean agregarFalla(FallaTipo falla) {
		if(falla != null){
			if(!this.validarFallaExiste(falla)){

				if(falla.getTipo() == 1){
					FallaTecnica fallaTecnica = (FallaTecnica) falla;
					this.agregarFallaTecnica(fallaTecnica);
				}else if(falla.getTipo() == 2){
					FallaReportada fallaReportada = (FallaReportada) falla;
					this.agregarFallaReportada(fallaReportada);
				}

				return true;
			}
			return false;
		}
		return false;
	}

	public boolean agregarFallaTecnica(FallaTecnica falla) {
		if(falla != null){
			if (falla.getSubTipo() < 0){
				falla.setSubTipo(1);//el subtipo Sin Clasificar de falla reportada
			}
			if(!this.validarFallaExiste(falla)){
				DaoTransaction tx = new DaoTransaction();
				try {
					tx.begin();

					FallaTecnicaDao dao = new FallaTecnicaDao();

					dao.save(falla);

					tx.commit();

					return true;

				} catch (Exception e) {
					tx.rollback();
					e.printStackTrace();
				}finally{
	            	tx.close();
	            }
			}
		}
		return false;
	}

	public boolean agregarFallaReportada(FallaReportada falla) {
		if(falla != null){
			if (falla.getSubTipo() < 0){
				falla.setSubTipo(2);//el subtipo Sin Clasificar de falla reportada
			}
			
			if(!this.validarFallaExiste(falla)){
				DaoTransaction tx = new DaoTransaction();
				try {
					tx.begin();

					FallaReportadaDao dao = new FallaReportadaDao();

					dao.save(falla);

					tx.commit();

					return true;

				} catch (Exception e) {
					tx.rollback();
					e.printStackTrace();
				}finally{
	            	tx.close();
	            }
			}
		}
		return false;
	}

	public boolean agregarTarea (Tarea tarea) {

		if(this.validarTareaExiste(tarea.getDescripcion())){
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();

				TareaDao dao = new TareaDao();

				dao.save(tarea);

				tx.commit();

				return true;

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally{
            	tx.close();
            }
		}

		return false;
	}


	public FallaTecnica buscarFallaTecnica(int id){
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			FallaTecnicaDao dao = new FallaTecnicaDao();
			FallaTecnica retorno = new FallaTecnica();

			retorno = dao.get(id);
			if(retorno != null){
				return retorno;
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}

		return null;
	}

	public FallaReportada buscarFallaReportada(int id){
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			FallaReportadaDao dao = new FallaReportadaDao();
			FallaReportada retorno = new FallaReportada();

			retorno = dao.get(id);
			return retorno;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
        	tx.close();
        }

		return null;
	}

	public Tarea buscarTarea(int id){
		DaoTransaction tx = new DaoTransaction();
		try 
		{
			tx.begin();
			TareaDao dao = new TareaDao();
			Tarea tareaRetorno = new Tarea();

			tareaRetorno = dao.get(id);
			if(tareaRetorno != null){
				return tareaRetorno.copiar();
			}
		} 
		catch (Exception e) 
		{
			tx.rollback();
			e.printStackTrace();
		}
		finally
		{
			tx.close();
		}
		return null;
	}

	public boolean modificarFallaTecnica(FallaTecnica falla){
		if(!this.validarFallaExiste(falla)){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				FallaTecnicaDao dao = new FallaTecnicaDao();
				dao.merge(falla);
				tx.commit();
				return true;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally{
            	tx.close();
            }
		}
		return false;
	}

	public boolean modificarFallaReportada(FallaReportada falla){
		if(!this.validarFallaExiste(falla)){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				FallaReportadaDao dao = new FallaReportadaDao();
				dao.merge(falla);
				tx.commit();
				return true;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally{
            	tx.close();
            }
		}

		return false;
	}

	public boolean modificarTarea(Tarea tarea){
		if(this.validarTareaExiste(tarea)){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				TareaDao dao = new TareaDao();
				dao.merge(tarea);
				tx.commit();
				return true;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally{
            	tx.close();
            }
		}
		return false;
	}

	public boolean validarFallaExiste(FallaTipo fallaAValidar){
		ArrayList<FallaTipo> fallas = new ArrayList<FallaTipo>();
		fallas = this.obtenerTodasLasFallas();

		for (int i = 0; i < fallas.size(); i++) {
			FallaTipo falla = (FallaTipo) fallas.get(i);

			if(falla.getDescripcion().equals(fallaAValidar.getDescripcion()) 
					&& ((fallaAValidar.getId() != falla.getId()) || fallaAValidar.getId() == 0)){
				return true;//ya existe
			}
		}
		return false;//ok
	}

	//Retorna FALSE si ya existe!
	public boolean validarTareaExiste(String descripcion){
		ArrayList<Tarea> tareas = new ArrayList<Tarea>();
		tareas = this.obtenerTodasLasTareas();

		for (int i = 0; i < tareas.size(); i++) {
			Tarea tarea = (Tarea) tareas.get(i);


			if(tarea.getDescripcion().equals(descripcion)){
				return false;
			}
		}
		return true;
	}

	//Retorna FALSE si ya existe!
	public boolean validarTareaExiste(Tarea tareaAValidar) {
		ArrayList<Tarea> tareas = new ArrayList<Tarea>();
		tareas = this.obtenerTodasLasTareas();

		for (int i = 0; i < tareas.size(); i++) {
			Tarea tarea = (Tarea) tareas.get(i);

			if(tarea.getDescripcion().equals(tareaAValidar.getDescripcion()) && tarea.getId() != tareaAValidar.getId())//distinta tarea con la misma descripcion
			{
				return false;
			}
		}
		return true;
	}

	public List<FallaReportada> obtenerFallasRPorTipo(int idTipo){
		List<FallaReportada> fallas = new ArrayList<FallaReportada>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			FallaReportadaDao dao = new FallaReportadaDao();

			List<FallaReportada> lista = dao.obtenerFallasReportadasActivas(idTipo);

			for (FallaReportada fr : lista) {
				fallas.add(fr.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		Collections.sort(fallas);
		return fallas;
	}

	public List<FallaTecnica> obtenerFallasTPorTipo(TipoFallaTecnica tipo) {
		List<FallaTecnica> fallas = new ArrayList<FallaTecnica>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			FallaTecnicaDao dao = new FallaTecnicaDao();

			List<FallaTecnica> lista = dao.obtenerFallasTecnicasActivas(tipo);

			for (FallaTecnica ft : lista) {
				fallas.add(ft.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		Collections.sort(fallas);
		return fallas;
	}
}



