package app.client.dominio.data;

import java.util.Date;

import app.client.dominio.EstadoPendiente;

public class PendienteDataList implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private String comentario;
	private boolean comentarioVisible = false;
	private Date plazo;
	private String urlFoto;
	private int idItemChequeado;
	private String textoItemChequado;
	private EstadoPendiente estado;
	private boolean plazoVencido;
	private int ordenCreado;
	private Date fechaReparado;

	public Date getFechaReparado() {
		return fechaReparado;
	}

	public void setFechaReparado(Date fechaReparado) {
		this.fechaReparado = fechaReparado;
	}
	
	private String destinatario;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isPlazoVencido() {
		return plazoVencido;
	}
	public void setPlazoVencido(boolean plazoVencido) {
		this.plazoVencido = plazoVencido;
	}
	public int getOrdenCreado() {
		return ordenCreado;
	}
	public void setOrdenCreado(int ordenCreado) {
		this.ordenCreado = ordenCreado;
	}
	public EstadoPendiente getEstado() {
		return estado;
	}
	public void setEstado(EstadoPendiente estado) {
		this.estado = estado;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public int getIdItemChequeado() {
		return idItemChequeado;
	}
	public void setIdItemChequeado(int idItemChequeado) {
		this.idItemChequeado = idItemChequeado;
	}
	
	public String getTextoItemChequado() {
		return textoItemChequado;
	}

	public void setTextoItemChequado(String textoItemChequado) {
		this.textoItemChequado = textoItemChequado;
	}

	public String getUrlFoto() {
		return urlFoto;
	}
	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public boolean isComentarioVisible() {
		return comentarioVisible;
	}
	public void setComentarioVisible(boolean comentarioVisible) {
		this.comentarioVisible = comentarioVisible;
	}
	public Date getPlazo() {
		return plazo;
	}
	public void setPlazo(Date plazo) {
		this.plazo = plazo;
	}


	public PendienteDataList() {
		super();
	}

}
