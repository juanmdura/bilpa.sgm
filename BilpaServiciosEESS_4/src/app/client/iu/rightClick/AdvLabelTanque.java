package app.client.iu.rightClick;

import app.client.dominio.Tanque;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

public class AdvLabelTanque extends AdvLabel{

	private Tanque tanque;


	public Tanque getTanque() {
		return tanque;
	}

	public void setTanque(Tanque tanque) {
		this.tanque = tanque;
	}

	public AdvLabelTanque(Entero entero, String text, Tanque tanque, Composite iu) {
		super(entero, text, iu);
		this.tanque = tanque;
	}
	
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event, this.getTanque());
	}
}
