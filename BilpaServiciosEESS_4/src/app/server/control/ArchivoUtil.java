package app.server.control;

public class ArchivoUtil {

	private static final String SEPARADOR = "/";
	
	public static String getNombreArchivo(String path) {
		String[] partes = path.split(SEPARADOR);
		return partes[partes.length -1];
	}
	
}
