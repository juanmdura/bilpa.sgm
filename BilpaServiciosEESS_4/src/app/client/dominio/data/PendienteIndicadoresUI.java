package app.client.dominio.data;

import java.util.ArrayList;
import java.util.List;

public class PendienteIndicadoresUI implements com.google.gwt.user.client.rpc.IsSerializable {

	private List<PendienteData> pendientes = new ArrayList<PendienteData>();
	private List<String> estaciones = new ArrayList<String>();
	private List<String> activos = new ArrayList<String>();
	
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

	public PendienteIndicadoresUI() {
		super();
	}
}
