package app.client.dominio.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CorrectivoJson {
	private int numero;
	private Date inicio;
	private Date fin;
	private Date inicioReal;
	private Date finReal;
	private int estado;
	private int tipoTrabajo;
	private String firma;
	private String comentarioFirma;
	List<String> mails = new ArrayList<String>();
	
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getTipoTrabajo() {
		return tipoTrabajo;
	}
	public void setTipoTrabajo(int tipoTrabajo) {
		this.tipoTrabajo = tipoTrabajo;
	}
	public Date getInicioReal() {
		return inicioReal;
	}
	public void setInicioReal(Date inicioReal) {
		this.inicioReal = inicioReal;
	}
	public Date getFinReal() {
		return finReal;
	}
	public void setFinReal(Date finReal) {
		this.finReal = finReal;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
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
	public List<String> getMails() {
		return mails;
	}
	public void setMails(List<String> mails) {
		this.mails = mails;
	}
}
