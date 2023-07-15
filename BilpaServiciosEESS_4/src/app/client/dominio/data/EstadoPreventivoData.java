package app.client.dominio.data;


public enum EstadoPreventivoData {

	SIN_INICIAR("Sin iniciar"),
	INICIADO("Iniciado"),
	COMPLETO("Completo");
	
	private String estado;
	
	private EstadoPreventivoData(String estado) {
		this.estado = estado;
	}
	
	public String getEstado(){
		return estado;
	}
	
	public static EstadoPreventivoData getEstadoActivo(String estado) {
		for (EstadoPreventivoData i : EstadoPreventivoData.values()) {
			if (i.getEstado().equals(estado	)) {
				return i;
			}
		}
		return null;
	}
	
}