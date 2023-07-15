package app.client.dominio.data;

public class OrganizacionData implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private String organizacion;
	
	public String getOrganizacion() {
		return organizacion;
	}
	public void setOrganizacion(String organizacion) {
		this.organizacion = organizacion;
	}
	public OrganizacionData() {
		super();
	}
}
