package app.server.download;

import java.io.IOException;
import java.io.InputStream;

/**
 * Una implemetación concreta de la interfaz <code>StreamInfo</code> que
 * simplifica la descarga de un archivo de disco.
 */
public abstract class AbstractStreamInfo implements StreamInfo {

	private final String contentType;
	private final boolean inline;
	private final String filename;

	/**
	 * Constructor de la clase.
	 * 
	 * @param contentType
	 *            el tipo de contenido del torrente.
	 * @param inline
	 *            <code>true</code> si se desea visualizar el archivo dentro del
	 *            navegador, <code>false</code> si se lo quiere descargar.
	 * @param filename
	 *            el nombre que tendrá el archivo descargado.
	 */
	public AbstractStreamInfo(String contentType, boolean inline, String filename) {
		this.contentType = contentType;
		this.inline = inline;
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public String getContentDisposition() {
		if (inline) {
			return "inline; filename=" + getFilename();
		} else {
			return "attachment; filename=" + getFilename();
		}
	}

	/**
	 * Retorna <code>true</code> si se desea visualizar el archivo dentro del
	 * navegador, <code>false</code> si se lo quiere descargar.
	 * 
	 * @return <code>true</code> si se desea visualizar el archivo dentro del
	 *         navegador, <code>false</code> si se lo quiere descargar.
	 */
	protected boolean isInline() {
		return inline;
	}

	/**
	 * Retorna el nombre que tendrá el archivo descargado.
	 * 
	 * @return el nombre que tendrá el archivo descargado.
	 */
	protected String getFilename() {
		return filename;
	}

	public abstract InputStream getInputStream() throws IOException;

}