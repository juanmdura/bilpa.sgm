package app.client.dominio;

import app.client.dominio.data.CalibreData;

public class Calibre  implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private Pico pico;
	
	private Double calibre1;
	private Double calibre2;
	private Double calibre3;
	
	private Double calibre4;
	private Double calibre5;
	private Double calibre6;
	
	
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

	public Double getCalibre1() {
		return calibre1;
	}

	public void setCalibre1(Double calibre1) {
		this.calibre1 = calibre1;
	}

	public Double getCalibre2() {
		return calibre2;
	}

	public void setCalibre2(Double calibre2) {
		this.calibre2 = calibre2;
	}

	public Double getCalibre3() {
		return calibre3;
	}

	public void setCalibre3(Double calibre3) {
		this.calibre3 = calibre3;
	}

	public Double getCalibre4() {
		return calibre4;
	}

	public void setCalibre4(Double calibre4) {
		this.calibre4 = calibre4;
	}

	public Double getCalibre5() {
		return calibre5;
	}

	public void setCalibre5(Double calibre5) {
		this.calibre5 = calibre5;
	}

	public Double getCalibre6() {
		return calibre6;
	}

	public void setCalibre6(Double calibre6) {
		this.calibre6 = calibre6;
	}

	public Calibre copiar() {
		Calibre copia = new Calibre();
		copia.setId(getId());
		
		copia.setCalibre1(getCalibre1());
		copia.setCalibre2(getCalibre2());
		copia.setCalibre3(getCalibre3());
		
		copia.setCalibre4(getCalibre4());
		copia.setCalibre5(getCalibre5());
		copia.setCalibre6(getCalibre6());
		
		copia.setPico(getPico().copiarMinimo());
		return copia;
	}

	public void merge(CalibreData calibre, Pico pico) {
		setId(calibre.getId());
		setCalibre1(calibre.getCalibre1());
		setCalibre2(calibre.getCalibre2());
		setCalibre3(calibre.getCalibre3());
		
		setCalibre4(calibre.getCalibre4());
		setCalibre5(calibre.getCalibre5());
		setCalibre6(calibre.getCalibre6());
		
		setPico(pico);
	}
}
