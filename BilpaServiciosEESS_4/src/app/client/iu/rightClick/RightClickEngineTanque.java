package app.client.iu.rightClick;

import app.client.dominio.Tanque;
import app.client.iu.activos.tanque.IUGestionarTanque;
import app.client.iu.activos.tanque.IUModificarTanque;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class RightClickEngineTanque extends RightClickEngine{
	
	
	public void createPopupMenu(Object o) 
	{
		if (o.getClass().equals(Tanque.class))
		{
			final Tanque tanque = (Tanque)o;

			Command commandModificar = new Command() {
				public void execute() {
					popupPanel.hide();
					
					IUModificarTanque iu = new IUModificarTanque(tanque, (IUGestionarTanque) advLabel.iu);
					DialogBox dialogo = iu.getDialogoModif();
					int hMargin = 92;
					int vMargin = 28;
					dialogo.show();
					dialogo.setPopupPosition(Window.getClientWidth()/3 - hMargin, Window.getClientHeight()/3 - vMargin);
				}
			};

			Command commandEliminar = new Command() {
				public void execute() {
					popupPanel.hide();					
					Window.alert("Eliminar a: " + tanque.toString());
				}
			};

			super.createPopupMenu( o, commandModificar, commandEliminar, "Modificar Tanque", "Eliminar");
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
