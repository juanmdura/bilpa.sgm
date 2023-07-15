package app.client.dominio;


@SuppressWarnings("serial")
public class TipoRepuesto implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<Object>{

	int id;
	String descripcion;
	private boolean inactivo;
	
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
	public boolean isInactivo() {
		return inactivo;
	}

	public void setInactivo(boolean inactivo) {
		this.inactivo = inactivo;
	}
	public TipoRepuesto() {}
	
	public int compareTo(Object o) 
	{
		TipoRepuesto t = (TipoRepuesto)o;
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
	
	public TipoRepuesto copiar() {
		TipoRepuesto copia = new TipoRepuesto();
		copia.setId(getId());
		copia.setDescripcion(getDescripcion());
		copia.setInactivo(isInactivo());
		return copia;
	}
	
}
