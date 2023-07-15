package app.client.utilidades;

import app.client.utilidades.utilObjects.FileDownload;



public class UtilPDF {

	private static UtilPDF instancia = null;

	public static UtilPDF getInstancia() {
		if(instancia == null){
			instancia = new UtilPDF();
		}
		return instancia;
	}
	
	public boolean abrirPDF(String rutaDelServidor) {		
		FileDownload file = new FileDownload();
		file.setPath(rutaDelServidor);
		file.download();
		return true;
	}
}
