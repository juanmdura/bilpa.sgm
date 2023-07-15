package app.client.dominio;

public enum EstadoVisita {
	SIN_VISITAS("SIN VISITAS"),
	SIN_ASIGNAR("SIN ASIGNAR"), 
	PENDIENTE("PENDIENTE"),
	INICIADA("INICIADA"),
	REALIZADA("REALIZADA"); 

	private String estado;


	private EstadoVisita(String estado){
		this.estado = estado;
	}

	public String getEstado(){
		return estado;
	}

	public static EstadoVisita getEstadoVisita(String estado) {
		//String estado = estadoParam.replace(' ', '_');
		for (EstadoVisita i : EstadoVisita.values()) {
			if (i.getEstado().equals(estado)) {
				return i;
			}
		}
		return null;
	}


	public static String getEstadoVisitaStr(String estado) {
		for (EstadoVisita estadoVisita : EstadoVisita.values()) {
			if (estadoVisita.name().equals(estado)) {
				return estadoVisita.getEstado();
			}
		}
		return null;
	}
}
