package app.client.iu.rightClick;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import app.client.dominio.Canio;


public class AdvLabelCanio extends AdvLabel{

	private Canio canio;
	
	public Canio getCanio() {
		return canio;
	}

	public void setCanio(Canio canio) {
		this.canio = canio;
	}

	public AdvLabelCanio(Entero entero, String text, Canio canio, Composite iu) {
		super(entero, text, iu);
		this.canio = canio;
	}
	
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event, this.getCanio());
	}
}
