package app.client.dominio.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import app.client.dominio.ContactoEmpresa;
import app.client.dominio.Estacion;
import app.client.dominio.Persona;
import app.client.dominio.Reparacion;
import app.client.dominio.RepuestoLinea;

public class DatoConsultaOrdenesPorSelloYFechas  implements com.google.gwt.user.client.rpc.IsSerializable{

	private int numero;
	private Date fechaInicio;
	private Date fechaFin;	
	private Persona tecnicoAsignado;
	private Estacion empresa;
	private Set<RepuestoLinea> repuestosLineas = new HashSet<RepuestoLinea>();
	private Set<Reparacion> reparaciones = new HashSet<Reparacion>();
	private Persona creador;
	private ContactoEmpresa contacto;	
	private int numeroParteDucsa;
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Persona getTecnicoAsignado() {
		return tecnicoAsignado;
	}
	public void setTecnicoAsignado(Persona tecnicoAsignado) {
		this.tecnicoAsignado = tecnicoAsignado;
	}
	public Estacion getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Estacion empresa) {
		this.empresa = empresa;
	}
	public Set<RepuestoLinea> getRepuestosLineas() {
		return repuestosLineas;
	}
	public void setRepuestosLineas(Set<RepuestoLinea> repuestosLineas) {
		this.repuestosLineas = repuestosLineas;
	}
	public Set<Reparacion> getReparaciones() {
		return reparaciones;
	}
	public void setReparaciones(Set<Reparacion> reparaciones) {
		this.reparaciones = reparaciones;
	}
	public Persona getCreador() {
		return creador;
	}
	public void setCreador(Persona creador) {
		this.creador = creador;
	}
	public ContactoEmpresa getContacto() {
		return contacto;
	}
	public void setContacto(ContactoEmpresa contacto) {
		this.contacto = contacto;
	}
	public int getNumeroParteDucsa() {
		return numeroParteDucsa;
	}
	public void setNumeroParteDucsa(int numeroParteDucsa) {
		this.numeroParteDucsa = numeroParteDucsa;
	}
	
	public DatoConsultaOrdenesPorSelloYFechas() {}
	
	
}
