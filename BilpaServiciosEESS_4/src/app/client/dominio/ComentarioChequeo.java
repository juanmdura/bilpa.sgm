package app.client.dominio;

import java.util.Date;

import app.client.dominio.json.ComentarioJson;

public class ComentarioChequeo implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private Persona usuario;
	private Date fecha;
	private String texto;
	private ItemChequeado itemChequeado;
	private boolean imprimible;
	private boolean activo;
	
	public ItemChequeado getItemChequeado() {
		return itemChequeado;
	}
	public void setItemChequeado(ItemChequeado itemChequeado) {
		this.itemChequeado = itemChequeado;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public ComentarioChequeo() {}
	
	public ComentarioChequeo(Date fecha, boolean imprimible, String texto, Persona usuario) 
	{
		this.fecha = fecha;
		this.imprimible = imprimible;
		this.texto = texto;
		this.usuario = usuario;
	}
	
	@Override
	public String toString() {
		return texto;
	}
	
	public ComentarioChequeo copiarMinimo() {
		ComentarioChequeo copia = new ComentarioChequeo();
		copia.setId(getId());
		copia.setFecha(getFecha());
		copia.setImprimible(isImprimible());
		copia.setTexto(getTexto());
		copia.setActivo(isActivo());
		copia.setUsuario(new Tecnico(getUsuario().getId(), getUsuario().toString()));
		// copia.setItemChequeado(new ItemChequeado(getItemChequeado().getId()));
		return copia;
	}
	
	public ComentarioChequeo copiar() {
		ComentarioChequeo copia = new ComentarioChequeo();
		copia.setId(getId());
		copia.setFecha(getFecha());
		copia.setImprimible(isImprimible());
		copia.setTexto(getTexto());
		if(getUsuario() != null){
			copia.setUsuario((Persona)getUsuario().copiar());
		}
		if(getItemChequeado() != null){
			copia.setItemChequeado(new ItemChequeado(getItemChequeado().getId()));
		}
		return copia;
	}
	
	public void merge(ComentarioJson comentario) {
		setTexto(comentario.getTexto());
		setImprimible(comentario.isVisible());
		setFecha(new Date());
	}
}
