package app.client.dominio.data;

import java.util.List;


public class EstacionData implements com.google.gwt.user.client.rpc.IsSerializable{

	private String numeroSerie;
	private String sello;
	private String zona;
	private String tiempoRespuesta;
	private int id;
	private String nombre;	
	private String localidad;
	private String departamento;
	private String direccion;
	private String telefono;
	private String rut;
	private String foto;
	private List<String> listaEmail;

	public EstacionData(){
		
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getSello() {
		return sello;
	}

	public void setSello(String sello) {
		this.sello = sello;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getTiempoRespuesta() {
		return tiempoRespuesta;
	}

	public void setTiempoRespuesta(String tiempoRespuesta) {
		this.tiempoRespuesta = tiempoRespuesta;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public List<String> getListaEmail() {
		return listaEmail;
	}

	public void setListaEmail(List<String> listaEmail) {
		this.listaEmail = listaEmail;
	}

}
