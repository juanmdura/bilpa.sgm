package app.client.dominio.data;

public class PicoData implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	private int numeroPico;
	private int numeroEnLaEstacion; 
	private String tipoCombusitble; 
	private int idSurtidor;
	private String codigoQR;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCodigoQR() {
		return codigoQR;
	}
	public void setCodigoQR(String codigoQR) {
		this.codigoQR = codigoQR;
	}
	public int getNumeroPico() {
		return numeroPico;
	}
	public void setNumeroPico(int numeroPico) {
		this.numeroPico = numeroPico;
	}
	public int getNumeroEnLaEstacion() {
		return numeroEnLaEstacion;
	}
	public void setNumeroEnLaEstacion(int numeroEnLaEstacion) {
		this.numeroEnLaEstacion = numeroEnLaEstacion;
	}
	public String getTipoCombusitble() {
		return tipoCombusitble;
	}
	public void setTipoCombusitble(String tipoCombusitble) {
		this.tipoCombusitble = tipoCombusitble;
	}
	public int getIdSurtidor() {
		return idSurtidor;
	}
	public void setIdSurtidor(int idSurtidor) {
		this.idSurtidor = idSurtidor;
	}
	
	
	public PicoData() {
		super();
	}

}
