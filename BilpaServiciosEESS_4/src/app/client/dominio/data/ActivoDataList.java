package app.client.dominio.data;

public class ActivoDataList implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	private String descripcion;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}
	
	public ActivoDataList() {}
}
