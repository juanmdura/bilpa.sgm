package app.client.dominio.data;

public class CaudalData {
	
	private int id;
	private int tiempo;
	
	private Double litrosPorMinuto;
	private Double volumen;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTiempo() {
		return tiempo;
	}
	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}
	public Double getLitrosPorMinuto() {
		return litrosPorMinuto;
	}
	public void setLitrosPorMinuto(Double litrosPorMinuto) {
		this.litrosPorMinuto = litrosPorMinuto;
	}
	public Double getVolumen() {
		return volumen;
	}
	public void setVolumen(Double volumen) {
		this.volumen = volumen;
	}
	
	
	public CaudalData() {
		super();
	}
	
}
