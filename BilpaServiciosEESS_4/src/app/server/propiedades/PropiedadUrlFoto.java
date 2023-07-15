package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropiedadUrlFoto extends Propiedad {
	
	private static String DEFAULT_PATH_FOTOS = "/var/www/html/testing/fotos/";
	private static String nombrePropiedad = "url_fotos";

	public PropiedadUrlFoto(){
		super(DEFAULT_PATH_FOTOS, nombrePropiedad);
	}

	public String getURLPropertie(boolean correctivo){
		String respuesta= DEFAULT_PATH_FOTOS;

		try {
			Properties propertie= new Properties();
			FileInputStream fis= new FileInputStream(getDirectorio() + File.separator + getArchivo());
			propertie.load(fis);
			respuesta= propertie.getProperty(nombrePropiedad);

			if (correctivo){
				respuesta = respuesta.substring(0, respuesta.length() -1) + "_correctivo/";
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respuesta;
	}
}