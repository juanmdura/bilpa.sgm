package app.client.dominio.json;

import java.util.List;

import app.client.dominio.Contador;
import app.client.dominio.data.CalibreData;

public class SolucionJson {
	private int id;
	private int idReparacion;
	private int numero;
	private int idActivo;
	private int idTecnico;
	
	private int idPendiente;
	
	private int idTarea;
	private int idFalla;
	private int idDestinoDelCargo;

	private List<RepuestoLineaCorregidoJson> repuestosLineaCorregidos;
	private ComentarioJson comentario;
	private int idPico;
	private String fotoBytes;
	private String foto2Bytes;
	
	private CalibreData calibre;
	private PrecintoJson precinto;
	private Contador contador;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdPendiente() {
		return idPendiente;
	}
	public void setIdPendiente(int idPendiente) {
		this.idPendiente = idPendiente;
	}
	public int getIdTecnico() {
		return idTecnico;
	}
	public void setIdTecnico(int idTecnico) {
		this.idTecnico = idTecnico;
	}
	public int getIdReparacion() {
		return idReparacion;
	}
	public void setIdReparacion(int idReparacion) {
		this.idReparacion = idReparacion;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getIdActivo() {
		return idActivo;
	}
	public void setIdActivo(int idActivo) {
		this.idActivo = idActivo;
	}
	public int getIdTarea() {
		return idTarea;
	}
	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}
	public int getIdFalla() {
		return idFalla;
	}
	public void setIdFalla(int idFalla) {
		this.idFalla = idFalla;
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
	public CalibreData getCalibre() {
		return calibre;
	}
	public void setCalibre(CalibreData calibre) {
		this.calibre = calibre;
	}
	public PrecintoJson getPrecinto() {
		return precinto;
	}
	public void setPrecinto(PrecintoJson precinto) {
		this.precinto = precinto;
	}
	public Contador getContador() {
		return contador;
	}
	public void setContador(Contador contador) {
		this.contador = contador;
	}
}
