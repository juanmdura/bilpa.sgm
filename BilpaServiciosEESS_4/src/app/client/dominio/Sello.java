package app.client.dominio;


public class Sello implements com.google.gwt.user.client.rpc.IsSerializable {

	public static final int N_A = 1;
    public static final int ANCAP = 2;
    public static final int PETROBRAS = 3;
    public static final int ESSO = 4;
    public static final int PRODUCTORES = 5;
    public static final int ANCAP_CONTRATOS = 6;
    
    public static final String N_As = "N/A";
    public static final String ANCAPs = "ANCAP";
    public static final String PETROBRASs = "PETROBRAS";
    public static final String ESSOs = "ESSO";
    public static final String PRODUCTORESs = "PRODUCTORES";
    public static final String ANCAP_CONTRATOSs = "ANCAP CONTRATOS";
	
    public static String getSelloById(int id){
    	if (id == N_A) return N_As;
    	if (id == ANCAP) return ANCAPs;
    	if (id == PETROBRAS) return PETROBRASs;
    	if (id == ESSO) return ESSOs;
    	if (id == PRODUCTORES) return PRODUCTORESs;
    	if (id == ANCAP_CONTRATOS) return ANCAP_CONTRATOSs;
    	return "";
    }
    
	private int id;
	private String nombre;
	
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
	
	public Sello() {}
	
	public Sello(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	public Sello(int idSello) {
		setId(idSello);
	}
	
	@Override
	public String toString() {
		return nombre;
	}
	
	public Sello copiar() {
		Sello copia = new Sello();
		copiarPropiedades(copia);
		return copia;
	}

	private void copiarPropiedades(Sello copia) {
		copia.setId(getId());
		copia.setNombre(getNombre());
	}
	
	
	

}
