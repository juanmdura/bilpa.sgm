package app.client.dominio.data;

public class UsuarioData implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	
	private int rol;
	
	private String nombre;
	private String apellido;
	
	public UsuarioData(){
		
	}
	
	public int getId() {
		return id;
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
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	
}
