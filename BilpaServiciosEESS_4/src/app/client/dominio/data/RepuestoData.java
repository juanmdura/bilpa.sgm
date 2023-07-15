package app.client.dominio.data;

import app.client.dominio.Repuesto;
import app.client.dominio.TipoRepuesto;

public class RepuestoData implements com.google.gwt.user.client.rpc.IsSerializable{
	private int id;
	private String descripcion;
	private String nroSerie;
	private boolean inactivo;
	private double precio;
	private TipoRepuesto tipoRepuesto;
	private int cantidad;
	private boolean esNuevo;
	
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
	
	public boolean isInactivo() {
		return inactivo;
	}

	public void setInactivo(boolean inactivo) {
		this.inactivo = inactivo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public boolean isEsNuevo() {
		return esNuevo;
	}

	public void setEsNuevo(boolean esNuevo) {
		this.esNuevo = esNuevo;
	}

	public RepuestoData() {
		
	}
	
	public String toString(){
		return  getDescripcion() + " - " + getNroSerie();
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

	public Repuesto copiarRepuesto() {
		Repuesto copia = new Repuesto();
		copia.setNroSerie(getNroSerie());
		copia.setDescripcion(getDescripcion());
		copia.setId(getId());
		copia.setInactiva(isInactivo());
		copia.setPrecio(getPrecio());
		copia.setTipoRepuesto(getTipoRepuesto().copiar());
		return copia;
	}

}
