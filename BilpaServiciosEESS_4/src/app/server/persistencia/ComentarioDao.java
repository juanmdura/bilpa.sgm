package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.Comentario;
import app.client.dominio.Solucion;

public class ComentarioDao  extends AbstractDao<Comentario, Integer>{

	public List<Comentario> obtenerComentarios(Solucion solucion) {
		return find(Restrictions.eq("solucion", solucion));	}

}
