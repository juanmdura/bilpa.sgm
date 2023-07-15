package app.client.iu.rightClick;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import app.client.dominio.Surtidor;

public class AdvLabelSurtidor extends AdvLabel{

	private Surtidor surtidor;

	public Surtidor getSurtidor() {
		return surtidor;
	}

	public void setSurtidor(Surtidor surtidor) {
		this.surtidor = surtidor;
	}

	public AdvLabelSurtidor(Entero entero, String text, Surtidor surtidor, Composite iu) {
		super(entero, text, iu);
		this.surtidor = surtidor;
	}
	
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event, this.getSurtidor());
	}
}
