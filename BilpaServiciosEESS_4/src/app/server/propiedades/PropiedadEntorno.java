package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropiedadEntorno extends Propiedad{

	private static String DEFAULT_ESTADO = "local";
	private static String nombrePropiedad = "entorno";
	
	public PropiedadEntorno()
	{
		super(DEFAULT_ESTADO, nombrePropiedad);
	}

	public String getEntorno(){
		String respuesta= DEFAULT_ESTADO;

		try {
			Properties propertie= new Properties();
			FileInputStream fis= new FileInputStream(getDirectorio() + File.separator + getArchivo());
			propertie.load(fis);
			respuesta= propertie.getProperty(nombrePropiedad);
			return respuesta;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return DEFAULT_ESTADO;
	}
	
}