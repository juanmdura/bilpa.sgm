package app.client.iu.activos;

import app.client.dominio.Persona;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IUEliminarActivo extends Composite{
	private HTML htmlTitulo = new HTML("Eliminar activos");
	
	private VerticalPanel vpPrincipal = new VerticalPanel();
	private Persona sesion;
	
	public VerticalPanel getVpPrincipal(){
		return this.vpPrincipal;
	}
	
	public IUEliminarActivo(Persona persona){
		this.sesion = persona;
		setearWidgets();			//Setea el tamano de los Widgets.
		cargarPanelesConWidgets();	//Agrega los Widget a los paneles.
		cargarLtBoxTareas();		
		
	}

	private void setearWidgets() {
		
	}

	private void cargarPanelesConWidgets() {
		this.vpPrincipal.add(htmlTitulo);
		
	}

	private void cargarLtBoxTareas() {
		
	}
}