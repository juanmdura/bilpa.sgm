package app.client.dominio.data;

import java.util.ArrayList;
import java.util.Date;

import app.client.dominio.Reparacion;

public class DatoConsultaHistoricoOrdenes implements com.google.gwt.user.client.rpc.IsSerializable{
	
	private Date fecha;
	private String nro;
	private ArrayList<Reparacion> reparaciones = new ArrayList<Reparacion>();
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNro() {
		return nro;
	}
	public void setNro(String nro) {
		this.nro = nro;
	}
		
	public ArrayList<Reparacion> getReparaciones() {
		return reparaciones;
	}
	public void setReparaciones(ArrayList<Reparacion> reparaciones) {
		this.reparaciones = reparaciones;
	}
	public DatoConsultaHistoricoOrdenes(){
		
	}

	public boolean tieneSoluciones(){
		for(Reparacion r : this.getReparaciones()){
			if(r.tieneSoluciones()){
				return true;
			}
		}
		return false;
	}
}
