package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropiedadUrlFirma extends Propiedad{
	private static String DEFAULT_PATH_FIRMAS = "/var/www/html/testing/firmas/";
	private static String nombrePropiedad = "url_firmas";

	public PropiedadUrlFirma(){
		super(DEFAULT_PATH_FIRMAS, nombrePropiedad);
	}

	public String getURLPropertie(boolean correctivo){
		String respuesta= DEFAULT_PATH_FIRMAS;

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