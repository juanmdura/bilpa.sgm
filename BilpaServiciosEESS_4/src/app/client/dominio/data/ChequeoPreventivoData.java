package app.client.dominio.data;

public class ChequeoPreventivoData {
	private int idPreventivo;
	private ChequeoData chequeo;
	
	public int getIdPreventivo() {
		return idPreventivo;
	}
	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}

	public ChequeoData getChequeo() {
		return chequeo;
	}

	public void setChequeo(ChequeoData chequeo) {
		this.chequeo = chequeo;
	}

}
