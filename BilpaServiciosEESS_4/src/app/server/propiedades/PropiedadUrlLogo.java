package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropiedadUrlLogo extends Propiedad{

	private static String DEFAULT_URL_LOGO = "/var/img/logo3.jpg";
	private static String nombrePropiedad = "url_logo";
	
	public PropiedadUrlLogo(){
		super(DEFAULT_URL_LOGO, nombrePropiedad);
	}
	
	public String getURLPropertie(){
		String respuesta= DEFAULT_URL_LOGO;

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