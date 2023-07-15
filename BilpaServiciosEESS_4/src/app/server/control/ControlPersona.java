package app.server.control;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sun.misc.BASE64Encoder;
import app.client.dominio.Administrador;
import app.client.dominio.Administrativo;
import app.client.dominio.ContactoEmpresa;
import app.client.dominio.Encargado;
import app.client.dominio.Estacion;
import app.client.dominio.Orden;
import app.client.dominio.Persona;
import app.client.dominio.Tecnico;
import app.client.dominio.data.TecnicoData;
import app.server.persistencia.AdministradorDao;
import app.server.persistencia.AdministrativoDao;
import app.server.persistencia.ContactoEmpresaDao;
import app.server.persistencia.DaoTransaction;
import app.server.persistencia.EncargadoDao;
import app.server.persistencia.PersonaDao;
import app.server.persistencia.TecnicoDao;

public class ControlPersona {

	private static ControlPersona instancia = null;

	public static ControlPersona getInstancia() {
		if(instancia == null){
			instancia = new ControlPersona();
		}
		return instancia;
	}

	public static void setInstancia(ControlPersona instancia) {
		ControlPersona.instancia = instancia;
	}

	private ControlPersona (){

	}

	public String encriptar(String clave) throws IllegalStateException {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("SHA");
		}
		catch(NoSuchAlgorithmException e) {
			throw new IllegalStateException(e.getMessage());
		}

		try {
			md.update(clave.getBytes("UTF-8"));
		}
		catch(UnsupportedEncodingException e) {
			throw new IllegalStateException(e.getMessage());
		}

