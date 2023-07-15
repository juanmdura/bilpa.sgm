package app.client.dominio;

public class ContactoEmpresa extends Persona{
	
	public ContactoEmpresa(){
		super(4);
	}
	
	public ContactoEmpresa(String nombre, String apellido, String email, String usuario, String clave, char sexo, String telefono) {
		super(4);
		this.setNombre(nombre);
		this.setApellido(apellido);
		this.setEmail(email);
		this.setNombreDeUSuario(usuario);
		this.setClave(clave);
		this.setSexo(sexo);
		this.setTelefono(telefono);
	}

	public ContactoEmpresa copiar() {
		ContactoEmpresa copia = new ContactoEmpresa();
		copiarPropiedades(copia);
		return copia;
	}

	public ContactoEmpresa copiarTodo() {
		ContactoEmpresa copia = new ContactoEmpresa();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(ContactoEmpresa copia) {
		copia.setId(getId());
		copia.setClave(getClave());
		copia.setEmail(getEmail());
		copia.setNombre(getNombre());
		copia.setApellido(getApellido());
		copia.setNombreDeUSuario(getNombreDeUSuario());
		copia.setRol(getRol());
		copia.setSexo(getSexo());
		copia.setTelefono(getTelefono());
		if (getEmpresa() != null)
		{
			copia.setEmpresa(getEmpresa().copiar());			
		}
	}

	public void copiarColecciones(ContactoEmpresa copia) {
		
	}
	

}
