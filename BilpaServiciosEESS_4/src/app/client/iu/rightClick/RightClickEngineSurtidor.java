package app.client.iu.rightClick;

import app.client.dominio.Surtidor;
import app.client.iu.activos.surtidor.IUGestionarSurtidores;
import app.client.iu.activos.surtidor.IUModificarSurtidor;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class RightClickEngineSurtidor extends RightClickEngine{
	
	
	public void createPopupMenu(Object o) 
	{
		if (o.getClass().equals(Surtidor.class))
		{
			final Surtidor surtidor = (Surtidor)o;

			Command commandModificar = new Command() {
				public void execute() {
					popupPanel.hide();
					int hMargin = 182;
					int vMargin = 90;
					IUModificarSurtidor iu = new IUModificarSurtidor(surtidor, (IUGestionarSurtidores) advLabel.iu);
					DialogBox dialogo = iu.getDialogoModif();
					dialogo.show();
					dialogo.setPopupPosition(Window.getClientWidth()/3 - hMargin, Window.getClientHeight()/3 - vMargin);
				}
			};

			Command commandEliminar = new Command() {
				public void execute() {
					popupPanel.hide();					
					Window.alert("Eliminar a: " + surtidor.toString());
				}
			};

			super.createPopupMenu( o, commandModificar, commandEliminar, "Modificar Surtidor", "Eliminar");
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
