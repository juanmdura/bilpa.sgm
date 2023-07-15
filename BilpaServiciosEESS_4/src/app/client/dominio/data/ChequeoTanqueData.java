package app.client.dominio.data;

public class ChequeoTanqueData extends ChequeoData {

	private String colorMarcoTapa;
	private String tapaLomoTanque;
	private String aguaTanque;
	private String ventilacion;
	private int tipoDeDescarga;
	private int medidaDelAgua;
	
	public ChequeoTanqueData(){
		
	}
	
	public String getColorMarcoTapa() {
		return colorMarcoTapa;
	}
	
	public void setColorMarcoTapa(String colorMarcoTapa) {
		this.colorMarcoTapa = colorMarcoTapa;
	}
	
	public String getTapaLomoTanque() {
		return tapaLomoTanque;
	}
	
	public void setTapaLomoTanque(String tapaLomoTanque) {
		this.tapaLomoTanque = tapaLomoTanque;
	}
	
	public String getAguaTanque() {
		return aguaTanque;
	}
	
	public void setAguaTanque(String aguaTanque) {
		this.aguaTanque = aguaTanque;
	}
	
	public String getVentilacion() {
		return ventilacion;
	}
	
	public void setVentilacion(String ventilacion) {
		this.ventilacion = ventilacion;
	}
	
	public int getTipoDeDescarga() {
		return tipoDeDescarga;
	}
	
	public void setTipoDeDescarga(int tipoDeDescarga) {
		this.tipoDeDescarga = tipoDeDescarga;
	}

	public int getMedidaDelAgua() {
		return medidaDelAgua;
	}

	public void setMedidaDelAgua(int medidaDelAgua) {
		this.medidaDelAgua = medidaDelAgua;
	}
}
