package app.client.dominio;


public class MarcaActivoGenerico implements com.google.gwt.user.client.rpc.IsSerializable{
	private int id;
	private TipoActivoGenerico tipo;
	private String nombre;
	private boolean activo;
	
	public MarcaActivoGenerico(int id2) {
		setId(id2);
	}

	public MarcaActivoGenerico() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoActivoGenerico getTipo() {
		return tipo;
	}

	public void setTipo(TipoActivoGenerico tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return nombre;
	}

	public MarcaActivoGenerico copiar() {
		MarcaActivoGenerico copia = new MarcaActivoGenerico();
		copia.setActivo(isActivo());
		copia.setId(getId());
		copia.setNombre(getNombre());
		copia.setTipo(getTipo().copiar());
		return copia;
	}
}
