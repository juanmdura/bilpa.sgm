package app.server;

import java.util.List;

import app.client.dominio.Corregido;
import app.client.dominio.ItemChequeado;
import app.client.dominio.ItemChequeo;
import app.client.dominio.Organizacion;
import app.client.dominio.Pendiente;
import app.server.persistencia.ChequeoDao;
import app.server.persistencia.CorregidoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.ItemChequeadoDao;
import app.server.persistencia.ItemChequeoDao;
import app.server.persistencia.PendienteDao;

public class InsertCorregidosYPendientes {

	public static void main(String[] args) {

		migrarPendientes();
		migrarCorregidos();
		// para cada pendiente
		// si preventivo.get
	}

	public static void migrarPendientes() {
		ItemChequeadoDao itemChequeadoDao = new ItemChequeadoDao();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			List<Pendiente> pendientes = new PendienteDao().list();

			for (Pendiente pendiente : pendientes) {
				if (pendiente.getPreventivo() != null && pendiente.getPreventivo().getChequeo() != null) {

					ItemChequeado ic = null;

					ic = new ItemChequeado();
					ItemChequeoDao itemChequeoDao = new ItemChequeoDao();
					List<ItemChequeo> itemsChequeo = itemChequeoDao.getPorTipo(pendiente.getPreventivo().getChequeo().getTipo(), null);
					for (ItemChequeo item :itemsChequeo){
						if (item.getNombre().startsWith("otros")){
							ic.setItemChequeo(item); 
						}
					}
					ic.setPendiente(true);
					ic.setValor("");

					if (ic != null) {
						pendiente.setOrganizacion(Organizacion.Bilpa);
						ic.setPendiente(true);
						ic.getListaDePendientes().add(pendiente);
						if (ic.getId() == 0){
							itemChequeadoDao.save(ic);
							pendiente.getPreventivo().getChequeo().getItemsChequeados().add(ic);
							new ChequeoDao().update(pendiente.getPreventivo().getChequeo());
						} else {
							itemChequeadoDao.update(ic);
						}
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			tx.close();
		}
	}

	public static void migrarCorregidos() {
		ItemChequeadoDao itemChequeadoDao = new ItemChequeadoDao();
		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			List<Corregido> corregidos = new CorregidoDao().list();

			for (Corregido corregido : corregidos) {
				if (corregido.getPreventivo() != null && corregido.getPreventivo().getChequeo() != null) {

					ItemChequeado ic = null;

					ic = new ItemChequeado();
					ItemChequeoDao itemChequeoDao = new ItemChequeoDao();
					List<ItemChequeo> itemsChequeo = itemChequeoDao.getPorTipo(corregido.getPreventivo().getChequeo().getTipo(), null);
					for (ItemChequeo item :itemsChequeo){
						if (item.getNombre().startsWith("otros")){
							ic.setItemChequeo(item); 
						}
					}

					if (ic != null) {
						ic.setValor("R");
						ic.getListaDeCorregidos().add(corregido);
						if (ic.getId() == 0){
							itemChequeadoDao.save(ic);
							corregido.getPreventivo().getChequeo().getItemsChequeados().add(ic);
							new ChequeoDao().update(corregido.getPreventivo().getChequeo());
						} else {
							itemChequeadoDao.update(ic);
						}
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			tx.close();
		}
	}
}
