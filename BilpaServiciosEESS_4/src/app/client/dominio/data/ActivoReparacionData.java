package app.client.dominio.data;

import app.client.dominio.Activo;
import app.client.dominio.Reparacion;

public class ActivoReparacionData {

	private Activo activo;
	private Reparacion reparacion;
	private boolean reparado;
	
	public Activo getActivo() {
		return activo;
	}
	public void setActivo(Activo activo) {
		this.activo = activo;
	}
	public Reparacion getReparacion() {
		return reparacion;
	}
	public void setReparacion(Reparacion reparacion) {
		this.reparacion = reparacion;
	}
	public boolean isReparado() {
		return reparado;
	}
	public void setReparado(boolean reparado) {
		this.reparado = reparado;
	}
}
