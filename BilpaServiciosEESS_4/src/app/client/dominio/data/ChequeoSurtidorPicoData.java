package app.client.dominio.data;

public class ChequeoSurtidorPicoData {
	private int idPreventivo;
	private ChequeoPicoData chequeo;

	public int getIdPreventivo() {
		return idPreventivo;
	}

	public void setIdPreventivo(int idPreventivo) {
		this.idPreventivo = idPreventivo;
	}

	public ChequeoPicoData getChequeo() {
		return chequeo;
	}

	public void setChequeo(ChequeoPicoData chequeo) {
		this.chequeo = chequeo;
	}
	
}
