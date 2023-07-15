package app.client.utilidades;

import com.google.gwt.user.client.ui.FlexTable;

public class UtilCss {
	
	public static void aplicarEstiloATabla(final FlexTable tableSoluciones, int i) {
		if(i %2==0){
			tableSoluciones.getRowFormatter().addStyleName(i-1, "FilaTabla1");				
		}else{
			tableSoluciones.getRowFormatter().addStyleName(i-1, "FilaTabla2");
		}
	}

}
