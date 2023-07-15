package app.client.dominio;


public class ModeloActivoGenerico implements com.google.gwt.user.client.rpc.IsSerializable{
	private int id;
	private TipoActivoGenerico tipo;
	private MarcaActivoGenerico marca;
	private String nombre;
	private boolean activo;
	
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

	public MarcaActivoGenerico getMarca() {
		return marca;
	}

	public void setMarca(MarcaActivoGenerico marca) {
		this.marca = marca;
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

	public ModeloActivoGenerico(){
		
	}
	public ModeloActivoGenerico (int id){
		setId(id);
	}
	
	public ModeloActivoGenerico copiar() {
		ModeloActivoGenerico modelo = new ModeloActivoGenerico();
		modelo.setId(getId());
		modelo.setActivo(isActivo());
		modelo.setMarca(getMarca().copiar());
		modelo.setNombre(getNombre());
		modelo.setTipo(getTipo().copiar());
		return modelo;
	}
}
