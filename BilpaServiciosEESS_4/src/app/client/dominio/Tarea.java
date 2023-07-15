package app.client.dominio;



public class Tarea implements com.google.gwt.user.client.rpc.IsSerializable, Comparable {
	
	private int id;
	private String descripcion;
	private boolean inactiva;
	private TipoTarea tipoTarea;


	public TipoTarea getTipoTarea() {
		return tipoTarea;
	}

	public void setTipoTarea(TipoTarea tipoTarea) {
		this.tipoTarea = tipoTarea;
	}

	public boolean isInactiva() {
		return inactiva;
	}

	public void setInactiva(boolean inactiva) {
		this.inactiva = inactiva;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Tarea (){
		
	}

	public Tarea(int idTarea) {
		this.id = idTarea;
	}

	public String toString(){
		return this.descripcion;
	}
	
	public Tarea copiar() {
		Tarea copia = new Tarea();
		copiarPropiedades(copia);
		return copia;
	}

	public Tarea copiarTodo() {
		Tarea copia = new Tarea();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}
	private void copiarPropiedades(Tarea copia) {
		copia.setDescripcion(getDescripcion());
		copia.setId(getId());
		copia.setInactiva(isInactiva());
		copia.setTipoTarea(getTipoTarea().copiar());
	}

	public void copiarColecciones(Tarea copia) {
		
	}

	public boolean equals(Tarea t) {
		// TODO Auto-generated method stub
		return getDescripcion().equals(t.getDescripcion());
	}
	
	public int compareTo(Object o) {
		Tarea t = (Tarea)o;
		if (t.getDescripcion().compareToIgnoreCase(getDescripcion()) < 0)
		{
			return 1;
		}
		else if (t.getDescripcion().compareToIgnoreCase(getDescripcion()) > 0)
		{
			return -1;
		}
		else
		{
			return 0;
		}		
	}
}
