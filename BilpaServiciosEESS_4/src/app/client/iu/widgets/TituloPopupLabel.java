package app.client.iu.widgets;

import com.google.gwt.user.client.ui.Label;

public class TituloPopupLabel extends Label{

	private final static String STYLE = "tituloPopupLabel";
	
	public TituloPopupLabel(String text) {
		super(text);
		this.setStyleName(STYLE);
	}

}
