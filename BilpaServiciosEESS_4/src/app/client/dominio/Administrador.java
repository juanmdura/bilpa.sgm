package app.client.dominio;

public class Administrador extends Persona{
	
	public Administrador() {
		super(1);
	}
	
	public Administrador(String nombre, String apellido, String email, String usuario, String clave, char sexo, String telefono) {
		super(1);
		this.setNombre(nombre);
		this.setApellido(apellido);
		this.setEmail(email);
		this.setNombreDeUSuario(usuario);
		this.setClave(clave);
		this.setSexo(sexo);
		this.setTelefono(telefono);
	}

	
	
	public Administrador copiar() {
		Administrador copia = new Administrador();
		copiarPropiedades(copia);
		return copia;
	}

	public Administrador copiarTodo() {
		Administrador copia = new Administrador();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(Administrador copia) {
		copia.setId(getId());
		copia.setClave(getClave());
		copia.setEmail(getEmail());
		copia.setNombre(getNombre());
		copia.setApellido(getApellido());
		copia.setNombreDeUSuario(getNombreDeUSuario());
		copia.setRol(getRol());
		copia.setSexo(getSexo());
		copia.setTelefono(getTelefono());
		if (getEmpresa() != null) //Usuario nuevo
		{
			copia.setEmpresa(getEmpresa().copiar());			
		}
	}

	public void copiarColecciones(Administrador copia) {
		
	}
	
	public String toString(){
		return this.getNombre() + " " + this.getApellido();
	}


}
