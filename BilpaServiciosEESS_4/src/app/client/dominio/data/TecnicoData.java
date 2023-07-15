package app.client.dominio.data;

public class TecnicoData implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private String nombre;
	private String apellido;
	private String nombreUsuario;
	private String nombreCompleto;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public void setApellido(String apel) {
		this.apellido = apel;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	
	public void setNombreCompleto(String nombre, String apellido) {
		this.nombreCompleto = (nombre+" "+apellido);
	}

}
