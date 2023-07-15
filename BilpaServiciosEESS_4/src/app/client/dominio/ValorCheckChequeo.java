package app.client.dominio;

public enum ValorCheckChequeo implements com.google.gwt.user.client.rpc.IsSerializable {
	
	R("R"),
	B("B"),
	N_A("N_A");
	
	private final String estado;
	
	private ValorCheckChequeo(String value) {
		this.estado = value;
	}
	
	public String estado() {
		return estado;
	}


}
