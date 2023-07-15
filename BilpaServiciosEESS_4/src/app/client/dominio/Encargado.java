package app.client.dominio;

public class Encargado extends Persona{

	public Encargado(){
		super(5);
	}
	
	public Encargado(String nombre, String apellido, String email, String usuario, String clave, char sexo, String telefono) {
		super(5);
		this.setNombre(nombre);
		this.setApellido(apellido);
		this.setEmail(email);
		this.setNombreDeUSuario(usuario);
		this.setClave(clave);
		this.setSexo(sexo);
		this.setTelefono(telefono);
	}


	public Encargado copiar() {
		Encargado copia = new Encargado();
		copiarPropiedades(copia);
		return copia;
	}

	public Encargado copiarTodo() {
		Encargado copia = new Encargado();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(Encargado copia) {
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

	public void copiarColecciones(Encargado copia) {
		
	}
	
	public String toString(){
		return this.getNombre() + " " + this.getApellido();
	}

}
