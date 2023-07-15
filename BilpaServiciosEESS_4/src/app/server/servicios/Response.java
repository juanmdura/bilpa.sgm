package app.server.servicios;

public class Response {
	
	private String status;
	private String error;
	
	private Object datos;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getDatos() {
		return datos;
	}

	public void setDatos(Object datos) {
		this.datos = datos;
	}
	
	

}
