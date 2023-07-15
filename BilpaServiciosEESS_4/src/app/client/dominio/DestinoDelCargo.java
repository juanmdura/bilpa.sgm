package app.client.dominio;

import app.client.dominio.data.DestinoDelCargoData;


public class DestinoDelCargo implements com.google.gwt.user.client.rpc.IsSerializable {
	
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
	
	
	public DestinoDelCargo() {
	}

	public DestinoDelCargo(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	public DestinoDelCargo(DestinoDelCargoData dcd) {
		this.id = dcd.getId();
		this.nombre = dcd.getNombre();
	}
	

	public DestinoDelCargo(int idDestinoDelCargo) {
		setId(idDestinoDelCargo);
	}

	public DestinoDelCargo copiar() {
		DestinoDelCargo copia = new DestinoDelCargo();
		copiarPropiedades(copia);
		return copia;
	}

	public DestinoDelCargo copiarTodo() {
		DestinoDelCargo copia = new DestinoDelCargo();
		copiarPropiedades(copia);
		return copia;
	}
	private void copiarPropiedades(DestinoDelCargo copia) {
		copia.setId(getId());
		copia.setNombre(getNombre());
	}
	
}
