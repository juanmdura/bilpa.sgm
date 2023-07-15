package app.client.dominio;

import java.util.HashSet;
import java.util.Set;

public class Marca  implements com.google.gwt.user.client.rpc.IsSerializable{
	
	private Set<Modelo> modelos = new HashSet<Modelo>();
	private String nombre;
	private int id;

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

	public Set<Modelo> getModelos() {
		return modelos;
	}

	public void setModelos(Set<Modelo> modelos) {
		this.modelos = modelos;
	}

	public Marca(){
		
	}
	
	public Marca(int id){
		this.setId(id);;
	}
	
	public Marca(String nombre){
		this.setNombre(nombre);
	}
	
	public String toString(){
		return this.getNombre();
	}
	
	public Marca copiar() {
		Marca copia = new Marca();
		copiarPropiedades(copia);
		return copia;
	}
	
	public Marca copiarTodo() {
		Marca copia = new Marca();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}

	private void copiarPropiedades(Marca copia) {
		copia.setNombre(getNombre());
		copia.setId(getId());
	}
	
	private void copiarColecciones(Marca copia) {
		for (Modelo m : this.modelos) {
			copia.getModelos().add(m.copiar());
		}
	}
}
