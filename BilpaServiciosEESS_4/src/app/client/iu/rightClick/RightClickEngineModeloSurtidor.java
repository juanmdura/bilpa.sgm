package app.client.iu.rightClick;

import app.client.dominio.ModeloSurtidor;
import app.client.iu.activos.surtidor.IUGestionarModelo;
import app.client.iu.activos.surtidor.IUModificarModelo;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class RightClickEngineModeloSurtidor extends RightClickEngine{
	
	
	public void createPopupMenu(Object o) 
	{
		if (o.getClass().equals(ModeloSurtidor.class))
		{
			final ModeloSurtidor modelSurt = (ModeloSurtidor)o;

			Command commandModificar = new Command() {
				public void execute() {
					popupPanel.hide();
					
					IUModificarModelo iu = new IUModificarModelo(modelSurt, (IUGestionarModelo) advLabel.iu);
					DialogBox dialogo = iu.getDialogoModif();
					dialogo.show();
					dialogo.center();
				}
			};

			Command commandEliminar = new Command() {
				public void execute() {
					popupPanel.hide();					
					Window.alert("Eliminar a: " + modelSurt.toString());
				}
			};

			super.createPopupMenu( o, commandModificar, commandEliminar, "Modificar", "Eliminar");
		}
	}

	@Override
	public void onClick(Widget sender, Event event, Object o) {}
	
	public void onClick(Widget sender) {}
	
	public void onClick(Widget sender, Event event, Object o, Composite iu) 
	{
		createPopupMenu( o );
		launchPopUp(event);
	}

	public void onRightClick(Widget sender, Event event, Object o, Composite iu) {
		createPopupMenu( o );
		launchPopUp(event);
		
	}

	@Override
	public void onRightClick(Widget sender, Event event, Object o) {
		createPopupMenu( o );
		launchPopUp(event);
		
	}


}
