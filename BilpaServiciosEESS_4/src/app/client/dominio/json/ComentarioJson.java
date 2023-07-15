package app.client.dominio.json;

/**
 * 
 * @author dfleitas
 *
 */
public class ComentarioJson {
	
	private int id;
	private String texto;
	private boolean visible;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	
	
	public ComentarioJson() {
		super();
	}

}
