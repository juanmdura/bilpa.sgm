package app.client.dominio;

import app.client.dominio.data.UsuarioData;


public abstract class Persona implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<Persona> {

	private int id;
	private String nombre;
	private String apellido;
	private String nombreDeUSuario;
	private String clave;
	private String email;
	private String telefono;
	private char sexo;
	private int rol;
	private Estacion empresa;
	
	public int getId() {
		return id;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRol() {
		return rol;
	}

	public void setRol(int rol) {
		this.rol = rol;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombreDeUSuario() {
		return nombreDeUSuario;
	}

	public void setNombreDeUSuario(String nombreDeUSuario) {
		this.nombreDeUSuario = nombreDeUSuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public char getSexo() {
		return sexo;
	}

	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Persona(int rol) {
		this.rol = rol;
	}
	
	public Persona(String nombreUsuario) {
		this.nombreDeUSuario = nombreUsuario;
	}
	
	public Estacion getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Estacion empresa) {
		this.empresa = empresa;
	}

	public Persona() {
		
	}
	
	public String toString(){
		return this.nombre.trim() + " " + this.apellido.trim();
	}
	
	//El sexo no se valida?????
	public boolean validarCampos(){
		if(!this.getApellido().equalsIgnoreCase("")){
			if(!this.getEmail().equalsIgnoreCase("")){
				if(!this.getNombre().equalsIgnoreCase("")){
					if(!this.getNombreDeUSuario().equalsIgnoreCase("")){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public abstract Object copiar();
	public abstract Object copiarTodo();
	
	public UsuarioData getUsuarioData(Persona persona) {
		
		UsuarioData aux = new UsuarioData();
		
		aux.setId(persona.getId());
		aux.setNombre(persona.getNombre());
		aux.setApellido(persona.getApellido());
		aux.setRol(persona.getRol());
		
		return aux;
	}

	public int compareTo(Persona p) {
		return getNombre().compareTo(p.getNombre());
	}
}
