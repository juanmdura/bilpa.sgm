package app.client.dominio.data;

public class RepuestoLineaCorregidoData implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	private int idRepuesto;
	private boolean nuevo;
	private int cantidad;
	private String descripcion;

	public RepuestoLineaCorregidoData(int id, int idRepuesto, boolean nuevo, int cantidad, String descripcion) {
		setId(id);
		setIdRepuesto(idRepuesto);
		setCantidad(cantidad);
		setNuevo(nuevo);
		setDescripcion(descripcion);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdRepuesto() {
		return idRepuesto;
	}
	public void setIdRepuesto(int idRepuesto) {
		this.idRepuesto = idRepuesto;
	}
	public boolean isNuevo() {
		return nuevo;
	}
	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
