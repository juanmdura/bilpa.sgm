package app.client.dominio;


public class BombaSumergible extends Activo {

	private String descripcion;
		
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BombaSumergible() {
		super(4);
		
	}
	
	@Override
	public String toString(){
		return this.getDescripcion();
	}
	
	public BombaSumergible copiar() {
		BombaSumergible copia = new BombaSumergible();
		copiarPropiedades(copia);
		return copia;
	}

	public BombaSumergible copiarTodo() {
		BombaSumergible copia = new BombaSumergible();
		copiarPropiedades(copia);
		return copia;
	}
	private void copiarPropiedades(BombaSumergible copia) {
		copia.setEmpresa(getEmpresa().copiar());
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setDescripcion(getDescripcion());
		copia.setInicioGarantia(getInicioGarantia());
		copia.setFinGarantia(getFinGarantia());
		copia.setDisplay(toStringLargo());
		if (getQr() != null)copia.setQr(getQr().copiar());
	}
	
	@Override
	public boolean equals(Activo a) {
		if (a.getTipo() == 4)
		{
			BombaSumergible bomba = (BombaSumergible) a;
			return (bomba.getDescripcion().equals(this.getDescripcion()));		
		}
		else
		{
			return false;
		}
	}

	@Override
	public String toStringLargo() {
		return "Bomba " + getDescripcion();
	}	
}
