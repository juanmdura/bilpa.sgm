package app.server.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.client.dominio.Estacion;
import app.client.dominio.Orden;
import app.client.dominio.Sello;
import app.client.dominio.data.DatoConsultaOrdenesPorSelloYFechas;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.OrdenDao;

public class ControlListado {

	private static ControlListado instancia = null;

	public static ControlListado getInstancia() {
		if(instancia == null){
			instancia = new ControlListado();
		}
		return instancia;
	}

	public List<DatoConsultaOrdenesPorSelloYFechas> ordenesPorSelloYFechas(Sello sello, Date fInicio, Date fFin)
	{
		List<DatoConsultaOrdenesPorSelloYFechas> ordenesDato = new ArrayList<DatoConsultaOrdenesPorSelloYFechas>();
		List<Estacion> estaciones = ControlEmpresa.getInstancia().estacionesPorSello(sello);

		DaoTransaction tx = new DaoTransaction();


		try {
			tx.begin();
			OrdenDao dao = new OrdenDao();

			for (Estacion e : estaciones)
			{
				List<Orden> ordenes = dao.ordenesFinalizadasPorTiempo(e, fInicio, fFin);

				for (Orden o : ordenes)
				{
					DatoConsultaOrdenesPorSelloYFechas ordenDato = new DatoConsultaOrdenesPorSelloYFechas();	
					ordenDato.setEmpresa(e);
					ordenDato.setNumero(o.getNumero());
					ordenesDato.add(ordenDato);
				}
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return ordenesDato;
	}



	public boolean validarSelloYFechas(Sello sello, Date fInicio, Date fFin)
	{
		DaoTransaction tx = new DaoTransaction();
		List<Estacion> estaciones = ControlEmpresa.getInstancia().estacionesPorSello(sello);

		try {
			tx.begin();

			OrdenDao dao = new OrdenDao();

			for (Estacion e : estaciones)
			{
				List<Orden> ordenes = dao.ordenesFinalizadasPorTiempo(e, fInicio, fFin);

				if (ordenes.size() > 0)
				{
					return true;
				}
			}
			
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return false;
	}
}
