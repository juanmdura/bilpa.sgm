package app.client.dominio.data;



public class EstacionDataList implements com.google.gwt.user.client.rpc.IsSerializable{

	private int id;
	private String nombre;
	private String numeroSerie;
	private String sello;
	private int tr;
		
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
	
	public String getNumeroSerie() {
		return numeroSerie;
	}
	
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getSello() {
		return sello;
	}

	public void setSello(String sello) {
		this.sello = sello;
	}

	public int getTr() {
		return tr;
	}

	public void setTr(int tr) {
		this.tr = tr;
	}

	public String toString(){
		return getNombre() + " - " + getNumeroSerie();
	}

	public EstacionDataList() {}
	
	
	
}
