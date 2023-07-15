package app.client.dominio;

import app.client.dominio.data.RepuestoData;


public class Repuesto implements com.google.gwt.user.client.rpc.IsSerializable, Comparable {
	
	private int id;
	private String descripcion;
	private String nroSerie;
	private boolean inactivo;
	private double precio;
	private TipoRepuesto tipoRepuesto;
	
	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getNroSerie() {
		return nroSerie;
	}

	public void setNroSerie(String nroSerie) {
		this.nroSerie = nroSerie;
	}

	public boolean isInactiva() {
		return inactivo;
	}

	public void setInactiva(boolean inactiva) {
		this.inactivo = inactiva;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoRepuesto getTipoRepuesto() {
		return tipoRepuesto;
	}

	public void setTipoRepuesto(TipoRepuesto tipoRepuesto) {
		this.tipoRepuesto = tipoRepuesto;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Repuesto() {
		
	}
	
	public Repuesto(String descripcion) {
		this.descripcion = descripcion;
		
	}
	
	public Repuesto(int idRepuesto) {
		setId(idRepuesto);
	}

	public String toString(){
		return  getDescripcion() + " - " + getNroSerie();
	}

	public RepuestoData copiarRepuestoData() {
		RepuestoData rd = new RepuestoData();
		rd.setId(id);
		rd.setDescripcion(descripcion);
		rd.setInactivo(inactivo);
		rd.setNroSerie(nroSerie);
		rd.setPrecio(precio);
		rd.setTipoRepuesto(tipoRepuesto);
		return rd;
	}
	
	public Repuesto copiar() {
		Repuesto copia = new Repuesto();
		copiarPropiedades(copia);
		return copia;
	}
	
	public Repuesto copiarTodo() {
		Repuesto copia = new Repuesto();
		copiarPropiedades(copia);
		copiarColecciones(copia);
		return copia;
	}

	private void copiarPropiedades(Repuesto copia) {
		copia.setNroSerie(getNroSerie());
		copia.setDescripcion(getDescripcion());
		copia.setId(getId());
		copia.setInactiva(isInactiva());
		copia.setPrecio(getPrecio());
		copia.setTipoRepuesto(getTipoRepuesto().copiar());
	}
	
	private void copiarColecciones(Repuesto copia) {
		
	}
	
	public String datosRepuesto(){
		return getDescripcion() + " - " + "Precio $ " + getPrecio() + " - " + "N° Parte: " + getNroSerie() ;
	}

	public int compareTo(Object o) {
		Repuesto t = (Repuesto)o;
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
