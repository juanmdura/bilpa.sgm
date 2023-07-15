package app.client.dominio.data;


public class ChequeoBombaData extends ChequeoData {

	private String sifon;
	private String fugasSump;
	private String sumpHermetico;
	private String detectorMecanicoFuga;
	
	public ChequeoBombaData(){
		
	}
	
	public String getSifon() {
		return sifon;
	}
	
	public void setSifon(String sifon) {
		this.sifon = sifon;
	}
	
	public String getFugasSump() {
		return fugasSump;
	}
	
	public void setFugasSump(String fugasSump) {
		this.fugasSump = fugasSump;
	}
	
	public String getSumpHermetico() {
		return sumpHermetico;
	}
	
	public void setSumpHermetico(String sumpHermetico) {
		this.sumpHermetico = sumpHermetico;
	}
	
	public String getDetectorMecanicoFuga() {
		return detectorMecanicoFuga;
	}
	
	public void setDetectorMecanicoFuga(String detectorMecanico) {
		this.detectorMecanicoFuga = detectorMecanico;
	}

}
