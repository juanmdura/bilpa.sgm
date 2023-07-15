package app.client.dominio.data;

public class ChequeoProductoData extends ChequeoData {
	
	private int idPreventivo;
	private ProductoData productoData;
	private String motorUnidadBombeo;
	private String eliminadorAireGas;
	private String correasPoleas;
	
	public ProductoData getProductoData() {
		return productoData;
	}

	public void setProductoData(ProductoData productoData) {
		this.productoData = productoData;
	}

	public String getMotorUnidadBombeo() {
		return motorUnidadBombeo;
	}
	
	public void setMotorUnidadBombeo(String motorUnidadBombeo) {
		this.motorUnidadBombeo = motorUnidadBombeo;
	}
	
	public String getEliminadorAireGas() {
		return eliminadorAireGas;
	}
	
	public void setEliminadorAireGas(String eliminadorAireGas) {
		this.eliminadorAireGas = eliminadorAireGas;
	}
	
	public String getCorreasPoleas() {
		return correasPoleas;
	}
	
	public void setCorreasPoleas(String correasPoleas) {
		this.correasPoleas = correasPoleas;
	}
	
	public int getIdPreventivo() {
		return idPreventivo;
	}

	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}

	public ChequeoProductoData() {
		super();
	}

	
}
