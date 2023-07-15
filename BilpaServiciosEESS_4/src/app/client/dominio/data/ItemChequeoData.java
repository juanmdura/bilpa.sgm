package app.client.dominio.data;

import app.client.dominio.MarcaActivoGenerico;


public class ItemChequeoData implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private String nombre;
	private String texto;
	private String tipo;
	private TipoActivoGenericoData tipoActivoGenerico;
	
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
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public TipoActivoGenericoData getTipoActivoGenerico() {
		return tipoActivoGenerico;
	}
	public void setTipoActivoGenerico(TipoActivoGenericoData tipoActivoGenerico) {
		this.tipoActivoGenerico = tipoActivoGenerico;
	}
	public ItemChequeoData copiar() {
		ItemChequeoData copia = new ItemChequeoData();
		copia.setId(id);
		copia.setNombre(nombre);
		copia.setTexto(texto);
		copia.setTipo(tipo);
		copia.setTipoActivoGenerico(tipoActivoGenerico);
		return copia;
	}
}
