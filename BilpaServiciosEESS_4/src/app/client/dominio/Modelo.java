package app.client.dominio;


public class Modelo  implements com.google.gwt.user.client.rpc.IsSerializable{
		
	private String nombre;
	private int id;
	private Marca marca;
	private int tipo;
	
	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Modelo(){
		
	}
	
	public Modelo(int tipo){
		this.setTipo(tipo);
	}
	
	public Modelo(String nombre, Marca marca){
		this.setNombre(nombre);
		this.setMarca(marca);
	}
	
	public Modelo copiar() {
		Modelo copia = new Modelo();
		copiarPropiedades(copia);
		return copia;
	}
	
	public Modelo copiarTodo() {
		Modelo copia = new Modelo();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}

	private void copiarPropiedades(Modelo copia) {
		copia.setNombre(getNombre());
		copia.setMarca(getMarca().copiar());
		copia.setId(getId());
		copia.setTipo(getTipo());
	}
	
	private void copiarColecciones(Modelo copia) {
		
	}
	
	public String toString(){
		return this.getMarca() + " - " + this.getNombre();
	}
}
