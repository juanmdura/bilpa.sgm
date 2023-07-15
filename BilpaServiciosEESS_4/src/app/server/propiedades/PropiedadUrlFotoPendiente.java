package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropiedadUrlFotoPendiente extends Propiedad {
	
	private static String DEFAULT_PATH_FOTOS = "/var/www/html/testing/fotos_pendientes/";
	private static String nombrePropiedad = "url_fotos_pendientes";

	public PropiedadUrlFotoPendiente(){
		super(DEFAULT_PATH_FOTOS, nombrePropiedad);
	}

	public String getURLPropertie(){
		String respuesta= DEFAULT_PATH_FOTOS;

		try {
			Properties propertie= new Properties();
			FileInputStream fis= new FileInputStream(getDirectorio() + File.separator + getArchivo());
			propertie.load(fis);
			respuesta= propertie.getProperty(nombrePropiedad);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respuesta;
	}
}