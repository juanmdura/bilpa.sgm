package app.client.dominio.json;

import java.util.List;

/**
 * 
 * @author dfleitas
 *
 */
public class CorregidoJson {
	
	private int idCorregido;
	private int idPreventivo;
	private int idFalla;
	private int idTarea;
	private int idDestinoDelCargo;
	private List<RepuestoLineaCorregidoJson> repuestosLineaCorregidos;
	private ComentarioJson comentario;
	private int idPico;
	private String fotoBytes;
	private String foto2Bytes;
	private int idItemChequeado;
	private int idPendiente;
	
	public int getIdCorregido() {
		return idCorregido;
	}
	public void setIdCorregido(int idCorregido) {
		this.idCorregido = idCorregido;
	}
	public int getIdPreventivo() {
		return idPreventivo;
	}
	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}
	public int getIdItemChequeado() {
		return idItemChequeado;
	}
	public void setIdItemChequeado(int idItemChequeado) {
		this.idItemChequeado = idItemChequeado;
	}
	public int getIdFalla() {
		return idFalla;
	}
	public void setIdFalla(int idFalla) {
		this.idFalla = idFalla;
	}
	public int getIdTarea() {
		return idTarea;
	}
	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}
	public int getIdDestinoDelCargo() {
		return idDestinoDelCargo;
	}
	public void setIdDestinoDelCargo(int idDestinoDelCargo) {
		this.idDestinoDelCargo = idDestinoDelCargo;
	}
	public List<RepuestoLineaCorregidoJson> getRepuestosLineaCorregidos() {
		return repuestosLineaCorregidos;
	}
	public void setRepuestosLineaCorregidos(
			List<RepuestoLineaCorregidoJson> repuestosLineaCorregidos) {
		this.repuestosLineaCorregidos = repuestosLineaCorregidos;
	}
	public ComentarioJson getComentario() {
		return comentario;
	}
	public void setComentario(ComentarioJson comentario) {
		this.comentario = comentario;
	}
	public int getIdPico() {
		return idPico;
	}
	public void setIdPico(int idPico) {
		this.idPico = idPico;
	}
	public String getFotoBytes() {
		return fotoBytes;
	}
	public void setFotoBytes(String fotoBytes) {
		this.fotoBytes = fotoBytes;
	}
	public String getFoto2Bytes() {
		return foto2Bytes;
	}
	public void setFoto2Bytes(String foto2Bytes) {
		this.foto2Bytes = foto2Bytes;
	}
	public int getIdPendiente() {
		return idPendiente;
	}
	public void setIdPendiente(int idPendiente) {
		this.idPendiente = idPendiente;
	}
	public CorregidoJson() {
		super();
	}

}
