package app.client.dominio;


public abstract class FallaTipo implements com.google.gwt.user.client.rpc.IsSerializable, Comparable {

	private int id;
	private Integer tipo;
	private int subTipo;
	private String descripcion;	
	private Activo activo;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Activo getActivo() {
		return activo;
	}

	public void setActivo(Activo reparable) {
		this.activo = reparable;
	}

	public Integer getTipo() {
		return tipo;
	}
	
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	
	public Integer getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(int subTipo) {
		this.subTipo = subTipo;
	}

	public FallaTipo(Integer tipo){
		this.tipo = tipo;
	}
	
	public String toString(){
		return this.getDescripcion();
	}
	

	public int compareTo(Object o) {
		FallaTipo ft = (FallaTipo)o;
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

	public abstract FallaTipo copiar();

	public abstract FallaTipo copiarTodo();
	
}
