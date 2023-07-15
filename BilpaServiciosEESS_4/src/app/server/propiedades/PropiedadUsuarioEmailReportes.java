package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropiedadUsuarioEmailReportes extends Propiedad{

	private static String DEFAULT_USUARIO_EMAIL_REPORTES = "xxx.estaciones@bilpa.com.uy";
	private static String nombrePropiedad = "usuario_email_reportes";
	
	public PropiedadUsuarioEmailReportes(){
		super(DEFAULT_USUARIO_EMAIL_REPORTES, nombrePropiedad);
	}
	
	public String getUsuario(){
		String respuesta= DEFAULT_USUARIO_EMAIL_REPORTES;

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