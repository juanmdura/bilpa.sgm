package app.client.dominio;

public enum TipoChequeo {

	Bomba("BOMBA"),
	Surtidor("SURTIDOR"),
	Tanque("TANQUE"),
	Producto("PRODUCTO"),
	Pico("PICO"),
	Generico("GENERICO");
	
	private String tipoChequeo;
	
	private TipoChequeo(String tipo) {
		this.tipoChequeo = tipo;
	}
	
	public String getTipoChequeo(){
		return tipoChequeo;
	}
	
	public static TipoChequeo getTipoChequeo(String tipo) {
		for (TipoChequeo t : TipoChequeo.values()) {
			if (t.getTipoChequeo().equals(tipo)) {
				return t;
			}
		}
		return null;
	}
}
