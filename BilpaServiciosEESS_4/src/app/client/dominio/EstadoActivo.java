package app.client.dominio;


public enum EstadoActivo {

	GARANTIA("Garantia"),
	ABONO("Abono"),
	SIN_ABONO("Sin abono");
	
	private String estado;
	
	private EstadoActivo(String estado) {
		this.estado = estado;
	}
	
	public String getEstado(){
		return estado;
	}
	
	public static EstadoActivo getEstadoActivo(String estado) {
		for (EstadoActivo i : EstadoActivo.values()) {
			if (i.getEstado().equals(estado	)) {
				return i;
			}
		}
		return null;
	}
	
}