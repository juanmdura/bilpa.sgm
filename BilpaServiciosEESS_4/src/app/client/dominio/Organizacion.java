package app.client.dominio;

public enum Organizacion {

	Operador("Operador"),
	Petrolera("Petrolera"),
	Bilpa("Bilpa");

	private final String organizacion;

	private Organizacion(String organizacion) {
		this.organizacion = organizacion;
	}

	public String getOrganizacion() {
		return organizacion;
	}

	public static Organizacion getById(int id){
		if (id == 1)return Organizacion.Operador;
		if (id == 2)return Organizacion.Petrolera;
		if (id == 3)return Organizacion.Bilpa;
		return null;
	}
	
	public static int getId(Organizacion org){
		if (org.equals(Organizacion.Operador))return 1;
		if (org.equals(Organizacion.Petrolera))return 2;
		if (org.equals(Organizacion.Bilpa))return 3;
		return 0;
	}
}