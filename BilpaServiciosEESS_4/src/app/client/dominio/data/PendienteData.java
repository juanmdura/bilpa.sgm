package app.client.dominio.data;

import java.util.Date;

import app.client.dominio.EstadoPendiente;


public class PendienteData implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	private int idPreventivo;
	private int ordenCreado;
	private int ordenReparado;
	private int idItemChequeado;
	private int idActivo;
	private int idEstacion;
	private int idCreador;
	private int idDestinoDelCargo;
	private int numeroCorrectivoAsignado;
	private EstadoPendiente estado;
	private String urlFoto;
	private String pathFoto;
	private String comentario;
	private boolean comentarioVisible = false;
	private Date plazo;
	private String destinatario;
	private String activo;
	private String empresa;
	private String motivoDescarte;
	private String creador;
	private Date fechaCreado;
	private int tipoActivo;
	
	private Date fechaReparado;

	public Date getFechaReparado() {
		return fechaReparado;
	}

	public void setFechaReparado(Date fechaReparado) {
		this.fechaReparado = fechaReparado;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTipoActivo() {
		return tipoActivo;
	}

	public void setTipoActivo(int tipoActivo) {
		this.tipoActivo = tipoActivo;
	}

	public int getIdEstacion() {
		return idEstacion;
	}
	public void setIdEstacion(int idEstacion) {
		this.idEstacion = idEstacion;
	}
	public int getIdActivo() {
		return idActivo;
	}
	public void setIdActivo(int idActivo) {
		this.idActivo = idActivo;
	}
	public EstadoPendiente getEstado() {
		return estado;
	}
	public void setEstado(EstadoPendiente estado) {
		this.estado = estado;
	}
	public int getOrdenReparado() {
		return ordenReparado;
	}
	public void setOrdenReparado(int ordenReparado) {
		this.ordenReparado = ordenReparado;
	}
	public int getOrdenCreado() {
		return ordenCreado;
	}
	public void setOrdenCreado(int ordenCreado) {
		this.ordenCreado = ordenCreado;
	}
	public int getIdDestinoDelCargo() {
		return idDestinoDelCargo;
	}
	public void setIdDestinoDelCargo(int idDestinoDelCargo) {
		this.idDestinoDelCargo = idDestinoDelCargo;
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
	public int getIdPreventivo() {
		return idPreventivo;
	}
	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}
	public String getPathFoto() {
		return pathFoto;
	}
	public void setPathFoto(String pathFoto) {
		this.pathFoto = pathFoto;
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

	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getCreador() {
		return creador;
	}
	public void setCreador(String creador) {
		this.creador = creador;
	}
	public Date getFechaCreado() {
		return fechaCreado;
	}
	public void setFechaCreado(Date fechaCreado) {
		this.fechaCreado = fechaCreado;
	}
	public int getIdCreador() {
		return idCreador;
	}

	public void setIdCreador(int idCreador) {
		this.idCreador = idCreador;
	}

	public PendienteData() {
		super();
	}
	public boolean esPendientePreventivo(){
		return getIdPreventivo() > 0;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getMotivoDescarte() {
		return motivoDescarte;
	}

	public void setMotivoDescarte(String motivoDescarte) {
		this.motivoDescarte = motivoDescarte;
	}

	public int getNumeroCorrectivoAsignado() {
		return numeroCorrectivoAsignado;
	}

	public void setNumeroCorrectivoAsignado(int numeroCorrectivoAsignado) {
		this.numeroCorrectivoAsignado = numeroCorrectivoAsignado;
	}
}

