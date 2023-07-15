package app.client.dominio;

public class Firma implements com.google.gwt.user.client.rpc.IsSerializable {

	private int id;
	
	private String nombre;
	private String apellido;
	private String path;
	private String url;
	private String comentario;
	
	public Firma(){
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
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

	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public void merge(Firma f) {
		setPath(f.getPath());
		setUrl(f.getUrl());
		setApellido(f.getApellido());
		setNombre(f.getNombre());
		setComentario(f.getComentario());
	}

	public Firma copiar() {
		Firma firma = new Firma();
		firma.setId(getId());
		firma.setComentario(getComentario());
		firma.setPath(getPath());
		firma.setUrl(getUrl());
		return firma;
	}
	

}
