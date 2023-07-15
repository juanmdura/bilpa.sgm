package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropiedadTest extends Propiedad{

	private static String DEFAULT_ESTADO = "false";
	private static String nombrePropiedad = "test";
	
	public PropiedadTest()
	{
		super(DEFAULT_ESTADO, nombrePropiedad);
	}

	public boolean getSistemaEnModoTest(){
		String respuesta= DEFAULT_ESTADO;

		try {
			Properties propertie= new Properties();
			FileInputStream fis= new FileInputStream(getDirectorio() + File.separator + getArchivo());
			propertie.load(fis);
			respuesta= propertie.getProperty(nombrePropiedad);
			return Boolean.valueOf(respuesta);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Boolean.valueOf(DEFAULT_ESTADO);
	}
	
}