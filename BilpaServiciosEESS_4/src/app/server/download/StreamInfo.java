package app.server.download;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpSession;

/**
 * La información del archivo, o stream, a ser descargado por el
 * {@link DownloadServlet}.
 * 
 * @see DownloadServlet
 */
public interface StreamInfo {

	/**
	 * Clave a usar para colocar la información del torrente en la
	 * {@link HttpSession}.
	 */
	public static final String SESSION_KEY = "STREAM_INFO_TO_DOWNLOAD";
	
	/**
	 * Retorna el tipo de contenido del torrente a descargar.
	 * 
	 * @return el tipo de contenido del torrente a descargar.
	 */
	String getContentType() throws IOException;

	/**
	 * Retorna la disposición del contenido del torrente a descargar.
	 * 
	 * @return la disposición del contenido del torrente a descargar.
	 */
	String getContentDisposition() throws IOException;

	/**
	 * Retorna el torrente de entrada del contenido a ser descargado. Este
	 * torrente será cerrado por el {@link DownloadServlet}.
	 * 
	 * @return el torrente de entrada para el contenido a ser descargado.
	 * @throws IOException
	 *             si ocurre un error retornar el torrente de entrada.
	 */
	InputStream getInputStream() throws IOException;

}