package app.client.dominio.data;

import java.util.Date;

public class OrdenData implements com.google.gwt.user.client.rpc.IsSerializable{

	private int numero;
	private int idEstado;
	private String estado;
	private String tecnico;
	private String prioridad;
	private String inicioServiceReal;
	private String finServiceReal;
	private String inicioServiceUsuario;
	private String finServiceUsuario;
	private String fechaInicio;
	private String fechaCumplimiento2;
	private String fechaFin2;
	private Date fechaCumplimiento;
	private Date fechaFin;
	private String estacion;
	private int idEstacion;
	private String numeroBoca;
	private String localidad;
	private String sello;
	private String plazo;
	private String fotoEstacion;
	private String fotoEstacionChica;
	private String numeroDucsaStr;
	private int numeroDucsa;
	
	private boolean activa;
	
	public OrdenData(){

	}

	public String getInicioServiceReal() {
		return inicioServiceReal;
	}

	public void setInicioServiceReal(String inicioServiceReal) {
		this.inicioServiceReal = inicioServiceReal;
	}

	public String getFinServiceReal() {
		return finServiceReal;
	}

	public void setFinServiceReal(String finServiceReal) {
		this.finServiceReal = finServiceReal;
	}

	public String getInicioServiceUsuario() {
		return inicioServiceUsuario;
	}

	public void setInicioServiceUsuario(String inicioServiceUsuario) {
		this.inicioServiceUsuario = inicioServiceUsuario;
	}

	public String getFinServiceUsuario() {
		return finServiceUsuario;
	}

	public void setFinServiceUsuario(String finServiceUsuario) {
		this.finServiceUsuario = finServiceUsuario;
	}

	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public int getNumeroDucsa() {
		return numeroDucsa;
	}

	public void setNumeroDucsa(int numeroDucsa) {
		this.numeroDucsa = numeroDucsa;
	}

	public String getNumeroDucsaStr() {
		return numeroDucsaStr;
	}

	public void setNumeroDucsaStr(String numeroDucsaStr) {
		this.numeroDucsaStr = numeroDucsaStr;
	}

	public int getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}

	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTecnico() {
		return tecnico;
	}
	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}
	public String getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}
	
	public String getFotoEstacionChica() {
		return fotoEstacionChica;
	}
	public void setFotoEstacionChica(String fotoEstacionChica) {
		this.fotoEstacionChica = fotoEstacionChica;
	}

	public String getFotoEstacion() {
		return fotoEstacion;
	}

	public void setFotoEstacion(String fotoEstacion) {
		this.fotoEstacion = fotoEstacion;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaCumplimiento() {
		return fechaCumplimiento;
	}

	public void setFechaCumplimiento(Date fechaCumplimiento) {
		this.fechaCumplimiento = fechaCumplimiento;
	}

	public String getFechaCumplimiento2() {
		return fechaCumplimiento2;
	}

	public void setFechaCumplimiento2(String fechaCumplimiento2) {
		this.fechaCumplimiento2 = fechaCumplimiento2;
	}

	public String getFechaFin2() {
		return fechaFin2;
	}

	public void setFechaFin2(String fechaFin2) {
		this.fechaFin2 = fechaFin2;
	}

	public String getEstacion() {
		return estacion;
	}
	public void setEstacion(String estacion) {
		this.estacion = estacion;
	}
	public int getIdEstacion() {
		return idEstacion;
	}
	public void setIdEstacion(int idEstacion) {
		this.idEstacion = idEstacion;
	}
	public String getNumeroBoca() {
		return numeroBoca;
	}
	public void setNumeroBoca(String numeroBoca) {
		this.numeroBoca = numeroBoca;
	}
	public String getPlazo() {
		return plazo;
	}

	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}

	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getSello() {
		return sello;
	}
	public void setSello(String sello) {
		this.sello = sello;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}
}
