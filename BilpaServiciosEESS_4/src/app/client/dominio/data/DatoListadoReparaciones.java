package app.client.dominio.data;

import java.util.ArrayList;

import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;

@SuppressWarnings("serial")
public class DatoListadoReparaciones implements com.google.gwt.user.client.rpc.IsSerializable {
	private Reparacion reparacion = new Reparacion();
	private ArrayList<RepuestoLinea> repuestosLinea = new ArrayList<RepuestoLinea>();
	
	public Reparacion getReparacion() {
		return reparacion;
	}

	public void setReparacion(Reparacion reparacion) {
		this.reparacion = reparacion;
	}

	public ArrayList<RepuestoLinea> getRepuestosLinea() 
	{
		return repuestosLinea;
	}
	
	public void setRepuestosLinea(ArrayList<RepuestoLinea> repuestosLinea) 
	{
		this.repuestosLinea = repuestosLinea;
	}
	
	public DatoListadoReparaciones(){}
	
	public DatoListadoReparaciones(Reparacion reparacion, ArrayList<RepuestoLinea> repuestosLinea) 
	{
		this.reparacion = reparacion;
		this.repuestosLinea = repuestosLinea;
	}
}
