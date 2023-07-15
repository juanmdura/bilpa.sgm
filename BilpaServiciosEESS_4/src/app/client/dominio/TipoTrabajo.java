package app.client.dominio;


public class TipoTrabajo implements com.google.gwt.user.client.rpc.IsSerializable{
	
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
	
	public TipoTrabajo(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	public TipoTrabajo() {	}
	
	public TipoTrabajo(int idTipoTrabajo) {
		setId(idTipoTrabajo);
	}
	@Override
	public String toString() {
		return nombre;
	}
	public TipoTrabajo copiar() {
		TipoTrabajo copia = new TipoTrabajo();
		copiarPropiedades(copia);
		return copia;
	}
	
	private void copiarPropiedades(TipoTrabajo copia) {
		copia.setId(getId());
		copia.setNombre(getNombre());
	
	}
}
