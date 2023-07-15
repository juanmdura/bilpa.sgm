package app.client.iu.rightClick;

import app.client.dominio.BombaSumergible;
import app.client.iu.activos.bomba.IUGestionarBomba;
import app.client.iu.activos.bomba.IUModificarBomba;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class RightClickEngineBomba extends RightClickEngine{
	public void createPopupMenu(Object o) 
	{
		if (o.getClass().equals(BombaSumergible.class))
		{
			final BombaSumergible bomba = (BombaSumergible)o;

			Command commandModificar = new Command() {
				public void execute() {
					popupPanel.hide();
					
					IUModificarBomba iu = new IUModificarBomba(bomba, (IUGestionarBomba) advLabel.iu);
					DialogBox dialogo = iu.getDialogoModif();
					
					int hMargin = 55;
					int vMargin = 65;
					
					dialogo.show();
					dialogo.setPopupPosition(Window.getClientWidth()/3 - hMargin, Window.getClientHeight()/3 - vMargin);
				}
			};

			Command commandEliminar = new Command() {
				public void execute() {
					popupPanel.hide();					
					Window.alert("Eliminar a: " + bomba.toString());
				}
			};

			super.createPopupMenu( o, commandModificar, commandEliminar, "Modificar Bomba", "Eliminar");
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
