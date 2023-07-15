package app.client.dominio;


@SuppressWarnings("serial")
public class TipoTarea implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<Object>{

	int id;
	String descripcion;
	private boolean inactiva;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String nombre) {
		this.descripcion = nombre;
	}
	public boolean isInactiva() {
		return inactiva;
	}

	public void setInactiva(boolean inactiva) {
		this.inactiva = inactiva;
	}
	public TipoTarea() {}
	
	public int compareTo(Object o) 
	{
		TipoTarea t = (TipoTarea)o;
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
	
	@Override
	public String toString() 
	{
		return getDescripcion();
	}
	
	public TipoTarea copiar() {
		TipoTarea copia = new TipoTarea();
		copia.setId(getId());
		copia.setDescripcion(getDescripcion());
		copia.setInactiva(isInactiva());
		return copia;
	}
	
}
