package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropiedadMailPreventivos extends Propiedad{

	//private static String DEFAULT_DESTINATARIOS = "wstenger@bilpa.com.uy, ldiaz@bilpa.com.uy, preventivos@bilpa.com.uy";
	private static String DEFAULT_DESTINATARIOS = "juanmdura@gmail.com";
	private static String nombrePropiedad = "mail_preventivos_destinatarios";
	
	public PropiedadMailPreventivos(){
		super(DEFAULT_DESTINATARIOS, nombrePropiedad);
	}
	
	public String getDestinatarios(){
		String respuesta= DEFAULT_DESTINATARIOS;

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