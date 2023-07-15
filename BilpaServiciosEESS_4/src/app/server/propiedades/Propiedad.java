package app.server.propiedades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Set;

public abstract class Propiedad {

	private String directorio = "/var/bilpa";
	//private String directorio = "c:\\bilpa";
	private String archivo = "bilpa.conf";
	//private String archivo = "bilpa_hd.conf";

	public Propiedad(String ruta, String propiedad){
		chequearYCrearArchivoDeConfiguracion(ruta, propiedad);
	}

	private void chequearYCrearArchivoDeConfiguracion(String ruta, String propiedad) {

		File file= new File(directorio + File.separator + archivo);

		try {
			if (!file.exists()) {
				File dir= new File(directorio);
				dir.mkdirs();
				PrintStream out = new PrintStream(new FileOutputStream(file));
				out.println("");
				out.println(propiedad + " = " + ruta);
				out.println("");
				out.close();
			}else{
				Properties propertie= new Properties();
				FileInputStream fis= new FileInputStream(getDirectorio() + File.separator + getArchivo());
				propertie.load(fis);
				String respuesta= propertie.getProperty(propiedad);
				
				if(respuesta == null || respuesta.equals("")){
					
					Set<String> stringPropertyNames = propertie.stringPropertyNames();
					
					PrintStream out = new PrintStream(new FileOutputStream(file));
					
					for (String string : stringPropertyNames) {
						propertie.load(fis);
						String lala = propertie.getProperty(string);
						out.println(string + " = " + lala);
					}
					
					out.println("");
					out.println(propiedad + " = " + ruta);
					out.println("");		
				}	
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public String getDirectorio() {
		return directorio;
	}

	public String getArchivo() {
		return archivo;
	}

}