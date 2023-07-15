package app.client.dominio;

public class Foto implements com.google.gwt.user.client.rpc.IsSerializable {
	
	private int id;
	
	private String path;
	private String url;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public Foto(){
		super();
	}

	public Foto(int id) {
		setId(id);
	}
}
