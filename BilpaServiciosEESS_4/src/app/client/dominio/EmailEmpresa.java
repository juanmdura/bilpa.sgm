package app.client.dominio;

public class EmailEmpresa implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	private String email;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public EmailEmpresa() {
		super();
	}
	
	
	public EmailEmpresa copiar() {
		EmailEmpresa ee = new EmailEmpresa();
		ee.setId(this.getId());
		ee.setEmail(this.getEmail());
		return ee;
	}
	
}
