package app.server.control;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import app.client.dominio.Activo;
import app.client.dominio.BombaSumergible;
import app.client.dominio.Canio;
import app.client.dominio.ChequeoPico;
import app.client.dominio.Contador;
import app.client.dominio.Estacion;
import app.client.dominio.Marca;
import app.client.dominio.Modelo;
import app.client.dominio.ModeloSurtidor;
import app.client.dominio.Orden;
import app.client.dominio.Pico;
import app.client.dominio.Producto;
import app.client.dominio.QR;
import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;
import app.client.dominio.Surtidor;
import app.client.dominio.Tanque;
import app.client.dominio.data.DatoListadoReparaciones;
import app.client.dominio.data.DatoOrdenesActivasEmpresa;
import app.server.persistencia.ActivoDao;
import app.server.persistencia.BombaSumergibleDao;
import app.server.persistencia.CanioDao;
import app.server.persistencia.ChequeoPicoDao;
import app.server.persistencia.ContadorDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.MarcaDao;
import app.server.persistencia.ModeloDao;
import app.server.persistencia.ModeloSurtidorDao;
import app.server.persistencia.PicoDao;
import app.server.persistencia.ProductoDao;
import app.server.persistencia.QrDao;
import app.server.persistencia.ReparacionDao;
import app.server.persistencia.RepuestoLineaDao;
import app.server.persistencia.SurtidorDao;
import app.server.persistencia.TanqueDao;

public class ControlActivo {

	private static ControlActivo instancia = null;

	public static ControlActivo getInstancia() {
		if(instancia == null){
			instancia = new ControlActivo();
		}
		return instancia;
	}

	public static void setInstancia(ControlActivo instancia) {
		ControlActivo.instancia = instancia;
	}

	private ControlActivo (){

	}

