package app.client.dominio.json;

/**
 * 
 * @author dfleitas
 *
 */
public class RepuestoLineaCorregidoJson {
	
	private int id;
	private int idRepuesto;
	private boolean nuevo;
	private int cantidad;
	
	
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
	
	
	public RepuestoLineaCorregidoJson() {
		super();
	}

}
