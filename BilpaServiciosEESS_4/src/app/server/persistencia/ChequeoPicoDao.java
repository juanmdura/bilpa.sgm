package app.server.persistencia;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import app.client.dominio.ChequeoPico;
import app.client.dominio.Pico;
import app.client.dominio.Preventivo;

public class ChequeoPicoDao extends AbstractDao<ChequeoPico, Integer> {

	public ChequeoPico get(int idPreventivo, int idPico) {
		return get(Restrictions.eq("preventivo", new Preventivo(idPreventivo)), Restrictions.eq("pico", new Pico(idPico)));
	}

	public List<ChequeoPico> obtenerPorPico(Pico pico) {
		return find(Restrictions.eq("pico", pico));
	}
}
