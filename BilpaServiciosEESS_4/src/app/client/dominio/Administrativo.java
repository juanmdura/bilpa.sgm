package app.client.dominio;

public class Administrativo extends Persona{
	
	public Administrativo(){
		super(2);
	}
	
	public Administrativo(String nombre, String apellido, String email, String usuario, String clave, char sexo, String telefono) {
		super(2);
		this.setNombre(nombre);
		this.setApellido(apellido);
		this.setEmail(email);
		this.setNombreDeUSuario(usuario);
		this.setClave(clave);
		this.setSexo(sexo);
		this.setTelefono(telefono);
	}

	public Administrativo copiar() {
		Administrativo copia = new Administrativo();
		copiarPropiedades(copia);
		return copia;
	}

	public Administrativo copiarTodo() {
		Administrativo copia = new Administrativo();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(Administrativo copia) {
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

	public void copiarColecciones(Administrativo copia) {
		
	}
	

}
