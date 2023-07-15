package app.client.dominio;

import java.util.Date;

public abstract class Activo implements com.google.gwt.user.client.rpc.IsSerializable, Comparable {
	
	private int id;
	private int tipo;
	
	private Estacion empresa;
	
	private EstadoActivo estado;
	
	private Date inicioGarantia;
	private Date finGarantia;
	
	private QR qr;
	
	private int anioFabricacion;
	
	private boolean tieneReparaciones;
	private boolean tienePendientes;
	private String display;
	
	public Estacion getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(Estacion empresa) {
		this.empresa = empresa;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	
	
	public int getTipo() {
		return tipo;
	}
	
	protected void setTipo(int tipo) {
		this.tipo = tipo;
	}
	

	public Date getInicioGarantia() {
		return inicioGarantia;
	}

	public void setInicioGarantia(Date inicioGarantia) {
		this.inicioGarantia = inicioGarantia;
	}

	public Date getFinGarantia() {
		return finGarantia;
	}

	public void setFinGarantia(Date finGarantia) {
		this.finGarantia = finGarantia;
	}

	public boolean isTieneReparaciones() {
		return tieneReparaciones;
	}

	public void setTieneReparaciones(boolean tieneReparaciones) {
		this.tieneReparaciones = tieneReparaciones;
	}

	public boolean isTienePendientes() {
		return tienePendientes;
	}

	public void setTienePendientes(boolean tienePendientes) {
		this.tienePendientes = tienePendientes;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public Activo(int tipo){
		this.tipo = tipo;
	}
	
	public QR getQr() {
		return qr;
	}

	public void setQr(QR qr) {
		this.qr = qr;
	}

	public EstadoActivo getEstado() {
		return estado;
	}

	public void setEstado(EstadoActivo estado) {
		this.estado = estado;
	}
	
	public int getAnioFabricacion() {
		return anioFabricacion;
	}

	public void setAnioFabricacion(int anioFabricacion) {
		this.anioFabricacion = anioFabricacion;
	}

	public static Activo getActivo(int tipo){
		if (tipo == 1){
			return new Surtidor();
		} else if (tipo == 2){
			return new Tanque();
		} else if (tipo == 3 ){
			return new Canio();
		} else if (tipo == 4 ){
			return new BombaSumergible();
		} else if (tipo == 6 ){
			return new ActivoGenerico();
		}
		return null;
	}
	
	protected String getAnioStr() {
		if (getAnioFabricacion() > 0){
			return " | " + getAnioFabricacion();
		}
		return "";
	}
	
	public Activo(){
		
	}

	public int compareTo(Object o) {
		Activo activo = (Activo) o;
		if(activo.getId() == this.getId()){
			return 0; // son iguales
		}
		return 1;
	}

	public abstract boolean equals(Activo a);
	
	public abstract Activo copiar();
	
	public abstract Activo copiarTodo();
	
	public abstract String toStringLargo();
}
