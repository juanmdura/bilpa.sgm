package app.client.dominio.json;

/**
 * 
 * @author dfleitas
 *
 */
public class ItemChequeadoJson {
	
	private String nombreItemChequeo;
	private String valor;
	private boolean pendiente;
	
	
	public String getNombreItemChequeo() {
		return nombreItemChequeo;
	}
	public void setNombreItemChequeo(String nombreItemChequeo) {
		this.nombreItemChequeo = nombreItemChequeo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public boolean isPendiente() {
		return pendiente;
	}
	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}
	
	
	public ItemChequeadoJson() {
		super();
	}


}
