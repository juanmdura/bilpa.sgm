package app.client.dominio;

import app.client.dominio.data.TipoActivoGenericoData;


public class TipoActivoGenerico implements com.google.gwt.user.client.rpc.IsSerializable{

	private int id;
	private String nombre;
	private boolean activo;
	
	public TipoActivoGenerico(int id2) {
		setId(id2);
	}
	public TipoActivoGenerico() {
	}

	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
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
	
	public TipoActivoGenerico copiar(){
		TipoActivoGenerico tag = new TipoActivoGenerico();
		tag.setActivo(isActivo());
		tag.setId(getId());
		tag.setNombre(getNombre());
		return tag;
	}
	
	public TipoActivoGenericoData getData(){
		TipoActivoGenericoData tag = new TipoActivoGenericoData();
		tag.setActivo(isActivo());
		tag.setId(getId());
		tag.setNombre(getNombre());
		return tag;
	}
}
