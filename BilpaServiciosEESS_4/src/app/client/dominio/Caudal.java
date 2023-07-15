package app.client.dominio;

import app.client.dominio.data.CaudalData;
import app.client.dominio.json.CaudalJson;

public class Caudal implements com.google.gwt.user.client.rpc.IsSerializable{

	private int id;
	
	private int tiempo;
	
	private Double litrosPorMinuto;
	private Double volumen;
	
	private Pico pico;
	
	public Caudal() {
	
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Pico getPico() {
		return pico;
	}

	public void setPico(Pico pico) {
		this.pico = pico;
	}

	public int getTiempo() {
		return tiempo;
	}
	
	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}
	public Double getLitrosPorMinuto(){
		return litrosPorMinuto;		
	}
	/*public Double getLitrosPorMinuto() {
		if (volumen != null && volumen > 0 && tiempo > 0){
			
			double valor = Double.valueOf(volumen * 60) / tiempo;
			String pattern = "#.##";
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			String valorFormatted = decimalFormat.format(valor);
			return Double.valueOf(valorFormatted);
			
		}
		return null;
	}*/
	
	public void setLitrosPorMinuto(Double litrosPorMinuto) {
		this.litrosPorMinuto = litrosPorMinuto;
	}
	
	public Double getVolumen() {
		return volumen;
	}
	
	public void setVolumen(Double volumen) {
		this.volumen = volumen;
	}

	public void setFromCaudalJson(CaudalJson caudal, int idPico) {
		
		this.setId(caudal.getId());
		this.setLitrosPorMinuto(caudal.getLitrosPorMinuto());
		this.setTiempo(caudal.getTiempo());
		this.setVolumen(caudal.getVolumen());
		this.setPico(new Pico(idPico));
	}

	public CaudalData getCaudalData() {
		
		CaudalData cd = new CaudalData();
		cd.setId(this.getId());
		// cd.setLitrosPorMinuto(this.getLitrosPorMinuto());
		cd.setTiempo(this.getTiempo());
		cd.setVolumen(this.getVolumen());
		return cd;
		
	}
	
}
