package app.client.dominio;

import java.util.Date;

public class HistoricoOrden implements com.google.gwt.user.client.rpc.IsSerializable{

	private int id;
	private Orden orden;
	private Date fecha;
	private Persona tecnicoAsignado;
	private int estadoOrden;
	private Persona sesion;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Orden getOrden() {
		return orden;
	}

	public void setOrden(Orden orden) {
		this.orden = orden;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Persona getTecnicoAsignado() {
		return tecnicoAsignado;
	}

	public void setTecnicoAsignado(Persona tecnicoAsignado) {
		this.tecnicoAsignado = tecnicoAsignado;
	}

	public int getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(int estado) {
		this.estadoOrden = estado;
	}
	
	public Persona getSesion() {
		return sesion;
	}

	public void setSesion(Persona sesion) {
		this.sesion = sesion;
	}

	public HistoricoOrden() {}

	public HistoricoOrden(Orden orden, Date fecha, Persona sesion) {
		this.orden = orden;
		this.fecha = fecha;
		this.estadoOrden = orden.getEstadoOrden();
		this.tecnicoAsignado = orden.getTecnicoAsignado();
		this.sesion = sesion;
		
	}
}
