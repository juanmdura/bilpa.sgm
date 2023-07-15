package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Contador;
import app.client.dominio.Pico;
import app.client.dominio.Solucion;

public class ContadorDao extends AbstractDao<Contador, Integer>{

	public List<Contador> obtenerContadores(int idPico){
		return find(Restrictions.eq("pico", new Pico(idPico)));
	}

	public List<Contador> obtenerContadores(Solucion solucion) {
		return find(Restrictions.eq("solucion", new Solucion(solucion.getId())));
	}

}
