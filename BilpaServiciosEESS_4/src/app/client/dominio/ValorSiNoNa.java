package app.client.dominio;

public enum ValorSiNoNa {
	SI("SI"),
	NO("NO"),
	N_A("N_A");
	
	private final String estado;
	
	private ValorSiNoNa(String value) {
		this.estado = value;
	}
	
	public String estado() {
		return estado;
	}
}
