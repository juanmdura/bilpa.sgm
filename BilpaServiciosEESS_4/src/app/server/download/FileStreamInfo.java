package app.server.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Una implemetación concreta de la interfaz <code>StreamInfo</code> que
 * simplifica la descarga de un archivo de disco.
 */
public class FileStreamInfo extends AbstractStreamInfo implements StreamInfo {

	private File file;

	/**
	 * Constructor de la clase.
	 * 
	 * @param contentType
	 *            el tipo de contenido del torrente.
	 * @param file
	 *            el archivo a ser descargado.
	 */
	public FileStreamInfo(String contentType, File file) {
		this(contentType, file, file.getName(), false);
	}
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param contentType
	 *            el tipo de contenido del torrente.
	 * @param file
	 *            el archivo a ser descargado.
	 * @param filename
	 *            el nombre a asignar al archivo a descargar.
	 */
	public FileStreamInfo(String contentType, File file, String filename) {
		this(contentType, file, filename, false);
	}

	/**
	 * Constructor de la clase.
	 * 
	 * @param contentType
	 *            el tipo de contenido del torrente.
	 * @param file
	 *            el archivo a ser descargado.
	 * @param filename
	 *            el nombre a asignar al archivo a descargar.
	 * @param <code>true</code> si se tiene que ver el archivo dentro del
	 *        Browser, <code>false</code> si se tiene que descargar.
	 */
	public FileStreamInfo(String contentType, File file, String filename, boolean inline) {
		super(contentType, inline, filename);
		this.file = file;
	}	

	/**
	 * Retorna el archivo a descargar.
	 * 
	 * @return el archivo a descargar.
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Retorna un torrente de entrada del archivo.
	 * 
	 * @return un torrente de entrada del archivo.
	 * @throws FileNotFoundException
	 *             si el archivo no existe o no se puede leer.
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		
		// Crear el torrente de entrada del archivo
		InputStream in = new FileInputStream(file);

		// Borrar el archivo solamente cuando el nombre del archivo original
		// no coincide con el nombre dado en la descarga. Esto significa que
		// el archivo es un archivo temporal, de lo contrario tendrían el
		// mismo nombre.
		if (!getFilename().equals(file.getName())) {
			in = new FilterInputStream(in) {
				public void close() throws IOException {
					super.close();
					file.delete();
				}
			};
		}

		return in;
	}

}