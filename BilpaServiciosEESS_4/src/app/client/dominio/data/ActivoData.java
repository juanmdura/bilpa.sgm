package app.client.dominio.data;

import java.util.Date;

import app.client.utilidades.UtilOrden;


public class ActivoData implements com.google.gwt.user.client.rpc.IsSerializable, Comparable<ActivoData> {
	
	private int id;
	private boolean fueModificado;
	private Date ultimaModificacion;
	private String tipoId;
	private String tipo;
	private String descripcion;
	private int codigoQR;
	private EstadoPreventivoData estadoPreventivo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTipo() {
		return tipoId;
	}
	public void setTipo(String tipo) {
		this.tipoId = tipo;
	}
	public String getTipoStr() {
		return tipo;
	}
	public void setTipoStr(String tipoStr) {
		this.tipo = tipoStr;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) 
	{
		this.descripcion = descripcion;
	}
	
	public Date getUltimaModificacion() {
		return ultimaModificacion;
	}
	
	public void setUltimaModificacion(Date ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}
	
	public boolean fueModificado() {
		return getUltimaModificacion() != null;
	}

	public void setFueModificado() {
		this.fueModificado = fueModificado();
	}

	public int getCodigoQR() {
		return codigoQR;
	}
	public void setCodigoQR(int codigoQR) {
		this.codigoQR = codigoQR;
	}
	public EstadoPreventivoData getEstadoPreventivo() {
		return estadoPreventivo;
	}
	public void setEstadoPreventivo(EstadoPreventivoData estadoPreventivo) {
		this.estadoPreventivo = estadoPreventivo;
	}
	
	
	public ActivoData(int id, String tipo, String descripcion, String nombreGenerico) {
		super();
		this.id = id;
		this.tipoId = tipo;
		this.descripcion = descripcion;
		this.tipo = UtilOrden.tiposDeActivos(Integer.valueOf(tipo));
	
		if (nombreGenerico != null){
			this.tipo = nombreGenerico;
		}
	}
	
	public ActivoData() {}
	
	public int compareTo(ActivoData activoData) {
		int order = 0;
		if (activoData.getTipo().compareTo(getTipo()) > 0){
			order--;
		} else if (activoData.getTipo().compareTo(getTipo()) < 0){
			order++;
		}
		return order;
	}
}
