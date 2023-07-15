package app.server.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Esta clase se encarga de descargar un archivo que previamente se había
 * generado y puesto en la sesión usando la clave {@link StreamInfo#SESSION_KEY}
 * . Opcionalmente se puede sobreescribir cargar un valor al parámetro
 * <code>buffer-size</code> para personalizar el tamaño del buffer usado en la
 * transferencia del archivo.
 */
public class DownloadServlet extends HttpServlet {

	/**
	 * Para compatibilidad con el motor de serialización.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Si el método {@link #getBufferSize()} no se sobreescribe, este es el
	 * tamaño de buffer que se va a usar en la trasferencia.
	 */
	protected static final int DEFAULT_BUFFER_SIZE = 4096; // 4 KB

	/**
	 * Tamaño de buffer que se va a usar en la trasferencia.
	 */
	private int bufferSize = DEFAULT_BUFFER_SIZE;

	/**
	 * En la inicialización se obtiene el tamaño del buffer a usar en la
	 * transferencia.
	 */
	@Override
	public void init() throws ServletException {
		super.init();

		String paramBufferSize = getInitParameter("bufferSize");
		if (paramBufferSize != null) {
			try {
				bufferSize = Integer.parseInt(paramBufferSize);
				if (bufferSize <= 0) {
					throw new NumberFormatException("bufferSize must be positive");
				}
			} catch (NumberFormatException e) {
				log("El valor del parámetro bufferSize debe ser un entero positivo (valor = " + paramBufferSize + "). Se utilizará el valor por defecto (" + DEFAULT_BUFFER_SIZE + " bytes).");
			}
		}
	}

	/**
	 * Retorna la información del torrente que está en la sesión asociado a la
	 * clave {@link #SESSION_KEY}.
	 * 
	 * @param request
	 *            la solicitud HTTP request que estamos procesando.
	 * @param response
	 *            la respuesta HTTP que estamos creando.
	 * 
	 * @return la información del archivo a ser descargado.
	 * 
	 * @throws IOException
	 *             if an exception occurs.
	 */
	protected StreamInfo getStreamInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StreamInfo streamInfo = (StreamInfo) request.getSession().getAttribute(StreamInfo.SESSION_KEY);
		if (streamInfo == null) {
			throw new IOException("Stream to download does not exist");
		}
		return streamInfo;
	}

	/**
	 * Procesa la solicitud HTTP especifica y descarga el archivo
	 * correspondiente.
	 * 
	 * @param request
	 *            la solicitud HTTP a procesar.
	 * @param response
	 *            la respuesta HTTP donde se descargará el archivo.
	 * @throws IOException
	 *             si hay un error de entrada/salida al descargar el archivo.
	 */
	protected void doDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {

		StreamInfo info = getStreamInfo(request, response);

		InputStream stream = null;
		try {
			//
			// Abrir el torrente de entrada
			//
			stream = info.getInputStream();

			//
			// Configurar el header: Content-Type
			//
			response.setContentType(info.getContentType());

			//
			// Configurar el header: Content-Disposition
			//
			response.setHeader("Content-Disposition", info.getContentDisposition());

			//
			// Copiar los datos del torrente de entrada a la respuesta
			//
			copy(stream, response.getOutputStream());

		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	/**
	 * Simplemente llama a
	 * {@link #doDownload(HttpServletRequest, HttpServletResponse)} .
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doDownload(request, response);
	}

	/**
	 * Simplemente llama a
	 * {@link #doDownload(HttpServletRequest, HttpServletResponse)}.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doDownload(request, response);
	}

	/**
	 * Copia el contenido del torrente de entrada en el torrente de salida.
	 * 
	 * @param input
	 *            el torrente de entrada del cual leer.
	 * @param output
	 *            el torrente de salida sobre el cual escribir.
	 * @return la cantidad de bytes que se copiaron.
	 * @throws IOException
	 *             en caso de un problema de entrada/salida.
	 */
	private long copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[bufferSize];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

}
