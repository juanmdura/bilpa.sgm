package app.client.dominio.json;

import java.util.Date;


public class VisitaJson {

	private String idVisita;
	private Date fechaInicio;
	private Date fechaFin;
	private String estado;
	private String firma;
	private String comentarioFirma;
	
	public String getIdVisita() {
		return idVisita;
	}
	public void setIdVisita(String idVisita) {
		this.idVisita = idVisita;
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
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	public String getComentarioFirma() {
		return comentarioFirma;
	}
	public void setComentarioFirma(String comentarioFirma) {
		this.comentarioFirma = comentarioFirma;
	}
}
