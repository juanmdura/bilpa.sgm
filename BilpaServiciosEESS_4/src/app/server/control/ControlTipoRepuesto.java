package app.server.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.client.dominio.Repuesto;
import app.client.dominio.TipoFallaTecnica;
import app.client.dominio.TipoRepuesto;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.RepuestoDao;
import app.server.persistencia.TipoFallaTecnicaDao;
import app.server.persistencia.TipoRepuestoDao;

public class ControlTipoRepuesto {

	private static ControlTipoRepuesto instancia = null;

	public static ControlTipoRepuesto getInstancia() {
		if(instancia == null){
			instancia = new ControlTipoRepuesto();
		}
		return instancia;
	}

	public static void setInstancia(ControlTipoRepuesto instancia) {
		ControlTipoRepuesto.instancia = instancia;
	}

	private ControlTipoRepuesto (){}
	
	public boolean agregarTipoRepuesto (TipoRepuesto Repuesto) {

		if(validarTipoRepuestoExiste(Repuesto.getDescripcion())){
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();

				TipoRepuestoDao dao = new TipoRepuestoDao();

				dao.save(Repuesto);

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
	
	public TipoRepuesto buscarTipoRepuesto(int id){
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			TipoRepuestoDao dao = new TipoRepuestoDao();
			TipoRepuesto RepuestoRetorno = new TipoRepuesto();

			RepuestoRetorno = dao.get(id);
			return RepuestoRetorno;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		return null;
	}
	
	public boolean modificarTipoRepuesto(TipoRepuesto repuesto){
		if(validarTipoRepuestoExiste(repuesto.getDescripcion())){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				TipoRepuestoDao dao = new TipoRepuestoDao();
				dao.merge(repuesto);
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
	public boolean validarTipoRepuestoExiste(String descripcion){
		ArrayList<TipoRepuesto> Repuestos = new ArrayList<TipoRepuesto>();
		Repuestos = obtenerTodasLosTiposRepuestos();

		for (int i = 0; i < Repuestos.size(); i++) {
			TipoRepuesto Repuesto = (TipoRepuesto) Repuestos.get(i);

			if(Repuesto.getDescripcion().trim().equals(descripcion.trim()))
			{
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<TipoRepuesto> obtenerTodasLosTiposRepuestosActivos(){
		ArrayList<TipoRepuesto> todasLasRepuestos = new ArrayList<TipoRepuesto>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			TipoRepuestoDao dao = new TipoRepuestoDao();

			List<TipoRepuesto> lista = dao.obtenerRepuestosActivos();

			for (TipoRepuesto r : lista) {
				todasLasRepuestos.add(r.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		Collections.sort(todasLasRepuestos);
		return todasLasRepuestos;
	}

	public ArrayList<TipoRepuesto> obtenerTodasLosTiposRepuestos(){
		ArrayList<TipoRepuesto> todasLasRepuestos = new ArrayList<TipoRepuesto>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			TipoRepuestoDao dao = new TipoRepuestoDao();

			List<TipoRepuesto> lista = dao.list();

			for (TipoRepuesto r : lista) {
				todasLasRepuestos.add(r.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
        	tx.close();
        }

		Collections.sort(todasLasRepuestos);
		return todasLasRepuestos;
	}
	
	public boolean eliminarTipoRepuesto(TipoRepuesto tipo) {
		if (setearInactivasRepuestosDeEsteTipo(tipo))
		{
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				TipoRepuestoDao dao = new TipoRepuestoDao();
				tipo.setInactivo(true);
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

	private boolean setearInactivasRepuestosDeEsteTipo(TipoRepuesto tipo) 
	{
		TipoRepuesto tipoRepuestoSinClasificar = buscarTipoRepuesto(1);
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			RepuestoDao dao = new RepuestoDao();
			ArrayList<Repuesto> repuestos = (ArrayList<Repuesto>) dao.obtenerRepuestos(tipo);
			for(Repuesto t : repuestos)
			{
				t.setTipoRepuesto(tipoRepuestoSinClasificar.copiar());
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

	public TipoRepuesto obtenerTipoRepuesto(int tipoInt) {
		DaoTransaction tx = new DaoTransaction();
		TipoRepuesto tipoRepuesto = null;
		try {
			tx.begin();

			TipoRepuestoDao dao = new TipoRepuestoDao();
			tipoRepuesto = dao.get(tipoInt);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return tipoRepuesto;
	}
}
