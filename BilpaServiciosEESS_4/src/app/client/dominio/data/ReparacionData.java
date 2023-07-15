package app.client.dominio.data;

import java.util.List;

import app.client.dominio.Reparacion;

public class ReparacionData {

	private Reparacion reparacion;
	private List<SolucionData> soluciones;
	
	public Reparacion getReparacion() {
		return reparacion;
	}
	public void setReparacion(Reparacion reparacion) {
		this.reparacion = reparacion;
	}
	public List<SolucionData> getSoluciones() {
		return soluciones;
	}
	public void setSoluciones(List<SolucionData> soluciones) {
		this.soluciones = soluciones;
	}
	
}
