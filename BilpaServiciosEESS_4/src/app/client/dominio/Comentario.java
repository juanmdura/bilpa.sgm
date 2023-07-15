package app.client.dominio;

import java.util.Date;

import app.client.dominio.json.ComentarioJson;

public class Comentario implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private boolean imprimible;
	private Persona usuario;
	private String nombreUsuario;
	private Date fecha;
	private String texto;
	private Orden orden;//queda solo para lectura, los comentarios ahora son de una reparacion
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Orden getOrden() {
		return orden;
	}
	public void setOrden(Orden orden) {
		this.orden = orden;
	}
	public boolean isImprimible() {
		return imprimible;
	}
	public void setImprimible(boolean imprimible) {
		this.imprimible = imprimible;
	}
	public Persona getUsuario() {
		return usuario;
	}
	public void setUsuario(Persona usuario) {
		this.usuario = usuario;
	}
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
	
	public Comentario() {}
	
	public Comentario(Date fecha, boolean imprimible, String texto, Persona usuario) 
	{
		this.fecha = fecha;
		this.imprimible = imprimible;
		this.texto = texto;
		this.usuario = usuario;
	}
	
	public Comentario(Date fecha, boolean imprimible, String texto, String usuario) 
	{
		this.fecha = fecha;
		this.imprimible = imprimible;
		this.texto = texto;
		this.nombreUsuario = usuario;
	}
	
	@Override
	public String toString() {
		return texto;
	}
	
	public Comentario copiarMinimo() {
		Comentario copia = new Comentario();
		copia.setId(getId());
		copia.setFecha(getFecha());
		copia.setImprimible(isImprimible());
		copia.setTexto(getTexto());
		return copia;
	}
	
	public Comentario copiar() {
		Comentario copia = new Comentario();
		copia.setId(getId());
		copia.setFecha(getFecha());
		copia.setImprimible(isImprimible());
		copia.setTexto(getTexto());
		if(getUsuario() != null){
			copia.setUsuario((Persona)getUsuario().copiar());
		}
		if(getOrden() != null){
			copia.setOrden(new Orden(getOrden().getNumero()));
		}
		return copia;
	}
	
	public void merge(ComentarioJson comentario) {
		setTexto(comentario.getTexto());
		setImprimible(comentario.isVisible());
		setFecha(new Date());
	}
}
