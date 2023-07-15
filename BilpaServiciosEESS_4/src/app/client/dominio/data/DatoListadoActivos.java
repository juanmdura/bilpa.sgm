package app.client.dominio.data;

import java.util.ArrayList;
import java.util.Date;

import app.client.dominio.Comentario;
import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;

public class DatoListadoActivos implements com.google.gwt.user.client.rpc.IsSerializable {
	private Date desde;
	private Date hasta;
	
	private ArrayList<Reparacion> reparaciones = new ArrayList<Reparacion> ();
	private ArrayList<RepuestoLinea> repuestosLinea = new ArrayList<RepuestoLinea>();
	private ArrayList<Comentario> comentarios = new ArrayList<Comentario>();

	
	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	public ArrayList<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(ArrayList<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public ArrayList<Reparacion> getReparaciones() {
		return reparaciones;
	}

	public void setReparaciones(ArrayList<Reparacion> reparaciones) {
		this.reparaciones = reparaciones;
	}

	public ArrayList<RepuestoLinea> getRepuestosLinea() 
	{
		return repuestosLinea;
	}
	
	public void setRepuestosLinea(ArrayList<RepuestoLinea> repuestosLinea) 
	{
		this.repuestosLinea = repuestosLinea;
	}
	
	public DatoListadoActivos(){}
	
	public DatoListadoActivos(ArrayList<Reparacion> reparaciones, ArrayList<RepuestoLinea> repuestosLinea, ArrayList<Comentario> comentarios) 
	{
		this.reparaciones = reparaciones;
		this.repuestosLinea = repuestosLinea;
		this.comentarios = comentarios;
	}

	public boolean tieneReparacionesDePicos() 
	{
		for (Reparacion r : getReparaciones())
		{
			if (r.tienePicosReparados())
			{
				return true;
			}
		}
		return false;
	}
}
