package app.client.dominio;

public enum EstadoPendiente {
	INICIADO("INICIADO"),
	DESCARTADO("DESCARTADO"),
	REPARADO("REPARADO"),
	CORRECTIVO_ASIGNADO("CORRECTIVO_ASIGNADO");

	private String estado;


	private EstadoPendiente(String estado){
		this.estado = estado;
	}

	public String getEstado(){
		return estado;
	}

	public static EstadoPendiente getEstadoVisita(String estado) {
		for (EstadoPendiente estadoPendiente : EstadoPendiente.values()) {
			if (estadoPendiente.getEstado().equals(estado)) {
				return estadoPendiente;
			}
		}
		return null;
	}


	public static String getEstadoPendienteStr(String estado) {
		for (EstadoPendiente estadoPendiente : EstadoPendiente.values()) {
			if (estadoPendiente.name().equals(estado)) {
				return estadoPendiente.getEstado();
			}
		}
		return null;
	}
}
