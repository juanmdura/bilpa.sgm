package app.client.utilidades.utilObjects;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;

/**
 * Implementa la llamada a un servlet de forma no visible para el usuario. Sirve
 * para implementar la descarga de archivos llamando a un servlet que escribe el
 * archivo en el response que no funciona en IE8 utilizando Window.open()
 * 
 * @author nico
 * 
 */
public class FileDownload extends Composite {

	public static final String DEFAULT_PATH = "download";

	private String path;
	private Frame frame;

	/**
	 * Cargar el path del servlet al que se redirige para bajar el archivo
	 */
	public FileDownload() {
		frame = new Frame();
		frame.setVisible(false);
		path = DEFAULT_PATH;
		initWidget(frame);
	}

	public void download() {
		if (path != null) {
			frame.setUrl(path);
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
