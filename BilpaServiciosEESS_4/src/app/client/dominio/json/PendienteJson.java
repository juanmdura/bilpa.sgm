package app.client.dominio.json;

import java.util.Date;


public class PendienteJson {
	
	private String id;
	private String idPreventivo;
	private int idActivo;
	private String comentarioVisible;
	private String comentario;
	private String motivoDescarte;
	private String fotoBytes;
	private Date plazo;
	private int idItemChequeado;
	private String destinatario;
	private int numero;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getIdActivo() {
		return idActivo;
	}
	public void setIdActivo(int idActivo) {
		this.idActivo = idActivo;
	}
	public String getIdPreventivo() {
		return idPreventivo;
	}
	public void setIdPreventivo(String idPreventivo) {
		this.idPreventivo = idPreventivo;
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
	public String getComentarioVisible() {
		return comentarioVisible;
	}
	public void setComentarioVisible(String comentarioVisible) {
		this.comentarioVisible = comentarioVisible;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getFotoBytes() {
		return fotoBytes;
	}
	public void setFotoBytes(String fotoBytes) {
		this.fotoBytes = fotoBytes;
	}
	public Date getPlazo() {
		return plazo;
	}
	public void setPlazo(Date plazo) {
		this.plazo = plazo;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getMotivoDescarte() {
		return motivoDescarte;
	}
	public void setMotivoDescarte(String motivoDescarte) {
		this.motivoDescarte = motivoDescarte;
	}
}
