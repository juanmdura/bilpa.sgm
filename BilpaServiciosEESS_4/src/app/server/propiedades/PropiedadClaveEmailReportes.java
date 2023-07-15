package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropiedadClaveEmailReportes extends Propiedad{

	private static String DEFAULT_CLAVE_EMAIL_REPORTES = "xxx";
	private static String nombrePropiedad = "clave_email_reportes";
	
	public PropiedadClaveEmailReportes(){
		super(DEFAULT_CLAVE_EMAIL_REPORTES, nombrePropiedad);
	}
	
	public String getClave(){
		String respuesta= DEFAULT_CLAVE_EMAIL_REPORTES;

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