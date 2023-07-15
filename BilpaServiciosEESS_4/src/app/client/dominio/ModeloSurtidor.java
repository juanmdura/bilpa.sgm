package app.client.dominio;

public class ModeloSurtidor extends Modelo{
	
	private int cantidadDePicos;
	private int cantidadDeProductos;

	public int getCantidadDeProductos() {
		return cantidadDeProductos;
	}

	public void setCantidadDeProductos(int cantidadDeProductos) {
		this.cantidadDeProductos = cantidadDeProductos;
	}

	public int getCantidadDePicos() {
		return cantidadDePicos;
	}
	
	public ModeloSurtidor(){
		super(1);
	}

	public ModeloSurtidor(int idModelo) {
		setId(idModelo);
	}

	public void setCantidadDePicos(int cantidadDePicos) {
		this.cantidadDePicos = cantidadDePicos;
	}
	
	public ModeloSurtidor copiar() {
		ModeloSurtidor copia = new ModeloSurtidor();
		copiarPropiedades(copia);
		return copia;
	}

	public ModeloSurtidor copiarTodo() {
		ModeloSurtidor copia = new ModeloSurtidor();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	
	private void copiarPropiedades(ModeloSurtidor copia) {
		copia.setCantidadDePicos(getCantidadDePicos());
		copia.setCantidadDeProductos(getCantidadDeProductos());
		copia.setNombre(getNombre());
		copia.setMarca(getMarca().copiar());
		copia.setId(getId());
		copia.setTipo(getTipo());
	}

	public void copiarColecciones(ModeloSurtidor copia) {
	
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(ModeloSurtidor.class))
		{
			ModeloSurtidor mod = (ModeloSurtidor)obj;
			return mod.getId()==getId();
		}
		return false;
	}
}
