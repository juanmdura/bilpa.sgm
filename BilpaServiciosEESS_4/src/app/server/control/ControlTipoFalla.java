package app.server.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.client.dominio.FallaReportada;
import app.client.dominio.SubTipoFalla;
import app.client.dominio.TipoFallaReportada;
import app.client.dominio.TipoFallaTecnica;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.FallaReportadaDao;
import app.server.persistencia.TipoFallaReportadaDao;
import app.server.persistencia.TipoFallaTecnicaDao;

public class ControlTipoFalla {

	private static ControlTipoFalla instancia = null;

	public static ControlTipoFalla getInstancia() {
		if(instancia == null){
			instancia = new ControlTipoFalla();
		}
		return instancia;
	}

	public static void setInstancia(ControlTipoFalla instancia) {
		ControlTipoFalla.instancia = instancia;
	}

	private ControlTipoFalla (){}

	public TipoFallaTecnica buscarTipoFallaT(int id)
	{
		DaoTransaction tx = new DaoTransaction();
		try 
		{
			tx.begin();
			TipoFallaTecnicaDao dao = new TipoFallaTecnicaDao();
			TipoFallaTecnica retorno = new TipoFallaTecnica();

			retorno = dao.get(id);
			return retorno;
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

	public TipoFallaReportada buscarTipoFallaR(int id)
	{
		DaoTransaction tx = new DaoTransaction();
		try
		{
			tx.begin();
			TipoFallaReportadaDao dao = new TipoFallaReportadaDao();
			TipoFallaReportada retorno = new TipoFallaReportada();

			retorno = dao.get(id);
			return retorno;
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

	public boolean eliminarTipoFallaT(TipoFallaTecnica falla)
	{
		DaoTransaction tx = new DaoTransaction();
		try
		{
			tx.begin();
			TipoFallaTecnicaDao dao = new TipoFallaTecnicaDao();
			falla.setInactiva(true);
			dao.merge(falla);
			tx.commit();
			return true;
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
		return false;		
	}

	public boolean eliminarTipoFallaR(TipoFallaReportada tipoFallaRAEliminar) 
	{
		if(cambiarFallasASinClasificarR(tipoFallaRAEliminar))
		{
			DaoTransaction tx = new DaoTransaction();
			try
			{
				tx.begin();
				TipoFallaReportadaDao dao = new TipoFallaReportadaDao();
				tipoFallaRAEliminar.setInactiva(true);
				dao.merge(tipoFallaRAEliminar);
				tx.commit();
				return true;
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
		}
		return false;		
	}

	private boolean cambiarFallasASinClasificarR(TipoFallaReportada tipoFallaRAEliminar) 
	{
		DaoTransaction tx = new DaoTransaction();
		try
		{
			tx.begin();
			FallaReportadaDao dao = new FallaReportadaDao();

			List<FallaReportada> fallasACambiarASinClasificar = ControlFalla.getInstancia().obtenerFallasRPorTipo(tipoFallaRAEliminar.getId());
			for(FallaReportada fallaACambiarASinClasificar : fallasACambiarASinClasificar)
			{
				fallaACambiarASinClasificar.setSubTipo(2);//reportadas
				dao.merge(fallaACambiarASinClasificar);
			}
			tx.commit();
			return true;
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
		return false;	
	}

	public ArrayList<TipoFallaTecnica> obtenerTiposFallasActivasT() {
		ArrayList<TipoFallaTecnica> fallas = new ArrayList<TipoFallaTecnica>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			TipoFallaTecnicaDao dao = new TipoFallaTecnicaDao();

			List<TipoFallaTecnica> lista = dao.obtenerTipoFallasTecnicasActivas();

			for (TipoFallaTecnica tft : lista) {
				fallas.add(tft.copiar());
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

	public ArrayList<TipoFallaReportada> obtenerTiposFallasActivasR() {
		ArrayList<TipoFallaReportada> fallas = new ArrayList<TipoFallaReportada>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			TipoFallaReportadaDao dao = new TipoFallaReportadaDao();

			List<TipoFallaReportada> lista = dao.obtenerTipoFallasReportadasActivas();

			for (TipoFallaReportada tfr : lista) {
				fallas.add(tfr.copiar());
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

	private void cargarFallasDetectadasTodas(ArrayList<SubTipoFalla> todasLasFallasT)
	{
		TipoFallaTecnicaDao dao = new TipoFallaTecnicaDao();
		List<TipoFallaTecnica> lista = dao.list();

		for (TipoFallaTecnica t : lista) 
		{
			todasLasFallasT.add(t);
		}
	}

	private void cargarFallasReportadasTodas(ArrayList<SubTipoFalla> todasLasFallasR) 
	{
		TipoFallaReportadaDao dao = new TipoFallaReportadaDao();

		List<TipoFallaReportada> lista = dao.list();
		for (TipoFallaReportada t : lista) 
		{
			todasLasFallasR.add(t);
		}
	}

	public ArrayList<SubTipoFalla> obtenerTodasSubTiposFalla()
	{
		ArrayList<SubTipoFalla> todasLasFallas = new ArrayList<SubTipoFalla>();
		DaoTransaction tx = new DaoTransaction();

		try 
		{
			tx.begin();

			cargarFallasReportadasTodas(todasLasFallas);
			cargarFallasDetectadasTodas(todasLasFallas);

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

		return todasLasFallas;
	}

	public boolean validarFallaExiste(SubTipoFalla subTipoFallaAValidar){
		ArrayList<SubTipoFalla> fallas = new ArrayList<SubTipoFalla>();
		fallas = this.obtenerTodasSubTiposFalla();

		for (int i = 0; i < fallas.size(); i++) {
			SubTipoFalla falla = (SubTipoFalla) fallas.get(i);

			if(falla.getDescripcion().equals(subTipoFallaAValidar.getDescripcion()) && falla.getTipo() == subTipoFallaAValidar.getTipo()){
				return true;
			}
		}
		return false;
	}

	public boolean agregarTipoFallaT(TipoFallaTecnica falla) 
	{
		if(falla != null)
		{
			if(!this.validarFallaExiste(falla))
			{
				DaoTransaction tx = new DaoTransaction();
				try
				{
					tx.begin();

					TipoFallaTecnicaDao dao = new TipoFallaTecnicaDao();
					dao.save(falla);
					tx.commit();

					return true;
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
			}
		}
		return false;
	}

	public boolean agregarTipoFallaR(TipoFallaReportada falla) 
	{
		if(falla != null)
		{
			if(!this.validarFallaExiste(falla))
			{
				DaoTransaction tx = new DaoTransaction();
				try 
				{
					tx.begin();

					TipoFallaReportadaDao dao = new TipoFallaReportadaDao();
					dao.save(falla);
					tx.commit();

					return true;
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
			}
		}
		return false;
	}

	public boolean modificarTipoFallaR(TipoFallaReportada falla) {
		if(!this.validarFallaExiste(falla)){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				TipoFallaReportadaDao dao = new TipoFallaReportadaDao();
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

	public boolean modificarTipoFallaT(TipoFallaTecnica falla) {
		if(!this.validarFallaExiste(falla)){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				TipoFallaTecnicaDao dao = new TipoFallaTecnicaDao();
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

	public TipoFallaTecnica obtenerTipoFallaT(int id) {
		DaoTransaction tx = new DaoTransaction();
		TipoFallaTecnica falla = null;
		try {
			tx.begin();

			TipoFallaTecnicaDao dao = new TipoFallaTecnicaDao();
			falla = dao.get(id);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return falla;
	}
}
