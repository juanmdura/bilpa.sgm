package app.client.dominio.data;

import java.util.ArrayList;
import java.util.List;

public class ReporteData implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private String nombre;
	private String titulo;
	private String subTitulo;

	private List<SeccionReporteData> secciones = new ArrayList<SeccionReporteData>();

	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getSubTitulo() {
		return subTitulo;
	}
	public void setSubTitulo(String subTitulo) {
		this.subTitulo = subTitulo;
	}
	public List<SeccionReporteData> getSecciones() {
		return secciones;
	}
	public void setSecciones(List<SeccionReporteData> secciones) {
		this.secciones = secciones;
	}
	
	
	public ReporteData() {
		super();
	}

	
}
