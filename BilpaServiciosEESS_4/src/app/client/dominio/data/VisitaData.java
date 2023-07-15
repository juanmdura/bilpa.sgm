package app.client.dominio.data;

import java.util.Date;
import java.util.List;

public class VisitaData implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	
	private Date fechaInicio;
	private Date fechaFin;
	
	private String fechaProximaVisita;
	private String nombreEstacion;
	private int idEstacion;
	private String url;
	private String comentarioFirma;
	
	private String estado;
	
	private List<ActivoData> listaActivos;

	public VisitaData() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdEstacion() {
		return idEstacion;
	}

	public void setIdEstacion(int idEstacion) {
		this.idEstacion = idEstacion;
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

	public String getFechaProximaVisita() {
		return fechaProximaVisita;
	}

	public void setFechaProximaVisita(String fechaProximaVisita) {
		this.fechaProximaVisita = fechaProximaVisita;
	}

	public String getNombreEstacion() {
		return nombreEstacion;
	}

	public void setNombreEstacion(String nombreEstacion) {
		this.nombreEstacion = nombreEstacion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<ActivoData> getListaActivos() {
		return listaActivos;
	}

	public void setListaActivos(List<ActivoData> listaActivos) {
		this.listaActivos = listaActivos;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getComentarioFirma() {
		return comentarioFirma;
	}

	public void setComentarioFirma(String comentarioFirma) {
		this.comentarioFirma = comentarioFirma;
	}
}
