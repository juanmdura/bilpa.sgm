package app.client.dominio.json;

public class ComentarioChequeoJson {

	private int id;
	private boolean imprimible;
	private int idUsuario;
	private String texto;
	private int idItemChequeado;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isImprimible() {
		return imprimible;
	}
	public void setImprimible(boolean imprimible) {
		this.imprimible = imprimible;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getIdItemChequeado() {
		return idItemChequeado;
	}
	public void setIdItemChequeado(int idItemChequeado) {
		this.idItemChequeado = idItemChequeado;
	}
	
	
	
}
