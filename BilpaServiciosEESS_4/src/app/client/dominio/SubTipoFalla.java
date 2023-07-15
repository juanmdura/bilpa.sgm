package app.client.dominio;


public abstract class SubTipoFalla implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<Object> {
	private int id;
	private int tipo;
	private String descripcion;	
	private boolean inactiva;
	
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

	public int getTipo() {
		return tipo;
	}
	
	protected void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public SubTipoFalla(){}
	
	public SubTipoFalla(int tipo){
		this.tipo = tipo;
	}

	public String toString(){
		return this.getDescripcion();
	}
	
	public int compareTo(Object o) {
		SubTipoFalla ft = (SubTipoFalla)o;
		if (ft.getDescripcion().compareToIgnoreCase(getDescripcion()) < 0)
		{
			return 1;
		}
		else if (ft.getDescripcion().compareToIgnoreCase(getDescripcion()) > 0)
		{
			return -1;
		}
		else
		{
			return 0;
		}		
	}

	public abstract SubTipoFalla copiar();

	public abstract SubTipoFalla copiarTodo();
	
}
