package app.server.download;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

/**
 * Una implementación concreta de la interfaz <code>StreamInfo</code> que
 * simplifica la descarga de recursos de la aplicación web.
 */
public class ResourceStreamInfo extends AbstractStreamInfo {

	private ServletContext context;
	private String path;

	/**
	 * Constructor de la clase.
	 * 
	 * @param contentType
	 *            el tipo de contenido del recurso.
	 * @param context
	 *            el contexto del servlet que contiene el recurso.
	 * @param path
	 *            el camino al recurso a ser descargado.
	 * @param inline
	 *            <code>true</code> si se quiere visualizar el contenido dentro
	 *            del navegador, <code>false</code> si se quiere que se
	 *            descargue.
	 */
	public ResourceStreamInfo(String contentType, ServletContext context, String path, String filename) {
		this(contentType, context, path, filename, false);
	}

	/**
	 * Constructor de la clase.
	 * 
	 * @param contentType
	 *            el tipo de contenido del recurso.
	 * @param context
	 *            el contexto del servlet que contiene el recurso.
	 * @param path
	 *            el camino al recurso a ser descargado.
	 * @param inline
	 *            <code>true</code> si se quiere visualizar el contenido dentro
	 *            del navegador, <code>false</code> si se quiere que se
	 *            descargue.
	 */
	public ResourceStreamInfo(String contentType, ServletContext context, String path, String filename, boolean inline) {
		super(contentType, inline, filename);
		this.context = context;
		this.path = path;
	}

	/**
	 * Retorna un torrente de entrada al recurso.
	 * 
	 * @return un torrente de entrada al recurso.
	 * @throws FileNotFoundException
	 *             si el recurso no existe.
	 */
	public InputStream getInputStream() throws IOException {
		InputStream input = context.getResourceAsStream(path);
		if (input == null) {
			throw new FileNotFoundException("Resource " + path + " not found");
		}
		return input;
	}

}