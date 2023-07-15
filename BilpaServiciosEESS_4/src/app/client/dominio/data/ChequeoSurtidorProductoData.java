package app.client.dominio.data;

public class ChequeoSurtidorProductoData {
	private int idPreventivo;
	private ChequeoProductoData chequeo;

	public int getIdPreventivo() {
		return idPreventivo;
	}

	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}

	public ChequeoProductoData getChequeo() {
		return chequeo;
	}

	public void setChequeo(ChequeoProductoData chequeo) {
		this.chequeo = chequeo;
	}
	

}
