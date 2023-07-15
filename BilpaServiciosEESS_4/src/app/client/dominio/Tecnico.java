package app.client.dominio;

import app.client.dominio.data.TecnicoData;

public class Tecnico extends Persona{

	public Tecnico(){
		super(3);
	}
	
	public Tecnico(String nombre){
		super(3);
		this.setNombre(nombre);
	}
	
	public Tecnico(String nombre, String apellido, String email, String usuario, String clave, char sexo, String telefono) {
		super(3);
		this.setNombre(nombre);
		this.setApellido(apellido);
		this.setEmail(email);
		this.setNombreDeUSuario(usuario);
		this.setClave(clave);
		this.setSexo(sexo);
		this.setTelefono(telefono);
	}
	
	public Tecnico(int idTecnico) {
		setId(idTecnico);
	}

	public Tecnico(int id, String string) {
		setId(id);
		setNombre(string);
	}

	public String toString() {
		return this.getNombre() + " " + this.getApellido();
	}

	public Tecnico copiar() {
		Tecnico copia = new Tecnico();
		copiarPropiedades(copia);
		return copia;
	}

	public Tecnico copiarTodo() {
		Tecnico copia = new Tecnico();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(Tecnico copia) {
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

	public void copiarColecciones(Tecnico copia) {
		
	}
	@Override
	public boolean equals(Object obj) 
	{
		if(obj != null && obj.getClass().equals(Tecnico.class))
		{
			Persona p = (Persona)obj;
			return p.getId()==getId();
		}
		return false;
	}

	public TecnicoData getTecnicoData() {
		
		TecnicoData td = new TecnicoData();
		td.setId(this.getId());
		td.setNombre(this.getNombre());
		td.setApellido(this.getApellido());
		td.setNombreCompleto(this.getNombre(), this.getApellido());
		td.setNombreUsuario(this.getNombreDeUSuario());
		return td;
	}
}