		byte raw[] = md.digest();
		String hash = (new BASE64Encoder()).encode(raw);
		return hash;
	}

	public Persona validarUsuario(String usuario, String password){
		ArrayList<Persona> todos = this.obtenerTodosLosUsuarios();
		for (Persona persona : todos) {
			if(persona.getNombreDeUSuario().equals(usuario)){
				String claveCifrada = this.encriptar(password);
				if(persona.getClave().equals(claveCifrada)){
					return (Persona) persona.copiar();
				}
			}
		}		
		return null;
	}

	public boolean validarNombreDeUsuarioDisponible(String nombreUsuario){
		if(!nombreUsuario.equalsIgnoreCase("")){
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();
				PersonaDao dao = new PersonaDao();
				Persona p = dao.obtenerUsuario(nombreUsuario);

				if(p == null){
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

	public Persona obtenerUsuario(String nombreUsuario){
		if(!nombreUsuario.equalsIgnoreCase("")){
			DaoTransaction tx = new DaoTransaction();

			try {
				tx.begin();
				PersonaDao dao = new PersonaDao();
				Persona p = dao.obtenerUsuario(nombreUsuario);

				return p;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
				tx.close();
			}
		}
		return null;
	}

	public Tecnico obtenerTecnico(int id){

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			TecnicoDao dao = new TecnicoDao();
			Tecnico t = dao.get(id);
			return t;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return null;
	}

	public boolean agregarUsuario(Persona persona){
		if(persona != null){

			if(persona.validarCampos()){
				int rol = persona.getRol();
				persona.setClave(this.encriptar(persona.getClave()));//Encriptamos
				if(rol > 0){
					if(rol == 1){
						Administrador admin = (Administrador) persona;
						this.agregarAdministrador(admin);
					}else if(rol == 2){
						Administrativo admin = (Administrativo) persona;
						this.agregarAdministrativo(admin);
					}else if(rol == 3){
						Tecnico tecnico = (Tecnico) persona;
						this.agregarTecnico(tecnico);
					}else if(rol == 4){
						ContactoEmpresa contacto = (ContactoEmpresa) persona;
						this.agregarContactoEmpresa(contacto);
					}else if(rol ==5){
						Encargado enc = (Encargado) persona;
						this.agregarEncargado(enc);
					}
					return true;
				}

			}
		}
		return false;		
	}

	public void agregarAdministrador(Administrador admin){
		if(admin != null){
			DaoTransaction tx = new DaoTransaction();
			try {

				tx.begin();

				AdministradorDao dao = new AdministradorDao();
				dao.save(admin);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
				tx.close();
			}
		}
	}

	public void agregarEncargado(Encargado enc){
		if(enc != null){
			DaoTransaction tx = new DaoTransaction();
			try {

				tx.begin();

				EncargadoDao dao = new EncargadoDao();
				dao.save(enc);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally{
				tx.close();
			}
		}
	}

	public void agregarAdministrativo(Administrativo admin){
		if(admin != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				AdministrativoDao dao = new AdministrativoDao();
				dao.save(admin);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
				tx.close();
			}
		}

	}


	public void agregarTecnico(Tecnico tecnico){
		if(tecnico != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				TecnicoDao dao = new TecnicoDao();
				dao.save(tecnico);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally{
				tx.close();
			}
		}

	}

	public void agregarContactoEmpresa(ContactoEmpresa contacto){
		if(contacto != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				ContactoEmpresaDao dao = new ContactoEmpresaDao();
				dao.save(contacto);
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally{
				tx.close();
			}
		}

	}

	public ArrayList<Persona> obtenerTodosLosUsuarios(){
		ArrayList<Persona> listaUsuarios = new ArrayList<Persona>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			this.cargarAdministradores(listaUsuarios);
			this.cargarAdministrativos(listaUsuarios);
			this.cargarEncargados(listaUsuarios);
			this.cargarContactosEmpresa(listaUsuarios);
			this.cargarTecnicos(listaUsuarios);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return listaUsuarios;
	}

	public void cargarAdministradores(ArrayList<Persona> listaUsuarios){
		AdministradorDao dao = new AdministradorDao();
		List<Administrador> lista = dao.list();

		for (Administrador a : lista) {
			listaUsuarios.add(a.copiar());
		}

	}

	public void cargarEncargados(ArrayList<Persona> listaUsuarios){
		EncargadoDao dao = new EncargadoDao();
		List<Encargado> lista = dao.list();

		for (Encargado a : lista) {
			listaUsuarios.add(a.copiar());
		}

	}

	public void cargarAdministrativos(ArrayList<Persona> listaUsuarios){
		AdministrativoDao dao = new AdministrativoDao();
		List<Administrativo> lista = dao.list();

		for (Administrativo a : lista) {
			listaUsuarios.add(a.copiar());
		}
	}

	public void cargarContactosEmpresa(ArrayList<Persona> listaUsuarios){
		ContactoEmpresaDao dao = new ContactoEmpresaDao();
		List<ContactoEmpresa> lista = dao.list();

		for (ContactoEmpresa a : lista) {
			listaUsuarios.add(a.copiar());
		}
	}

	public void cargarTecnicos(ArrayList<Persona> listaUsuarios){
		TecnicoDao dao = new TecnicoDao();
		List<Tecnico> lista = dao.list();

		for (Tecnico a : lista) {
			listaUsuarios.add(a.copiar());
		}
	}

	public Persona buscarUsuario(int id){
		if(id > 0){
			ArrayList<Persona> todos = new ArrayList<Persona>();
			todos = this.obtenerTodosLosUsuarios();
			for (Persona p : todos) {
				if(p.getId() == id){
					return p;
				}
			}
		}
		return null;
	}

	public ArrayList<Tecnico> listaTecnicos(){
		ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			TecnicoDao daoT = new TecnicoDao();

			List<Tecnico> listaTec = daoT.list();

			for (Tecnico a : listaTec) {
				tecnicos.add(a.copiar());
			}


		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return tecnicos;
	}

	public ArrayList<Persona> cargarTecnicosYEnc(){
		ArrayList<Persona>  tecnicosYEnc = new ArrayList<Persona>();
		ArrayList<Encargado> encargados = listaEncargados();
		ArrayList<Tecnico> tecnicos = listaTecnicos();

		for(Persona enc: encargados){
			tecnicosYEnc.add((Persona) enc.copiar());
		}

		for(Persona tec: tecnicos){
			tecnicosYEnc.add((Persona) tec.copiar());
		}
		Collections.sort(tecnicosYEnc);
		return tecnicosYEnc;
	}

	public ArrayList<Encargado> listaEncargados(){
		ArrayList<Encargado> encargados = new ArrayList<Encargado>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			EncargadoDao dao = new EncargadoDao();
			List<Encargado> lista = dao.list();

			for (Encargado a : lista) {
				encargados.add(a.copiar());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();			
		}finally{
			tx.close();
		}
		return encargados;
	}

	public void notificarEncargados(Orden orden){
		ArrayList<Encargado> encargados = this.listaEncargados();
		String body = "Fue ingresado el Correctivo Nro " + orden.getNumero() + ", correspondiente a la empresa: " + orden.getEmpresa().toString() + ", creada por: " + orden.getCreador().toString();
		for (Encargado encargado : encargados) {
			ControlEmail.getInstancia().enviarEmail(body, encargado.getEmail());

		}
	}

	public void notificarEncargadosReparado(Orden orden){
		ArrayList<Encargado> encargados = this.listaEncargados();
		String body = "Fue reparado el Correctivo Nro " + orden.getNumero() + ", correspondiente a la empresa: " + orden.getEmpresa().toString() + ", creada por: " + orden.getCreador().toString();
		for (Encargado encargado : encargados) {
			ControlEmail.getInstancia().enviarEmail(body, encargado.getEmail());

		}
	}

	public void notificarEncargadosOrdenRebotada(Orden orden, Tecnico tecnico){
		ArrayList<Encargado> encargados = this.listaEncargados();
		String body = "El Correctivo Nro " + orden.getNumero() + ", correspondiente a la empresa: " + orden.getEmpresa().toString() + ", fue rechazado por el técnico " + tecnico.toString();
		for (Encargado encargado : encargados) {
			ControlEmail.getInstancia().enviarEmail(body, encargado.getEmail());

		}
	}

	public void notificarTecnico(Orden orden){
		String body = "El Correctivo Nro " + orden.getNumero() + " le ha sido asignado.";
		String email = orden.getTecnicoAsignado().getEmail();
		ControlEmail.getInstancia().enviarEmail(body, email);
	}

	public boolean validarEliminarPersona(Persona persona){
		return ControlOrden.getInstancia().validarOrdenesUsuario(persona.getId());
	}

	public boolean modificarPersona(Persona persona, boolean claveCambiada){
		if(persona != null){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				PersonaDao dao = new PersonaDao();
				if(claveCambiada == true){
					persona.setClave(this.encriptar(persona.getClave()));
				}
				dao.merge(persona);
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

	public boolean eliminarPersona(Persona persona)
	{
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			PersonaDao dao = new PersonaDao();
			dao.delete(persona);
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

	public ArrayList<ContactoEmpresa> obtenerEmpleadosSinEmpresa(){
		ArrayList<ContactoEmpresa> contactos = new ArrayList<ContactoEmpresa>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ContactoEmpresaDao daoCE = new ContactoEmpresaDao();

			List<ContactoEmpresa> listaCont = daoCE.list();

			for (ContactoEmpresa ce : listaCont) {
				if (ce.getEmpresa() == null)
				{
					contactos.add(ce);					
				}
			}


		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return contactos;
	}

	public ArrayList<ContactoEmpresa> obtenerEmpleadosPorEstacion(Estacion estacion){
		ArrayList<ContactoEmpresa> contactos = new ArrayList<ContactoEmpresa>();

		DaoTransaction tx = new DaoTransaction();

		try {
			tx.begin();
			ContactoEmpresaDao daoCE = new ContactoEmpresaDao();

			List<ContactoEmpresa> listaCont = daoCE.list();

			for (ContactoEmpresa ce : listaCont) {
				if (ce.getEmpresa() != null && ce.getEmpresa().getId() == estacion.getId())
				{
					contactos.add(ce.copiar());					
				}
			}
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			tx.close();
		}
		return contactos;
	}

	public boolean actualizarUsuariosSinAsignarParche(List<ContactoEmpresa> listaEmpleadosSinAsignar) {
		if(listaEmpleadosSinAsignar.size()>0){
			DaoTransaction tx = new DaoTransaction();
			try {
				tx.begin();
				PersonaDao dao = new PersonaDao();

				for (ContactoEmpresa ce : listaEmpleadosSinAsignar)
				{
					dao.merge(ce);
				}

				tx.commit();
				return true;

			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			}finally{
				tx.close();
			}
		}
		else
		{
			return true;
		}
		return false;
	}

	public List<TecnicoData> obtenerTodosLosTecnicosData(){
		List<TecnicoData> tecnicosData = new ArrayList<TecnicoData>();

		DaoTransaction tx = new DaoTransaction();

		try 
		{
			tx.begin();
			TecnicoDao dao = new TecnicoDao();
			List<Tecnico> tecnicos = dao.list();
			Collections.sort(tecnicos);

			for (Tecnico t : tecnicos) 
			{
				tecnicosData.add(t.getTecnicoData());
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

		return tecnicosData;
	}

	public ArrayList<Tecnico> obtenerTodosLosTecnicos() 
	{
		ArrayList<Tecnico> tecnicos = new ArrayList<Tecnico>();
		DaoTransaction tx = new DaoTransaction();

		try 
		{
			tx.begin();
			TecnicoDao dao = new TecnicoDao();
			List<Tecnico> lista = dao.list();

			for (Tecnico a : lista) 
			{
				tecnicos.add(a.copiar());
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

		return tecnicos;
	}

	public Tecnico obtnerTecnicoPorNombre(String nombre, String apellido) {
		DaoTransaction tx = new DaoTransaction();
		try {
			tx.begin();
			TecnicoDao dao = new TecnicoDao();
			Tecnico t = dao.obtenerTecnicoPorNombreCompleto(nombre, apellido);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			tx.close();
		}

		return null;
	}

}
