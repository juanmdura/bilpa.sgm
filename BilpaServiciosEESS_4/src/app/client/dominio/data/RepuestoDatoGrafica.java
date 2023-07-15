package app.client.dominio.data;

import app.client.dominio.Repuesto;

public class RepuestoDatoGrafica implements com.google.gwt.user.client.rpc.IsSerializable, Comparable{

	private Repuesto repuesto;
	private int cantidad;
	private int porcentaje;
	
	public int getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
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

	public RepuestoDatoGrafica(){

	}
	
	
	public int compareTo(Object o) {
		RepuestoDatoGrafica rd = (RepuestoDatoGrafica) o;
		if(this.porcentaje < rd.porcentaje){
			return -1;
		}
		if(this.porcentaje > rd.porcentaje){
			return 1;
		}
		return 0;
	}
}