	public boolean validarNombreDeMarcaDisponible(String nombreMarca){
		if(!nombreMarca.equalsIgnoreCase("")){
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();
				MarcaDao dao = new MarcaDao();
				Marca m =  dao.buscarMarca(nombreMarca);

				if(m == null){
					return true;
				}

				return false;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
				tx.close();
			}
		}
		return false;
	}

	public boolean validarModeloDisponible(String nombre, int idMarca, int idModeloExistente){
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			ModeloSurtidorDao dao = new ModeloSurtidorDao();

			List<ModeloSurtidor> modelos = dao.obtenerModelos(nombre, idMarca, idModeloExistente);

			return modelos.size() == 0;
			/*for (ModeloSurtidor modeloSurtidor : modelos) {
				if(modeloSurtidor.getNombre().equalsIgnoreCase(nombre) && modeloSurtidor.getMarca().getId() == idMarca){
					return false;//nombre de modelo en uso
				}
			}*/				
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return true;
	}

	public ArrayList<Marca> obtenerTodasLasMArcas(){
		ArrayList<Marca> marcas = new ArrayList<Marca>();
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			MarcaDao dao = new MarcaDao();
			List<Marca> lista = dao.list();
			for (Marca m : lista) {
				marcas.add(m.copiar());
			}
			return marcas;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}
		return marcas;
	}

	public List<Producto> obtenerTiposCombustibles(){
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			return new ProductoDao().list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}

		/*retorno.add(TipoCombustible.Super95SP);
		retorno.add(TipoCombustible.Especial87SP);
		retorno.add(TipoCombustible.Premium97SP);
		retorno.add(TipoCombustible.GasOil);
		retorno.add(TipoCombustible.GasOilEspecial);
		retorno.add(TipoCombustible.Super95SPEthanol);
		retorno.add(TipoCombustible.Especial87SPEthanol);
		retorno.add(TipoCombustible.Premium97SPEthanol);
		retorno.add(TipoCombustible.Queroseno);*/
		return new ArrayList<Producto>();
	}

	public Marca buscarMarca(String nombre){

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			MarcaDao dao = new MarcaDao();
			Marca marcaRetorno;
			marcaRetorno = dao.buscarMarca(nombre);
			if(marcaRetorno != null){
				return marcaRetorno.copiar();

			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}

	public boolean agregarMarca(Marca marca){
		if(marca != null){

			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();
				MarcaDao dao = new MarcaDao();
				dao.save(marca);
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

	public boolean modificarMarca(int idMarca, String nuevoNombreMarca){
		Marca marca = buscarMarca(idMarca);
		if(marca != null){
			marca.setNombre(nuevoNombreMarca);
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				MarcaDao dao = new MarcaDao();
				dao.merge(marca);
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

	public Marca buscarMarca(int id){
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			MarcaDao dao = new MarcaDao();
			Marca marcaRetorno;
			marcaRetorno = dao.get(id);

			return marcaRetorno.copiarTodo();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}

	public Activo buscarActivo(int id){
		if(id > 0){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				ActivoDao dao = new ActivoDao();
				Activo activoRetorno;

				activoRetorno = dao.get(id);
				if (activoRetorno != null){
					return activoRetorno.copiarTodo();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				tx.close();
			}
		}
		return null;
	}

	public Activo buscarActivo(String codigoQR) {

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			QrDao qrDao = new QrDao();
			QR qr = qrDao.get(codigoQR);
			if(qr != null) {
				ActivoDao dao = new ActivoDao();
				Activo activo = dao.get(qr);
				if (activo != null)
					return activo.copiarTodo();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;

	}

	public boolean agregarActivo (Activo activo) {

		if(this.validarActivoExiste(activo)){
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();

				ActivoDao dao = new ActivoDao();

				dao.save(activo);

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

	public boolean modificarActivo(Activo activo){
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			ActivoDao dao = new ActivoDao();
			
			if (activo.getTipo() == 1){
				Surtidor surtidor = (Surtidor)activo;

				SurtidorDao daoSurtidor = new SurtidorDao();
				Surtidor surtidorDB = daoSurtidor.get(surtidor.getId());
				
				ModeloDao daoModelo = new ModeloDao();
				Modelo modeloDB = daoModelo.get(surtidorDB.getModelo().getId());
				Modelo modelo = surtidor.getModelo();
				
				corregirCantidadPicos(modelo, modeloDB, false, surtidor);
				activo = surtidor;
			}
			dao.merge(activo);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}
		return false;
	}

	public boolean eliminarAcivo(Activo activo) {

		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			ActivoDao dao = new ActivoDao();
			dao.delete(activo);
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

	//Retorna FALSE si ya existe!
	public boolean validarActivoExiste(Activo nuevoActivo){
		ArrayList<Activo> activos = new ArrayList<Activo>();
		activos = this.obtenerTodosLosActivos();

		for (int i = 0; i < activos.size(); i++) {
			Activo activo = (Activo) activos.get(i);

			if(activo.equals(nuevoActivo) && activo.getId() != nuevoActivo.getId()){
				return false;
			}
		}
		return true;
	}

	public ArrayList<Activo> obtenerTodosLosActivos(){
		ArrayList<Activo> todosLosActivos = new ArrayList<Activo>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			ActivoDao dao = new ActivoDao();

			List<Activo> lista = dao.list();

			for (Activo a : lista) {
				todosLosActivos.add(a.copiarTodo());
			}

			return todosLosActivos;

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}

		return todosLosActivos;
	}

	public ArrayList<Activo> obtenerTodosLosSurtidores() {
		ArrayList<Activo> activos = new ArrayList<Activo>();
		activos = this.listaSurtidores();
		return activos;
	}

	public ArrayList<Activo> obtenerTodosLosSurtidores(int idEmpresa) {
		ArrayList<Activo> activos = new ArrayList<Activo>();
		activos = this.listaSurtidores(idEmpresa);
		return activos;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Activo> obtenerTodosLosActivosTipoPorEstacion(int tipo, int idEstacion){
		ArrayList activos = new ArrayList();

		Estacion e = ControlEmpresa.getInstancia().buscarEstacion(idEstacion);

		if(tipo == 1){
			activos = this.obtenerSurtidoresPorEstacion(e);
		}

		return activos;
	}

	private ArrayList<Surtidor> obtenerSurtidoresPorEstacion(Estacion estacion){
		ArrayList<Surtidor> todosLosSurtidores = new ArrayList<Surtidor>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			SurtidorDao dao = new SurtidorDao();

			List<Surtidor> lista = dao.obtenerSurtidoresPorEstacion(estacion);

			for (Surtidor s : lista) {
				todosLosSurtidores.add(s.copiarTodo());
			}

			return todosLosSurtidores;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return todosLosSurtidores;
	}

	private ArrayList<Activo> listaSurtidores(){
		ArrayList<Activo> todosLosSurtidores = new ArrayList<Activo>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			SurtidorDao dao = new SurtidorDao();

			List<Surtidor> lista = dao.list();

			for (Surtidor s : lista) {
				todosLosSurtidores.add(s.copiarTodo());
			}

			return todosLosSurtidores;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return todosLosSurtidores;
	}

	private ArrayList<Activo> listaSurtidores(int idEmpresa){
		ArrayList<Activo> todosLosSurtidores = new ArrayList<Activo>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();

			SurtidorDao dao = new SurtidorDao();

			List<Surtidor> lista = dao.list();

			for (Surtidor s : lista) {
				todosLosSurtidores.add(s.copiarTodo());
			}

			return todosLosSurtidores;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return todosLosSurtidores;
	}

	public boolean agregarModelo(Modelo modelo){
		if(modelo != null){

			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();
				ModeloDao dao = new ModeloDao();
				dao.save(modelo);
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

	public boolean modificarModelo(Modelo modelo){
		if(modelo != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				ModeloDao dao = new ModeloDao();
				Modelo modeloDB = dao.get(modelo.getId());
				
				corregirCantidadPicos(modelo, modeloDB, true, null);
				
				dao.merge(modelo);
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

	private void corregirCantidadPicos(Modelo modelo, Modelo modeloDB, boolean todosLosModelos, Surtidor surtidor) throws Exception {//esta horrible este metodo, asi es la vida
		
		if (modeloDB.getTipo() == 1 && modelo.getTipo() == 1){
			ModeloSurtidor modeloSurtidorDB = (ModeloSurtidor)modeloDB;
			ModeloSurtidor modeloSurtidor = (ModeloSurtidor)modelo;

			boolean cambioCantidadDePicos = modeloSurtidor.getCantidadDePicos() != modeloSurtidorDB.getCantidadDePicos();

			if (cambioCantidadDePicos){
				SurtidorDao daoSurtidor = new SurtidorDao();
				List<Surtidor> surtidores;
				if (todosLosModelos){
					surtidores = daoSurtidor.obtenerSurtidoresPorModelo(modeloSurtidor.getId());
				} else {
					surtidores = new ArrayList<Surtidor>();
					surtidores.add(surtidor);
				}

				if (modeloSurtidor.getCantidadDePicos() > modeloSurtidorDB.getCantidadDePicos()){//si el nuevo tiene mas picos
					agregarPicosATodosLosSurtidores(surtidores, daoSurtidor, modeloSurtidor.getCantidadDePicos());

				} else if (modeloSurtidor.getCantidadDePicos() < modeloSurtidorDB.getCantidadDePicos()){ //si el nuevo tiene menos picos
					eliminarPicosATodosLosSurtidores(surtidores, daoSurtidor, modeloSurtidor.getCantidadDePicos());
				}

				actualizarNumerosDePicos(surtidores);
			}
		}
	}

	private void actualizarNumerosDePicos(List<Surtidor> surtidores) throws Exception {
		try {

			PicoDao daoPico = new PicoDao();
			for (Surtidor s : surtidores){
				List<Pico> picos = daoPico.getPicos(s.getId());

				int cont = 1;
				for (Pico pico : picos){
					pico.setNumeroPico(cont);
					daoPico.update(pico);
					cont++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}

	private void agregarPicosATodosLosSurtidores(List<Surtidor> surtidores, SurtidorDao daoSurtidor, int cantidadDePicos) throws Exception {
		try {

			for (Surtidor s : surtidores){
				int cantidadDePicosAAgregar = cantidadDePicos - s.getPicos().size();
				int cont = 0;
				while (cantidadDePicosAAgregar > cont){
					cont++;

					s = daoSurtidor.get(s.getId());
					Pico pico = new Pico();
					pico.setProducto(new Producto(1));
					pico.setSurtidor(s);
					pico.setNumeroPico(s.getPicos().size()+1);
					s.agregarPico(pico);

					daoSurtidor.save(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public boolean eliminarPicosATodosLosSurtidores(List<Surtidor> surtidores, SurtidorDao daoSurtidor, int cantidadDePicos) throws Exception {
		try {

			PicoDao daoPico = new PicoDao();
			ContadorDao daoCont = new ContadorDao();
			ChequeoPicoDao daoChequeoPico = new ChequeoPicoDao();
			for (Surtidor s : surtidores){
				List<Pico> picos = daoPico.getPicos(s.getId());
				int cantidadDePicosARemover = picos.size() - cantidadDePicos;
				int cont = 0;
				ArrayList<Pico> picosARemover = new ArrayList<Pico>();
				for (Pico pico : picos){
					if  (cont < cantidadDePicosARemover){
						cont++;
						List<Contador> contadores = daoCont.obtenerContadores(pico.getId());
						for (Contador contador : contadores){
							daoCont.delete(contador);
						}
						
						List<ChequeoPico> chequeosPicos = daoChequeoPico.obtenerPorPico(pico);
						for (ChequeoPico chequeoPico : chequeosPicos){
							daoChequeoPico.delete(chequeoPico);
						}
						
						picosARemover.add(pico);
						daoPico.delete(pico);
					}
				}
				s.getPicos().clear();
				
				for (Pico p : picos){
					boolean add = true;
					for (Pico pRemovido : picosARemover){
						if (p.getId() == pRemovido.getId()){
							add = false;
						}
					}
					if (add){
						s.getPicos().add(p);
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Modelo buscarModelo(int id){
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			ModeloDao dao = new ModeloDao();
			Modelo modeloRetorno;
			modeloRetorno = dao.get(id);
			return modeloRetorno;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return null;
	}

	public ArrayList<ModeloSurtidor> obtenerTodosLosModelosSurtidores(){
		ArrayList<ModeloSurtidor> modelos = new ArrayList<ModeloSurtidor>();
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			ModeloSurtidorDao dao = new ModeloSurtidorDao();
			//List<ModeloSurtidor> lista = dao.list();
			List<ModeloSurtidor> lista = dao.modelosOrdenados();
			
			for (ModeloSurtidor m : lista) {
				modelos.add(m.copiar());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally{
			tx.close();
		}
		return modelos;
	}

	public boolean guardarSurtidor(Surtidor s){
		if(s != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				SurtidorDao dao = new SurtidorDao();
				dao.save(s);
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

	public boolean actualizarSurtidor(Surtidor s){
		if(s != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				SurtidorDao dao = new SurtidorDao();
				dao.merge(s);
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

	public boolean guardarTanque(Tanque tanque){
		if(tanque != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();

				TanqueDao dao = new TanqueDao();
				dao.save(tanque);
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

	public boolean guardarCanio(Canio canio){
		if(canio != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();

				CanioDao dao = new CanioDao();
				dao.save(canio);
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

	public boolean guardarBomba(BombaSumergible bomba){
		if(bomba != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();

				BombaSumergibleDao dao = new BombaSumergibleDao();
				dao.save(bomba);
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

	public boolean validarSurtidorExiste(Surtidor s){
		//Retorna true en caso de que ya exista un surtidor con ese numero de serie.
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			SurtidorDao dao = new SurtidorDao();
			Surtidor aux = dao.obtenerSurtidor(s.getNumeroSerie());
			if(aux != null){
				return true;
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return false;
	}

	public ArrayList<RepuestoLinea> obtenerRepuestosLinea(Activo activo, Orden orden) 
	{
		ArrayList<RepuestoLinea> repuestos = new ArrayList<RepuestoLinea>();

		DaoTransaction tx = new DaoTransaction();
		try 
		{
			tx.begin();
			RepuestoLineaDao dao = new RepuestoLineaDao();
			List<RepuestoLinea> lista = dao.obtenerRepuestosOrden(orden);

			for (RepuestoLinea rl : lista)
			{
				if (rl.getActivo().getId() == activo.getId())
				{
					repuestos.add(rl.copiar());
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			tx.close();
		}
		return repuestos;
	}

	public ArrayList<RepuestoLinea> obtenerRepuestosLinea(Activo activo) 
	{
		ArrayList<RepuestoLinea> repuestos = new ArrayList<RepuestoLinea>();

		DaoTransaction tx = new DaoTransaction();
		try 
		{
			tx.begin();
			RepuestoLineaDao dao = new RepuestoLineaDao();
			List<RepuestoLinea> lista = dao.list();

			for (RepuestoLinea rl : lista)
			{
				if (rl.getActivo().getId() == activo.getId())
				{
					repuestos.add(rl.copiar());
				}
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
		return repuestos;
	}

	public ArrayList<DatoListadoReparaciones> obtenerDatoListadoActivosPorTipo(DatoOrdenesActivasEmpresa o) 
	{
		Orden orden = new Orden();
		int numeroOrden = Integer.valueOf(o.getNumero());
		orden.setNumero(numeroOrden);
		ArrayList<Reparacion> reparaciones = ControlReparacion.getInstancia().obtenerTodosLasReparaciones(orden);

		ArrayList<DatoListadoReparaciones> datoReparaciones = new ArrayList<DatoListadoReparaciones>();

		for (Reparacion reparacion : reparaciones)
		{
			Activo activo = reparacion.getActivo();
			ArrayList<RepuestoLinea> repuestos = obtenerRepuestosLinea(activo, orden);
			DatoListadoReparaciones dla = new DatoListadoReparaciones(reparacion, repuestos);
			datoReparaciones.add(dla);
		}

		return datoReparaciones;
	}

	public ArrayList<Reparacion> obtenerTodosLasReparacionesDeUnActivo(Activo a) 
	{
		ArrayList<Reparacion> todosLasReparaciones = new ArrayList<Reparacion>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ReparacionDao dao = new ReparacionDao();

			List<Reparacion> lista = dao.obtenerTodosLasReparacionesDeUnActivo(a);

			for (Reparacion r : lista) {

				todosLasReparaciones.add(r.copiarTodo());
			}

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return todosLasReparaciones;

	}

	public boolean asociarQrAActivo(int idActivo, int codigoQr) throws Exception {

		DaoTransaction tx = new DaoTransaction(); 
		try {
			tx.begin();
			ActivoDao activoDao = new ActivoDao();
			Activo activo = activoDao.get(idActivo);

			if(activo.getQr() == null) {
				QR qr = new QR();
				qr.setCodigo(codigoQr+"");
				activo.setQr(qr);
			} else {
				activo.getQr().setCodigo(codigoQr+"");
			}

			activoDao.update(activo);
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
}
