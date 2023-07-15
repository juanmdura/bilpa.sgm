package app.server.control;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import app.client.dominio.Pico;
import app.client.dominio.QR;
import app.client.dominio.data.PicoData;
import app.client.dominio.data.ProductoData;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.PicoDao;
import app.server.persistencia.QrDao;

public class ControlPico {

	private static ControlPico instancia = null;

	public static ControlPico getInstancia() {
		if(instancia == null){
			instancia = new ControlPico();
		}
		return instancia;
	}

	private ControlPico() {
		super();
	}

	public List<PicoData> obtenerPicos(int idSurtidor) {

		List<PicoData> picosData = new ArrayList<PicoData>();

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			PicoDao picoDao = new PicoDao();
			List<Pico> picos = picoDao.getPicos(idSurtidor);

			for(Pico p : picos) {
				picosData.add(p.getPicoData());
			}

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			tx.close();
		}
		return picosData;

	}

	public boolean asociarQrAPico(int codigoQr, int idPico) throws ConstraintViolationException, Exception {

		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PicoDao picoDao = new PicoDao();
			Pico pico = picoDao.get(idPico);

			if(pico.getQr() == null) {
				QR qr = new QR();
				qr.setCodigo(codigoQr+"");
				pico.setQr(qr);
			} else {
				pico.getQr().setCodigo(codigoQr+"");
			}

			picoDao.update(pico);
			tx.commit();
			return true;
		} catch (ConstraintViolationException cve) {
			tx.rollback();
			throw cve;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			tx.close();
		}
	}

	public Pico getPico(int idPico) throws Exception {

		Pico pico = null;
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PicoDao picoDao = new PicoDao();
			pico = picoDao.get(idPico);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			tx.close();
		}
		return pico;
	}

	public Pico buscarPico(String codigoQR) {
		Pico pico = null;
		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			PicoDao picoDao = new PicoDao();
			pico = picoDao.get(new QrDao().get(codigoQR));
			if (pico != null)
				pico.copiar();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return pico;
	}

	private List<Pico> getPicos(int idSurtidor) {

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			PicoDao picoDao = new PicoDao();
			List<Pico> picos = picoDao.getPicos(idSurtidor);
			tx.commit();
			return picos;

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();

		}finally{
			tx.close();
		}
		return null;

	}

	public List<ProductoData> obtenerProductos(int idSurtidor) {

		List<ProductoData> productosData = new ArrayList<ProductoData>();
		List<Pico> picos = getPicos(idSurtidor);

		for(Pico p : picos) {
			ProductoData pd = p.getProducto().getData();
			if(pd.getId() != 1 && !productosData.contains(pd)) {
				productosData.add(pd);
			}
		}
		return productosData;
	}
}
