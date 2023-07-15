package app.client.dominio;

import app.client.dominio.data.TipoDescargaData;

public class TipoDescarga{

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
	
	public TipoDescarga() {
		super();
	}

	public TipoDescargaData getTipoDescargaData() {
		TipoDescargaData tdd = new TipoDescargaData();
		tdd.setId(this.getId());
		tdd.setNombre(this.getNombre());
		return tdd;
	}

}
