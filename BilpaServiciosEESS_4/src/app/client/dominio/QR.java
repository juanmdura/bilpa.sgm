package app.client.dominio;

public class QR implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	
	private String codigo;

	public QR(){
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public QR copiar() {
		QR qr = new QR();
		qr.setId(this.getId());
		qr.setCodigo(this.getCodigo());
		return qr;
	}

}
