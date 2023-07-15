package app.client.dominio;

public class Tanque extends Activo {

	private String descripcion;
	private Producto producto;
	private int capacidad;	

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public Tanque() {
		super(2);		
	}

	public Tanque(Integer cap, String desc) {
		this.capacidad = cap;
		this.descripcion = desc;
	}

	public String toString(){
		return this.descripcion;
	}

	public Tanque copiar() {
		Tanque copia = new Tanque();
		copiarPropiedades(copia);
		return copia;
	}

	public Tanque copiarTodo() {
		Tanque copia = new Tanque();
		copiarPropiedades(copia);
		return copia;
	}
	private void copiarPropiedades(Tanque copia) {
		copia.setCapacidad(getCapacidad());
		copia.setProducto(getProducto().copiar());
		copia.setDescripcion(getDescripcion());
		copia.setEmpresa(getEmpresa().copiar());
		copia.setId(getId());
		copia.setTipo(getTipo());
		copia.setInicioGarantia(getInicioGarantia());
		copia.setFinGarantia(getFinGarantia());
		copia.setEstado(getEstado());
		copia.setAnioFabricacion(getAnioFabricacion());
		copia.setDisplay(getDescripcion() + (getCapacidad() > 0 ? ( " | Capacidad " + getCapacidad() + " lts") : ""));
		if (getQr() != null)copia.setQr(getQr().copiar());
	}

	@Override
	public String toStringLargo() {
		return "Tanque " + getDescripcion();
	}	
	
	@Override
	public boolean equals(Activo a) {
		if (a.getTipo() == 2)
		{
			Tanque tanque = (Tanque) a;
			return (tanque.getDescripcion().equals(this.getDescripcion()));		
		}
		else
		{
			return false;
		}		
	}
}
