package app.client.dominio;


public class RepuestoLinea implements com.google.gwt.user.client.rpc.IsSerializable, Comparable {

	private Orden orden;
	private Repuesto repuesto;
	private Activo activo;
	
	private int cantidad;
	private int id;
	private boolean nuevo;
	private int row;
	
	private Solucion solucion;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public boolean isNuevo() {
		return nuevo;
	}

	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public Orden getOrden() {
		return orden;
	}
	
	public void setOrden(Orden orden) {
		this.orden = orden;
	}
	
	public Repuesto getRepuesto() {
		return repuesto;
	}
	
	public void setRepuesto(Repuesto repuesto) {
		this.repuesto = repuesto;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public Activo getActivo() {
		return activo;
	}

	public void setActivo(Activo activo) {
		this.activo = activo;
	}
	
	public Solucion getSolucion() {
		return solucion;
	}

	public void setSolucion(Solucion solucion) {
		this.solucion = solucion;
	}

	public RepuestoLinea(){
		
	}
	
	
	
	@Override
	public boolean equals(Object obj) 
	{	
		RepuestoLinea rl = (RepuestoLinea)obj;
		return rl.getOrden().getNumero() == getOrden().getNumero() && 
		rl.getActivo().getId() == getActivo().getId() &&
		rl.getRepuesto().getId() == getRepuesto().getId();
	}

	public RepuestoLinea(Orden orden, Repuesto repuesto, int cantidad) {
		this.setOrden(orden);
		this.setRepuesto(repuesto);
		this.setCantidad(cantidad);
	}
	
	public RepuestoLinea(int id) {
		setId(id);
	}

	public String toString(){
		return this.getRepuesto() + " " + this.getCantidad();
	}
	
	public RepuestoLinea copiar() {
		RepuestoLinea copia = new RepuestoLinea();
		copiarPropiedades(copia);
		return copia;
	}
	
	public RepuestoLinea copiarTodo() {
		RepuestoLinea copia = new RepuestoLinea();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}

	private void copiarPropiedades(RepuestoLinea copia) {
		copia.setCantidad(getCantidad());
		copia.setId(getId());
		copia.setOrden(getOrden().copiar());
		copia.setRepuesto(getRepuesto().copiar());
		copia.setActivo(getActivo().copiar());
		copia.setNuevo(isNuevo());
		if(getSolucion() != null){
			copia.setSolucion(getSolucion().copiar());
		}
	}
	
	private void copiarColecciones(RepuestoLinea copia) {
		
	}

	public int compareTo(Object r) {
		RepuestoLinea rl = (RepuestoLinea)r;
		if (rl.getId() > this.getId())
		{
			return 1;
		}
		else if (rl.getId() < this.getId())
		{
			return -1;
		}
		else
		{
			return 0;
		}		
	}
}
