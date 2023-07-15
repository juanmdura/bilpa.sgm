package app.client.dominio.data;

public class ComentarioData  implements com.google.gwt.user.client.rpc.IsSerializable {

	private String texto;
	private boolean visible;
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public ComentarioData() {
		super();
	}
	public ComentarioData(String texto2, boolean visible2) {
		setTexto(texto2);
		setVisible(visible2);
	}
	
}
