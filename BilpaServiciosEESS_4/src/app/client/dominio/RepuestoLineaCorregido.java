package app.client.dominio;



public class RepuestoLineaCorregido implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;

	private Repuesto repuesto;
	
	private int cantidad;
	
	private boolean nuevo;
		
	public RepuestoLineaCorregido(){
		
	}
	
	public RepuestoLineaCorregido(int id,	Repuesto repuesto, boolean nuevo, int cantidad2) {
		if (id > 0){
			setId(id);
		}
		setNuevo(nuevo);
		setCantidad(cantidad2);
		setRepuesto(repuesto);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isNuevo() {
		return nuevo;
	}

	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public Repuesto getRepuesto() {
		return repuesto;
	}
	
	public void setRepuesto(Repuesto repuesto) {
		this.repuesto = repuesto;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public String toString(){
		return this.getRepuesto() + " " + this.getCantidad();
	}
	
}
