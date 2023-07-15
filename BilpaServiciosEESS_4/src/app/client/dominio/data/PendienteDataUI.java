package app.client.dominio.data;

import java.util.ArrayList;
import java.util.List;

public class PendienteDataUI implements com.google.gwt.user.client.rpc.IsSerializable {

	private int cantidadIniciados;
	private int cantidadReparados;
	private int cantidadDescartados;
	private int cantidadAsignados;
	
	private List<PendienteData> pendientes = new ArrayList<PendienteData>();
	private List<String> estaciones = new ArrayList<String>();
	private List<String> activos = new ArrayList<String>();
	
	public int getCantidadIniciados() {
		return cantidadIniciados;
	}

	public void setCantidadIniciados(int cantidadIniciados) {
		this.cantidadIniciados = cantidadIniciados;
	}

	public int getCantidadReparados() {
		return cantidadReparados;
	}

	public void setCantidadReparados(int cantidadReparados) {
		this.cantidadReparados = cantidadReparados;
	}

	public int getCantidadDescartados() {
		return cantidadDescartados;
	}

	public void setCantidadDescartados(int cantidadDescartados) {
		this.cantidadDescartados = cantidadDescartados;
	}

	public int getCantidadAsignados() {
		return cantidadAsignados;
	}

	public void setCantidadAsignados(int cantidadAsignados) {
		this.cantidadAsignados = cantidadAsignados;
	}

	public List<PendienteData> getPendientes() {
		return pendientes;
	}

	public void setPendientes(List<PendienteData> pendientes) {
		this.pendientes = pendientes;
	}

	public List<String> getEstaciones() {
		return estaciones;
	}

	public void setEstaciones(List<String> estaciones) {
		this.estaciones = estaciones;
	}
	
	public List<String> getActivos() {
		return activos;
	}

	public void setActivos(List<String> activos) {
		this.activos = activos;
	}

	public PendienteDataUI() {
		super();
	}
}
