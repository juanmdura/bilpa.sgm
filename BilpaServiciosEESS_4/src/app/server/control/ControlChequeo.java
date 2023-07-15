package app.server.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import app.client.dominio.Activo;
import app.client.dominio.ActivoGenerico;
import app.client.dominio.Chequeo;
import app.client.dominio.ChequeoPico;
import app.client.dominio.ChequeoProducto;
import app.client.dominio.ChequeoSurtidor;
import app.client.dominio.ComentarioChequeo;
import app.client.dominio.ItemChequeado;
import app.client.dominio.ItemChequeo;
import app.client.dominio.Pico;
import app.client.dominio.Preventivo;
import app.client.dominio.Producto;
import app.client.dominio.TipoActivoGenerico;
import app.client.dominio.TipoChequeo;
import app.client.dominio.Visita;
import app.client.dominio.data.ChequeoData;
import app.client.dominio.data.ChequeoPreventivoData;
import app.client.dominio.data.ChequeoSurtidorPicoData;
import app.client.dominio.data.ChequeoSurtidorProductoData;
import app.client.dominio.data.ItemChequeoData;
import app.server.persistencia.ChequeoDao;
import app.server.persistencia.ChequeoPicoDao;
import app.server.persistencia.ChequeoProductoDao;
import app.server.persistencia.ChequeoSurtidorDao;
import app.server.persistencia.ComentarioChequeoDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.ItemChequeadoDao;
import app.server.persistencia.ItemChequeoDao;
import app.server.persistencia.PreventivoDao;

/**
 * 
 * @author dfleitas
 *
 */
public class ControlChequeo {

	private static ControlChequeo instancia = null;

	public static ControlChequeo getInstancia() {
		if(instancia == null){
			instancia = new ControlChequeo();
		}
		return instancia;
	}

	private ControlChequeo() {
		super();
	}

	public ItemChequeado obtenerItemChequeado(int id) {
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			return new ItemChequeadoDao().get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}
	
