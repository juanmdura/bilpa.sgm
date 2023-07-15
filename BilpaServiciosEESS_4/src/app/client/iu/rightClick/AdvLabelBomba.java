package app.client.iu.rightClick;

import app.client.dominio.BombaSumergible;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;


public class AdvLabelBomba extends AdvLabel{

	private BombaSumergible bomba;

	public BombaSumergible getBomba() {
		return bomba;
	}

	public void setBomba(BombaSumergible bomba) {
		this.bomba = bomba;
	}

	public AdvLabelBomba(Entero entero, String text, BombaSumergible bomba, Composite iu) {
		super(entero, text, iu);
		this.bomba = bomba;
	}
	
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event, this.getBomba());
	}
}
