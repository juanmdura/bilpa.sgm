package app.client.dominio;


public class Canio extends Activo {
	
	private String descripcion;
	
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Canio() {
		super(3);
	}
	
	public String toString(){
		return this.getDescripcion();
	}
	
	public Canio copiar() {
		Canio copia = new Canio();
		copiarPropiedades(copia);
		return copia;
	}

	public Canio copiarTodo() {
		Canio copia = new Canio();
		copiarPropiedades(copia);
		return copia;
	}
	private void copiarPropiedades(Canio copia) {
		copia.setEmpresa(getEmpresa().copiar());
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setDescripcion(getDescripcion());
		copia.setQr(getQr().copiar());
		copia.setDisplay(toStringLargo());
	}
	
	@Override
	public String toStringLargo() {
		return "Canio " + getDescripcion();
	}	
	
	@Override
	public boolean equals(Activo a) {
		if (a.getTipo() == 3)
		{
			Canio canio = (Canio) a ;
			return (canio.getDescripcion().equals(this.getDescripcion()));			
		}
		else
		{
			return false;
		}
	}
}