	public boolean guardarChequeoPico(ChequeoPico chequeoPico) {
		if(chequeoPico != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				ChequeoPicoDao dao = new ChequeoPicoDao();
				dao.save(chequeoPico);
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

	public boolean guardarChequeoSurtidor(ChequeoSurtidor chequeoSurtidor) {
		if(chequeoSurtidor != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				ChequeoSurtidorDao dao = new ChequeoSurtidorDao();
				dao.save(chequeoSurtidor);
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

	private int agregarPreventivoYChequeo(Visita visita, Activo activo, PreventivoDao preventivoDao, Preventivo preventivo, Chequeo chequeo) {
		int idPreventivo;
		if (preventivo == null){
			preventivo = new Preventivo();
			preventivo.setActivo(activo);
			preventivo.setVisita(visita);
			idPreventivo = preventivoDao.save(preventivo);
		} else {
			idPreventivo = preventivo.getId();
		}

		TipoActivoGenerico tipoActivoGenerico = null;
		if(activo.getClass().equals(ActivoGenerico.class)){
			tipoActivoGenerico = ((ActivoGenerico)activo).getTipoActivoGenerico();
		}
		List<ItemChequeo> items = ControlChequeo.getInstancia().obtenerItemsChequeo(chequeo.getTipo(), tipoActivoGenerico);

		chequeo.setItemsChequeados(new HashSet<ItemChequeado>());
		for(ItemChequeo itemChequeo : items) {
			ItemChequeado itemChequeado = new ItemChequeado();
			itemChequeado.setItemChequeo(itemChequeo);
			chequeo.getItemsChequeados().add(itemChequeado);
		}

		preventivo.setChequeo(chequeo);
		preventivoDao.save(preventivo);
		new ChequeoDao().save(chequeo);
		return idPreventivo;
	}

	public ChequeoPreventivoData obtenerChequeoPreventivo(Visita visita, Activo activo, ChequeoPreventivoData chequeoPreventivoData, Chequeo chequeo) throws Exception {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			PreventivoDao preventivoDao = new PreventivoDao();
			Preventivo preventivo = preventivoDao.get(visita.getId(), activo.getId());

			if(preventivo == null || preventivo.getChequeo() == null) {
				int idPreventivo = agregarPreventivoYChequeo(visita, activo, preventivoDao, preventivo, chequeo);

				chequeoPreventivoData.setIdPreventivo(idPreventivo);
				ChequeoData cd = preventivoDao.get(idPreventivo).getChequeo().getChequeoData();
				Collections.sort(cd.getItemsChequeados());
				chequeoPreventivoData.setChequeo(cd);
				tx.commit();
				return chequeoPreventivoData;
			} else {
				chequeoPreventivoData.setIdPreventivo(preventivo.getId());
				ChequeoData cd = preventivo.getChequeo().getChequeoData();
				Collections.sort(cd.getItemsChequeados());
				chequeoPreventivoData.setChequeo(cd);
				return chequeoPreventivoData;
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}
	}

	public ChequeoSurtidorProductoData getChequeoProducto(int idPreventivo, int idProducto) {

		DaoTransaction tx = new DaoTransaction();
		try {

			tx.begin();
			ChequeoProductoDao dao = new ChequeoProductoDao();
			ChequeoProducto chequeoDB = dao.obtenerChequeoProducto(idPreventivo, idProducto);
			Preventivo preventivo = new PreventivoDao().get(idPreventivo);
			
			ChequeoSurtidorProductoData cspd = new ChequeoSurtidorProductoData();
			cspd.setIdPreventivo(idPreventivo);
			if (chequeoDB == null){
				chequeoDB = new ChequeoProducto(new Producto(idProducto), new Preventivo(idPreventivo));
				getChequeoPicoYProducto(chequeoDB);
				dao.save(chequeoDB);
				
				ChequeoSurtidor chequeoSurtidor = new ChequeoSurtidorDao().get(preventivo.getChequeo().getId());
				if (chequeoSurtidor.buscarChequeoProducto(idProducto) == null){
					chequeoSurtidor.getListaDeProductos().add(chequeoDB);
					new ChequeoSurtidorDao().update(chequeoSurtidor);
				}
				
				tx.commit();
			}
			cspd.setChequeo(chequeoDB.getChequeoData());
			return cspd;

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;

	}

	public ChequeoSurtidorPicoData getChequeoPico(int idPreventivo, int idPico) {

		DaoTransaction tx = new DaoTransaction();
		try {

			tx.begin();
			ChequeoPicoDao dao = new ChequeoPicoDao();
			ChequeoPico chequeoDB = dao.get(idPreventivo, idPico);
			Preventivo preventivo = new PreventivoDao().get(idPreventivo);
			
			ChequeoSurtidorPicoData cspd = new ChequeoSurtidorPicoData();
			cspd.setIdPreventivo(idPreventivo);
			if (chequeoDB == null){
				chequeoDB = new ChequeoPico(new Pico(idPico), preventivo);
				getChequeoPicoYProducto(chequeoDB);
				dao.save(chequeoDB);
				
				ChequeoSurtidor chequeoSurtidor = new ChequeoSurtidorDao().get(preventivo.getChequeo().getId());
				if (chequeoSurtidor.buscarChequeoPico(idPico) == null){
					chequeoSurtidor.getListaDeChequeosPicos().add(chequeoDB);
					new ChequeoSurtidorDao().update(chequeoSurtidor);
				}
				tx.commit();
			}
			cspd.setChequeo(chequeoDB.getChequeoData());
			return cspd;

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}finally{
			tx.close();
		}
		return null;

	}

	private void getChequeoPicoYProducto(Chequeo chequeoNew) {
		List<ItemChequeo> items = ControlChequeo.getInstancia().obtenerItemsChequeo(chequeoNew.getTipo(), null);

		for(ItemChequeo itemChequeo : items) {
			ItemChequeado itemChequeado = new ItemChequeado();
			itemChequeado.setItemChequeo(itemChequeo);
			chequeoNew.getItemsChequeados().add(itemChequeado);
		}
	}

	public ItemChequeo getItemChequeo(String nombreItemChequeo) {
		DaoTransaction tx = new DaoTransaction();
		try {

			tx.begin();
			ItemChequeoDao dao = new ItemChequeoDao();
			ItemChequeo item = dao.get(nombreItemChequeo);

			return item;

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}

	public List<ItemChequeoData> obtenerItemsChequeoData(TipoChequeo tipoChequeo) {
		DaoTransaction tx = new DaoTransaction();
		List<ItemChequeoData> itemsChequeoData = new ArrayList<ItemChequeoData>();
		try {

			tx.begin();
			ItemChequeoDao dao = new ItemChequeoDao();
			List<ItemChequeo> items = dao.getPorTipo(tipoChequeo, null);

			for (ItemChequeo item : items){
				itemsChequeoData.add(item.getItemChequeoData());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return itemsChequeoData;
	}

	public List<ItemChequeo> obtenerItemsChequeo(TipoChequeo tipoChequeo, TipoActivoGenerico tipoActivoGenerico) {
		return new ItemChequeoDao().getPorTipo(tipoChequeo, tipoActivoGenerico);
	}

	public boolean actualizarPreventivo(int idPreventivo, Chequeo chequeo) throws Exception {
		return ControlPreventivo.getInstancia().actualizarPreventivo(idPreventivo, chequeo);
	}
}
