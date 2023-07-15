package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropiedadReporteVisitas extends Propiedad{

	private static String PATH_REPORTE_VISITAS = "/var/visitas";
	private static String nombrePropiedad = "path_reporte_visitas";
	
	public PropiedadReporteVisitas(){
		super(PATH_REPORTE_VISITAS, nombrePropiedad);
	}
	
	public String getPath(){
		String respuesta= PATH_REPORTE_VISITAS;

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