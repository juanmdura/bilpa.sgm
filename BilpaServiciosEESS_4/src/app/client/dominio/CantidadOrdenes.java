package app.client.dominio;


public class CantidadOrdenes implements com.google.gwt.user.client.rpc.IsSerializable
{
	private int id;
	private int cantidad;
	private int cantidadTotalDeOrdenesInactivas;
	private Persona usuario;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCantidadTotalDeOrdenesInactivas() {
		return cantidadTotalDeOrdenesInactivas;
	}
	public void setCantidadTotalDeOrdenesInactivas(
			int cantidadTotalDeOrdenesInactivas) {
		this.cantidadTotalDeOrdenesInactivas = cantidadTotalDeOrdenesInactivas;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public Persona getUsuario() {
		return usuario;
	}
	public void setUsuario(Persona usuario) {
		this.usuario = usuario;
	}
	public CantidadOrdenes(int cantidad, Persona usuario) 
	{
		super();
		this.cantidad = cantidad;
		this.usuario = usuario;
	}
	
	public CantidadOrdenes() {}
	
	
}
