package app.client.dominio.data;

import java.util.ArrayList;
import java.util.List;

public class SeccionReporteData implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private String nombre;
	private String titulo;
	
	private List<DetalleReporteData> detalles = new ArrayList<DetalleReporteData>();

	
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
	public List<DetalleReporteData> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetalleReporteData> detalles) {
		this.detalles = detalles;
	}
	
	
	public SeccionReporteData() {
		super();
	}
	public SeccionReporteData(String nombre, String titulo, List<DetalleReporteData> detallesHeader) {
		setTitulo(titulo);
		setNombre(nombre);
		setDetalles(detallesHeader);
	}
}
