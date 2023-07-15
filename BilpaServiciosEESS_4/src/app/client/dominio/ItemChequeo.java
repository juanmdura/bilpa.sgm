package app.client.dominio;

import app.client.dominio.data.ItemChequeoData;

public class ItemChequeo implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	private String nombre;
	private String texto;
	private TipoChequeo tipoChequeo;
	private TipoActivoGenerico tipoActivoGenerico;
	private boolean activo;
	
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
	public TipoChequeo getTipoChequeo() {
		return tipoChequeo;
	}
	public void setTipoChequeo(TipoChequeo tipoChequeo) {
		this.tipoChequeo = tipoChequeo;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public TipoActivoGenerico getTipoActivoGenerico() {
		return tipoActivoGenerico;
	}
	public void setTipoActivoGenerico(TipoActivoGenerico tipoActivoGenerico) {
		this.tipoActivoGenerico = tipoActivoGenerico;
	}
	public ItemChequeo() {
		super();
	}
	public ItemChequeoData getItemChequeoData() {
		ItemChequeoData icd = new ItemChequeoData();
		icd.setId(getId());
		icd.setNombre(getNombre());
		icd.setTexto(getTexto());
		icd.setTipo(getTipoChequeo().getTipoChequeo());
		icd.setTipoActivoGenerico(getTipoActivoGenerico().getData());
		return icd;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((tipoChequeo == null) ? 0 : tipoChequeo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemChequeo other = (ItemChequeo) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (tipoChequeo != other.tipoChequeo)
			return false;
		return true;
	}
	
	public String getTextoTipoChequeo() {
		return tipoChequeo.toString().replace("Pico", "Manguera");
	}

}
