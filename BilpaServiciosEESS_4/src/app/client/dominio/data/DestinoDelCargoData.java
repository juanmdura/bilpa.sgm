package app.client.dominio.data;


public class DestinoDelCargoData implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;

	private String nombre;

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

	public DestinoDelCargoData() {

	}

	public DestinoDelCargoData(int id, String nombre) {
		setId(id);
		setNombre(nombre);
	}

}
