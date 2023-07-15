package app.client.dominio.data;

import java.util.HashSet;
import java.util.Set;

import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;

public class DatoOrdenesActivasEmpresa implements com.google.gwt.user.client.rpc.IsSerializable{
	
	private String fecha;
	private String numero;
	private String autor;
	private String tecnico;
	private String estado;
	private int idEmpresa;
	private Set<Reparacion> reparaciones = new HashSet<Reparacion>();
	private Set<RepuestoLinea> repuestos = new HashSet<RepuestoLinea>();
	
	public Set<RepuestoLinea> getRepuestos() {
		return repuestos;
	}

	public void setRepuestos(Set<RepuestoLinea> repuestos) {
		this.repuestos = repuestos;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}

	public String getEstado() {
		return estado;
	}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Set<Reparacion> getReparaciones() {
		return reparaciones;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setEstado(int estado) {
		if(estado == 1){
			this.estado = "Iniciada";
		}
		if(estado == 2){
			this.estado = "Inspeccion Pendiente";
		}
		if(estado == 3){
			this.estado = "Finalizada";
		}
		if(estado == 4){
			this.estado = "Anulada";
		}
	}

	public DatoOrdenesActivasEmpresa(){}
	
	public DatoOrdenesActivasEmpresa(String fecha, String numero,String autor, String tecnico, String estado){
		this.fecha = fecha;
		this.numero = numero;
		this.autor = autor;
		this.tecnico = tecnico;
		this.estado = estado;
	}

	public void setReparaciones(Set<Reparacion> reparaciones) 
	{
		this.reparaciones = reparaciones;
	}
	
}

